package com.khudama.dynoui.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khudama.dynoui.compose.DynoRenderer
import com.khudama.dynoui.compose.applyDynoModifiers
import com.khudama.dynoui.core.model.DynoNode
import kotlinx.serialization.json.*

fun registerCoreComponents() {
    DynoRenderer.registry.register("column") { node ->
        val arrStr = node.properties?.get("verticalArrangement")?.jsonPrimitive?.content
        val alignStr = node.properties?.get("horizontalAlignment")?.jsonPrimitive?.content
        
        val arrangement = when(arrStr) { 
            "space_between" -> Arrangement.SpaceBetween
            else -> Arrangement.Top 
        }
        val alignment = when(alignStr) {
            "center_horizontally" -> Alignment.CenterHorizontally
            "end" -> Alignment.End
            else -> Alignment.Start
        }
        Column(
            modifier = Modifier.applyDynoModifiers(node.modifiers), 
            verticalArrangement = arrangement,
            horizontalAlignment = alignment
        ) {
            node.children?.forEach { DynoRenderer.Render(it) }
        }
    }
    DynoRenderer.registry.register("row") { node ->
        val arrStr = node.properties?.get("horizontalArrangement")?.jsonPrimitive?.content
        val arrangement = when(arrStr) { 
            "space_evenly" -> Arrangement.SpaceEvenly
            "space_between" -> Arrangement.SpaceBetween
            else -> Arrangement.Start 
        }
        Row(
            modifier = Modifier.applyDynoModifiers(node.modifiers),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = arrangement
        ) {
            node.children?.forEach { DynoRenderer.Render(it) }
        }
    }
    DynoRenderer.registry.register("box") { node ->
        Box(modifier = Modifier.applyDynoModifiers(node.modifiers), contentAlignment = Alignment.Center) {
            node.children?.forEach { DynoRenderer.Render(it) }
        }
    }
    DynoRenderer.registry.register("text") { node ->
        val text = node.properties?.get("text")?.jsonPrimitive?.content ?: ""
        val colorHex = node.properties?.get("color")?.jsonPrimitive?.content
        val fontSize = node.properties?.get("fontSize")?.jsonPrimitive?.intOrNull
        val fontWeight = node.properties?.get("fontWeight")?.jsonPrimitive?.content
        
        Text(
            text = text,
            modifier = Modifier.applyDynoModifiers(node.modifiers),
            color = colorHex?.let { try { Color(it.removePrefix("#").toLong(16) or 0x00000000FF000000) } catch (e: Exception) { Color.Unspecified } } ?: Color.Unspecified,
            fontSize = fontSize?.sp ?: 14.sp,
            fontWeight = if (fontWeight == "bold") FontWeight.Bold else FontWeight.Normal
        )
    }
    DynoRenderer.registry.register("spacer") { node ->
        val height = node.properties?.get("height")?.jsonPrimitive?.intOrNull ?: 0
        val width = node.properties?.get("width")?.jsonPrimitive?.intOrNull ?: 0
        Spacer(modifier = Modifier.height(height.dp).width(width.dp).applyDynoModifiers(node.modifiers))
    }
}
