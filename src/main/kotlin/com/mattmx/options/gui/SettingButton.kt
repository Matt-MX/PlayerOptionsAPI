package com.mattmx.options.gui

import com.mattmx.ktgui.components.button.GuiButton
import com.mattmx.ktgui.event.EventCallback
import com.mattmx.options.Setting
import com.mattmx.options.loader.options
import org.bukkit.Material

open class SettingButton<T>(
    val setting: Setting<T>
) : GuiButton<SettingButton<T>>(Material.STONE)

open class BooleanSettingButton(
    setting: Setting<Boolean>
) : SettingButton<Boolean>(setting) {
    val onEnabled = EventCallback<BooleanSettingButton>()
    val onDisabled = EventCallback<BooleanSettingButton>()

    init {
        click {
            left {
                val options = player.options
                options(setting, !options(setting))
            }
        }
    }

    fun <T> state(state: Boolean, func: KFunction2<BooleanSettingButton, T>, arg: T) = apply {
        if (state) {
            onEnabled {
                func.call(this, arg)
            }
        } else {
            onDisabled {
                func.call(this, arg)
            }
        }
    }

    fun state(state: Boolean, block: BooleanSettingButton.() -> Unit) = apply {
        if (state) {
            onEnabled(block)
        } else onDisabled(block)
    }

}

fun OptionsGui.button(setting: Setting<Boolean>) =
    BooleanSettingButton(setting).apply { childOf(this@button) }