package com.mattmx.options.loader

import com.mattmx.options.OptionsHolder
import com.mattmx.options.OptionsPluginImpl
import com.mattmx.options.loader.storage.YamlOptionsStorage
import java.io.File
import java.time.Duration
import java.util.*

class PersistentOptionsManager(
    val pluginImpl: OptionsPluginImpl
) : OptionsManager {
    private val cache = Collections.synchronizedMap(hashMapOf<UUID, OptionsHolder>())
    private var storageImpl = YamlOptionsStorage(
        File("${pluginImpl.dataFolder}/data.yml"),
        Duration.ofMinutes(5L).toSeconds() * 20L
    )

    override fun getOptions(uniqueId: UUID) = cache.getOrPut(uniqueId) {
        storageImpl.getOptions(uniqueId) ?: OptionsHolder.default().clone()
    }

    override fun saveOptions(uniqueId: UUID) {
        storageImpl.setOptions(uniqueId, getOptions(uniqueId))
    }

    fun freeFromCache(uniqueId: UUID) {
        this.cache.remove(uniqueId)
    }
}