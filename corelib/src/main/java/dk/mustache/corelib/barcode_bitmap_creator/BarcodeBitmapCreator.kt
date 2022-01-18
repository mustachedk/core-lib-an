package dk.mustache.corelib.barcode_bitmap_creator

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.view.View
import com.google.android.gms.dynamic.OnDelegateCreatedListener
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import dk.mustache.corelib.utils.toPx
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

object BarcodeBitmapCreator {
    fun createBarcodeBitmapFromString(
        barcodeText: String,
        barcodeFormat: BarcodeFormat = BarcodeFormat.QR_CODE,
        xSize: Int = 200.toPx(),
        ySize: Int = 200.toPx(),
        rotate: Boolean = false,
        barcodeCreatedListener: (barcodeBitmap: Bitmap?) -> Unit
    ) {
        val observable = Observable.fromArray(ArrayList<Bitmap>())
        observable.subscribeOn(Schedulers.io())
            .map { barcodes ->

                try {
                    val writer = MultiFormatWriter()
                    val bitMatrix = writer.encode(barcodeText, barcodeFormat, xSize, ySize)
                    val barcodeBitmap: Bitmap =
                        Bitmap.createBitmap(xSize, ySize, Bitmap.Config.ARGB_8888)
                    for (x in 0 until xSize) {
                        for (y in 0 until ySize) {
                            barcodeBitmap.setPixel(
                                x,
                                y,
                                if (bitMatrix.get(x, y)) Color.BLACK else Color.TRANSPARENT
                            )
                        }
                    }

                    val finalBitmap = if (rotate) {
                        //90 degrees rotation
                        val matrix = Matrix()
                        matrix.postRotate(-90f)
                        Bitmap.createBitmap(
                            barcodeBitmap,
                            0,
                            0,
                            barcodeBitmap.getWidth(),
                            barcodeBitmap.getHeight(),
                            matrix,
                            true
                        )
                    } else {
                        barcodeBitmap
                    }
                    barcodes.add(finalBitmap)
                } catch (e: WriterException) {
                    e.printStackTrace()
                } catch (ex: IllegalArgumentException) {
                    ex.printStackTrace()
                }
                barcodes
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<List<Bitmap>>() {
                override fun onNext(t: List<Bitmap>) {
                    barcodeCreatedListener(
                        if (t.isNotEmpty()) {
                            t[0]
                        } else {
                            null
                        }
                    )
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }

            })
    }
}