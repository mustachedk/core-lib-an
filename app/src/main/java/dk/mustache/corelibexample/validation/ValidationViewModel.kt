package dk.mustache.corelibexample.validation

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import dk.mustache.corelib.utils.MDate
import dk.mustache.corelib.validation.views.MenuItem
import dk.mustache.corelib.validation.views.ValidationMenuAdapter

class ValidationViewModel : ViewModel() {
    val alwaysTrue = ObservableField("")
    val notEmpty = ObservableField("")
    val phone = ObservableField("")
    val birthdate = ObservableField(MDate.BuilderDk().now())

    val menuAdapter = ValidationMenuAdapter(
        listOf(
            MenuItem("Option 1", 1),
            MenuItem("Option 2", 2),
            MenuItem("Option 3", 3)
        ), MenuItem("", null)
    )

    lateinit var iconClickListener: ((layoutView: View, failureView: View, message: String) -> Unit)

    fun doTheThing(): String {
        val alwaysTrueVal = alwaysTrue.get()
        val notEmptyVal = notEmpty.get()
        val phoneVal = phone.get()
        val menuVal = menuAdapter.getSelectedTitle()
        val birthdateVal = birthdate.get()
        return "$alwaysTrueVal, $notEmptyVal, $phoneVal, $menuVal, ${birthdateVal?.year}"
    }
}