package org.example

import java.lang.foreign.*
import java.lang.invoke.MethodHandle
import java.nio.file.Path
import kotlin.jvm.optionals.getOrNull

class NdiLibrary (
    private val arena: Arena,
    private val linker: Linker = Linker.nativeLinker()


    ) : AutoCloseable {

    private val lookup: SymbolLookup
    private val initFuncHandle: MethodHandle
    private val versionFuncHandle: MethodHandle
    private val destroyFuncHandle: MethodHandle
    private val sendCreateHandle: MethodHandle
    private val sendVideoHandle: MethodHandle
    private val sendDestroyHandle: MethodHandle

    private val toNative: NDISendCreate.(Arena) -> MemorySegment
    private val toNativeFrame: NDIVideoFrameV2.(Arena) -> MemorySegment

    fun getMethodHandle(symbolName: String, descriptor: FunctionDescriptor): MethodHandle {
        val address = lookup.find(symbolName).getOrNull()
            ?: throw IllegalStateException("Cannot find address for symbol $symbolName")

        return linker.downcallHandle(address, descriptor)
    }

    init {
        val path = NDILibraryLoader.getForCurrentNDIPlatform().linkedLibrary.path
        lookup = SymbolLookup.libraryLookup(path, arena)
        initFuncHandle = getMethodHandle("NDIlib_initialize",
            FunctionDescriptor.of(ValueLayout.JAVA_BOOLEAN))
        versionFuncHandle = getMethodHandle("NDIlib_version",
            FunctionDescriptor.of(ValueLayout.ADDRESS))
        destroyFuncHandle = getMethodHandle("NDIlib_destroy", FunctionDescriptor.ofVoid())
        val builder = NDISendCreateBuilder()
        val builderFrame = NDIVideoFrameV2Builder()
        toNative = {
            builder.toNative(arena, this)
        }
        toNativeFrame = {
            builderFrame.toNative(arena, this)
        }
        sendCreateHandle = getMethodHandle("NDIlib_send_create",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS))
        sendVideoHandle = getMethodHandle("NDIlib_send_send_video_v2",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS))
        sendDestroyHandle = getMethodHandle("NDIlib_send_destroy",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS))
    }

    fun initialize(): Boolean {
        return initFuncHandle.invoke() as Boolean
    }

    fun sendCreate(data: NDISendCreate): NDISendInstance {
        val ptr = data.toNative(arena)
        val result = sendCreateHandle.invoke(ptr) as MemorySegment
        return NDISendInstance(result, this)
    }

    fun sendVideo(instance: MemorySegment, data: NDIVideoFrameV2) {
        val ptr = data.toNativeFrame(arena)
        sendVideoHandle.invoke(instance, ptr)
    }

    fun destroy() {
        destroyFuncHandle.invoke()
    }

    fun sendDestroy(instance: MemorySegment) {
        sendDestroyHandle.invoke(instance)
    }

    fun version(): String {
        val result = versionFuncHandle.invoke() as MemorySegment
        return result.reinterpret(Long.MAX_VALUE).getString(0)
    }

    override fun close() {
        destroy()
    }


}
