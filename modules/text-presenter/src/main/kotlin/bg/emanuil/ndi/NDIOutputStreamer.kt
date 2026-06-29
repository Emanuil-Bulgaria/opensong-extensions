package bg.emanuil.ndi

import bg.emanuil.NDIFourCCVideoType
import bg.emanuil.NDIFrameFormatType
import bg.emanuil.NDISendCreate
import bg.emanuil.NDISendInstance
import bg.emanuil.NDIVideoFrameV2
import bg.emanuil.NdiLibrary
import java.lang.foreign.Arena
import java.util.concurrent.TimeUnit

class NDIOutputStreamer(
    val library: NdiLibrary,
    val name: String,
    val groups: List<String> = listOf(),
    val dimensions: Dimensions = Dimensions(1920, 1080),
    val frameRate: FrameRate = FrameRate(60000, 1000),
    val fourCC: NDIFourCCVideoType = NDIFourCCVideoType.RGBA,
    val frameFormatType: NDIFrameFormatType = NDIFrameFormatType.PROGRESSIVE,
    private val initialArena: Arena? = null,

) : Runnable, AutoCloseable {

    private lateinit var arena: Arena
    private var closeArena = false
    private val width: Int = dimensions.width
    private val height: Int = dimensions.height
    private val aspectRatio: Float = dimensions.aspectRatio()

    private lateinit var sendInstance: NDISendInstance
    private lateinit var frame: NDIVideoFrameV2
    private lateinit var frameInfo: NDIOutputFrame

    private var streams: MutableList<NDIOutputStreamState> = mutableListOf()

    fun add(stream: NDIOutputStream) {
        streams.add(NDIOutputStreamState(stream))
    }

    private fun initialize() {
        if(initialArena == null) {
            arena = Arena.ofShared()
            closeArena = true
        } else {
            arena = initialArena
            closeArena = false
        }
        println(library.version())
        sendInstance = library.sendCreate(
            NDISendCreate(name, groups,
                clockVideo = false,
                clockAudio = false)
        )
        val bufferSize = width * height * 4L

        val dataPtr = arena.allocate(bufferSize)

        frame = NDIVideoFrameV2(
            xRes = width, yRes = height,
            frameRateN = frameRate.N, frameRateD = frameRate.D,
            pictureAspectRatio = aspectRatio,
            fourCC = fourCC,
            lineStrideInBytes = width * 4,
            frameFormatType = frameFormatType,
            data = dataPtr
        )

        frameInfo = NDIOutputFrame(dimensions, frameRate, fourCC, dataPtr)
    }

    private fun destroy() {
        streams.forEach { stream -> stream.stream.destroy()}
        sendInstance.close()
        if(closeArena) {
            arena.close()
        }
    }

    override fun run() {
        try {
            initialize()
            val frameDuration = 1_000_000_000.0 / frameRate.frameRate()
            println("Frame rate ${frameRate.frameRate()}")
            println("Frame duration $frameDuration")
            var frames = 0
            var startTime = System.nanoTime()
            val initialTime = System.nanoTime()
            while (true) {
                val frameStart = System.nanoTime()
                frames++

                for(streamState in streams) {
                    if(!streamState.initialized) {
                        streamState.stream.initialize(frameInfo, initialTime)
                        streamState.initialized = true
                    }

                    streamState.stream.render(frameInfo, frameStart)
                }
                sendInstance.send(frame)
                val waitTime = frameDuration - (System.nanoTime() - frameStart)
                if(waitTime > 0) {
                    TimeUnit.NANOSECONDS.sleep(waitTime.toLong())
                }

                if((System.nanoTime() - startTime) > 1_000_000_000) {
                    startTime = System.nanoTime()
                    println(frames)
                    frames = 0
                }

            }
        } catch (e: InterruptedException) {
            println("Interrupted")
        } finally {
            destroy()
        }
    }

    override fun close() {
        destroy()
    }


}