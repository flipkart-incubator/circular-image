package com.flipkart.circularImageView.notification;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Draws notification
 * Created by vivek.soneja on 23/04/15.
 */
@SuppressWarnings("unused")
public abstract class NotificationDrawer {
    private float mNotificationCenterX;
    private float mNotificationCenterY;
    protected float mNotificationPadding; //Pixels
    protected float mNotificationHeight; //Pixels
    protected float mNotificationWidth; //Pixels
    protected double mNotificationAngleFromHorizontal; //Radians
    protected Rect mNotificationBounds;
    protected final Paint mNotificationPaint;
    protected String mNotificationText;
    protected final Paint mNotificationTextPaint;

    public NotificationDrawer() {
        mNotificationPaint = new Paint();
        mNotificationPaint.setAntiAlias(true);
        mNotificationPaint.setDither(true);
        mNotificationPaint.setColor(Color.RED);

        mNotificationTextPaint = new Paint();
        mNotificationTextPaint.setColor(Color.WHITE);
        mNotificationTextPaint.setTextSize(90);
        mNotificationTextPaint.setLinearText(true);
        mNotificationTextPaint.setAntiAlias(true);
        mNotificationTextPaint.setDither(true);
        mNotificationTextPaint.setTextAlign(Paint.Align.CENTER);

        this.mNotificationAngleFromHorizontal = 0.785; //Radians (45 Degrees)
        mNotificationBounds = new Rect();
    }

    /**
     * Show the notification ticker text
     *
     * @param notificationText Text to be shown
     */
    public NotificationDrawer setNotificationText(String notificationText) {
        this.mNotificationText = notificationText;
        return this;
    }

    /**
     * Set Notification Angle from horizontal
     *
     * @param notificationAngle angle from horizontal (in Degree).
     */
    public NotificationDrawer setNotificationAngle(int notificationAngle) {
        this.mNotificationAngleFromHorizontal = 3.14 * notificationAngle / 180;
        return this;
    }

    /**
     * Set notification background Color
     *
     * @param textColor       Text color
     * @param backgroundColor Background color
     */
    public NotificationDrawer setNotificationColor(int textColor, int backgroundColor) {
        mNotificationPaint.setColor(backgroundColor);
        mNotificationTextPaint.setColor(textColor);
        return this;
    }

    public void drawNotification(Canvas canvas, RectF outerBounds) {
        draw(canvas, outerBounds, mNotificationCenterX, mNotificationCenterY);
    }

    public void onBoundsChange(Rect bounds, float borderWidth) {
        mNotificationTextPaint.setTextSize((bounds.height() - 2 * borderWidth) * 0.4f * 0.65f);
        mNotificationPadding = mNotificationTextPaint.getTextSize() * 0.3f;

        int notificationTextLength = mNotificationText.length();
        float textSize = mNotificationTextPaint.getTextSize();
        mNotificationTextPaint.getTextBounds(mNotificationText, 0, notificationTextLength, mNotificationBounds);
        mNotificationCenterX = (float) (bounds.centerX() + (bounds.width() / 2) * Math.cos(mNotificationAngleFromHorizontal));
        //Adjust notificationCenterX so that it shall not go out of bounds
        float effectiveWidth = mNotificationBounds.width() + mNotificationPadding * 2;
        if (mNotificationCenterX + effectiveWidth / 2 > bounds.right) mNotificationCenterX = bounds.right - effectiveWidth / 2;
        else if (mNotificationCenterX - effectiveWidth / 2 < bounds.left) mNotificationCenterX = bounds.left + effectiveWidth / 2;

        mNotificationCenterY = (float) (bounds.centerY() - (bounds.width() / 2) * Math.sin(mNotificationAngleFromHorizontal));
    }

    public abstract void draw(Canvas canvas, RectF outerBounds, float centerX, float centerY);
}
