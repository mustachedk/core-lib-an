package dk.mustache.corelib.custom_material_drawable

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.google.android.material.shape.CornerTreatment
import com.google.android.material.shape.MaterialShapeDrawable
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
import dk.mustache.corelib.custom_material_drawable.treatments.*
import dk.mustache.corelib.utils.toPx


class TicketConstraintLayoutMasked @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var backgroundDrawable: MaterialShapeDrawable
    private lateinit var backgroundDrawableFront: MaterialShapeDrawable
    private lateinit var backgroundViewFront: View
    private lateinit var backgroundViewBack: View

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
    private var archesHorizontal: Int = 0
    private var archesVertical: Int = 0

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

        clipToPadding = false
        clipChildren = false

        setBackgroundMaterial(true)
        redraw()
    }

    fun redraw() {

        val set = ConstraintSet()
        val layout = this
        set.clone(layout)

        val button = TicketMaterialView(context, attrs)
        button.redrawView(false)

        button.setId(101)

        layout.addView(button)

        set.connect(
            button.getId(),
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP,
            0
        )

        set.connect(
            button.getId(),
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            0
        )
        set.connect(
            button.getId(),
            ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT,
            0
        )
        set.connect(
            button.getId(),
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT,
            0
        )
        set.applyTo(layout)

        backgroundViewFront = button

    }

    fun calculateArches() {
        val realHeight = height.toFloat() - paddingBottom.toFloat() - paddingStart.toFloat()
        val realWidth = width.toFloat() - paddingEnd.toFloat() - paddingStart.toFloat()
        val archesSpacing = archSize/3*2
        archesHorizontal = Math.floor(((realWidth - centerArchSize - archesSpacing) / (archSize + archesSpacing)).toDouble()).toInt() - 6
        archesVertical = Math.floor(((realHeight - centerArchSize - archesSpacing) / (archSize + archesSpacing)).toDouble()).toInt() - 6
    }

    fun getTopPath(length: Float, interpolation: Float, shapePath: Path, drawSmallArches: Boolean): Path {
        val topEdge = paddingTop.toFloat()
        val rightEdge = width.toFloat() - paddingEnd.toFloat()
        val archesSpacing = archSize/3*2

        val centerStartX = length/2 + centerArchSize/2

        var toX = rightEdge - archesSpacing*2 - 3.toPx()

        shapePath.moveTo(rightEdge, topEdge)
        if (drawSmallArches) {
            for (arch in 0..archesHorizontal / 2) {
                shapePath.lineTo(toX, topEdge)
                shapePath.addArc(
                    toX - (2.0f * archSize / 2),
                    -((2.0f * archSize / 2 * interpolation - archSize / 2) + topEdge),
                    toX,
                    archSize / 2 + topEdge,
                    0.0f,
                    180.0f
                )
                toX -= archSize

                toX -= archesSpacing
            }
        }

        val centerSpacing = Math.abs(centerStartX - toX)

        val leftArch = centerStartX - centerArchSize
        shapePath.lineTo(centerStartX, topEdge)
        shapePath.addArc(
            leftArch,
            -((2.0f * centerArchSize / 2 * interpolation - centerArchSize / 2) + topEdge),
            centerStartX,
            centerArchSize / 2 + topEdge,
            0.0f,
            180.0f
        )
        shapePath.moveTo(leftArch, topEdge)

        if (drawSmallArches) {
            toX = leftArch - centerSpacing - archesSpacing

            for (arch in 0..archesHorizontal / 2) {
                shapePath.lineTo(toX, topEdge)
                shapePath.addArc(
                    toX - (2.0f * archSize / 2),
                    -((2.0f * archSize / 2 * interpolation - archSize / 2) + topEdge),
                    toX,
                    archSize / 2 + topEdge,
                    0.0f,
                    180.0f
                )
                toX -= archSize

                toX -= archesSpacing
            }
        }
        shapePath.lineTo(paddingStart.toFloat(), topEdge)
        return shapePath
    }

    fun getBottomPath(length: Float, interpolation: Float, shapePath: Path, drawSmallArches: Boolean): Path {
        val y = height.toFloat() - paddingBottom.toFloat()
        val archesSpacing = archSize/3*2

        val centerStartX = length/2 - centerArchSize/2

        //Variables for calculating arches
        var controlX = archesSpacing + archSize/2
        var toX = 3.toPx()+2*archesSpacing + paddingStart.toFloat()

        shapePath.moveTo(paddingStart.toFloat(), y)

        if (drawSmallArches) {
            for (arch in 0..archesHorizontal / 2) {
                shapePath.lineTo(toX, y)
                shapePath.addArc(
                    toX,
                    -archSize / 2 + y,
                    2.0f * archSize / 2 * interpolation + toX,
                    (2.0f * archSize / 2 * interpolation - archSize / 2) + y,
                    -180.0f,
                    180.0f
                )
                toX += archSize

                controlX += archSize + archesSpacing
                toX += archesSpacing
            }
        }

        toX -= archesSpacing
        val centerSpacing = centerStartX - toX

        shapePath.lineTo(centerStartX, y)
        shapePath.addArc(
            centerStartX,
            -centerArchSize / 2 + y,
            2.0f * centerArchSize / 2 * interpolation + centerStartX,
            (2.0f * centerArchSize / 2 * interpolation - centerArchSize / 2) + y,
            -180.0f,
            180.0f
        )
        shapePath.moveTo(centerStartX + centerSpacing, y)

        controlX = centerStartX + centerSpacing + archSize/2
        toX = centerStartX + centerArchSize + centerSpacing

        if (drawSmallArches) {
            for (arch in 0..archesHorizontal / 2) {
                shapePath.addArc(
                    toX,
                    -archSize / 2 + y,
                    2.0f * archSize / 2 * interpolation + toX,
                    2.0f * archSize / 2 * interpolation - archSize / 2 + y,
                    -180.0f,
                    180.0f
                )
                controlX += archSize + archesSpacing
                toX += archesSpacing

                shapePath.lineTo(toX, y)
                toX += archSize
            }
        }
        shapePath.lineTo(length, y)
        return shapePath
    }

    fun getLeftPath(length: Float, interpolation: Float, shapePath: Path): Path {
        val y = paddingTop.toFloat()
        val x = paddingStart.toFloat()
        val startY = length/2 - centerArchSize/2

        shapePath.moveTo(x, y)
        shapePath.lineTo(x, startY)
        shapePath.addArc(
            -centerArchSize / 2 + x,
            startY,
            2.0f * centerArchSize / 2 * interpolation - centerArchSize / 2 + x,
            2.0f * centerArchSize / 2 * interpolation + startY,
            -90.0f,
            180.0f
        )
        shapePath.moveTo(x, 2.0f * centerArchSize / 2 * interpolation + startY)
        shapePath.lineTo(x, length - paddingBottom.toFloat())
        return shapePath
    }

    fun getRightPath(length: Float, interpolation: Float, shapePath: Path): Path {
        val y = paddingTop.toFloat()//10.toPx().toFloat()
        val x = width.toFloat() - paddingEnd.toFloat()//10.toPx().toFloat()
        val startY = length/2 - centerArchSize/2

        shapePath.moveTo(x, length)
        shapePath.lineTo(x, 2.0f * centerArchSize / 2 * interpolation + startY)
        shapePath.addArc(
            -centerArchSize / 2 + x,
            startY,
            2.0f * centerArchSize / 2 * interpolation - centerArchSize / 2 + x,
            2.0f * centerArchSize / 2 * interpolation + startY,
            -270.0f,
            180.0f
        )
        shapePath.moveTo(x, startY)
        shapePath.lineTo(x, y)
        return shapePath
    }

    @SuppressLint("NewApi")
    override fun dispatchDraw(canvas: Canvas){

        if (maskChildren) {
            if (::backgroundDrawable.isInitialized) {
                backgroundDrawable.setBounds(
                    paddingStart,
                    paddingTop,
                    width - paddingRight,
                    height - paddingBottom
                );
                backgroundDrawable.draw(canvas);
            }

            calculateArches()
            val topEdge = paddingTop.toFloat()
            val leftEdge = paddingStart.toFloat()
            val rightEdge = width.toFloat() - paddingEnd.toFloat()
            val bottomEdge = height.toFloat() - paddingBottom.toFloat()
            var path = Path()
            if (containsFlag(centerArch, BORDER_LEFT)) {
                path = getLeftPath(height.toFloat(), 1.0F, path)
            } else {
                path.moveTo(leftEdge, topEdge)
                path.lineTo(leftEdge, bottomEdge)
            }

            if (containsFlag(centerArch, BORDER_BOTTOM)) {
                path = getBottomPath(width.toFloat(), 1.0f, path, containsFlag(arching, BORDER_BOTTOM))
            } else {
                path.moveTo(leftEdge, bottomEdge)
                path.lineTo(rightEdge, bottomEdge)
            }

            if (containsFlag(centerArch, BORDER_RIGHT)) {
                path = getRightPath(height.toFloat(), 1.0f, path)
            } else {
                path.moveTo(rightEdge, bottomEdge)
                path.lineTo(rightEdge, topEdge)
            }

            if (containsFlag(centerArch, BORDER_TOP)) {
                path = getTopPath(width.toFloat(), 1.0f, path, containsFlag(arching, BORDER_TOP))
            } else {
                path.moveTo(rightEdge, topEdge)
                path.lineTo(leftEdge, topEdge)
            }

            canvas.clipOutPath(path)
        }

        super.dispatchDraw(canvas);

    }

    fun setBackgroundMaterial(isBackground: Boolean) {
        val shadow = shadowElevation
        val radius = shadowRadius

        val backgroundShapeAppearanceModel = getTicketShape()

        backgroundDrawable = if (isBackground) {
            MaterialShapeDrawable(backgroundShapeAppearanceModel).apply {
                setTint(shadowColor)
                paintStyle = Paint.Style.FILL
                shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
                this.setShadowColor(shadowColor)
                elevation = shadow.toFloat()
            }
        } else {
            MaterialShapeDrawable(backgroundShapeAppearanceModel).apply {
//                setTint(color)
                setTint(ContextCompat.getColor(context, R.color.black))
                this.paintStyle = Paint.Style.FILL
                this.shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_NEVER
            }
        }


//        this.background = backgroundDrawable
        this.setPadding(5.toPx(), 5.toPx(),5.toPx(),5.toPx())
        clipToOutline = false

    }

    private fun getTicketShape(): ShapeAppearanceModel {

        val offset = 10.toPx()

        val newOffset = offset.toFloat()//(offset/3).toFloat()

        val shapeModel = ShapeAppearanceModel().toBuilder()

        /** LEFT **/
        if(containsFlag(arching, BORDER_LEFT)) {
            shapeModel.setLeftEdge(
                if (containsFlag(centerArch, BORDER_LEFT)) ArchesWithCenterTreatment(
                    paddingStart.toFloat(),
                    archSize,
                    centerArchSize,
                    height.toFloat()
                ) else ArchesTreatment(paddingStart.toFloat(), archSize)
            )
        } else if(containsFlag(centerArch, BORDER_LEFT)) {
            shapeModel.setLeftEdge(ArchCenterTreatment(newOffset, centerArchSize))
        }

        /** TOP **/
        if(containsFlag(arching, BORDER_TOP))
            shapeModel.setTopEdge(
                if (containsFlag(centerArch, BORDER_TOP)) ArchesWithCenterTreatment(
                    paddingStart.toFloat(),
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
                if (containsFlag(centerArch, BORDER_RIGHT))
                    ArchesWithCenterTreatment(
                    0f,
                    archSize,
                    centerArchSize,
                    height.toFloat()
                ) else ArchesTreatment(0f, archSize)
            )
        else if(containsFlag(centerArch, BORDER_RIGHT))
            shapeModel.setRightEdge(ArchCenterTreatment(0f, centerArchSize))

        /** BOTTOM **/
        if(containsFlag(arching, BORDER_BOTTOM))
            shapeModel.setBottomEdge(
                if (containsFlag(centerArch, BORDER_BOTTOM)) ArchesWithCenterTreatment(
                    paddingStart.toFloat(),
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

    private fun containsFlag(flagSet: Int, flag: Int): Boolean {
        return flagSet or flag == flagSet
    }
}
