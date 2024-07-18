package com.mattmx.options.loader

import com.mattmx.options.OptionsHolder
import java.util.UUID

interface OptionsManager {

    fun getOptions(uniqueId: UUID) : OptionsHolder

    fun saveOptions(uniqueId: UUID)

    fun stop() {}

}