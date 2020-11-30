package dk.mustache.corelib.custom_material_drawable.treatments

import com.google.android.material.shape.CornerTreatment
import com.google.android.material.shape.ShapePath

class InnerCutCornerTreatment(val cornerSize: Float): CornerTreatment() {
    override fun getCornerPath(angle: Float, interpolation: Float, shapePath: ShapePath) {
        val radius = cornerSize * interpolation
        shapePath.reset(0f, radius, 180f, 180 - angle)
        shapePath.lineTo(radius, radius)
        shapePath.lineTo(radius, 0f)
    }
}