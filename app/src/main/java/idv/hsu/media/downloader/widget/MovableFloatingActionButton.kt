package idv.hsu.media.downloader.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import idv.hsu.media.downloader.R
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

// https://stackoverflow.com/questions/9398057/android-move-a-view-on-touch-move-action-move/31094315#31094315
// https://github.com/bara96/MovableFloatingActionButton/blob/master/MovableFloatingActionButton.java
class MovableFloatingActionButton : FloatingActionButton, View.OnTouchListener {

    // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.
    private val CLICK_DRAG_TOLERANCE = 10f
    private val CLICK_DURATION = 175

    private var onMove = false
    private var downRawX = 0f
    private var downRawY = 0f
    private var dX = 0f
    private var dY = 0f

    private var coordinatorLayout: CoordinatorLayout.LayoutParams? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        setOnTouchListener(this)
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                Timber.d("ACTION_DOWN")
                dX = view.x - motionEvent.rawX
                dY = view.y - motionEvent.rawY
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                Timber.d("ACTION_MOVE")
                if (!onMove) {
                    if (motionEvent.eventTime - motionEvent.downTime < CLICK_DURATION) {
                        return false
                    } else {
                        onMove = true
                    }
                }
                val viewParent = (view.parent as View)
                val parentWidth = viewParent.width.toFloat()
                val parentHeight = viewParent.height.toFloat()

                var newX = motionEvent.rawX + dX
                newX = max(0f, newX)  // Don't allow the FAB past the left hand side of the parent
                newX = min(
                    parentWidth - view.width,
                    newX
                ) // Don't allow the FAB past the right hand side of the parent

                var newY = motionEvent.rawY + dY
                newY = max(0f, newY) // Don't allow the FAB past the top of the parent
                newY = min(
                    parentHeight - view.height,
                    newY
                ) // Don't allow the FAB past the bottom of the parent

                view.animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(0L)
                    .start()
                return true // Consumed
            }

            MotionEvent.ACTION_UP -> {
                Timber.d("ACTION_UP")
                if (motionEvent.eventTime - motionEvent.downTime < CLICK_DURATION) {
                    performClick()
                    return true
                } else {
                    onMove = true
                }

                val upRawX = motionEvent.rawX
                val upRawY = motionEvent.rawY

                val upDX = upRawX - downRawX
                val upDY = upRawY - downRawY

                if (abs(upDX) < CLICK_DRAG_TOLERANCE && abs(upDY) < CLICK_DRAG_TOLERANCE && performClick()) return true

                val viewParent = view.parent as View
                val borderY: Float = min(view.y - viewParent.top, viewParent.bottom - view.y)
                val borderX: Float = min(view.x - viewParent.left, viewParent.right - view.x)
                val oldX = view.x
                val oldY = view.y
                var finalX: Float
                var finalY: Float

                // You can set your dp margin from dimension resources (Suggested)
                // float fabMargin= getResources().getDimension(R.dimen.fab_margin);
                val fabMargin = resources.getDimension(R.dimen.fab_margin)

                // check if is nearest Y o X
                if (borderX > borderY) {
                    if (view.y > viewParent.height / 2) { //view near Bottom
                        finalY = (viewParent.bottom - view.height).toFloat()
                        finalY = min(
                            (viewParent.height - view.height).toFloat(),
                            finalY
                        ) - fabMargin // Don't allow the FAB past the bottom of the parent
                    } else {  // view vicina a Top
                        finalY = viewParent.top.toFloat()
                        finalY = max(
                            0f,
                            finalY
                        ) + fabMargin // Don't allow the FAB past the top of the parent
                    }
                    //check if X it's over fabMargin
                    finalX = oldX
                    if (view.x + viewParent.left < fabMargin) {
                        finalX = viewParent.left + fabMargin
                    }
                    if (viewParent.right - view.x - view.width < fabMargin) {
                        finalX = viewParent.right - view.width - fabMargin
                    }
                } else {  //view near Right
                    if (view.x > viewParent.width / 2) {
                        finalX = (viewParent.right - view.width).toFloat()
                        finalX = max(
                            0f,
                            finalX
                        ) - fabMargin // Don't allow the FAB past the left hand side of the parent
                    } else {  //view near Left
                        finalX = viewParent.left.toFloat()
                        finalX = min(
                            (viewParent.width - view.width).toFloat(),
                            finalX
                        ) + fabMargin // Don't allow the FAB past the right hand side of the parent
                    }
                    //check if Y it's over fabMargin
                    finalY = oldY
                    if (view.y + viewParent.top < fabMargin) finalY = viewParent.top + fabMargin
                    if (viewParent.bottom - view.y - view.height < fabMargin) finalY =
                        viewParent.bottom - view.height - fabMargin
                }

                view.animate()
                    .x(finalX)
                    .y(finalY)
                    .setDuration(400)
                    .start()
                return false
            }
            else -> {
                Timber.d(motionEvent.toString())
                return super.onTouchEvent(motionEvent)
            }
        }
    }

    fun getCoordinatorLayout(): CoordinatorLayout.LayoutParams? {
        return coordinatorLayout
    }

    fun setCoordinatorLayout(coordinatorLayout: CoordinatorLayout.LayoutParams?) {
        this.coordinatorLayout = coordinatorLayout
    }
}