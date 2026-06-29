package bg.emanuil

data class NDISendCreate(
    val name: String,
    val groups: List<String> = listOf(),
    val clockVideo: Boolean = false,
    val clockAudio: Boolean = false) {
}
