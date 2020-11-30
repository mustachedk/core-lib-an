package dk.mustache.corelib.utils

import android.annotation.SuppressLint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.BufferedReader
import java.net.URL

object DownloadFile {

    @SuppressLint("CheckResult")
    fun withCallback(link: String, callback: (read: String) -> Unit) {
        val observable = Observable.fromCallable {
            return@fromCallable try {
                val content = StringBuilder()
                URL(link).openStream().use { input ->
                    val reader = BufferedReader(input.reader())
                    try {
                        var line = reader.readLine()
                        while (line != null) {
                            content.append(line)
                            line = reader.readLine()
                        }
                    } finally {
                        reader.close()
                    }
                }
                content.toString()

            } catch (e: Exception) {
                ""
            }
        }
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(@SuppressLint("CheckResult")
            object: DisposableObserver<String>() {
                override fun onNext(hasInternet: String) {
                    callback(hasInternet)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }
            })
    }
}