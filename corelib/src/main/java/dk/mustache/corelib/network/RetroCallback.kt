package dk.mustache.corelib.network

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/** custom simplified Retrofit callback that gives us a central point to do error handling **/

abstract class RetroCallback<T> : Callback<T> {
    override fun onResponse(@NonNull call: Call<T>?, response: Response<T>) {
        if (response.isSuccessful) {
            onSuccess(response.body(), response.code())
        } else {
            when (response.code()) {
                401 -> isHandledInAuthorizationInterceptor()
                404 -> showDialogue("Unauthenticated")
                400 -> showDialogue("Client error")
                500 -> showDialogue("Server error")
                else -> showDialogue("Unknown error: ${response.code()}")
            }
            onError(response.errorBody(), response.code())
        }
    }

    private fun showDialogue(message: String) {

    }

    private fun isHandledInAuthorizationInterceptor() {

    }

    override fun onFailure(@NonNull call: Call<T>?, @NonNull throwable: Throwable?) {
        if (throwable is IOException) {
            showIOError()
        } else {
            showUnexpectedErrorDialog()
        }

        showOnHttpErrorDialogue()
        onError(null, -1)
    }

    private fun showUnexpectedErrorDialog() {

    }

    private fun showIOError() {

    }

    private fun showOnHttpErrorDialogue() {


    }


    abstract fun onSuccess(@NonNull response: T?, code: Int)
    abstract fun onError(@Nullable body: ResponseBody?, code: Int)
}