package com.mattmx.options.gui

import com.mattmx.ktgui.components.screen.pagination.GuiMultiPageScreen
import com.mattmx.ktgui.extensions.getOpenGui
import com.mattmx.ktgui.scheduling.sync
import com.mattmx.options.loader.defaultOptions
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent

open class OptionsGui(
    val player: Player,
    title: Component,
    rows: Int = 6
) : GuiMultiPageScreen(title, rows) {
    val options = player.defaultOptions
    var dirty = false
        private set

    init {
        close {
            sync {
                val nextOpenGui = this@OptionsGui.player.getOpenGui()
                    ?: return@sync saveIfDirty()

                // todo should be impl in ktgui natively tbh
                if (nextOpenGui is ChildGuiScreen && nextOpenGui.getParentGui() != this@OptionsGui) {
                    saveIfDirty()
                }
            }
        }
    }

    fun markDirty() {
        this.dirty = true
    }

    fun unmarkDirty() {
        this.dirty = false
    }

    fun saveIfDirty() {
        if (dirty) {
            options.save(player)
        }
    }
}

inline fun Player.createOptionsGui(title: Component, rows: Int = 6, block: OptionsGui.() -> Unit) =
    OptionsGui(this, title, rows).apply(block)