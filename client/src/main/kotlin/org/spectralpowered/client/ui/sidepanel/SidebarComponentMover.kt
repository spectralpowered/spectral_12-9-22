package org.spectralpowered.client.ui.sidepanel

import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.SwingUtilities

open class SidebarComponentMover : MouseAdapter {

    companion object {
        private var PREV_AREA = Rectangle()
        private var NEXT_AREA = Rectangle()
    }

    private var index: Int = -1

    /**
     * Get the drag insets
     *
     * @return  the drag insets
     */
    /**
     * Set the drag insets. The insets specify an area where mouseDragged
     * events should be ignored and therefore the component will not be moved.
     * This will prevent these events from being confused with a
     * MouseMotionListener that supports component resizing.
     *
     * @param  dragInsets
     */
    var dragInsets = Insets(0, 0, 0, 0)
    var snapSize = Dimension(1, 1)
    /**
     * Get the bounds insets
     *
     * @return  the bounds insets
     */
    /**
     * Set the edge insets. The insets specify how close to each edge of the parent
     * component that the child component can be moved. Positive values means the
     * component must be contained within the parent. Negative values means the
     * component can be moved outside the parent.
     *
     * @param  edgeInsets
     */
    var edgeInsets = Insets(0, 0, 0, 0)
    /**
     * Get the change cursor property
     *
     * @return  the change cursor property
     */
    /**
     * Set the change cursor property
     *
     * @param  changeCursor when true the cursor will be changed to the
     * Cursor.MOVE_CURSOR while the mouse is pressed
     */
    var isChangeCursor = true
    /**
     * Get the auto layout property
     *
     * @return  the auto layout property
     */
    /**
     * Set the auto layout property
     *
     * @param  autoLayout when true layout will be invoked on the parent container
     */
    var isAutoLayout = false
    private var destinationClass: Class<*>? = null
    private var destinationComponent: Component? = null
    private var destination: Component? = null
    private lateinit var source: Component
    private var pressed: Point = Point()
    private var location: Point = Point()
    private var originalCursor: Cursor = Cursor(Cursor.DEFAULT_CURSOR)
    private var autoscrolls = false
    private var potentialDrag = false

    /**
     * Constructor for moving individual components. The components must be
     * regisetered using the registerComponent() method.
     */
    constructor() {}

    /**
     * Constructor to specify a Class of Component that will be moved when
     * drag events are generated on a registered child component. The events
     * will be passed to the first ancestor of this specified class.
     *
     * @param destinationClass  the Class of the ancestor component
     * @param components         the Components to be registered for forwarding
     * drag events to the ancestor Component.
     */
    constructor(destinationClass: Class<*>, vararg components: Component) {
        this.destinationClass = destinationClass
        registerComponent(*components)
    }

    /**
     * Constructor to specify a parent component that will be moved when drag
     * events are generated on a registered child component.
     *
     * @param destinationComponent  the component drage events should be forwareded to
     * @param components    the Components to be registered for forwarding drag
     * events to the parent component to be moved
     */
    constructor(destinationComponent: Component, vararg components: Component) {
        this.destinationComponent = destinationComponent
        registerComponent(*components)
    }

    /**
     * Remove listeners from the specified component
     *
     * @param components  the component the listeners are removed from
     */
    fun deregisterComponent(vararg components: Component) {
        for (component in components) component.removeMouseListener(this)
    }

    /**
     * Add the required listeners to the specified component
     *
     * @param components  the component the listeners are added to
     */
    fun registerComponent(vararg components: Component) {
        for (component in components) component.addMouseListener(this)
    }

    /**
     * Setup the variables used to control the moving of the component:
     *
     * source - the source component of the mouse event
     * destination - the component that will ultimately be moved
     * pressed - the Point where the mouse was pressed in the destination
     * component coordinates.
     */
    override fun mousePressed(e: MouseEvent) {
        source = e.component
        val width = source.size.width - dragInsets.left - dragInsets.right
        val height = source.size.height - dragInsets.top - dragInsets.bottom
        val r = Rectangle(dragInsets.left, dragInsets.top, width, height)
        if (r.contains(e.point)) setupForDragging(e)
    }

    private fun setupForDragging(e: MouseEvent) {
        source.addMouseMotionListener(this)
        potentialDrag = true

        //  Determine the component that will ultimately be moved
        destination = if (destinationComponent != null) {
            destinationComponent
        } else if (destinationClass == null) {
            source
        } else  //  forward events to destination component
        {
            SwingUtilities.getAncestorOfClass(destinationClass, source)
        }
        pressed = e.locationOnScreen
        location = destination!!.location
        if (isChangeCursor) {
            originalCursor = source.cursor
            source.cursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR)
        }

        //  Making sure autoscrolls is false will allow for smoother dragging of
        //  individual components
        if (destination is JComponent) {
            val jc = destination as JComponent
            autoscrolls = jc.autoscrolls
            jc.autoscrolls = false
        }
    }

    /**
     * Move the component to its new location. The dragged Point must be in
     * the destination coordinates.
     */
    override fun mouseDragged(e: MouseEvent) {
        val dragged = e.locationOnScreen
        val dragX = getDragDistance(dragged.x, pressed.x, snapSize.width)
        val dragY = getDragDistance(dragged.y, pressed.y, snapSize.height)
        var locationX = location.x + dragX
        var locationY = location.y + dragY

        //  Mouse dragged events are not generated for every pixel the mouse
        //  is moved. Adjust the location to make sure we are still on a
        //  snap value.
        while (locationX < edgeInsets.left) locationX += snapSize.width
        while (locationY < edgeInsets.top) locationY += snapSize.height
        val d = getBoundingSize(destination)
        while (locationX + destination!!.size.width + edgeInsets.right > d.width) locationX -= snapSize.width
        while (locationY + destination!!.size.height + edgeInsets.bottom > d.height) locationY -= snapSize.height

        //  Adjustments are finished, move the component
        destination!!.setLocation(locationX, locationY)

        val parent = destination!!.parent as Container
        for(i in 0 until parent.componentCount) {
            val c = parent.getComponent(i)
            if(c !is JButton) continue
            val r = c.bounds
            val height = r.height / 2
            PREV_AREA.setBounds(r.x, r.y, r.width, height)
            NEXT_AREA.setBounds(r.x, r.y + height, r.width, height)
            if(PREV_AREA.contains(destination!!.location)) {
                swapComponentLocation(parent, destination!!, if(i > 0) i else 0)
                return
            } else if(NEXT_AREA.contains(destination!!.location)) {
                swapComponentLocation(parent, destination!!, i)
                return
            }
        }
    }

    /*
	 *  Determine how far the mouse has moved from where dragging started
	 *  (Assume drag direction is down and right for positive drag distance)
	 */
    private fun getDragDistance(larger: Int, smaller: Int, snapSize: Int): Int {
        val halfway = snapSize / 2
        var drag = larger - smaller
        drag += if (drag < 0) -halfway else halfway
        drag = drag / snapSize * snapSize
        return drag
    }

    /*
	 *  Get the bounds of the parent of the dragged component.
	 */
    private fun getBoundingSize(source: Component?): Dimension {
        return if (source is Window) {
            val env = GraphicsEnvironment.getLocalGraphicsEnvironment()
            val bounds = env.maximumWindowBounds
            Dimension(bounds.width, bounds.height)
        } else {
            source!!.parent.size
        }
    }

    /**
     * Restore the original state of the Component
     */
    override fun mouseReleased(e: MouseEvent) {
        if (!potentialDrag) return
        source!!.removeMouseMotionListener(this)
        potentialDrag = false
        if (isChangeCursor) source!!.cursor = originalCursor
        if (destination is JComponent) {
            (destination as JComponent).autoscrolls = autoscrolls
        }

        //  Layout the components on the parent container
        if (isAutoLayout) {
            if (destination is JComponent) {
                (destination as JComponent).revalidate()
            } else {
                destination!!.validate()
            }
        }

        val parent = destination!!.parent as Container

        if(parent.bounds.contains(destination!!.location)) {
            swapComponentLocation(parent, destination!!, parent.componentCount)
        } else {
            swapComponentLocation(parent, destination!!, index)
        }

        parent.revalidate()
        index = -1
    }

    private fun swapComponentLocation(parent: Container, component: Component,  idx: Int) {
        if(idx != index) {
            parent.setComponentZOrder(component, idx)
            index = idx
        }
        parent.repaint()
    }
}