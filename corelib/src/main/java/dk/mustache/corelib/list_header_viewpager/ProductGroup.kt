package dk.mustache.corelib.list_header_viewpager

import java.io.Serializable

data class ProductGroup(val guid: String = "",
                        var name: String? = null,
                        var fieldExternalId: String? = null,
                        var nameAlt: String? = null,
                        val weight: Int? = 999,
                        val offerSize: String? = null): Serializable {

    fun getDeepCopy(): ProductGroup {
        return ProductGroup(guid,name,fieldExternalId, nameAlt,weight, offerSize)
    }
}