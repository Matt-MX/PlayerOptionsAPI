package com.mattmx.options

import com.mattmx.ktgui.utils.Invokable
import org.bukkit.entity.Player
import java.util.UUID
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class OptionsHolder : Invokable<OptionsHolder> {
    val allOptions = hashMapOf<String, Setting<*>>()

    fun <T> setting(default: T) = Setting(DELEGATED_NAME, default).let {
        return@let ReadOnlyProperty { _: OptionsHolder, prop: KProperty<*> ->
            if (it.id == DELEGATED_NAME) {
                it.id = prop.name
                allOptions[it.id] = it
            }
            it
        }
    }

    fun <T> get(setting: Setting<T>) = invoke(setting)
    operator fun <T> invoke(setting: Setting<T>): T {
        return allOptions[setting.id]?.value as? T ?: setting.defaultOption
    }

    fun <T> set(setting: Setting<T>, newValue: Any?) = invoke(setting, newValue)
    operator fun <T> invoke(setting: Setting<T>, newValue: Any?) = apply {
        newValue ?: return@apply
        val existing = allOptions.getOrPut(setting.id) { setting.clone() }

        existing.setAny(newValue)
    }

    fun save(player: Player) = save(player.uniqueId)
    fun save(uniqueId: UUID) {
        OptionsPluginImpl.get().optionsManager.saveOptions(uniqueId)
    }

    fun clone() = OptionsHolder().apply {
        allOptions.putAll(
            this@OptionsHolder.allOptions
                .mapValues { (k, v) -> v.cloneWithValue() }
                .toMap()
        )
    }

    companion object {
        const val DELEGATED_NAME = "DELEGATED_PROP_NAME"
        val DEFAULT = OptionsHolder()
        fun default() = DEFAULT
        val settings = hashMapOf<String, Setting<*>>()
    }
}

fun <T> setting(default: T) = Setting(OptionsHolder.DELEGATED_NAME, default).let {
    return@let ReadOnlyProperty { _: Any, prop: KProperty<*> ->
        if (it.id == OptionsHolder.DELEGATED_NAME) {
            it.id = prop.name
            OptionsHolder.settings[prop.name] = it
        }
        it
    }
}