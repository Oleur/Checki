package com.checki.core.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.*

class StyledStringBuilder : SpannableStringBuilder() {

    fun appendBold(text: CharSequence): StyledStringBuilder {
        return appendStyled(text, StyleSpan(android.graphics.Typeface.BOLD))
    }

    fun appendItalic(text: CharSequence): StyledStringBuilder {
        return appendStyled(text, StyleSpan(android.graphics.Typeface.ITALIC))
    }

    fun appendColor(text: CharSequence, color: Int): StyledStringBuilder {
        return appendStyled(text, ForegroundColorSpan(color))
    }

    fun appendStyle(text: CharSequence, context: Context, resId: Int): StyledStringBuilder {
        return appendStyled(text, TextAppearanceSpan(context, resId))
    }

    fun appendBackgroundColor(text: CharSequence, color: Int): StyledStringBuilder {
        return appendStyled(text, BackgroundColorSpan(color))
    }

    fun appendLine(): StyledStringBuilder {
        super.append("\n")
        return this
    }

    fun appendClickable(text: CharSequence, clickableSpan: ClickableSpan): StyledStringBuilder {
        return appendStyled(text, clickableSpan)
    }

    private fun appendStyled(text: CharSequence, what: Any): StyledStringBuilder {
        if (TextUtils.isEmpty(text))
            return this
        val pos1 = this.length
        super.append(text)
        this.setSpan(what, pos1, pos1 + text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    fun appendImage(drawable: Drawable?): StyledStringBuilder {
        if (drawable == null) {
            return this
        }
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val ds = ImageSpan(drawable)
        return appendStyled(" ", ds)
    }

    fun appendImage(resources: Resources, bitmap: Bitmap?): StyledStringBuilder {
        if (bitmap == null) {
            return this
        }
        val d = BitmapDrawable(resources, bitmap)
        d.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
        val ds = ImageSpan(d)
        return appendStyled(" ", ds)
    }

    /***
     * appendImage bug when displayin many images on same line.
     * but using ImageSpan has bug with vertical alignment
     */
    fun appendDrawable(d: Drawable?, top: Int, align: Int): StyledStringBuilder {
        if (d == null) {
            return this
        }
        d.setBounds(0, top, d.intrinsicWidth, d.intrinsicHeight)
        return appendStyled(" ", ImageSpan(d, align))
    }
}
