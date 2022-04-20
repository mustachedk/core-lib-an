package dk.mustache.corelib.paging

/** Expects zero-indexed page numbers */
class PageCalc(private val pageSize: Int) {

    /** Number of items up to and including the given page */
    fun itemCountIncluding(page: Int): Int {
        return page + 1 * pageSize
    }

    /** Number of items on all pages before, and not including, the given page */
    fun itemCountExcluding(page: Int): Int {
        return page * pageSize
    }

    /** Index for the last item in the given page */
    fun indexLastIn(page: Int): Int {
        return itemCountIncluding(page) - 1
    }
}