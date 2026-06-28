package org.example

import java.lang.foreign.MemorySegment

class NDISendInstance(private val pointer: MemorySegment,
                      private val ndi: NdiLibrary) : AutoCloseable {

    fun send(frame: NDIVideoFrameV2) {
        ndi.sendVideo(pointer, frame)
    }

    override fun close() { ndi.sendDestroy(pointer) }
}