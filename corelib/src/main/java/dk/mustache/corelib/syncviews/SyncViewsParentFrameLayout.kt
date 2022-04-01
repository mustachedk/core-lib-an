package dk.mustache.corelib.syncviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.children
import dk.mustache.corelib.databinding.LayoutSyncViewsBinding
import kotlin.random.Random

class SyncViewsParentFrameLayout : EventSyncParentView {

    lateinit var binding : LayoutSyncViewsBinding
    lateinit var viewModel : PositionSyncViewsViewModel

    constructor(context: Context) : super(context) { init(context, null) }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init(context, attrs) }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init(context, attrs) }

    private fun init(context: Context, attrs: AttributeSet?) {
        binding = LayoutSyncViewsBinding.inflate(LayoutInflater.from(context), this, true)
        viewModel = PositionSyncViewsViewModel()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        findSyncViews((this.parent as View?)?:this)
    }

    fun findSyncViews(view: View) {
        if (view is ViewGroup) {
            val syncViews = findViewsOfType<EventSyncView>(view)
            syncViews.forEach { childView ->
                childView.viewModel = viewModel
                val syncListener = childView.syncListener
                viewModel.addEventListener(Random(System.currentTimeMillis()).nextInt(), syncListener as SyncViewsViewModel.SyncEventListener<SyncViewEvent>)
            }
        }
    }

    fun <T: SyncViewEvent> sendMessage(position: T) {
        viewModel.sendSyncEvent(position)
    }
}