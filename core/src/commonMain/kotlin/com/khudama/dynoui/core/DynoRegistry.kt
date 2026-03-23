package com.khudama.dynoui.core

class DynoRegistry<T> {
    private val registryMap = mutableMapOf<String, T>()

    fun register(type: String, renderer: T) {
        registryMap[type.lowercase()] = renderer
    }

    fun get(type: String): T? {
        return registryMap[type.lowercase()]
    }
    
    fun contains(type: String): Boolean {
        return registryMap.containsKey(type.lowercase())
    }
}
