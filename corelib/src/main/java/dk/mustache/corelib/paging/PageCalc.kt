package dk.mustache.corelib.paging

class PageCalc(private val pageSize: Int) {

    /** Number of items up to and including the given page */
    fun numberItemsIncluding(page: Int): Int {
        return page + 1 * pageSize
    }

    /** Index for the last item in the given page */
    fun lastIndexIn(page: Int): Int {
        return numberItemsIncluding(page) - 1
    }
}