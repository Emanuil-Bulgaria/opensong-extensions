package bg.emanuil.ndi

import kotlinx.coroutines.Job

data class NDIStreamJob(
    val job: Job,
    val template: String,
) {
    fun cancel() {
        job.cancel()
    }
}
