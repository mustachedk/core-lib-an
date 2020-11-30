package dk.mustache.corelib.custom_material_drawable.treatments

import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.ShapePath

class ArchesWithCenterTreatment(private var customPadding: Float, private val archesSize: Float, private val centerArchSize: Float, private val myLength: Float) : EdgeTreatment() {

    override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
        customPadding = 0f
        val archesSpacing = archesSize/3*2

        val centerStartX = length/2 - centerArchSize/2

        var controlX = archesSpacing + archesSize/2
        var toX = archesSpacing + customPadding

        val arches = Math.floor(((length-centerArchSize-archesSpacing) / (archesSize+archesSpacing)).toDouble()).toInt() - 4

        shapePath.reset(customPadding, customPadding)
        for(arch in 0..arches/2) {
            shapePath.lineTo(toX, customPadding)
            shapePath.addArc(
                toX,
                -archesSize/2 + customPadding,
                2.0f * archesSize/2 * interpolation + toX,
                2.0f * archesSize/2 * interpolation - archesSize/2 + customPadding,
                180.0f,
                -180.0f
            )
            toX += archesSize

            controlX += archesSize+archesSpacing
            toX += archesSpacing
        }

        toX -= archesSpacing
        val centerSpacing = centerStartX - toX

        shapePath.lineTo(centerStartX, customPadding)
        shapePath.addArc(
            centerStartX,
            -centerArchSize/2 + customPadding,
            2.0f * centerArchSize/2 * interpolation + centerStartX,
            2.0f * centerArchSize/2 * interpolation - centerArchSize/2 + customPadding,
            180.0f,
            -180.0f
        )

        val startPoint = centerStartX+centerArchSize+centerSpacing

        shapePath.lineTo(startPoint, customPadding)

        controlX = centerStartX + centerSpacing + archesSize/2
        toX = centerStartX + centerArchSize + centerSpacing

        for(archesSizech in 0..arches/2) {
            shapePath.addArc(
                toX,
                -archesSize/2 + customPadding,
                2.0f * archesSize/2 * interpolation + toX,
                2.0f * archesSize/2 * interpolation - archesSize/2 + customPadding,
                180.0f,
                -180.0f
            )
            controlX += archesSize+archesSpacing
            toX += archesSize+archesSpacing

            shapePath.lineTo(toX, customPadding)
        }
        shapePath.lineTo(length-customPadding , customPadding)
    }
}
