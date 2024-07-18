package com.mattmx.options.gui

import com.mattmx.ktgui.components.screen.pagination.GuiMultiPageScreen
import com.mattmx.options.loader.options
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

open class OptionsGui(
    val player: Player,
    title: Component,
    rows: Int = 6
) : GuiMultiPageScreen(title, rows) {
    val options = player.options
}

inline fun Player.createOptionsGui(title: Component, rows: Int = 6, block: OptionsGui.() -> Unit) =
    OptionsGui(this, title, rows).apply(block)