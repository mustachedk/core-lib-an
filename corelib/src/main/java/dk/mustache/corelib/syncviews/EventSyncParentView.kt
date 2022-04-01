package dk.mustache.corelib.syncviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.children
import dk.mustache.corelib.databinding.LayoutSyncViewsBinding

open class EventSyncParentView: FrameLayout {

    constructor(context: Context) : super(context) { init(context, null) }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init(context, attrs) }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init(context, attrs) }

    private fun init(context: Context, attrs: AttributeSet?) {

    }

    fun findAllViewGroups(viewGroup: ViewGroup): List<ViewGroup> {
        val viewGroups = mutableListOf<ViewGroup>()
        val children = viewGroup.children
        viewGroups.add(viewGroup)
        children.forEach { childView ->
            if (childView is ViewGroup) {
                viewGroups.addAll(findAllViewGroups(childView))
            }
        }
        return viewGroups
    }

    fun findTopViewGroup(view: View): ViewGroup? {
        var v : View? = view
        do {
            val p = v?.parent
            if (p!=null && p is ViewGroup) {
                v = p
            } else {
                return v as ViewGroup
            }
        } while (v!=null)
        return v
    }

    inline fun <reified T> findViewsOfType(viewGroup: ViewGroup): List<T> {
        val tList = mutableListOf<T>()
        val topViewGroup = findTopViewGroup(viewGroup) as ViewGroup
        val viewGroups = findAllViewGroups(topViewGroup)
        viewGroups.forEach {
            val children = it.children
            children.forEach { childView ->
                if (childView is T) {
                    tList.add(childView)
                }
            }
        }

        return tList
    }
}