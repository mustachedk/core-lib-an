package dk.mustache.corelib.custom_material_drawable.treatments

import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.ShapePath

class ArchesTreatment(private var padding: Float, private val archesSize: Float) : EdgeTreatment() {

    override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
        padding = 0f
        val archesSpacing = archesSize/3*2

        val arches = Math.floor(length / (archesSize+archesSpacing).toDouble()).toInt() -2
        val spacingStartEnd = ((length - (archesSize+archesSpacing)*arches) / 2) - archesSpacing

        //Variables for calculating arches
        var controlX = spacingStartEnd + archesSize/2
        var toX = spacingStartEnd



        shapePath.lineTo(padding, padding)
        for(arch in 0..arches) {
            shapePath.lineTo(toX, padding)
            shapePath.addArc(
                toX,
                -archesSize/2 + padding,
                2.0f * archesSize/2 * interpolation + toX,
                2.0f * archesSize/2 * interpolation - archesSize/2 + padding,
                180.0f,
                -180.0f
            )
            toX += archesSize

            controlX += archesSize+archesSpacing
            toX += archesSpacing
        }

        toX -= archesSpacing
        shapePath.lineTo(length, padding)
    }
}
