package idv.hsu.media.downloader.ui.search

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import androidx.annotation.IntDef
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


@IntDef(
    BUTTON_STATE_GONE,
    BUTTON_STATE_RIGHT,
    BUTTON_STATE_LEFT
)
@Retention(AnnotationRetention.SOURCE)
annotation class ButtonState

const val BUTTON_STATE_GONE = 0
const val BUTTON_STATE_RIGHT = 1
const val BUTTON_STATE_LEFT = 1

// https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28
class SwipeController constructor(
    private val buttonsActions: SwipeControllerActions
): ItemTouchHelper.Callback() {

    private var swipeBack = false
    private var buttonShowedState = BUTTON_STATE_GONE
    private var buttonInstance: RectF? = null
    private var currentItemViewHolder: RecyclerView.ViewHolder? = null
    private val buttonWidth = 300

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = buttonShowedState != BUTTON_STATE_GONE
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun setTouchListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                swipeBack =
                    event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
                if (swipeBack) {
                    if (dX < -buttonWidth) buttonShowedState =
                        BUTTON_STATE_RIGHT else if (dX > buttonWidth) buttonShowedState =
                        BUTTON_STATE_LEFT
                    if (buttonShowedState != BUTTON_STATE_GONE) {
                        setTouchDownListener(
                            c,
                            recyclerView,
                            viewHolder,
                            dX,
                            dY,
                            actionState,
                            isCurrentlyActive
                        )
                        setItemsClickable(recyclerView, false)
                    }
                }
                return false
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchDownListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchUpListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                super@SwipeController.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    0f,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                recyclerView.setOnTouchListener { v, event -> false }
                setItemsClickable(recyclerView, true)
                swipeBack = false
                if (buttonsActions != null && buttonInstance != null && buttonInstance!!.contains(
                        event.x,
                        event.y
                    )
                ) {
                    if (buttonShowedState == BUTTON_STATE_LEFT) {
                        buttonsActions.onLeftClicked(viewHolder.adapterPosition)
                    } else if (buttonShowedState == BUTTON_STATE_RIGHT) {
                        buttonsActions.onRightClicked(viewHolder.adapterPosition)
                    }
                }
                buttonShowedState = BUTTON_STATE_GONE
                currentItemViewHolder = null
            }
            false
        }
    }

    private fun setItemsClickable(recyclerView: RecyclerView, isClickable: Boolean) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }

    private fun drawButtons(c: Canvas, viewHolder: RecyclerView.ViewHolder) {
        val buttonWidthWithoutPadding = (buttonWidth - 20).toFloat()
        val corners = 16f
        val itemView = viewHolder.itemView
        val p = Paint()
        val leftButton = RectF(
            itemView.left.toFloat(),
            itemView.top.toFloat(), itemView.left + buttonWidthWithoutPadding,
            itemView.bottom.toFloat()
        )
        p.setColor(Color.BLUE)
        c.drawRoundRect(leftButton, corners, corners, p)
        drawText("EDIT", c, leftButton, p)
        val rightButton = RectF(
            itemView.right - buttonWidthWithoutPadding,
            itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat()
        )
        p.setColor(Color.RED)
        c.drawRoundRect(rightButton, corners, corners, p)
        drawText("DELETE", c, rightButton, p)
        buttonInstance = null
        if (buttonShowedState === BUTTON_STATE_LEFT) {
            buttonInstance = leftButton
        } else if (buttonShowedState === BUTTON_STATE_RIGHT) {
            buttonInstance = rightButton
        }
    }

    private fun drawText(text: String, c: Canvas, button: RectF, p: Paint) {
        val textSize = 60f
        p.setColor(Color.WHITE)
        p.setAntiAlias(true)
        p.setTextSize(textSize)
        val textWidth: Float = p.measureText(text)
        c.drawText(text, button.centerX() - textWidth / 2, button.centerY() + textSize / 2, p)
    }

    fun onDraw(c: Canvas) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder!!)
        }
    }
}

interface SwipeControllerActions {
    fun onLeftClicked(position: Int) {}
    fun onRightClicked(position: Int) {}
}