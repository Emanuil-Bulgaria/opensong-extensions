package org.example.ndi

import org.example.NDIFourCCVideoType
import org.example.NDIFrameFormatType
import org.example.NDISendCreate
import org.example.NDIVideoFrameV2
import org.example.NdiLibrary
import java.lang.foreign.Arena

class NDISenderInstance(
    val name: String,
    val groups: List<String> = listOf(),
    val dimensions: Dimensions = Dimensions(1920, 1080),
    val frameRate: FrameRate = FrameRate(60000, 1000),
    val fourCC: NDIFourCCVideoType = NDIFourCCVideoType.RGBA,
    val frameFormatType: NDIFrameFormatType = NDIFrameFormatType.PROGRESSIVE,
    initialArena: Arena?

) : Runnable, AutoCloseable {

    private val arena: Arena
    private val closeArena: Boolean
    private val width: Int = dimensions.width
    private val height: Int = dimensions.height
    private val aspectRatio: Float = dimensions.aspectRatio()

    init {
        if(initialArena == null) {
            arena = Arena.ofConfined()
            closeArena = true
        } else {
            arena = initialArena
            closeArena = false
        }
    }

    override fun run() {
        try {
            NdiLibrary(arena).use { library ->
                println(library.version())
                library.sendCreate(NDISendCreate(
                    name = name, groups = groups,
                )).use { sendInstance ->
                    val bufferSize = width * height * 4L

                    val dataPtr = arena.allocate(bufferSize)

                    val frame = NDIVideoFrameV2(
                        xRes = width, yRes = height,
                        frameRateN = frameRate.N, frameRateD = frameRate.D,
                        pictureAspectRatio = aspectRatio,
                        fourCC = fourCC,
                        lineStrideInBytes = width * 4,
                        data = dataPtr
                    )
                }
            }
        } catch (e: InterruptedException) {

        } finally {
            close()
        }
    }

    override fun close() {
        if(closeArena) {
            arena?.close()
        }
    }


}