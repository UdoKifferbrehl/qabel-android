package de.qabel.qabelbox.contacts.view.widgets

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build


class ContactIconDrawable(var colors: List<Int>) : Drawable() {

    private var round = 360;
    private var space = 10;

    override fun draw(canvas: Canvas?) {
        val allDegrees = round.minus(if (colors.size > 1) space * colors.size else 0);
        val eachDegrees = allDegrees / colors.size.toFloat();

        val backgroundPaint = Paint();
        var current = space / 2f;
        for (i in 0 until colors.size) {
            backgroundPaint.color = colors[i];
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas?.drawArc(bounds.left.toFloat(), bounds.top.toFloat(), bounds.right.toFloat(),
                        bounds.bottom.toFloat(), current, eachDegrees, true, backgroundPaint);
            }
            current = current.plus(eachDegrees).plus(if (i < colors.size.dec()) space.toFloat() else (space / 2).toFloat());
        }
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE;
    }

    override fun getAlpha(): Int {
        return super.getAlpha()
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

}

