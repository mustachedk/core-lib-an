package dk.mustache.corelib.custom_material_drawable.treatments

import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapePath

class CustomCornerTreatment(private val offset: Float, private val radius: Float) : RoundedCornerTreatment(radius) {

    override fun getCornerPath(angle: Float, interpolation: Float, shapePath: ShapePath) {
        shapePath.reset(offset, this.radius * interpolation)
        shapePath.addArc(
            offset,
            offset,
            2.0f * this.radius * interpolation,
            2.0f * this.radius * interpolation,
            angle + 180.0f,
            90.0f
        )
        shapePath.lineTo(offset, this.radius * interpolation)
    }
}
