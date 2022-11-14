package dk.mustache.corelib.interscroll

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView

class InterscrollView : FrameLayout {
    var imageView: ImageView? = null
        private set

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setup(imageView: ImageView, height: Int) {
        // Setup interscrollview height. This is the height of the view-slit
        val rootLayoutParams =
            LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, height)
        this.layoutParams = rootLayoutParams

        // Setup imageview. The position of this imageview will be manipulated by InterscrollHandler
        imageView.layoutParams = LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        addView(imageView)
        this.imageView = imageView
    }
}
