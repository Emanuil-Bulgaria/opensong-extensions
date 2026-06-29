package bg.emanuil.ndi

data class FrameRate(val N: Int, val D: Int) {
    fun frameRate(): Double = N.toDouble() / D.toDouble()
}