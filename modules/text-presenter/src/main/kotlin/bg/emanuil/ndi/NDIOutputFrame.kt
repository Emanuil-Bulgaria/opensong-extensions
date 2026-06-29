package bg.emanuil.ndi

import bg.emanuil.NDIFourCCVideoType
import java.lang.foreign.MemorySegment

data class NDIOutputFrame(
    val dimensions: Dimensions,
    val frameRate: FrameRate,
    val formatType: NDIFourCCVideoType,
    val data: MemorySegment)
