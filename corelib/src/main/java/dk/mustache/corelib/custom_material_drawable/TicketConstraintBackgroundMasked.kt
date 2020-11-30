package dk.mustache.corelib.custom_material_drawable

import android.R.attr
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
import com.google.android.material.shape.MaterialShapeDrawable.SHADOW_COMPAT_MODE_NEVER
import com.google.android.material.shape.ShapeAppearanceModel
import dk.mustache.corelib.R
import dk.mustache.corelib.custom_material_drawable.TicketMaterialView.Companion.ARCH_SIZE_DEFAULT
import dk.mustache.corelib.custom_material_drawable.TicketMaterialView.Companion.BORDER_BOTTOM
import dk.mustache.corelib.custom_material_drawable.TicketMaterialView.Companion.BORDER_LEFT
import dk.mustache.corelib.custom_material_drawable.TicketMaterialView.Companion.BORDER_NONE_DEFAULT
import dk.mustache.corelib.custom_material_drawable.TicketMaterialView.Companion.BORDER_RIGHT
import dk.mustache.corelib.custom_material_drawable.TicketMaterialView.Companion.BORDER_TOP
import dk.mustache.corelib.custom_material_drawable.TicketMaterialView.Companion.CENTER_ARCH_SIZE_DEFAULT
import dk.mustache.corelib.custom_material_drawable.TicketMaterialView.Companion.COLOR_DEFAULT
import dk.mustache.corelib.custom_material_drawable.TicketMaterialView.Companion.CORNER_RADIUS_DEFAULT
import dk.mustache.corelib.custom_material_drawable.TicketMaterialView.Companion.SHADOW_COLOR_DEFAULT
import dk.mustache.corelib.custom_material_drawable.TicketMaterialView.Companion.SHADOW_ELEVATION_DEFAULT
import dk.mustache.corelib.custom_material_drawable.TicketMaterialView.Companion.SHADOW_RADIUS_DEFAULT
import dk.mustache.corelib.utils.toPx


class TicketConstraintBackgroundMasked @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var color: Int = -1
    private var arching: Int
    private var centerArch: Int
    private var archSize: Float
    private var centerArchSize: Float
    private var shadowColor: Int
    private var shadowElevation: Int
    private var shadowRadius: Int
    private var cornerRadius: Int
    private var maskChildren: Boolean = true

    init {
        val ta = getContext().obtainStyledAttributes(
            attrs,
            R.styleable.TicketMaterialDrawable
        )
        try {
            color = ta.getColor(
                R.styleable.TicketMaterialDrawable_color,
                COLOR_DEFAULT
            )
            arching = ta.getInteger(
                R.styleable.TicketMaterialDrawable_arches,
                BORDER_NONE_DEFAULT
            )
            centerArch = ta.getInteger(
                R.styleable.TicketMaterialDrawable_centerArch,
                BORDER_NONE_DEFAULT
            )
            archSize = ta.getFloat(
                R.styleable.TicketMaterialDrawable_archSize,
                ARCH_SIZE_DEFAULT
            ).toPx()
            centerArchSize = ta.getFloat(
                R.styleable.TicketMaterialDrawable_centerArchSize,
                CENTER_ARCH_SIZE_DEFAULT
            ).toPx()
            shadowElevation = ta.getInt(
                R.styleable.TicketMaterialDrawable_shadowElevation,
                SHADOW_ELEVATION_DEFAULT
            ).toPx()
            shadowRadius = ta.getInt(
                R.styleable.TicketMaterialDrawable_shadowRadius,
                SHADOW_RADIUS_DEFAULT
            ).toPx()
            cornerRadius = ta.getInt(
                R.styleable.TicketMaterialDrawable_cornersRadiusd,
                CORNER_RADIUS_DEFAULT
            ).toPx()
            shadowColor = ta.getInt(
                R.styleable.TicketMaterialDrawable_shadowColor,
                SHADOW_COLOR_DEFAULT
            )
            maskChildren = ta.getBoolean(
                R.styleable.TicketMaterialDrawable_maskChildren,
                true
            )
        } finally {
            ta.recycle()
        }

        redrawView()
    }

    fun redrawView() {

        val set = ConstraintSet()
        val layout = this
        set.clone(layout)

        clipToPadding = false
        clipChildren = false

        val button2 = TicketMaterialView(context, attrs)
        button2.setId(1001)

        layout.addView(button2)

        set.connect(
            button2.getId(),
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP,
            10.toPx()
        )

        set.connect(
            button2.getId(),
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            9.toPx()
        )
        set.connect(
            button2.getId(),
            ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT,
            9.toPx()
        )
        set.connect(
            button2.getId(),
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT,
            10.toPx()
        )
        set.applyTo(layout)

    }

    @SuppressLint("NewApi")
    override fun dispatchDraw(canvas: Canvas){


        super.dispatchDraw(canvas);

    }
}
