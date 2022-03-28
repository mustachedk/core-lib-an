package dk.mustache.corelibexample.sandbox.toolbar_expandable_test

import dk.mustache.corelib.selectable_list.SelectableAdapter
import dk.mustache.corelib.selectable_list.SelectableAdapterSettings
import dk.mustache.corelibexample.R

class CoursesTypeSelectAdapter (courseItems: List<CourseTypeItem>,
                                onSelected: (item: CourseTypeItem, selected: Boolean) -> Unit) :
                                    SelectableAdapter<CourseTypeItem>(courseItems, onSelected,
                                        SelectableAdapterSettings(
                                            layoutResId = R.layout.item_selectable_text,
                                            selectedIcon = R.drawable.ico_check_circle,
                                            unselectedIcon = R.drawable.ic_vector_radiostroke))