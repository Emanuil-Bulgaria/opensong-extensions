package org.example

import java.lang.foreign.Arena
import java.lang.foreign.MemoryLayout
import java.lang.foreign.MemorySegment
import java.lang.foreign.ValueLayout
import java.lang.invoke.VarHandle

class NDISendCreateBuilder {

    private val layout: MemoryLayout

    private val nameHandle: VarHandle
    private val groupsHandle: VarHandle
    private val clockVideoHandle: VarHandle
    private val clockAudioHandle: VarHandle

    init {
        layout = MemoryLayout.structLayout(
            ValueLayout.ADDRESS.withName("name"),
            ValueLayout.ADDRESS.withName("groups"),
            ValueLayout.JAVA_BOOLEAN.withName("clock_video"),
            ValueLayout.JAVA_BOOLEAN.withName("clock_audio"),
        )

        nameHandle = layout.varHandle(MemoryLayout.PathElement.groupElement("name"))
        groupsHandle = layout.varHandle(MemoryLayout.PathElement.groupElement("groups"))
        clockVideoHandle = layout.varHandle(MemoryLayout.PathElement.groupElement("clock_video"))
        clockAudioHandle = layout.varHandle(MemoryLayout.PathElement.groupElement("clock_audio"))
    }

    fun toNative(arena: Arena, data: NDISendCreate): MemorySegment {
        val segment = arena.allocate(layout)
        val namePtr = arena.allocateFrom(data.name)
        val groupsPtr = data.groups?.let(arena::allocateFrom) ?: MemorySegment.NULL

        nameHandle.set(segment, 0, namePtr)
        groupsHandle.set(segment, 0, groupsPtr)
        clockVideoHandle.set(segment, 0, data.clockVideo)
        clockAudioHandle.set(segment, 0, data.clockAudio)

        return segment
    }
}