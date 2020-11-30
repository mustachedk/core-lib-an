package dk.mustache.corelib.custom_material_drawable.treatments

import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.ShapePath

class StraightTreatment(private val padding: Float) : EdgeTreatment() {

    override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
        shapePath.reset(padding, padding)
        shapePath.lineTo(length, padding)
    }
}
