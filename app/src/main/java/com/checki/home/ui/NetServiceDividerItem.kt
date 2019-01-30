package com.checki.home.ui

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.checki.core.extensions.px

internal class NetServiceDividerItem(private val divider: Drawable) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = divider.intrinsicHeight
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight

        val adapter = parent.adapter ?: return

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            //get adapter position of view
            val position = parent.getChildAdapterPosition(child)
            if (position == RecyclerView.NO_POSITION) {
                continue
            }
            val itemViewType = adapter.getItemViewType(position)

            //Do not draw divider if next item is different from current one
            if (position == 0 || (position < adapter.itemCount - 1 && adapter.getItemViewType(position + 1)
                            .javaClass.simpleName != itemViewType.javaClass.simpleName)) {
                continue
            }

            //apply different divider margin left depending on item type
            val customMargin = parent.px(72.0)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + divider.intrinsicHeight

            val marginLeft = if (i < childCount - 1) dividerLeft + customMargin else dividerLeft

            divider.setBounds(marginLeft, dividerTop, dividerRight, dividerBottom)
            divider.draw(canvas)
        }
    }
}
