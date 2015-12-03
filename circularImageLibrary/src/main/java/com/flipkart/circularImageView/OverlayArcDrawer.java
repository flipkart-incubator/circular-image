package com.flipkart.circularImageView;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;

/**
 * Drawer for overlay arc
 * Created by vivek.soneja on 04/12/15.
 */
public class OverlayArcDrawer {
    private BitmapShader overlayShader;
    private Paint mPaint;
    private Bitmap bitmap;
    private int startAngle;
    private int swipeAngle;

    public OverlayArcDrawer(int color, int startAngle, int swipeAngle) {
        this.startAngle = startAngle;
        this.swipeAngle = swipeAngle;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public OverlayArcDrawer(Bitmap bitmap, int startAngle, int swipeAngle) {
        this.bitmap = bitmap;
        this.startAngle = startAngle;
        this.swipeAngle = swipeAngle;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        overlayShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(overlayShader);
    }

    public void setBounds(Rect bounds) {
        if (overlayShader != null) {
            Matrix matrix = new Matrix();
            double scaley, scalex;
            if (swipeAngle % 360 > 180) {
                int angle = 90 - (swipeAngle % 360 - 180) / 2;
                double radians = Math.toRadians(angle);
                double desiredHeight = bounds.height() / 2 + bounds.height() * Math.sin(radians) / 2;
                scaley = desiredHeight / bitmap.getHeight();
            } else {
                int angle = 90 - (swipeAngle % 360) / 2;
                double radians = Math.toRadians(angle);
                double desiredHeight = bounds.height() / 2 - bounds.height() * Math.sin(radians) / 2;
                scaley = desiredHeight / bitmap.getHeight();
            }

            if((double)bitmap.getWidth() * scaley >= bounds.width()) {
                scalex = scaley;
            } else  {
                scalex = (double)bounds.width() / (double) bitmap.getWidth();
            }
            matrix.setScale((float) scalex, (float) scaley);
            overlayShader.setLocalMatrix(matrix);
        }
    }

    public Paint getPaint() {
        return mPaint;
    }

    public int getStartAngle() {
        return startAngle;
    }

    public int getSwipeAngle() {
        return swipeAngle;
    }
}
