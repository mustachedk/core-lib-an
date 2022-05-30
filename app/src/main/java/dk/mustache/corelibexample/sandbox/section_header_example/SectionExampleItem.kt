package dk.mustache.corelibexample.sandbox.section_header_example

import dk.mustache.corelib.section_header_list.SectionItem

data class SectionExampleItem(val header: String, val order: Int): SectionItem(header, order)