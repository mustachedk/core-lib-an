package dk.mustache.corelib.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import dk.mustache.corelib.utils.ViewUtil

private const val RESET_DURATION = 500L

open class OneClickButton : androidx.appcompat.widget.AppCompatTextView {

    constructor(context: Context) : super(context) { init(context, null) }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init(
        context,
        attrs
    ) }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init(context, attrs) }

    open fun init (context: Context, attrs: AttributeSet?) {

    }

    override fun setOnClickListener(l: OnClickListener?) {
        if (l != null) {
            super.setOnClickListener(ViewUtil.getOneClickListener(RESET_DURATION, l))
        } else {
            super.setOnClickListener(l)
        }
    }
}