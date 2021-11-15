package dk.mustache.corelib.swipe_recyclerview_item;

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.SwipeListItemLayoutBinding
import dk.mustache.corelib.utils.toPx
import java.util.*
import kotlin.math.abs


class SwipeListItemLayout : ConstraintLayout {

    lateinit var binding : SwipeListItemLayoutBinding
    var listener: SwipeLayoutActionListener? = null
    var startX = 0f
    var startY = 0f
    var currentTranslation = 0f
    var maxSwipeDistanceLeftToRight = 0f
    var maxSwipeDistanceRightToLeft = 0f
    var swipeDirection = SwipeDirectionEnum.NOT_SWIPING
    var swipePosition = SwipePositionEnum.START_POSITION
    var scrollLocked = false
    var rect: Rect? = null
    var endTranslation = 0f
    private var tempSwipeLock = false
    private var swipeSettingsEnum = SwipeSettingsEnum.BOTH
    var lockClick = false
    var startClickTime = 0L

    constructor(context: Context) : super(context) { init(context, null) }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init(context, attrs) }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init(context, attrs) }

    private fun init(context: Context, attrs: AttributeSet?) {
        binding = SwipeListItemLayoutBinding.inflate(LayoutInflater.from(context), this, true)

        val a = context.obtainStyledAttributes(attrs, R.styleable.SwipeListItemLayout, 0, 0)

        maxSwipeDistanceLeftToRight = a.getDimension(R.styleable.SwipeListItemLayout_swipeDistanceLeft, 0f)
        maxSwipeDistanceRightToLeft = a.getDimension(R.styleable.SwipeListItemLayout_swipeDistanceRight, 0f)
        swipePosition = SwipePositionEnum.START_POSITION
    }

    fun setSwipeSettings(swipeSettings: SwipeSettingsEnum) {
        swipeSettingsEnum = swipeSettings
    }

    fun close(animated: Boolean = true) {
        currentTranslation = 0f
        swipePosition = SwipePositionEnum.START_POSITION
        swipeDirection = SwipeDirectionEnum.NOT_SWIPING
        val childToSwipe = this.getChildAt(1)
        if (animated) {
            childToSwipe.animate().translationX(0f)
                .setDuration(100).withEndAction {

                }.start()
        } else {
            childToSwipe.translationX = 0f
        }
    }

    fun setSwipedLeftToRight(animated: Boolean = true) {
        val childToSwipe = this.getChildAt(1)
        swipePosition = SwipePositionEnum.SWIPED_LEFT_TO_RIGHT
        swipeDirection = SwipeDirectionEnum.NOT_SWIPING
        if (animated) {
            childToSwipe.animate().translationX(maxSwipeDistanceLeftToRight)
                .setDuration(200).withEndAction {
                    listener?.onSwipedLeftToRight()
                }.start()
        } else {
            childToSwipe.translationX = maxSwipeDistanceLeftToRight
            listener?.onSwipedLeftToRight()
        }
        tempSwipeLock = true
    }

    fun setSwipedRightToLeft(animated: Boolean = true) {
        swipePosition = SwipePositionEnum.SWIPED_RIGHT_TO_LEFT
        val childToSwipe = this.getChildAt(1)
        if (animated) {
            childToSwipe.animate().translationX(-maxSwipeDistanceRightToLeft)
                .setDuration(200).withEndAction {
                    listener?.onSwipedRightToLeft()
                }.start()

        } else {
            childToSwipe.translationX = -maxSwipeDistanceRightToLeft
            listener?.onSwipedRightToLeft()
        }
        swipeDirection = SwipeDirectionEnum.NOT_SWIPING
        tempSwipeLock = true
    }

    fun determineStateAndAnimate(swipeX: Float) {
        when (swipeDirection) {
            SwipeDirectionEnum.LEFT_TO_RIGHT -> {
                if(swipePosition==SwipePositionEnum.SWIPED_RIGHT_TO_LEFT) {
                    close()
                } else {
                    setSwipedLeftToRight()
                }
            }
            SwipeDirectionEnum.RIGHT_TO_LEFT -> {
                if(swipePosition==SwipePositionEnum.SWIPED_LEFT_TO_RIGHT) {
                    close()
                } else {
                    setSwipedRightToLeft()
                }
            }
            else -> {

            }
        }
        tempSwipeLock = false
    }

    val enableClickRunnable = {
        lockClick = false
    }

    private fun clickLockLayout(e: MotionEvent) {
        val clickDuration: Long = Calendar.getInstance().timeInMillis - startClickTime
        if (clickDuration < 200 &&
            (e.x>startX+10 || e.x<startX-10) ||
            (e.y>startY+10 || e.y<startY-10)) {
            lockClick = true
        }
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        val child = this.getChildAt(0)
        val childToSwipe = this.getChildAt(1)

        if (swipeSettingsEnum!=SwipeSettingsEnum.SWIPE_LOCKED) {
            when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    startX = e.x
                    startY = e.y
                    swipeDirection = SwipeDirectionEnum.NOT_SWIPING
                    tempSwipeLock = false
                    rect = Rect(left, top, right, bottom)

                    startClickTime = Calendar.getInstance().timeInMillis;
                }

                MotionEvent.ACTION_MOVE -> {
                    val currentX = e.x
                    val currentY = e.y

                    if (!tempSwipeLock) {
                        if (swipeDirection == SwipeDirectionEnum.NOT_SWIPING) {
                            if ((swipeSettingsEnum != SwipeSettingsEnum.LEFT_TO_RIGHT && startX > currentX && startX > currentX + 10f.toPx()) ||
                                (swipeSettingsEnum != SwipeSettingsEnum.RIGHT_TO_LEFT && startX < currentX && startX < currentX - 10f.toPx())
                            ) {
                                val direction = -(startX - currentX)
                                currentTranslation += direction
                                swipeDirection = if (direction < 0) {
                                    SwipeDirectionEnum.RIGHT_TO_LEFT
                                } else {
                                    SwipeDirectionEnum.LEFT_TO_RIGHT
                                }
                            } else {

                            }
                        } else {
                            currentTranslation = -(startX - currentX) + endTranslation
                        }

                        if (swipeDirection != SwipeDirectionEnum.NOT_SWIPING) {
                            when (swipePosition) {
                                SwipePositionEnum.SWIPED_LEFT_TO_RIGHT -> {
                                    //TODO give bouncy feel when dragging
                                }
                                SwipePositionEnum.SWIPED_RIGHT_TO_LEFT -> {
                                    //TODO give bouncy feel when dragging
                                }
                                else -> {
                                    if (Math.abs(currentTranslation) > maxSwipeDistanceRightToLeft
                                        && swipeDirection == SwipeDirectionEnum.RIGHT_TO_LEFT
                                        && swipePosition != SwipePositionEnum.SWIPED_RIGHT_TO_LEFT
                                    ) {

                                        setSwipedRightToLeft()

                                    } else if (Math.abs(currentTranslation) > maxSwipeDistanceLeftToRight
                                        && swipeDirection == SwipeDirectionEnum.LEFT_TO_RIGHT
                                        && swipePosition != SwipePositionEnum.SWIPED_LEFT_TO_RIGHT
                                    ) {

                                        setSwipedLeftToRight()

                                    } else {
                                        when (swipePosition) {
                                            SwipePositionEnum.SWIPED_RIGHT_TO_LEFT -> {
                                                currentTranslation =
                                                    Math.max(
                                                        -maxSwipeDistanceRightToLeft,
                                                        currentTranslation
                                                    )
                                            }
                                            SwipePositionEnum.SWIPED_LEFT_TO_RIGHT -> {
                                                currentTranslation =
                                                    Math.min(
                                                        maxSwipeDistanceLeftToRight,
                                                        currentTranslation
                                                    )
                                            }
                                            else -> {

                                            }
                                        }
                                        listener?.onSwiping(currentTranslation, swipeDirection)
                                        childToSwipe.translationX = currentTranslation
                                    }

                                    val dx = abs(currentX - startX)
                                    val dy = abs(currentY - startY)
                                    scrollLocked = dx > dy
                                    listener?.onLockScroll(scrollLocked)
                                }

                            }
                        }
                    } else {
                        return true
                    }

                }

                MotionEvent.ACTION_CANCEL,
                MotionEvent.ACTION_UP -> {
                    val currentX = e.x
                    val currentY = e.y
                    determineStateAndAnimate(currentX)
                    swipeDirection = SwipeDirectionEnum.NOT_SWIPING
                    listener?.onLockScroll(false)
                    endTranslation = currentTranslation
                    clickLockLayout(e)
                    post(enableClickRunnable)
                }
            }
        }

        return if(lockClick) {
            true
        } else {
            super.onInterceptTouchEvent(e)
        }
    }

    interface SwipeLayoutActionListener {
        fun onLockScroll(lock: Boolean)
        fun onSwiping(swipeDistance: Float, swipeDirection: SwipeDirectionEnum)
        fun onSwipedLeftToRight()
        fun onSwipedRightToLeft()
    }
}