package com.khudama.dynoui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.foundation.layout.fillMaxSize
import com.khudama.dynoui.core.DynoRegistry
import com.khudama.dynoui.core.DynoStateResolver
import com.khudama.dynoui.core.model.DynoNode

import com.khudama.dynoui.core.model.DynoTheme
import kotlinx.serialization.json.JsonObject

typealias DynoComposable = @Composable (DynoNode) -> Unit

val LocalDynoActionHandler = staticCompositionLocalOf<(String) -> Unit> {
    { println("Unhandled DynoAction fired: $it") }
}

val LocalDynoStringState = staticCompositionLocalOf<Map<String, String>> { emptyMap() }
val LocalDynoListState = staticCompositionLocalOf<Map<String, List<Map<String, String>>>> { emptyMap() }
val LocalDynoItemState = staticCompositionLocalOf<Map<String, String>> { emptyMap() }
val LocalDynoTheme = staticCompositionLocalOf<DynoTheme?> { null }

object DynoRenderer {
    
    val registry = DynoRegistry<DynoComposable>()

    @Composable
    fun Render(node: DynoNode) {
        val component = registry.get(node.type)
        if (component != null) {
            val theme = LocalDynoTheme.current
            
            // 1. Merge Theme Classes
            var mergedProps = node.properties
            val mergedModifiers = node.modifiers?.toMutableList() ?: mutableListOf()
            
            node.classes?.forEach { className ->
                val themeClass = theme?.classes?.get(className)
                if (themeClass != null) {
                    val tProps = themeClass.properties
                    if (tProps != null) {
                        val baseProps = tProps.toMutableMap()
                        mergedProps?.let { baseProps.putAll(it) }
                        mergedProps = JsonObject(baseProps)
                    }
                    
                    val tMods = themeClass.modifiers
                    if (tMods != null) {
                        mergedModifiers.addAll(0, tMods)
                    }
                }
            }
            
            // 2. Data Binding & Theme Colors
            val themeColors = theme?.colors?.mapKeys { "theme.colors.${"$"}{it.key}" } ?: emptyMap()
            val combinedState = themeColors + LocalDynoStringState.current + LocalDynoItemState.current
            val resolvedProps = DynoStateResolver.resolve(mergedProps, combinedState)
            
            component.invoke(node.copy(
                properties = resolvedProps,
                modifiers = if (mergedModifiers.isEmpty()) null else mergedModifiers
            ))
        } else {
            // Versioning fallback: Ignore unknown components silently
        }
    }
    
    @Composable
    fun RenderResponsive(schema: com.khudama.dynoui.core.model.DynoSchema) {
        androidx.compose.foundation.layout.BoxWithConstraints(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
            val width = maxWidth.value
            val key = when {
                width < 600 -> "compact"
                width < 840 -> "medium"
                else -> "expanded"
            }
            
            val layoutNode = schema.layout[key] ?: schema.layout["compact"] ?: schema.layout.values.firstOrNull()
            
            if (layoutNode != null) {
                androidx.compose.runtime.CompositionLocalProvider(
                    LocalDynoTheme provides schema.theme
                ) {
                    Render(layoutNode)
                }
            }
        }
    }
}
