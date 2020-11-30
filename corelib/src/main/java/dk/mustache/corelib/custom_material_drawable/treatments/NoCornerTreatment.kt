package dk.mustache.corelib.custom_material_drawable.treatments

import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapePath

class NoCornerTreatment(private val offset: Float) : RoundedCornerTreatment(0f) {

    override fun getCornerPath(angle: Float, interpolation: Float, shapePath: ShapePath) {
        shapePath.reset(offset, offset)
        //Draw no corners
    }
}
