package dk.mustache.corelib.interscroll

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

class InterscrollHandler(
    list: RecyclerView,
    imageView: ImageView,
    @DrawableRes image: Int? = null,
    imageLoading: ((ImageView) -> Unit)? = null
) {
    private var scrollY = 0

    init {
        ConditionListener().await({ checkListAndImageViewReady(list, imageView) }) {
            findImagePos(imageView)
            initializeImageViewHeight(list, imageView)
            initializeImageViewPosition(imageView)
        }
        setupImageViewAutoScrolling(list, imageView)

        if (image != null) {
            imageView.setImageResource(image)
        }
        if (imageLoading != null) {
            imageView.post {
                imageLoading(imageView)
            }
        }
    }

    private fun checkListAndImageViewReady(list: RecyclerView, imageView: ImageView): Boolean {
        return imageView.getLocationOnScreen()[1] == 0 || list.height == 0
    }

    private fun findImagePos(imageView: ImageView) {
        val yPos = imageView.getLocationOnScreen()[1]
        scrollY -= yPos
    }

    private fun initializeImageViewHeight(list: RecyclerView, imageView: ImageView) {
        val lp = imageView.layoutParams as ViewGroup.LayoutParams
        lp.height = list.height
        imageView.layoutParams = lp
    }

    private fun initializeImageViewPosition(imageView: ImageView) {
        val mlp = imageView.layoutParams as ViewGroup.MarginLayoutParams
        mlp.topMargin = scrollY
        imageView.layoutParams = mlp
        imageView.requestLayout()
    }

    private fun setupImageViewAutoScrolling(list: RecyclerView, imageView: ImageView) {
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollY += dy
                val mlp = imageView.layoutParams as ViewGroup.MarginLayoutParams
                mlp.topMargin = scrollY
                imageView.layoutParams = mlp
                imageView.requestLayout()
            }
        })
    }

    private fun View.getLocationOnScreen(): IntArray {
        val posArray = IntArray(2)
        this.getLocationOnScreen(posArray)
        return posArray
    }

    class ConditionListener() {
        private val asyncScope = CoroutineScope(Job() + Dispatchers.IO)

        fun await(condition: () -> Boolean, func: () -> Unit) {
            asyncScope.launch {
                while (condition()) {
                    delay(50)
                }
                withContext(Dispatchers.Main) {
                    func()
                }
            }
        }
    }
}