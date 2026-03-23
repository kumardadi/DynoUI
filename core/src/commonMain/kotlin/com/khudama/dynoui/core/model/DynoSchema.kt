package com.khudama.dynoui.core.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class DynoSchema(
    val apiVersion: String? = null,
    val theme: DynoTheme? = null,
    val actions: Map<String, DynoAction>? = null,
    val layout: Map<String, DynoNode>
)

@Serializable
data class DynoTheme(
    val colors: Map<String, String>? = null,
    val typography: Map<String, DynoTypography>? = null,
    val classes: Map<String, DynoThemeClass>? = null
)

@Serializable
data class DynoThemeClass(
    val properties: JsonObject? = null,
    val modifiers: List<DynoModifier>? = null
)

@Serializable
data class DynoTypography(
    val fontSize: Int? = null,
    val fontWeight: String? = null,
    val fontFamily: String? = null
)
