package dk.mustache.corelib.custom_material_drawable.treatments

import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.ShapePath


class ArchCenterTreatment(var padding: Float, private val centerArchSize: Float) : EdgeTreatment() {

    override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
        padding = 0f
        val startX =  length/2 - centerArchSize/2

        shapePath.lineTo(padding, padding)
        shapePath.lineTo(startX, padding)
        shapePath.addArc(
            startX,
            -centerArchSize/2 + padding,
            2.0f * centerArchSize/2 * interpolation + startX,
            2.0f * centerArchSize/2 * interpolation - centerArchSize/2 + padding,
            180.0f,
            -180.0f
        )
        shapePath.lineTo(length , padding)
    }
}
