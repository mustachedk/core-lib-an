package dk.mustache.corelib.network

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/** custom simplified Retrofit callback that gives us a central point to do error handling **/

abstract class RetroCallback<T> : Callback<T> {
    override fun onResponse(@NonNull call: Call<T>?, response: Response<T>) {
        if (response.isSuccessful) {
            onSuccess(response.body(), response.code())
        } else {
            onError(response.errorBody(), response.code())
        }
    }

    override fun onFailure(@NonNull call: Call<T>?, @NonNull throwable: Throwable?) {
        onError(null, -1)
    }

    abstract fun onSuccess(@NonNull response: T?, code: Int)
    abstract fun onError(@Nullable body: ResponseBody?, code: Int)
}