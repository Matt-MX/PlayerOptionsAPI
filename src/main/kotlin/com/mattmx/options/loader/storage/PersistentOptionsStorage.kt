package com.mattmx.options.loader.storage

import com.mattmx.options.OptionsHolder
import java.util.UUID

interface PersistentOptionsStorage {

    fun getOptions(uniqueId: UUID) : OptionsHolder?

    fun setOptions(uniqueId: UUID, options: OptionsHolder)

}