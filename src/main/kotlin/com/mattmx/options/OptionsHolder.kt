package com.mattmx.options

import com.mattmx.ktgui.utils.Invokable
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
        return allOptions.getOrPut(setting.id) { setting.clone() }.value as T
    }

    fun <T> set(setting: Setting<T>, newValue: Any?) = invoke(setting, newValue)
    operator fun <T> invoke(setting: Setting<T>, newValue: Any?) {
        newValue ?: return
        val existing = allOptions[setting.id]
            ?: setting.clone()

        return existing.setAny(newValue)
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