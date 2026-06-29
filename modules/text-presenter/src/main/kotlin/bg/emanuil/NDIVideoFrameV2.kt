package bg.emanuil

import java.lang.foreign.MemorySegment

data class NDIVideoFrameV2(
    val xRes: Int,
    val yRes: Int,
    val fourCC: NDIFourCCVideoType = NDIFourCCVideoType.UYVY,
    val frameRateN: Int = 30000,
    val frameRateD: Int = 1001,
    val pictureAspectRatio: Float = 0.0f,
    val frameFormatType: NDIFrameFormatType = NDIFrameFormatType.PROGRESSIVE,
    // When you specify this as a timecode, the timecode will be synthesized for you. This may be used when
    // sending video, audio or metadata. If you never specify a timecode at all, asking for each to be
    // synthesized, then this will use the current system time as the starting timecode and then generate
    // synthetic ones, keeping your streams exactly in sync as long as the frames you are sending do not deviate
    // from the system time in any meaningful way. In practice this means that if you never specify timecodes
    // that they will always be generated for you correctly. Timecodes coming from different senders on the same
    // machine will always be in sync with each other when working in this way. If you have NTP installed on your
    // local network, then streams can be synchronized between multiple machines with very high precision.
    //
    // If you specify a timecode at a particular frame (audio or video), then ask for all subsequent ones to be
    // synthesized. The subsequent ones will be generated to continue this sequence maintaining the correct
    // relationship both the between streams and samples generated, avoiding them deviating in time from the
    // timecode that you specified in any meaningful way.
    //
    // If you specify timecodes on one stream (e.g. video) and ask for the other stream (audio) to be
    // synthesized, the correct timecodes will be generated for the other stream and will be synthesize exactly
    // to match (they are not quantized inter-streams) the correct sample positions.
    //
    // When you send metadata messages and ask for the timecode to be synthesized, then it is chosen to match the
    // closest audio or video frame timecode so that it looks close to something you might want ... unless there
    // is no sample that looks close in which a timecode is synthesized from the last ones known and the time
    // since it was sent.
    val timecode: Long = Long.MAX_VALUE,
    val data: MemorySegment = MemorySegment.NULL,
    val lineStrideInBytes: Int = 0,
    val dataSizeInBytes: Int? = null,
    val pMetadata: String? = null,
    val timestamp: Long = 0
)
