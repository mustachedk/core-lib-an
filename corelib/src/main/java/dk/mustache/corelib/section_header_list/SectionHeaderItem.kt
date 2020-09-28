package dk.mustache.corelib.section_header_list

data class SectionHeaderItem(val header: String, val orderWeight: Int): SectionItem(header, orderWeight, true)