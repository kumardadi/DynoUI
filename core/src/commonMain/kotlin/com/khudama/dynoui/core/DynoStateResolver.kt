package com.khudama.dynoui.core

import kotlinx.serialization.json.*

object DynoStateResolver {
    
    fun resolve(properties: JsonObject?, state: Map<String, String>): JsonObject? {
        if (properties == null || state.isEmpty()) return properties
        return resolveElement(properties, state) as JsonObject
    }

    private fun resolveElement(node: JsonElement, state: Map<String, String>): JsonElement {
        return when (node) {
            is JsonObject -> {
                val newMap = node.mapValues { (_, value) -> resolveElement(value, state) }
                JsonObject(newMap)
            }
            is JsonArray -> {
                val newList = node.map { resolveElement(it, state) }
                JsonArray(newList)
            }
            is JsonPrimitive -> {
                if (node.isString) {
                    val resolvedString = interpolate(node.content, state)
                    JsonPrimitive(resolvedString)
                } else {
                    node
                }
            }
            else -> node
        }
    }

    private fun interpolate(text: String, state: Map<String, String>): String {
        var result = text
        state.forEach { (key, value) ->
            result = result.replace("\${$key}", value)
        }
        return result
    }
}
