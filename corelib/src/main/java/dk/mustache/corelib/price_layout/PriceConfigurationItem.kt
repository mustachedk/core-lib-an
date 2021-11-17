package dk.mustache.corelib.price_layout

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class PriceConfigurationItem(
    val discountPrice: Double,
    val price: Double,
    val discountAmount: Double,
    val discountMaxQuantity: Double
):Parcelable, Serializable {

    fun displayYellowOfferPrice(): Boolean {
        return discountAmount == 1.0
    }

    fun displayDiscountLabel(): Boolean {
        return discountAmount > 1.0
    }

    fun displayMaxNumberPerCustomerLabel() : Boolean {
        return discountMaxQuantity > 0.0
    }
}