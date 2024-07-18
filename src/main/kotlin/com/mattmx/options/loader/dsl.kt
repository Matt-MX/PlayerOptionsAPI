package com.mattmx.options.loader

import com.mattmx.options.OptionsHolder
import com.mattmx.options.OptionsPluginImpl
import org.bukkit.entity.Player

val Player.options: OptionsHolder
    get() = OptionsPluginImpl.get()
        .optionsManager
        .getOptions(uniqueId)