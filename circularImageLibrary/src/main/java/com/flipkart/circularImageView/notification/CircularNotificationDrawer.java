package com.flipkart.circularImageView.notification;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Draws circular notification drawer
 * Created by vivek.soneja on 23/04/15.
 */
public class CircularNotificationDrawer extends NotificationDrawer {
    private Float radius;
    private Float textSize;

    public CircularNotificationDrawer setNotificationSize(float radius, float textSize) {
        this.radius = radius;
        this.textSize = textSize;
        return this;
    }

    @Override
    public void draw(Canvas canvas, Rect outerBounds, float notificationCenterX, float notificationCenterY) {
        //Adjust notificationCenterY so that it shall not go out of bounds
        float effectiveWidth = getEffectiveWidth();

        if (notificationCenterY - effectiveWidth / 2 < outerBounds.top) notificationCenterY = outerBounds.top + effectiveWidth / 2;
        else if (notificationCenterY + effectiveWidth / 2 > outerBounds.bottom) notificationCenterY = outerBounds.bottom - effectiveWidth / 2;

        if(notificationCenterX + effectiveWidth / 2 > outerBounds.right) notificationCenterX = outerBounds.right - effectiveWidth /2;
        else if (notificationCenterX - effectiveWidth / 2 < outerBounds.left) notificationCenterX = outerBounds.left + effectiveWidth / 2;

        canvas.drawCircle(notificationCenterX, notificationCenterY, effectiveWidth / 2, mNotificationPaint);

        prepareNotificationTextPaint();

        canvas.drawText(mNotificationText, 0, mNotificationText.length(), notificationCenterX, notificationCenterY - (mNotificationTextPaint.ascent() +
                mNotificationTextPaint.descent()) / 2, mNotificationTextPaint);
    }

    private void prepareNotificationTextPaint() {
        if (textSize != null) mNotificationTextPaint.setTextSize(textSize);
    }

    private float getEffectiveWidth() {
        if(radius == null) return mNotificationBounds.width() + mNotificationPadding * 2;
        else return 2 * radius;
    }
}
