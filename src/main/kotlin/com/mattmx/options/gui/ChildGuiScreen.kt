package com.mattmx.options.gui

import com.mattmx.ktgui.components.screen.GuiScreen

fun interface ChildGuiScreen {

    fun getParentGui(): GuiScreen?

}