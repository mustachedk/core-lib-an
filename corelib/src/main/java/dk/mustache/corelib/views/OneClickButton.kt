package dk.mustache.corelib.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import dk.mustache.corelib.utils.ViewUtil

private const val RESET_DURATION = 500L

class OneClickButton(context: Context?, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    override fun setOnClickListener(l: OnClickListener?) {
        if (l != null) {
            super.setOnClickListener(ViewUtil.getOneClickListener(RESET_DURATION, l))
        } else {
            super.setOnClickListener(l)
        }
    }
}