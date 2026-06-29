package bg.emanuil.ndi

data class NDIOutputStreamState(
    val stream: NDIOutputStream,
    var initialized: Boolean = false)
