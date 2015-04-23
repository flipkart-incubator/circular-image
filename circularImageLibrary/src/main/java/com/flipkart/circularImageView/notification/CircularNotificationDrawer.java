package com.flipkart.circularImageView.notification;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Draws circular notification drawer
 * Created by vivek.soneja on 23/04/15.
 */
public class CircularNotificationDrawer extends NotificationDrawer {

    @Override
    public void draw(Canvas canvas, RectF outerBounds, float notificationCenterX, float notificationCenterY) {
        //Adjust notificationCenterY so that it shall not go out of bounds
        float effectiveWidth = mNotificationBounds.width() + mNotificationPadding * 2;
        if (notificationCenterY - effectiveWidth / 2 < outerBounds.top) notificationCenterY = outerBounds.top + effectiveWidth / 2;
        else if (notificationCenterY + effectiveWidth / 2 > outerBounds.bottom) notificationCenterY = outerBounds.bottom - effectiveWidth / 2;
        canvas.drawCircle(notificationCenterX, notificationCenterY, mNotificationBounds.width() / 2 + mNotificationPadding, mNotificationPaint);

        canvas.drawText(mNotificationText, 0, mNotificationText.length(), notificationCenterX, notificationCenterY - (mNotificationTextPaint.ascent() +
                mNotificationTextPaint.descent()) / 2, mNotificationTextPaint);
    }
}
