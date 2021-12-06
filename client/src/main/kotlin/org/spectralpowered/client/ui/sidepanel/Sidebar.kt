package org.spectralpowered.client.ui.sidepanel

import com.formdev.flatlaf.extras.FlatSVGIcon
import com.formdev.flatlaf.ui.FlatToolBarUI
import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.lang.Math.toRadians
import javax.swing.*
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

class Sidebar : JToolBar() {

    private lateinit var buttonDragListener: SidebarComponentMover

    private val menuButton = JButton()
    private val devToolsButton = JButton()
    private val worldSwitcherButton = JButton()
    private val newsAndUpdatesButton = JButton()
    private val infoButton = JButton()

    init {
        margin = Insets(3, 3, 3, 3)
        orientation = VERTICAL
        isRollover = false
        isBorderPainted = false
        this.initButtons()
        this.initButtonMover()
        setUI(object : FlatToolBarUI() {
            override fun paintDragWindow(g: Graphics) {
                val img = when(dragWindow.orientation) {
                    toolBar.orientation -> createPreviewImage(toolBar)
                    else -> createPreviewImage(toolBar,
                        ((dragWindow.orientation - toolBar.orientation) * 90).toDouble()
                    )
                }
                g.drawImage(img, 0, 0, null)
            }

            override fun setFloating(b: Boolean, p: Point?) {
                super.setFloating(false, p)
            }
        })
    }

    private fun initButtons() {
        menuButton.toolTipText = "Expand Menu"
        menuButton.icon = FlatSVGIcon("images/ui/menu.svg").derive(18, 18)
        add(menuButton)
        addSeparator()
        add(Box.createRigidArea(Dimension(2, 2)))

        devToolsButton.toolTipText = "Developer Tools"
        devToolsButton.icon = FlatSVGIcon("images/ui/devtools.svg").derive(18, 18)
        add(devToolsButton)
        add(Box.createRigidArea(Dimension(2, 2)))

        worldSwitcherButton.toolTipText = "World Switcher"
        worldSwitcherButton.icon = FlatSVGIcon("images/ui/worldswitcher.svg").derive(18, 18)
        add(worldSwitcherButton)
        add(Box.createRigidArea(Dimension(2, 2)))

        newsAndUpdatesButton.toolTipText = "News and Updates"
        newsAndUpdatesButton.icon = FlatSVGIcon("images/ui/news.svg").derive(18, 18)
        add(newsAndUpdatesButton)
        add(Box.createRigidArea(Dimension(2, 2)))

        infoButton.toolTipText = "Information"
        infoButton.icon = FlatSVGIcon("images/ui/info.svg").derive(18, 18)
        add(infoButton)
        add(Box.createRigidArea(Dimension(2, 2)))

        add(Box.createHorizontalGlue())
    }

    private fun initButtonMover() {
        buttonDragListener = SidebarComponentMover()
        buttonDragListener.isChangeCursor = false
        buttonDragListener.edgeInsets = Insets(5, 5, 5, 5)
        buttonDragListener.registerComponent(menuButton, devToolsButton, worldSwitcherButton, newsAndUpdatesButton, infoButton)
    }

    companion object {

        private fun createPreviewImage(component: Component, rotation: Double = 0.0): BufferedImage {
            val image = BufferedImage(
                component.width,
                component.height,
                BufferedImage.TYPE_INT_ARGB
            )
            component.paint(image.graphics)
            return image.rotate(rotation)
        }

        private fun BufferedImage.rotate(angle: Double): BufferedImage {
            val rads = toRadians(angle)
            val sin = abs(sin(rads))
            val cos = abs(cos(rads))
            val w = this.width
            val h = this.height
            val nw = floor(w * cos + h * sin)
            val nh = floor(h * cos + w * sin)

            val rotated = BufferedImage(nw.toInt(), nh.toInt(), BufferedImage.TYPE_INT_ARGB)
            val g2d = rotated.createGraphics()
            val at = AffineTransform()
            at.translate((nw - w) / 2, (nh - h) / 2)

            val x = w / 2
            val y = h / 2

            at.rotate(rads, x.toDouble(), y.toDouble())
            g2d.transform = at
            g2d.drawImage(this, 0, 0, null)
            g2d.drawRect(0, 0, (nw - 1).toInt(), (nh - 1).toInt())
            g2d.dispose()

            return rotated
        }
    }
}