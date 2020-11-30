package dk.mustache.corelib.custom_material_drawable

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
import com.google.android.material.shape.MaterialShapeDrawable.SHADOW_COMPAT_MODE_NEVER
import com.google.android.material.shape.ShapeAppearanceModel
import dk.mustache.corelib.R
import dk.mustache.corelib.custom_material_drawable.treatments.*
import dk.mustache.corelib.utils.toPx


class TicketMaterialView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var backgroundDrawable: MaterialShapeDrawable

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

    companion object {
        const val COLOR_DEFAULT = Color.WHITE
        const val ARCH_SIZE_DEFAULT = 15f
        const val CENTER_ARCH_SIZE_DEFAULT = 50f
        const val SHADOW_ELEVATION_DEFAULT = 1
        const val SHADOW_RADIUS_DEFAULT = 1
        const val CORNER_RADIUS_DEFAULT = 0

        const val BORDER_NONE_DEFAULT = 0
        const val BORDER_LEFT = 1
        const val BORDER_TOP = 2
        const val BORDER_RIGHT = 4
        const val BORDER_BOTTOM = 8

        const val SHADOW_COLOR_DEFAULT = Color.LTGRAY
    }

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
        redrawView(true)
    }

    fun redrawView(isBackground: Boolean) {
        val shadow = shadowElevation
        val radius = shadowRadius

        val backgroundShapeAppearanceModel = getTicketShape()

        backgroundDrawable = if (isBackground) {
            MaterialShapeDrawable(backgroundShapeAppearanceModel).apply {
            setTint(shadowColor)
//                setTint(ContextCompat.getColor(context, R.color.ticket_shadow_gray))
                paintStyle = Paint.Style.FILL
                shadowCompatibilityMode = SHADOW_COMPAT_MODE_ALWAYS
                setShadowColor(shadowColor)
                elevation = shadow.toFloat()
//                strokeColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.button_gradient_start))
//                strokeWidth = 2.toPx().toFloat()
//                shadowRadius = radius
            }
        } else {
            MaterialShapeDrawable(backgroundShapeAppearanceModel).apply {
//                setTint(color)
                setTint(ContextCompat.getColor(context, R.color.black))
                this.paintStyle = Paint.Style.FILL
                this.shadowCompatibilityMode = SHADOW_COMPAT_MODE_NEVER
            }
        }

        this.background = backgroundDrawable
        this.setPadding(5.toPx(), 5.toPx(),5.toPx(),5.toPx())
        clipToOutline = false

    }

    private fun getTicketShape(): ShapeAppearanceModel {

        val offset = 10.toPx()

        val newOffset = offset.toFloat()

        val shapeModel = ShapeAppearanceModel().toBuilder()

        /** LEFT **/
        if(containsFlag(arching, BORDER_LEFT))
            shapeModel.setLeftEdge(
                if (containsFlag(centerArch, BORDER_LEFT)) ArchesWithCenterTreatment(
                    0f,
                    archSize,
                    centerArchSize,
                    height.toFloat()
                ) else ArchesTreatment(paddingStart.toFloat(), archSize)
            )

        else if(containsFlag(centerArch, BORDER_LEFT))
            shapeModel.setLeftEdge(ArchCenterTreatment(newOffset, centerArchSize))

        /** TOP **/
        if(containsFlag(arching, BORDER_TOP))
            shapeModel.setTopEdge(
                if (containsFlag(centerArch, BORDER_TOP)) ArchesWithCenterTreatment(
                    0f,
                    archSize,
                    centerArchSize,
                    width.toFloat()
                ) else ArchesTreatment(paddingStart.toFloat(), archSize)
            )
        else if(containsFlag(centerArch, BORDER_TOP))
            shapeModel.setTopEdge(ArchCenterTreatment(newOffset, centerArchSize))

        /** RIGHT **/
        if(containsFlag(arching, BORDER_RIGHT))
            shapeModel.setRightEdge(
                if (containsFlag(centerArch, BORDER_RIGHT)) ArchesWithCenterTreatment(
                    0f,
                    archSize,
                    centerArchSize,
                    height.toFloat()
                ) else ArchesTreatment(paddingStart.toFloat(), archSize)
            )
        else if(containsFlag(centerArch, BORDER_RIGHT))
            shapeModel.setRightEdge(ArchCenterTreatment(newOffset, centerArchSize))

        /** BOTTOM **/
        if(containsFlag(arching, BORDER_BOTTOM))
            shapeModel.setBottomEdge(
                if (containsFlag(centerArch, BORDER_BOTTOM)) ArchesWithCenterTreatment(
                    0f,
                    archSize,
                    centerArchSize,
                    width.toFloat()
                ) else ArchesTreatment(paddingStart.toFloat(), archSize)
            )
        else if(containsFlag(centerArch, BORDER_BOTTOM))
            shapeModel.setBottomEdge(ArchCenterTreatment(newOffset, centerArchSize))

        shapeModel.setAllCorners(CustomCornerTreatment(0f, shadowRadius.toFloat()))

        val appearance = shapeModel.build()

        return appearance
    }

    @SuppressLint("NewApi")
    override fun dispatchDraw(canvas: Canvas){

        super.dispatchDraw(canvas);

    }

    private fun containsFlag(flagSet: Int, flag: Int): Boolean {
        return flagSet or flag == flagSet
    }
}
