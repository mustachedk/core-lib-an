package dk.mustache.corelibexample.toolbar_expandable_test

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dk.mustache.corelibexample.model.MockResponse

class CoursesViewModel : ViewModel() {
    var listener: Listener? = null
    var allCourses: List<MockResponse>? = null
    val showFinishedCoursesObservable = ObservableBoolean(false)

    fun getCurrentCourseSelection(): List<MockResponse> {
        val showFinishedCourses = showFinishedCoursesObservable.get()
        return if (showFinishedCourses) {
            getFinishedCourses()
        } else {
            getUnfinishedCourses()
        }
    }

    fun getFinishedCourses(): List<MockResponse> {
        return allCourses?.filter { it.body != null }?:ArrayList()
    }

    fun getUnfinishedCourses(): List<MockResponse> {
        return allCourses?.filter { it.body == null }?:ArrayList()
    }

    fun getCourses() : LiveData<List<MockResponse>> {
        val list = MutableLiveData<List<MockResponse>>()
        list.value = listOf(MockResponse(id = 100, body = null, title = "tester", userId = 1000), MockResponse(id = 101, body = "testdfkgjldgfk", title = "tester2", userId = 10001))
        return list
    }

    interface Listener {
        fun courses(loginResponseDTO: MockResponse)
        fun showErrors(baseDTO: MockResponse)
    }
}