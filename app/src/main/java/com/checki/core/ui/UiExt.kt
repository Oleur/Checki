package com.checki.core.ui

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.IBinder
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 * Created by juliensalvi on 08/11/17.
 * Extension functions for Activities, Fragments and Views.
 */

fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
    return unsafeLazy { findViewById<T>(idRes) }
}

fun <T : View> Fragment.bind(@IdRes res : Int) : Lazy<T> {
    return unsafeLazy { view?.findViewById(res) as T }
}

fun <T : View> View.bind(@IdRes idRes: Int): Lazy<T> {
    return unsafeLazy { findViewById<T>(idRes) }
}

fun View.bindDimenAsFloat(@DimenRes idRes: Int): Lazy<Float> {
    return unsafeLazy { resources.getDimensionPixelSize(idRes).toFloat() }
}

fun View.bindDimen(@DimenRes idRes: Int): Lazy<Int> {
    return unsafeLazy { resources.getDimensionPixelSize(idRes) }
}

fun Activity.bindDimen(@DimenRes idRes: Int): Lazy<Int> {
    return unsafeLazy { resources.getDimensionPixelSize(idRes) }
}

fun Fragment.bindDimen(@DimenRes idRes: Int): Lazy<Int> {
    return unsafeLazy { resources.getDimensionPixelSize(idRes) }
}

fun Context?.getActionBarHeight(): Int {
    if (this?.theme == null) {
        return 0
    }
    val tv = TypedValue()
    return if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    } else 0
}

fun Context?.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = this?.resources?.getIdentifier("status_bar_height", "dimen", "android") ?: 0
    if (resourceId > 0) {
        result = this?.resources?.getDimensionPixelSize(resourceId) ?: 0
    }
    return if (result != 0) result else px(24.0)
}

fun Activity.getScreenHeight(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.y
}

fun Activity.getScreenWidth(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}

fun Context.getScreenHeight(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.y
}

fun Context.getScreenWidth(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}

fun Activity.makeShortToast(@StringRes stringResId: Int) {
    Toast.makeText(applicationContext, stringResId, Toast.LENGTH_SHORT).show()
}

fun Activity.makeLongToast(@StringRes stringResId: Int) {
    Toast.makeText(applicationContext, stringResId, Toast.LENGTH_LONG).show()
}

fun Activity.makeShortToast(message: String) {
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}

fun Activity.makeLongToast(message: String) {
    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
}

fun Fragment.makeShortToast(@StringRes stringResId: Int) {
    activity?.let {
        Toast.makeText(it.applicationContext, stringResId, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.makeLongToast(@StringRes stringResId: Int) {
    activity?.let {
        Toast.makeText(it.applicationContext, stringResId, Toast.LENGTH_LONG).show()
    }
}

fun View.makeLongToast(@StringRes stringResId: Int) {
    context?.applicationContext?.let {
        Toast.makeText(context.applicationContext, stringResId, Toast.LENGTH_LONG).show()
    }
}

fun View.makeLongToast(message: String) {
    context?.applicationContext?.let {
        Toast.makeText(context.applicationContext, message, Toast.LENGTH_LONG).show()
    }
}

fun Activity.showKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Activity.hideKeyboard(windowToken: IBinder?) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun Context?.hideKeyboard(windowToken: IBinder?) {
    val imm = this?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun View.hideKeyboard(windowToken: IBinder?) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun Activity.px(dip: Double): Int {
    return if (applicationContext == null) {
        0
    } else (dip * resources.displayMetrics.density + 0.5f).toInt()
}

fun View.px(dip: Double): Int =  if (context == null) {
        0
    } else (dip * resources.displayMetrics.density + 0.5f).toInt()

fun Fragment.px(dip: Double): Int {
    return if (activity == null) {
        0
    } else (dip * resources.displayMetrics.density + 0.5f).toInt()
}

fun Context?.px(dip: Double): Int =
        (dip * (this?.resources?.displayMetrics?.density ?: 0f) + 0.5f).toInt()

private fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
