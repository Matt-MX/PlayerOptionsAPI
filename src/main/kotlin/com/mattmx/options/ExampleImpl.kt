package com.mattmx.options

import com.mattmx.ktgui.commands.declarative.runs
import com.mattmx.ktgui.utils.not
import com.mattmx.options.gui.button
import com.mattmx.options.gui.createOptionsGui
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class ExampleImpl : JavaPlugin() {
    val showDeathAnimation by setting(true)

    override fun onEnable() {

        "settings".runs<Player> {

            sender.createOptionsGui(!"Settings") {
                page(0) {
                    button(showDeathAnimation)
                        .state(true) {
                            material(Material.LIME_BED)
                            named(!"&aShow Death Animations")
                            lore(!"&fCurrently &aenabled")
                        }.state(false) {
                            material(Material.GRAY_BED)
                            named(!"&cShow Death Animations")
                            lore(!"&fCurrently &cenabled")
                        } slot 10
                }
            }

        } register this

    }

}