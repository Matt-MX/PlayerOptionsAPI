package com.mattmx.options.example

import com.mattmx.ktgui.commands.declarative.arg.impl.booleanArgument
import com.mattmx.ktgui.commands.declarative.arg.impl.multiChoiceArgument
import com.mattmx.ktgui.commands.declarative.div
import com.mattmx.ktgui.commands.declarative.invoke
import com.mattmx.ktgui.components.screen.pagination.GuiCramMultiPageScreen
import com.mattmx.ktgui.dsl.button
import com.mattmx.ktgui.dsl.gui
import com.mattmx.ktgui.utils.not
import com.mattmx.options.Setting
import com.mattmx.options.gui.button
import com.mattmx.options.gui.createOptionsGui
import com.mattmx.options.loader.defaultOptions
import com.mattmx.options.setting
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class ExampleImpl {
    val showDeathAnimation by setting(true)
    val terrain by setting(TerrainTypes.DESERT_VANILLA)

    fun fakeOnEnable() {

        "settings" {

            runs<Player> {
                sender.createOptionsGui(!"Settings") {
                    page {
                        fun Setting<Boolean>.styledButton() =
                            button(this)
                                .state(true) {
                                    material(Material.LIME_BED)
                                    named(!"&aShow Death Animations")
                                    lore(!"&fCurrently &aenabled")
                                }.state(false) {
                                    material(Material.GRAY_BED)
                                    named(!"&cShow Death Animations")
                                    lore(!"&fCurrently &cdisabled")
                                }

                        button(options(terrain).material) {
                            named(!"&fTerrain Generator")
                            lore(!"&fCurrently &a${options(terrain)}")
                            click.left {
                                GuiCramMultiPageScreen(!"Terrain Selection").apply {
                                    +TerrainTypes.entries.mapIndexed { i, terrainType ->
                                        button(terrainType.material) {
                                            if (terrainType == options(terrain)) {
                                                named(!"&a${terrainType}")
                                                lore(!"&f(desc)")
                                                enchant { put(Enchantment.MENDING, 1) }
                                                postBuild {
                                                    addItemFlags(ItemFlag.HIDE_ENCHANTS)
                                                }
                                            } else {
                                                named(!"&c${terrainType}")
                                                lore(!"&f(desc)")

                                                click.left {
                                                    options(terrain, terrainType)

                                                    // todo ktgui needs a dif way of updating the component here
                                                    this@createOptionsGui.open(player)

                                                    this@createOptionsGui.markDirty()
                                                }
                                            }
                                        }
                                    }
                                }.open(player)
                            }
                        } slot 10

                        showDeathAnimation.styledButton() slot 12
                    }
                }.open(sender)
            }

            val boolean by booleanArgument()
            (showDeathAnimation.id / boolean) {
                runs<Player> {
                    sender.defaultOptions(showDeathAnimation, boolean())
                    reply(!"&aToggled death animations to ${boolean()}")
                }
            }

            val terrainType by multiChoiceArgument(
                TerrainTypes.entries.map { it.name.lowercase() to it }
            )
            (terrain.id / terrainType) {
                runs<Player> {
                    sender.defaultOptions(terrain, terrainType())
                    reply(!"&aTerrain type set to ${terrainType()}")
                }
            }
        } register this
    }

}