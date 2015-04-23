package com.flipkart.circularImageView.notification;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Draws Rectangular Notification
 * Created by vivek.soneja on 23/04/15.
 */
public class RectangularNotificationDrawer extends NotificationDrawer {
    @Override
    public void draw(Canvas canvas, Rect outerBounds, float notificationCenterX, float notificationCenterY) {
        //Adjust notificationCenterY so that it shall not go out of bounds
        float effectiveHeight = mNotificationBounds.height() + mNotificationPadding * 2;
        if (notificationCenterY - effectiveHeight / 2 < outerBounds.top) notificationCenterY = outerBounds.top + effectiveHeight / 2;
        else if (notificationCenterY + effectiveHeight / 2 > outerBounds.bottom) notificationCenterY = outerBounds.bottom - effectiveHeight / 2;
        RectF rectf = new RectF(notificationCenterX - mNotificationBounds.width() / 2 - mNotificationPadding, notificationCenterY - mNotificationBounds
                .height() / 2 - mNotificationPadding, notificationCenterX + mNotificationBounds.width() / 2 + mNotificationPadding, notificationCenterY +
                mNotificationBounds.height() / 2 + mNotificationPadding);
        canvas.drawRoundRect(rectf, mNotificationTextPaint.getTextSize() * 0.15f, mNotificationTextPaint.getTextSize() * 0.15f, mNotificationPaint);

        canvas.drawText(mNotificationText, 0, mNotificationText.length(), notificationCenterX, notificationCenterY - (mNotificationTextPaint.ascent() +
                mNotificationTextPaint.descent()) / 2, mNotificationTextPaint);
    }
}
