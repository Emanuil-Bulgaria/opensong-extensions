package bg.emanuil

import bg.emanuil.draw.ComposeNDIStreamer
import bg.emanuil.draw.OpenSongTextProvider
import bg.emanuil.draw.SkiaNDIStreamer
import bg.emanuil.draw.TextProvider
import bg.emanuil.ndi.NDIOutputStream
import bg.emanuil.ndi.NDIStreamManager
import bg.emanuil.watcher.ConfigurationStore
import org.koin.dsl.bind
import org.koin.dsl.module

val modules = module {
    single {
        ConfigurationStore()
    }

    single {
        mapOf(
            LowerThirdsExample::class.simpleName!! to LowerThirdsExample(getAll()),
            ScriptureExample::class.simpleName!! to ScriptureExample(getAll()),
            "ComposeExample" to ComposeNDIStreamer({ ComposeExample() })
        )
    }

    single {
        OpenSongTextProvider(get(ConfigurationStore::class))
            .apply { start() }
    }.bind(TextProvider::class)

    single {
        NDIStreamManager(get(ConfigurationStore::class), get())
    }
}