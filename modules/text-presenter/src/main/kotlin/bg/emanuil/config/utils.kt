package bg.emanuil.config

import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.module.dsl.new
import org.koin.core.parameter.ParametersHolder
import org.koin.core.scope.Scope
import org.koin.dsl.onClose

inline fun <reified R : Service> Module.service(
    createdAtStart: Boolean = false,
    noinline definition: Scope.(ParametersHolder) -> R
): KoinDefinition<R> {
    return single(
        createdAtStart = createdAtStart
    ) {
        definition(it).apply { start() }
    }.onClose { it?.stop() }
}

inline fun <reified R : Service> Module.serviceOf(
    crossinline constructor: () -> R
) = service { new(constructor) }

inline fun <reified R : Service, reified P1> Module.serviceOf(
    crossinline constructor: (P1) -> R
) = service { new(constructor) }

inline fun <reified R : Service, reified P1, reified P2> Module.serviceOf(
    crossinline constructor: (P1, P2) -> R
) = service { new(constructor) }
