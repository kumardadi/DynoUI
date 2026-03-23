package com.khudama.dynoui.core.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class DynoNode(
    val type: String,
    val id: String? = null,
    val properties: JsonObject? = null,
    val classes: List<String>? = null,
    val modifiers: List<DynoModifier>? = null,
    val child: DynoNode? = null,
    val children: List<DynoNode>? = null,
    val action: DynoAction? = null
)

@Serializable
data class DynoModifier(
    val type: String,
    val properties: JsonObject? = null
)

@Serializable
data class DynoAction(
    val type: String,
    val properties: JsonObject? = null
)
