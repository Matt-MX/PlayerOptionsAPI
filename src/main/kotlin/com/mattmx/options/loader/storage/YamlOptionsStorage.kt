package com.mattmx.options.loader.storage

import com.mattmx.ktgui.scheduling.async
import com.mattmx.ktgui.scheduling.asyncRepeat
import com.mattmx.options.OptionsHolder
import com.mattmx.options.OptionsPluginImpl
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

class YamlOptionsStorage(
    val file: File,
    val autoSavePeriod: Long
) : PersistentOptionsStorage {
    val yaml = file.let { f ->
        if (!f.exists()) {
            f.parentFile.mkdirs()
            f.createNewFile()
        }
        YamlConfiguration.loadConfiguration(f)
    }
    private val unknownIdsLogged: MutableSet<String> = Collections.synchronizedSet(mutableSetOf<String>())

    override fun start() {
        asyncRepeat(autoSavePeriod) { save() }
    }

    fun save() {
        yaml.save(file)
    }

    override fun getOptions(uniqueId: UUID): OptionsHolder? {
        val section = yaml.getConfigurationSection(uniqueId.toString())
            ?: return null

        val holder = OptionsHolder()
        for (key in section.getKeys(false)) {
            val existingSetting = OptionsHolder.settings[key]

            if (existingSetting == null) {
                // Log unknown key once
                if (!unknownIdsLogged.contains(key)) {
                    OptionsPluginImpl.get().logger.warning("Unknown setting ID '$key' found for player $uniqueId.")
                    unknownIdsLogged.add(key)
                }
                continue
            }

            holder(existingSetting, section.get(key, existingSetting.defaultOption))
        }
        return holder
    }

    override fun setOptions(uniqueId: UUID, options: OptionsHolder) {
        val section = yaml.getConfigurationSection(uniqueId.toString())
            ?: yaml.createSection(uniqueId.toString())

        for (setting in options.allOptions.values) {
            section.set(setting.id, setting.value)
        }

        yaml.set(uniqueId.toString(), section)
    }
}