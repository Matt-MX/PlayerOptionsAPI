package com.mattmx.options

import com.mattmx.ktgui.GuiManager
import com.mattmx.ktgui.dsl.event
import com.mattmx.options.loader.OptionsManager
import com.mattmx.options.loader.PersistentOptionsManager
import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

class OptionsPluginImpl : JavaPlugin() {
    var optionsManager: OptionsManager = PersistentOptionsManager(this)
        set(value) {
            field.stop()
            field = value
        }

    override fun onEnable() {
        instance = this
        GuiManager.init(this)

        // Load pre join
        event<AsyncPlayerPreLoginEvent>(EventPriority.MONITOR) {
            if (loginResult != AsyncPlayerPreLoginEvent.Result.ALLOWED) return@event

            optionsManager.getOptions(uniqueId)
        }

        // Release from cache
        event<PlayerQuitEvent> {
            (optionsManager as? PersistentOptionsManager)?.freeFromCache(player.uniqueId)
        }
    }

    companion object {
        private lateinit var instance: OptionsPluginImpl
        fun get() = instance
    }
}