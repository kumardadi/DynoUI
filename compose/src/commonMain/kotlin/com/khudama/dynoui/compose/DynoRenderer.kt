package com.khudama.dynoui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.khudama.dynoui.core.DynoRegistry
import com.khudama.dynoui.core.model.DynoNode

typealias DynoComposable = @Composable (DynoNode) -> Unit

val LocalDynoActionHandler = staticCompositionLocalOf<(String) -> Unit> {
    { println("Unhandled DynoAction fired: $it") }
}

object DynoRenderer {
    
    val registry = DynoRegistry<DynoComposable>()

    @Composable
    fun Render(node: DynoNode) {
        val component = registry.get(node.type)
        if (component != null) {
            component.invoke(node)
        } else {
            // Versioning fallback: Ignore unknown components silently to prevent crashing older app versions
        }
    }
}
