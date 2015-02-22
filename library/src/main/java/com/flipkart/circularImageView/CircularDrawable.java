package com.flipkart.circularImageView;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.flipkart.circularImageView.DrawerHelper.DrawingType;

public class CircularDrawable extends Drawable {
    private final static boolean USE_THIN_FONT = false;

    private final RectF mRect;
    private float mBorderWidth;
    private int mBorderColor;
    private final Paint mPaint;
    private final Paint mBorderPaint;
    private final Paint mTextPaint;
    private final Paint mBackgroundPaint;
    private final Paint mNotificationPaint;
    private final Paint mNotificationTextPaint;

    private Bitmap badge;
    private final Paint mBadgePaint;
    private final RectF mBadgeRect;

    private float notificationPadding = 20;
    private DrawerHelper drawerHelper;

    //Objects (Text or Bitmap) that shall be drawn in CircleDrawable
    private List<Object> sourceObjects = new ArrayList();

    private ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;
    private String notificationText;
    private NotificationStyle notificationStyle;
    private double angleFromHorizontal; //Radians

    public enum NotificationStyle {
        Rectangle, Circle
    }

    public CircularDrawable() {
        //Initializing all the variables during construction, in order to avoid initializations during draw operation.
        mRect = new RectF();
        mBadgeRect = new RectF();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setDither(true);

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setDither(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);

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
//        mNotificationTextPaint.setTextLocale(Locale.ENGLISH);
        mNotificationTextPaint.setTextAlign(Paint.Align.CENTER);

        notificationStyle = NotificationStyle.Rectangle;
        this.angleFromHorizontal = 0.785; //Radians (45 Degrees)

        mBadgePaint = new Paint();
        mBadgePaint.setAntiAlias(true);
        mBadgePaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(100);
        mTextPaint.setLinearText(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
//        mTextPaint.setTextLocale(Locale.ENGLISH);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        //Setting Thin Font space
        if (USE_THIN_FONT) {
            Typeface tf = Typeface.create("sans-serif-light", Typeface.NORMAL);
            mTextPaint.setTypeface(tf);
        }

        //Default border color
        mBorderColor = Color.BLACK;
        mBorderPaint.setColor(mBorderColor);

        drawerHelper = new DrawerHelper(mRect, mPaint, mBorderPaint, mTextPaint, mBackgroundPaint, sourceObjects);
    }

    public void setBorder(int color, float width) {
        mBorderWidth = width;
        mBorderPaint.setStrokeWidth(width);

        mBorderColor = color;
        mBorderPaint.setColor(mBorderColor);
    }

    /**
     * Show the notification ticker text
     *
     * @param notificationText Text to be shown
     */
    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    /**
     * Set Notification Angle from horizontal
     *
     * @param notificationAngle angle from horizontal (in Degree).
     */
    public void setNotificationAngle(int notificationAngle) {
        this.angleFromHorizontal = 3.14 * notificationAngle / 180;
    }

    /**
     * Set Notification Style
     *
     * @param notificationStyle Notification style (Circle or Rectangle)
     */
    public void setNotificationStyle(NotificationStyle notificationStyle) {
        this.notificationStyle = notificationStyle;
    }

    /**
     * Set notification background Color
     *
     * @param textColor       Text color
     * @param backgroundColor Background color
     */
    public void setNotificationColor(int textColor, int backgroundColor) {
        mNotificationPaint.setColor(backgroundColor);
        mNotificationTextPaint.setColor(textColor);
    }


    /**
     * Provide Icon for Badge
     *
     * @param badge Icon to be used for creating a badge
     */
    public void setBadge(Bitmap badge) {
        this.badge = badge;
        BitmapShader badgeShader = new BitmapShader(this.badge, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBadgePaint.setShader(badgeShader);
    }

    /**
     * Provide multiple Bitmaps to support
     * As of now we only support FOUR sources, and hence only first four will be considered.
     * Also, the order of drawing is same as the order of arguments.
     *
     * @param sources Either "Bitmap" or "TextDrawer" which needs to be drawn.
     */
    public void setBitmapOrText(Object... sources) throws IllegalArgumentException {
        sourceObjects.clear();
        for (int i = 0; i < sources.length && i < 4; i++) {
            if (sources[i] instanceof Bitmap) {
                Bitmap bitmap = (Bitmap) sources[i];
                BitmapDrawer drawer = new BitmapDrawer();
                drawer.bitmap = bitmap;
                drawer.bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                sourceObjects.add(drawer);
            } else if (sources[i] instanceof TextDrawer) {
                sourceObjects.add(sources[i]);
            } else {
                throw new IllegalArgumentException("Arguments can either be instance of Bitmap or String");
            }
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mRect.set(mBorderWidth, mBorderWidth, bounds.width() - mBorderWidth, bounds.height() - mBorderWidth);

        for (int i = 0; i < sourceObjects.size(); i++) {
            Object sourceObject = sourceObjects.get(i);
            if (sourceObject instanceof BitmapDrawer) {
                BitmapDrawer bitmapDrawer = (BitmapDrawer) sourceObject;
                bitmapDrawer.bitmapShader.setLocalMatrix(getLocalMatrix(mRect, mBorderWidth, bitmapDrawer.bitmap, drawerHelper.getDrawingType(i,
                        sourceObjects.size())));
            }
        }

        //Set Local Matrix for Shader of badge
        if(badge != null) {
            mBadgeRect.set(0, 0, mRect.width() / 2.5f, mRect.height() / 2.5f);
            mBadgePaint.getShader().setLocalMatrix(getLocalMatrixForBottomCorner(mRect, mBadgeRect, badge));
        }

        //Set Text size for drawing text
        mTextPaint.setTextSize((bounds.height() - 2 * mBorderWidth) * 0.4f);
        mNotificationTextPaint.setTextSize(mTextPaint.getTextSize() * 0.65f);
        notificationPadding = mNotificationTextPaint.getTextSize() * 0.3f;
    }

    @Override
    public void draw(Canvas canvas) {
        long currentTime = System.currentTimeMillis();
        drawerHelper.drawComplexCircle(canvas);

        //Draw border
        if (mBorderWidth > 0) canvas.drawCircle(mRect.centerX(), mRect.centerY(), mRect.width() / 2 + mBorderWidth / 2 - 1, mBorderPaint);

        //Draw Badge
        if (badge != null) {
            canvas.drawCircle(mRect.right - mBadgeRect.width() / 2, mRect.bottom - mBadgeRect.height() / 2, mBadgeRect.width() / 2, mBadgePaint);
        }

        //Draw Notification
        if (notificationText != null && !notificationText.equals("")) {
            //Draw Notification
            drawNotification(canvas);
        }
        Log.v("CircularImageView", "Time taken to draw: " + (System.currentTimeMillis() - currentTime) + "ms");
    }

    private void drawNotification(Canvas canvas) {
        Rect bound = new Rect();
        int notificationTextLength = notificationText.length();
        float textSize = mNotificationTextPaint.getTextSize();
        mNotificationTextPaint.getTextBounds(notificationText, 0, notificationTextLength, bound);
        float notificationCenterX = (float) (mRect.centerX() + (mRect.width() / 2) * Math.cos(angleFromHorizontal));
        //Adjust notificationCenterX so that it shall not go out of bounds
        float effectiveWidth = bound.width() + notificationPadding * 2;
        if (notificationCenterX + effectiveWidth / 2 > mRect.right) notificationCenterX = mRect.right - effectiveWidth / 2;
        else if (notificationCenterX - effectiveWidth / 2 < mRect.left) notificationCenterX = mRect.left + effectiveWidth / 2;

        float notificationCenterY = (float) (mRect.centerY() - (mRect.width() / 2) * Math.sin(angleFromHorizontal));

        switch (notificationStyle) {
            case Rectangle:
                //Adjust notificationCenterY so that it shall not go out of bounds
                float effectiveHeight = bound.height() + notificationPadding * 2;
                if (notificationCenterY - effectiveHeight / 2 < mRect.top) notificationCenterY = mRect.top + effectiveHeight / 2;
                else if (notificationCenterY + effectiveHeight / 2 > mRect.bottom) notificationCenterY = mRect.bottom - effectiveHeight / 2;
                RectF rectf = new RectF(notificationCenterX - bound.width() / 2 - notificationPadding, notificationCenterY - bound.height() / 2 -
                        notificationPadding, notificationCenterX + bound.width() / 2 + notificationPadding, notificationCenterY + bound.height() / 2 +
                        notificationPadding);
                canvas.drawRoundRect(rectf, textSize * 0.15f, textSize * 0.15f, mNotificationPaint);
                break;

            case Circle:
                //Adjust notificationCenterY so that it shall not go out of bounds
                if (notificationCenterY - effectiveWidth / 2 < mRect.top) notificationCenterY = mRect.top + effectiveWidth / 2;
                else if (notificationCenterY + effectiveWidth / 2 > mRect.bottom) notificationCenterY = mRect.bottom - effectiveWidth / 2;
                canvas.drawCircle(notificationCenterX, notificationCenterY, bound.width() / 2 + notificationPadding, mNotificationPaint);
                break;
        }

        canvas.drawText(notificationText, 0, notificationTextLength, notificationCenterX, notificationCenterY - (mNotificationTextPaint.ascent() +
                mNotificationTextPaint.descent()) / 2, mNotificationTextPaint);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    public Matrix getLocalMatrix(RectF rectF, float borderWidth, Bitmap bitmap, DrawingType drawingType) {
        return new MatrixGenerator(rectF, borderWidth).generateMatrix(scaleType, bitmap, drawingType);
    }

    private Matrix getLocalMatrixForBottomCorner(RectF containerRect, RectF cornerRect, Bitmap badge) {
        return new MatrixGenerator(mRect, mBorderWidth).generateMatrix(containerRect, cornerRect, badge);
    }
}