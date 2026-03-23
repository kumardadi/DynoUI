package com.khudama.dynoui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.khudama.dynoui.core.model.DynoModifier
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun Modifier.applyDynoModifiers(modifiers: List<DynoModifier>?): Modifier {
    if (modifiers == null) return this
    val actionHandler = LocalDynoActionHandler.current
    var current = this
    for (mod in modifiers) {
        current = when (mod.type.lowercase()) {
            "clickable" -> {
                val actionId = mod.properties?.get("actionId")?.jsonPrimitive?.content ?: ""
                current.clickable { actionHandler(actionId) }
            }
            "padding" -> {
                val all = mod.properties?.get("all")?.jsonPrimitive?.intOrNull ?: 0
                val horizontal = mod.properties?.get("horizontal")?.jsonPrimitive?.intOrNull ?: 0
                val vertical = mod.properties?.get("vertical")?.jsonPrimitive?.intOrNull ?: 0
                val top = mod.properties?.get("top")?.jsonPrimitive?.intOrNull ?: 0
                val bottom = mod.properties?.get("bottom")?.jsonPrimitive?.intOrNull ?: 0
                if (all > 0) current.padding(all.dp)
                else if (horizontal > 0 || vertical > 0) current.padding(horizontal = horizontal.dp, vertical = vertical.dp)
                else current.padding(top = top.dp, bottom = bottom.dp)
            }
            "fillmaxsize" -> current.fillMaxSize()
            "fillmaxwidth" -> current.fillMaxWidth()
            "size" -> {
                val size = mod.properties?.get("value")?.jsonPrimitive?.intOrNull ?: 0
                current.size(size.dp)
            }
            "height" -> {
                val size = mod.properties?.get("value")?.jsonPrimitive?.intOrNull ?: 0
                current.height(size.dp)
            }
            "width" -> {
                val size = mod.properties?.get("value")?.jsonPrimitive?.intOrNull ?: 0
                current.width(size.dp)
            }
            "background" -> {
                val colorHex = mod.properties?.get("color")?.jsonPrimitive?.content ?: "#FFFFFF"
                val cornerRadius = mod.properties?.get("cornerRadius")?.jsonPrimitive?.intOrNull ?: 0
                val color = try {
                    Color(colorHex.removePrefix("#").toLong(16) or 0x00000000FF000000)
                } catch (e: Exception) { Color.Transparent }
                current.background(color, RoundedCornerShape(cornerRadius.dp))
            }
            else -> current
        }
    }
    return current
}
