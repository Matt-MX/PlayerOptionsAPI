package com.mattmx.options.gui

import com.mattmx.ktgui.components.button.GuiButton
import com.mattmx.ktgui.event.EventCallback
import com.mattmx.options.OptionsHolder
import com.mattmx.options.Setting
import com.mattmx.options.loader.defaultOptions
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class SettingButton<T>(
    val optionsHolder: OptionsHolder,
    val setting: Setting<T>
) : GuiButton<SettingButton<T>>(Material.STONE)

open class BooleanSettingButton(
    optionsHolder: OptionsHolder,
    setting: Setting<Boolean>
) : SettingButton<Boolean>(optionsHolder, setting) {
    val onEnabled = EventCallback<BooleanSettingButton>()
    val onDisabled = EventCallback<BooleanSettingButton>()
    var saveImmediately = false

    init {
        click {
            left {
                val newState = !optionsHolder(setting)

                optionsHolder(setting, newState)

                if (saveImmediately) {
                    (parent as OptionsGui).unmarkDirty()
                    optionsHolder.save(player)
                } else {
                    (parent as OptionsGui).markDirty()
                }

                setItem(newState)

                update()
            }
        }
    }

    override fun getItemStack(): ItemStack? {
        setItem(optionsHolder(setting))
        return super.getItemStack()
    }

    fun setItem(state: Boolean) {
        if (state) onEnabled(this@BooleanSettingButton)
        else onDisabled(this@BooleanSettingButton)
    }

    fun <T> state(state: Boolean, func: (BooleanSettingButton, T) -> Unit, arg: T) = apply {
        if (state) {
            onEnabled {
                func(this, arg)
            }
        } else {
            onDisabled {
                func(this, arg)
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
    BooleanSettingButton(options, setting).apply { childOf(this@button) }