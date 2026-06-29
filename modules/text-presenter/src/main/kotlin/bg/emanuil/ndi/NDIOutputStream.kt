package bg.emanuil.ndi

interface NDIOutputStream {

    fun initialize(frame: NDIOutputFrame, startTime: Long) {}

    fun render(frame: NDIOutputFrame, frameStartTime: Long)

    fun destroy() {}
}