package dk.mustache.corelib.section_header_list

open class SectionItem(var headerText: String? = "", val weight: Int = -Int.MAX_VALUE, var isHeader: Boolean? = false, var isPlaceholder: Boolean? = false)