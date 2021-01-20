package dk.mustache.corelib.views

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText


class EditTextWithKeyboardCloseDetection : AppCompatEditText {
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context) : super(context) {}

    var mKeyboardClosedListener: KeyBoardClosedListener? = null

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            mKeyboardClosedListener?.onKeyboardClosed()
            dispatchKeyEvent(event)
            return false
        }
        return super.onKeyPreIme(keyCode, event)
    }

    interface KeyBoardClosedListener {
        fun onKeyboardClosed()
    }
}