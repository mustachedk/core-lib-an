package dk.mustache.corelibexample.syncviews

import android.widget.Button
import dk.mustache.corelib.syncviews.PositionSyncSubject
import dk.mustache.corelib.syncviews.SyncConductor
import dk.mustache.corelib.syncviews.SyncSubject
import dk.mustache.corelibexample.R
import kotlin.reflect.KFunction2

/**
 * Created primarily to demonstrate a SyncHandler that isn't based on a list. Even
 * though this totally could be.
 */
class ButtonsSyncHandler(
    private val btn1: Button,
    private val btn2: Button,
    private val btn3: Button,
    private val btn4: Button,
    private val btn5: Button,
    private val btn6: Button,
    conductor: SyncConductor<() -> Int>
) : PositionSyncSubject {
    override var sendEvent: KFunction2<SyncSubject<() -> Int>, () -> Int, Unit>? = null
    override var eventIsOnGoing: Boolean = false

    init {
        conductor.subscribe(this)
        btn1.setOnClickListener { sendPosition(it as Button, 0) }
        btn2.setOnClickListener { sendPosition(it as Button, 1) }
        btn3.setOnClickListener { sendPosition(it as Button, 2) }
        btn4.setOnClickListener { sendPosition(it as Button, 3) }
        btn5.setOnClickListener { sendPosition(it as Button, 4) }
        btn6.setOnClickListener { sendPosition(it as Button, 5) }
    }

    private fun sendPosition(button: Button, position: Int) {
        selectButton(button)
        super.sendPosition(position)
    }

    override fun syncReceive(position: Int) {
        when (position) {
            0 -> selectButton(btn1)
            1 -> selectButton(btn2)
            2 -> selectButton(btn3)
            3 -> selectButton(btn4)
            4 -> selectButton(btn5)
            5 -> selectButton(btn6)
        }
    }

    private fun selectButton(button: Button) {
        val res = button.resources
        btn1.backgroundTintList = res.getColorStateList(R.color.sync_normalbutton)
        btn2.backgroundTintList = res.getColorStateList(R.color.sync_normalbutton)
        btn3.backgroundTintList = res.getColorStateList(R.color.sync_normalbutton)
        btn4.backgroundTintList = res.getColorStateList(R.color.sync_normalbutton)
        btn5.backgroundTintList = res.getColorStateList(R.color.sync_normalbutton)
        btn6.backgroundTintList = res.getColorStateList(R.color.sync_normalbutton)
        button.backgroundTintList = res.getColorStateList(R.color.sync_selectedbutton)
    }
}