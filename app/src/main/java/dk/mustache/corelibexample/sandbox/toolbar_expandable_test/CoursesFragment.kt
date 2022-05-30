package dk.mustache.corelibexample.sandbox.toolbar_expandable_test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.lifecycle.ViewModelProviders
import dk.mustache.corelib.list_header_viewpager.GenericPagerFragment
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.databinding.FragmentSelectableDropdownBinding

class CoursesFragment : GenericPagerFragment() {
    lateinit var binding: FragmentSelectableDropdownBinding
    private lateinit var viewModel: CoursesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSelectableDropdownBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProviders.of(requireActivity()).get(CoursesViewModel::class.java)
        val overlayLayout = CoursesSelectLayout(requireContext())
        overlayLayout.setDefaultSelection(getCoursesSectionString())
        overlayLayout.selectionListener = object : CoursesSelectLayout.SelectionListener {
            override fun onItemSelected(courseFilterType: CourseTypeItem) {
                viewModel.showFinishedCoursesObservable.set(courseFilterType.filterCompleted)
                binding.constraintToolbar.setTitle(courseFilterType.name)
                binding.constraintToolbar.hideOverlay(delayMillis = 300)
            }
        }
        binding.overlayView = overlayLayout
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.coursesCountSubtitle = "${0}"
        binding.coursesSelectionTitle = getCoursesSectionString()
    }

    fun getCoursesSectionString(): String {
        viewModel.getCourses()
        val showFinished = viewModel.showFinishedCoursesObservable.get()
        return if (showFinished) getString(R.string.finished_courses) else getString(R.string.unfinished_courses)
    }

    val filterCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            val filteredCourses = viewModel.getCurrentCourseSelection()
            binding.constraintToolbar.setSubtitle("${filteredCourses.size} ${resources.getString(R.string.courses_subtitle)}")
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.showFinishedCoursesObservable.addOnPropertyChangedCallback(filterCallback)
    }

    override fun onPause() {
        super.onPause()
        viewModel.showFinishedCoursesObservable.removeOnPropertyChangedCallback(filterCallback)
    }
}