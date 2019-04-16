package dk.mustache.corelib.utils

import android.text.TextUtils
import android.util.Patterns

/**
 * Validate Email
 */
fun String.isEmail(): Boolean = !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

/**
 * Validate Phone Number
 */
fun String.isDanishPhoneNumber(): Boolean = !TextUtils.isEmpty(this) && Patterns.PHONE.matcher(this).matches()

/**
 * Validate Password:
 * - Min 6 chars
 */
fun String.isPassword(): Boolean = !TextUtils.isEmpty(this)
