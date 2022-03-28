package dk.mustache.corelibexample.sandbox.toolbar_expandable_test

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.databinding.LayoutSelectCoursesBinding


class CoursesSelectLayout : ConstraintLayout {

    lateinit var binding: LayoutSelectCoursesBinding
    lateinit var adapter: CoursesTypeSelectAdapter
    var selectionListener: SelectionListener? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(
            context,
            attrs
        )
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun buildCourseTypeSelection(): List<CourseTypeItem> {
        val list = mutableListOf(CourseTypeItem(resources.getString(R.string.unfinished_courses), resources.getString(R.string.unfinished_courses),
            defaultSelected = true,
            filterCompleted = false
        ), CourseTypeItem(resources.getString(R.string.finished_courses), resources.getString(R.string.finished_courses),
            defaultSelected = false,
            filterCompleted = true
        ))
        return list
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_select_courses,
            this as ViewGroup?,
            true
        )


        val selectionOptions = buildCourseTypeSelection()
        adapter = CoursesTypeSelectAdapter(selectionOptions) { item, selected ->
            selectionListener?.onItemSelected(item)
        }
        
        binding.selectCompletedCoursesList.adapter = adapter
        binding.selectCompletedCoursesList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    fun setDefaultSelection(id: String): Boolean {
        adapter.deselectAll()
        val item = adapter.items.find {
            it.id == id
        }
        item?.selected = true
        adapter.notifyDataSetChanged()
        return item!=null
    }

    interface SelectionListener {
        fun onItemSelected(courseType: CourseTypeItem)
    }
}