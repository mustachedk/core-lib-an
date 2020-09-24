package dk.mustache.corelib.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.databinding.ObservableField
import dk.mustache.corelib.MustacheCoreLib.getContextCheckInit
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.InetAddress


enum class ConnectionStatus {
    NO_CONNECTION, NETWORK_BUT_NO_INTERNET, HAS_INTERNET, NOT_INITIALIZED
}

object InternetConnection {
    var connectionStatus = ObservableField(ConnectionStatus.NOT_INITIALIZED)

    private lateinit var connectionCallback : ConnectivityManager.NetworkCallback
    var isRegistered = false

    fun registerConnectionChangedListener() {
        val connectivityManager = getContextCheckInit().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!isRegistered) {
                    connectionCallback = object : ConnectivityManager.NetworkCallback() {
                        override fun onAvailable(network: Network) {
                            TaskHandler.doTaskOnMainThread {
                                hasInternetConnection {
                                    if (it) {
                                        connectionStatus.set(ConnectionStatus.HAS_INTERNET)
                                    } else {
                                        connectionStatus.set(ConnectionStatus.NETWORK_BUT_NO_INTERNET)
                                    }
                                }
                            }
                        }

                        override fun onLost(network: Network?) {
                            TaskHandler.doTaskOnMainThread {
                                connectionStatus.set(ConnectionStatus.NO_CONNECTION)
                            }
                        }
                    }
                    it.registerDefaultNetworkCallback(connectionCallback)
                    isRegistered = true
                }
            } else {
                //Earlier versions should be handled in onResume
            }
        }
    }

    fun unregisterConnectionChangedListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (isRegistered) {
                val connectivityManager =
                    getContextCheckInit().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                connectivityManager.let {
                    it.unregisterNetworkCallback(connectionCallback)
                }
                isRegistered = false
            }
        }
    }

    fun isNetworkConnected(): Boolean {
        var result = false
        val connectivityManager =
            getContextCheckInit().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }

        return result
    }

    @SuppressLint("CheckResult")
    fun hasInternetConnection(onCallback: (hasInternet: Boolean) -> Unit) {

        val observable = Observable.fromCallable {
            return@fromCallable try {
                if (isNetworkConnected()) {
                    val ipAddr: InetAddress = InetAddress.getByName("google.com")
                    !ipAddr.equals("")
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
        observable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(object: DisposableObserver<Boolean>() {
            override fun onNext(hasInternet: Boolean) {
                    onCallback(hasInternet)
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        })

    }
}