package org.spectralpowered.client.ui

import com.formdev.flatlaf.extras.FlatSVGIcon
import org.spectralpowered.common.inject
import org.spectralpowered.logger.Logger
import java.awt.*
import java.awt.dnd.DragSource
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*
import java.util.Timer
import javax.swing.*

class Sidebar : JToolBar() {

    private val dragHandler = DragHandler()

    private val menuButton = JButton()
    private val devToolsButton = JButton()

    init {
        margin = Insets(3, 3, 3, 3)
        orientation = VERTICAL
        isFloatable = false
        this.initDragHandler()
        this.initButtons()
    }

    private fun initButtons() {
        menuButton.register("images/ui/menu.svg")
        devToolsButton.register("images/ui/devtools.svg")
    }

    private fun initDragHandler() {
        addMouseListener(dragHandler)
        addMouseMotionListener(dragHandler)
    }

    private fun JButton.register(iconPath: String) {
        val icon = FlatSVGIcon(iconPath).derive(18, 18)
        this.icon = icon
        this.isOpaque = false
        this.addMouseListener(object : TimedMouseAdapter() {
            private var isDragging = false
            override fun longMousePressed(e: MouseEvent) {
                if(!isDragging) {
                    isDragging = true

                    val source = e.source as Container
                    val parentEvent = SwingUtilities.convertMouseEvent(source, e, source.parent)
                    e.dispatchToParent()
                    dragHandler.startDragging(source, parentEvent.point)
                }
            }

            override fun shortMousePressed(e: MouseEvent) {
                Logger.info("Short mouse press")
            }

            override fun mouseDragged(e: MouseEvent) {
                if(isDragging) e.dispatchToParent()
            }

            private fun MouseEvent.dispatchToParent() {
                val source = this.source as Container
                val parentEvent = SwingUtilities.convertMouseEvent(source, this, source.parent)
                source.parent.dispatchEvent(parentEvent)
            }
        })
        this@Sidebar.add(this)
    }

    private class DragHandler : MouseAdapter() {
        private val spectralUI: SpectralUI by inject()

        private val window = JWindow(spectralUI.frame)
        private var draggingComponent: Component? = null
        private var index: Int = -1
        private val gap = Box.createHorizontalStrut(24)
        private var startPoint: Point = Point()
        private val dragThreshold = DragSource.getDragThreshold()

        override fun mousePressed(e: MouseEvent) {
            val parent = e.component as Container
            if(parent.componentCount > 0) {
                startPoint.location = e.point
                window.background = Color(0x0, true)
            }
        }

        override fun mouseDragged(e: MouseEvent) {
            val pt = e.point
            val parent = e.component as Container

            if(!window.isVisible || draggingComponent == null) {
                if(startPoint.distance(pt) > dragThreshold) {
                    startDragging(parent, pt)
                }
                return
            }

            val dim = draggingComponent!!.preferredSize
            val p = Point(pt.x - dim.width / 2, pt.y - dim.height / 2)
            SwingUtilities.convertPointToScreen(p, parent)
            window.location = Point(draggingComponent!!.locationOnScreen.x, p.y)

            for(i in 0 until parent.componentCount) {
                val c = parent.getComponent(i)
                val rect = c.bounds
                val hd2 = rect.height / 2
                PREV_AREA.bounds = Rectangle(rect.x, rect.y, rect.width, hd2)
                NEXT_AREA.bounds = Rectangle(rect.x, rect.y + hd2, rect.width, hd2)

                if(PREV_AREA.contains(pt)) {
                    swapComponentLocation(parent, gap, gap, if(i > 1) i else 0)
                    return
                } else if(NEXT_AREA.contains(pt)) {
                    swapComponentLocation(parent, gap, gap, i)
                    return
                }
            }

            parent.remove(gap)
            parent.revalidate()
        }

        override fun mouseReleased(e: MouseEvent) {
            if(!window.isVisible || draggingComponent == null) return
            val pt = e.point
            val parent = e.component as Container

            val curComp = draggingComponent!!
            draggingComponent = null
            window.isVisible = false

            for(i in 0 until parent.componentCount) {
                val c = parent.getComponent(i)
                val rect = c.bounds
                val hd2 = rect.height / 2
                PREV_AREA.bounds = Rectangle(rect.x, rect.y, rect.width, hd2)
                NEXT_AREA.bounds = Rectangle(rect.x, rect.y + hd2, rect.width, hd2)
                if(PREV_AREA.contains(pt)) {
                    swapComponentLocation(parent, gap, curComp, if(i > 1) i else 0)
                    return
                } else if(NEXT_AREA.contains(pt)) {
                    swapComponentLocation(parent, gap, curComp, i)
                    return
                }
            }

            if(parent.bounds.contains(pt)) {
                swapComponentLocation(parent, gap, curComp, parent.componentCount)
            } else {
                swapComponentLocation(parent, gap, curComp, index)
            }
        }

        fun startDragging(parent: Container, pt: Point) {
            val c = parent.getComponentAt(pt)
            index = parent.getComponentZOrder(c)

            if(c == parent || index < 0) return

            draggingComponent = c
            swapComponentLocation(parent, c, gap, index)

            window.add(draggingComponent)
            window.pack()

            val dim = draggingComponent!!.preferredSize
            val p = Point(pt.x - dim.width / 2, pt.y - dim.height / 2)
            SwingUtilities.convertPointToScreen(p, parent)
            window.location = p
            window.isVisible = true
        }

        private fun swapComponentLocation(
            parent: Container,
            remove: Component,
            add: Component,
            idx: Int
        ) {
            parent.remove(remove)
            parent.add(add, idx)
            parent.revalidate()
        }

        companion object {
            private var PREV_AREA = Rectangle()
            private var NEXT_AREA = Rectangle()
        }
    }

    private abstract class TimedMouseAdapter(private val delay: Long = 500L) : MouseAdapter() {
        private var mousePressedTime = 0L
        private var timer: Timer? = null

        abstract fun shortMousePressed(e: MouseEvent)
        abstract fun longMousePressed(e: MouseEvent)

        override fun mousePressed(e: MouseEvent) {
            mousePressedTime = e.`when`
            if(timer != null) {
                timer!!.cancel()
            }
            timer = Timer()
            timer!!.schedule(object : TimerTask() {
                override fun run() {
                    longMousePressed(e)
                    this.cancel()
                }
            }, delay)
        }

        override fun mouseReleased(e: MouseEvent) {
            timer?.cancel()
            if(e.`when` - mousePressedTime <= delay) {
                shortMousePressed(e)
            }
        }
    }
}