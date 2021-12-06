package org.spectralpowered.client.ui.sidepanel

import com.formdev.flatlaf.extras.FlatSVGIcon
import java.awt.*
import javax.swing.*

class Sidebar : JToolBar() {

    private lateinit var buttonDragListener: SidebarComponentMover

    private val menuButton = JButton()
    private val devToolsButton = JButton()
    private val worldSwitcherButton = JButton()
    private val newsAndUpdatesButton = JButton()
    private val infoButton = JButton()

    init {
        margin = Insets(6, 3, 6, 3)
        orientation = VERTICAL
        this.initButtons()
        this.initButtonMover()
    }

    private fun initButtons() {
        menuButton.toolTipText = "Expand Menu"
        menuButton.icon = FlatSVGIcon("images/ui/menu.svg").derive(18, 18)
        add(menuButton)

        devToolsButton.toolTipText = "Developer Tools"
        devToolsButton.icon = FlatSVGIcon("images/ui/devtools.svg").derive(18, 18)
        add(devToolsButton)

        worldSwitcherButton.toolTipText = "World Switcher"
        worldSwitcherButton.icon = FlatSVGIcon("images/ui/worldswitcher.svg").derive(18, 18)
        add(worldSwitcherButton)

        newsAndUpdatesButton.toolTipText = "News and Updates"
        newsAndUpdatesButton.icon = FlatSVGIcon("images/ui/news.svg").derive(18, 18)
        add(newsAndUpdatesButton)

        infoButton.toolTipText = "Information"
        infoButton.icon = FlatSVGIcon("images/ui/info.svg").derive(18, 18)
        add(infoButton)
    }

    private fun initButtonMover() {
        buttonDragListener = SidebarComponentMover()
        buttonDragListener.isChangeCursor = false
        buttonDragListener.edgeInsets = Insets(0, 5, 0, 5)
        buttonDragListener.registerComponent(menuButton, devToolsButton, worldSwitcherButton, newsAndUpdatesButton, infoButton)
    }
}