package com.khudama.dynoui.core

import com.khudama.dynoui.core.model.DynoSchema
import kotlinx.serialization.json.Json

object DynoParser {
    
    val jsonConfig = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }
    
    fun parseSchema(jsonString: String): DynoSchema {
        return jsonConfig.decodeFromString(DynoSchema.serializer(), jsonString)
    }
    
}
