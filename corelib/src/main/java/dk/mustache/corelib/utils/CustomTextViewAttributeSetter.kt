package dk.mustache.corelib.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Typeface
import android.widget.TextView
import dk.mustache.corelib.util.ViewAttributeIdentifiers
import dk.mustache.corelib.util.whenNotNegative
import dk.mustache.corelib.util.whenNotNull

/**
 * Collects attributes from a view's AttributeSet and applies them to
 * the given textView.
 *
 * The helper collects only those attributes that have been defined
 * when calling setTextAttributes, so if you only want a subset of the
 * attributes you don't need to define all of them.
 *
 * See ValidationEditTextWithTitle for an example of this class in use.
 */
class CustomTextViewAttributeSetter(
    private val textView: TextView,
    private val context: Context,
    private val attrArray: TypedArray
) {
    fun applyTextAttributes(attributeIds: ViewAttributeIdentifiers) {
        applyTextAttributes(
            attributeIds.textAppearance,
            attributeIds.textColor,
            attributeIds.textColorHint,
            attributeIds.textSize,
            attributeIds.fontFamily,
            attributeIds.textStyle,
            attributeIds.textAllCaps
        )
    }

    fun applyTextAttributes(
        textAppearance: Int = -99,
        textColor: Int = -99,
        textColorHint: Int = -99,
        textSize: Int = -99,
        fontFamily: Int = -99,
        textStyle: Int = -99,
        textAllCaps: Int = -99
    ) {
        if (textAppearance != -99) {
            val textAppearanceVal = attrArray.getResourceId(textAppearance, -1)
            textAppearanceVal.whenNotNegative {
                textView.setTextAppearance(context, textAppearanceVal)
            }
        }

        if (textColor != -99) {
            val textColorVal = getTextColor(attrArray, textColor)
            textColorVal.colorStateList.whenNotNull { textView.setTextColor(textColorVal.colorStateList) }
            textColorVal.singleColor.whenNotNegative { textView.setTextColor(textColorVal.singleColor) }
        }

        if (textColorHint != -99) {
            val textColorHintVal = getTextColor(attrArray, textColorHint)
            textColorHintVal.colorStateList.whenNotNull {
                textView.setHintTextColor(textColorHintVal.colorStateList)
            }
            textColorHintVal.singleColor.whenNotNegative {
                textView.setHintTextColor(textColorHintVal.singleColor)
            }
        }

        if (textSize != -99) {
            val textSizeVal = attrArray.getDimension(textSize, -1f)
            textSizeVal.whenNotNegative { textView.textSize = textSizeVal }
        }

        if (fontFamily != -99) {
            val fontFamilyVal = attrArray.getString(fontFamily)
            fontFamilyVal.whenNotNull {
                val textStyleVal = attrArray.getInt(textStyle, -1)
                val typeFace = Typeface.create(fontFamilyVal, textStyleVal)
                typeFace.whenNotNull { textView.typeface = typeFace }
            }
        }

        if (textAllCaps != -99 && attrArray.hasValue(textAllCaps)) {
            val textAllCapsVal = attrArray.getBoolean(textAllCaps, false)
            textView.isAllCaps = textAllCapsVal
        }
    }

    private fun getTextColor(
        attrArray: TypedArray,
        textColorId: Int
    ): TextColor {
        val textColor = TextColor()
        try {
            textColor.colorStateList = attrArray.getColorStateList(textColorId)
        } catch (e: Resources.NotFoundException) {
            textColor.singleColor = attrArray.getColor(textColorId, -1)
        }
        if (textColor.colorStateList == null && textColor.singleColor == -1) {
            textColor.singleColor = attrArray.getColor(textColorId, -1)
        }
        return textColor
    }

    data class TextColor(var colorStateList: ColorStateList? = null, var singleColor: Int = -1)
}