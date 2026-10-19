package bg.emanuil.config

import bg.emanuil.draw.ComposeNDIStreamer
import bg.emanuil.opensong.OpenSongRepository
import bg.emanuil.draw.OpenSongTextProvider
import bg.emanuil.draw.TextProvider
import bg.emanuil.examples.LowerThirdsExample
import bg.emanuil.examples.ScriptureExample
import bg.emanuil.ndi.NDIStreamManager
import bg.emanuil.presentation.compose_example.ComposeExampleViewModel
import bg.emanuil.presentation.lower_thirds_scripture.LowerThirdsScripture
import bg.emanuil.presentation.lower_thirds_scripture.LowerThirdsScriptureViewModel
import bg.emanuil.presentation.lower_thirds_song.LowerThirdsSong
import bg.emanuil.presentation.lower_thirds_song.LowerThirdsSongViewModel
import bg.emanuil.watcher.ConfigurationStore
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val modules = module {

    service { ConfigurationStore() }

    serviceOf(::OpenSongRepository)
//    serviceOf(::OpenSongTextProvider).bind(TextProvider::class)
    service(createdAtStart = true) {
        NDIStreamManager(get(ConfigurationStore::class), get())
    }

    viewModelOf(::ComposeExampleViewModel)
    viewModelOf(::LowerThirdsSongViewModel)
    viewModelOf(::LowerThirdsScriptureViewModel)

    single {
        mapOf(
//            LowerThirdsExample::class.simpleName!! to LowerThirdsExample(getAll()),
//            ScriptureExample::class.simpleName!! to ScriptureExample(getAll()),
            "LowerThirdsSong" to ComposeNDIStreamer({ LowerThirdsSong() }),
            "LowerThirdsScripture" to ComposeNDIStreamer({ LowerThirdsScripture() })
        )
    }
}