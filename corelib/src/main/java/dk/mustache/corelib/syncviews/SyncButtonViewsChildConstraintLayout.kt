package dk.mustache.corelib.syncviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.LayoutSyncViewsBinding
import kotlin.random.Random

class SyncButtonViewsChildConstraintLayout  : ConstraintLayout, EventSyncView {

    override var viewModel: SyncViewsViewModel<*>? = null
    lateinit var binding: LayoutSyncViewsBinding
    val buttonViewList = mutableListOf<Button>()
    lateinit var syncConductor: SyncConductor<SyncViewEvent>

    constructor(context: Context) : super(context) { init(context, null) }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init(context, attrs) }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init(context, attrs) }

    private fun init(context: Context, attrs: AttributeSet?) {
        binding = LayoutSyncViewsBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        findButtonViews(this, 0)
    }

    fun findButtonViews(view: View, currentIndex: Int) {
        var index = currentIndex
        if (view is ViewGroup) {
            val children = view.children
            children.forEach { childView ->
                if (childView is Button) {
                    buttonViewList.add(childView)
                    childView.tag = buttonViewList.lastIndex
                    childView.setOnClickListener {
                        (viewModel as PositionSyncViewsViewModel).sendSyncEvent(SyncViewEvent(it.tag as Int))
                    }
                } else if (childView is ViewGroup) {
                    findButtonViews(childView, index+1)
                }
            }
        }
    }

    fun selectButton(index: Int) {
        buttonViewList.forEachIndexed { index, button ->
            button.backgroundTintList = button.resources.getColorStateList(R.color.sync_normalbutton)
        }
        buttonViewList[index].backgroundTintList = buttonViewList[index].resources.getColorStateList(R.color.sync_selectedbutton)
    }

    override var syncListener: SyncViewsViewModel.SyncEventListener<*> = object : SyncViewsViewModel.SyncEventListener<SyncViewEvent> {
        override var id: Int
            get() = Random.nextInt((System.currentTimeMillis()/1000).toInt())
            set(value) {}

        override fun receiveEvent(event: SyncViewEvent?) {
            selectButton(event?.data?:0)
        }

    }

}