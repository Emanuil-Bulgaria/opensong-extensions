package org.example

data class NDISendCreate(
    val name: String,
    val groups: String? = null,
    val clockVideo: Boolean = false,
    val clockAudio: Boolean = false) {


}
