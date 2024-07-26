package com.mattmx.options.example

import org.bukkit.Material
import org.bukkit.configuration.serialization.ConfigurationSerializable

enum class TerrainTypes(
    val material: Material,
    val displayName: String
) : ConfigurationSerializable {
    DESERT_VANILLA(Material.SAND, "Desert"),
    DESERT_MOONSAND(Material.SANDSTONE, "MoonSand (Desert)"),
    PLAINS_VANILLA(Material.GRASS_BLOCK, "Plains"),
    PLAINS_MOONSAND(Material.DIRT_PATH, "MoonSand (Plains)")
    ;

    override fun toString() = displayName
    override fun serialize(): MutableMap<String, Any> {
        return mutableMapOf("value" to name)
    }

    companion object {
        @JvmStatic
        fun deserialize(args: Map<String, Any>) : TerrainTypes =
            args["value"]?.let { valueOf(it.toString()) } ?: DESERT_VANILLA
    }
}