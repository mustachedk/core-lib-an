package dk.mustache.corelib.utils

import android.graphics.Bitmap
import android.graphics.Color


object BitmapCropper {
    fun trimBitmap(bmp: Bitmap): Bitmap? {
        val imgHeight = bmp.height
        val imgWidth = bmp.width

        //TRIM WIDTH - LEFT
        var startWidth = 0
        for (x in 0 until imgWidth) {
            if (startWidth == 0) {
                for (y in 0 until imgHeight) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        startWidth = x
                        break
                    }
                }
            } else break
        }


        //TRIM WIDTH - RIGHT
        var endWidth = 0
        for (x in imgWidth - 1 downTo 0) {
            if (endWidth == 0) {
                for (y in 0 until imgHeight) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        endWidth = x
                        break
                    }
                }
            } else break
        }


        //TRIM HEIGHT - TOP
        var startHeight = 0
        for (y in 0 until imgHeight) {
            if (startHeight == 0) {
                for (x in 0 until imgWidth) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        startHeight = y
                        break
                    }
                }
            } else break
        }


        //TRIM HEIGHT - BOTTOM
        var endHeight = 0
        for (y in imgHeight - 1 downTo 0) {
            if (endHeight == 0) {
                for (x in 0 until imgWidth) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        endHeight = y
                        break
                    }
                }
            } else break
        }
        return Bitmap.createBitmap(
            bmp,
            startWidth,
            startHeight,
            endWidth - startWidth,
            endHeight - startHeight
        )
    }
}