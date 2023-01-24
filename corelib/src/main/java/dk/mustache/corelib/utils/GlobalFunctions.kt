package dk.mustache.corelib.util

fun <T> ifElseNull(comparable: Boolean, result: () -> T): T? {
    if (comparable) {
        return result()
    }
    return null
}

fun <T> T?.unlessNullThen(thenDo: () -> T): T {
    if (this == null) {
        return thenDo()
    }
    return this
}

fun <T> T?.whenNotNull(thenDo: (T) -> Unit) {
    if (this != null) {
        thenDo(this)
    }
}

fun Float.whenNotNegative(thenDo: (Float) -> Unit) {
    if (this >= 0f) {
        thenDo(this)
    }
}

fun Int.whenNotNegative(thenDo: (Int) -> Unit) {
    if (this >= 0) {
        thenDo(this)
    }
}