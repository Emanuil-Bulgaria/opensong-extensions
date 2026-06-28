package org.example.ndi

data class Dimensions(val width: Int, val height: Int, val aspectRatio: Float? = null) {
    fun aspectRatio(): Float = aspectRatio ?: (width.toFloat() / height.toFloat())
}
