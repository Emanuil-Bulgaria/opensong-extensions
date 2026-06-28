package org.example

import java.lang.foreign.Arena
import java.lang.foreign.MemoryLayout
import java.lang.foreign.MemorySegment
import java.lang.foreign.ValueLayout
import java.lang.invoke.VarHandle

class NDIVideoFrameV2Builder {

    private val layout: MemoryLayout

    private val xResHandle: VarHandle
    private val yResHandle: VarHandle
    private val fourCCHandle: VarHandle
    private val frameRateNHandle: VarHandle
    private val frameRateDHandle: VarHandle
    private val pictureAspectRatioHandle: VarHandle
    private val frameFormatTypeHandle: VarHandle
    private val timecodeHandle: VarHandle
    private val pDataHandle: VarHandle
    private val strideHandle: VarHandle
    private val pMetadataHandle: VarHandle
    private val timestampHandle: VarHandle

    init {
        layout = MemoryLayout.structLayout(
            ValueLayout.JAVA_INT.withName("xres"), // 0-4
            ValueLayout.JAVA_INT.withName("yres"), // 4-8
            ValueLayout.JAVA_INT.withName("FourCC"), // 8-12
            ValueLayout.JAVA_INT.withName("frame_rate_N"), // 12-16
            ValueLayout.JAVA_INT.withName("frame_rate_D"), // 16-20
            ValueLayout.JAVA_FLOAT.withName("picture_aspect_ratio"), // 20-24
            ValueLayout.JAVA_INT.withName("frame_format_type"),
            MemoryLayout.paddingLayout(4),
            ValueLayout.JAVA_LONG.withName("timecode"),
            ValueLayout.ADDRESS.withName("p_data"),
            ValueLayout.JAVA_INT.withName("line_stride_in_bytes"),
            MemoryLayout.paddingLayout(4),
            ValueLayout.ADDRESS.withName("p_metadata"),
            ValueLayout.JAVA_LONG.withName("timestamp"),
        )

        xResHandle = layout.varHandle(MemoryLayout.PathElement.groupElement("xres"))
        yResHandle = layout.varHandle(MemoryLayout.PathElement.groupElement("yres"))
        fourCCHandle = layout.varHandle(MemoryLayout.PathElement.groupElement("FourCC"))
        frameRateNHandle = layout.varHandle(MemoryLayout.PathElement.groupElement("frame_rate_N"))
        frameRateDHandle = layout.varHandle(MemoryLayout.PathElement.groupElement("frame_rate_D"))
        pictureAspectRatioHandle = layout.varHandle(MemoryLayout.PathElement.groupElement("picture_aspect_ratio"))
        frameFormatTypeHandle = layout.varHandle(MemoryLayout.PathElement.groupElement("frame_format_type"))
        timecodeHandle = layout.varHandle(MemoryLayout.PathElement.groupElement("timecode"))

        pDataHandle = layout.varHandle(MemoryLayout.PathElement.groupElement("p_data"))
        strideHandle = layout.varHandle(MemoryLayout.PathElement.groupElement("line_stride_in_bytes"))
        pMetadataHandle = layout.varHandle(MemoryLayout.PathElement.groupElement("p_metadata"))
        timestampHandle = layout.varHandle(MemoryLayout.PathElement.groupElement("timestamp"))
    }

    fun toNative(arena: Arena, data: NDIVideoFrameV2): MemorySegment {
        val segment = arena.allocate(layout)
        xResHandle.set(segment, 0L, data.xRes)
        yResHandle.set(segment, 0L, data.yRes)
        fourCCHandle.set(segment, 0L, data.fourCC.code)
        frameRateNHandle.set(segment, 0L, data.frameRateN)
        frameRateDHandle.set(segment, 0L, data.frameRateD)
        frameFormatTypeHandle.set(segment, 0L, data.frameFormatType.code)
        timecodeHandle.set(segment, 0L, data.timecode)

        pDataHandle.set(segment, 0L, data.data)
        strideHandle.set(segment, 0L, data.dataSizeInBytes ?: data.lineStrideInBytes)
        pMetadataHandle.set(segment, 0L, data.pMetadata?.let(arena::allocateFrom) ?: MemorySegment.NULL)
        timestampHandle.set(segment, 0L, data.timecode)
        return segment
    }
}