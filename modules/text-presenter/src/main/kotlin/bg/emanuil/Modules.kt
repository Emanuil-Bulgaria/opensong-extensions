package bg.emanuil

import bg.emanuil.draw.ComposeNDIStreamer
import bg.emanuil.draw.OpenSongRepository
import bg.emanuil.draw.OpenSongTextProvider
import bg.emanuil.draw.TextProvider
import bg.emanuil.ndi.NDIStreamManager
import bg.emanuil.presentation.compose_example.*
import bg.emanuil.watcher.ConfigurationStore
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.ParametersHolder
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.onClose

inline fun <reified T : Service> Module.service(
    noinline definition: Scope.(ParametersHolder) -> T
) : KoinDefinition<T> {
    return single {
        val service = definition(it)
        service.start()
        service
    } onClose { instance ->
        instance?.stop()
    }
}

val modules = module {
    single {
        ConfigurationStore()
    }

    single {
        OpenSongRepository(get()).apply { start() }
    }

    viewModelOf(::ComposeExampleViewModel)
    viewModelOf(::LowerThirdsSongViewModel)
    viewModelOf(::LowerThirdsScriptureViewModel)

    single {
        mapOf(
            LowerThirdsExample::class.simpleName!! to LowerThirdsExample(getAll()),
            ScriptureExample::class.simpleName!! to ScriptureExample(getAll()),
            "LowerThirdsSong" to ComposeNDIStreamer({ LowerThirdsSong() }),
            "LowerThirdsScripture" to ComposeNDIStreamer({ LowerThirdsScripture() })
        )
    }

    single {
        OpenSongTextProvider(get(ConfigurationStore::class))
            .apply { start() }.apply { start() }
    }.bind(TextProvider::class)

    single {
        NDIStreamManager(get(ConfigurationStore::class), get())
    }
}