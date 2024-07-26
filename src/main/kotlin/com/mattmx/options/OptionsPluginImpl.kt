package com.mattmx.options

import com.mattmx.ktgui.GuiManager
import com.mattmx.ktgui.commands.declarative.invoke
import com.mattmx.ktgui.dsl.event
import com.mattmx.ktgui.utils.not
import com.mattmx.options.example.ExampleImpl
import com.mattmx.options.loader.OptionsManager
import com.mattmx.options.loader.PersistentOptionsManager
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

class OptionsPluginImpl : JavaPlugin() {
    var optionsManager = PersistentOptionsManager(this)
        set(value) {
            field.stop()
            field = value
            value.start()
        }

    override fun onEnable() {
        GuiManager.init(this)
        instance = this

        optionsManager.start()

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