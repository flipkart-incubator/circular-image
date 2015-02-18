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
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

class CircularDrawable extends Drawable {
    private final RectF mRect;
    private final Paint mPaint;
    private float mBorderWidth;
    private int mBorderColor;
    private final Paint mBorderPaint;

    //Objects (Text or Bitmap) that shall be drawn in CircleDrawable
    private List<Object> sourceObjects = new ArrayList();

    private ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;

    private class BitmapDrawer {
        private Bitmap bitmap;
        private BitmapShader bitmapShader;
    }

    private enum DrawingType {
        QUARTER_CIRCLE, HALF_CIRCLE, FULL_CIRCLE;
    }

    CircularDrawable() {
        //Initializing all the variables during construction, in order to avoid initializations during draw operation.
        mRect = new RectF();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setDither(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);

        //Default border color
        mBorderColor = Color.BLACK;
        mBorderPaint.setColor(mBorderColor);
    }

    public void setBorder(int color, float width) {
        mBorderWidth = width;
        mBorderPaint.setStrokeWidth(width);

        mBorderColor = color;
        mBorderPaint.setColor(mBorderColor);
    }

    /**
     * Provide multiple Bitmaps to support
     * As of now we only support FOUR sources, and hence only first four will be considered.
     * Also, the order of drawing is same as the order of arguments.
     *
     * @param sources Either "Bitmap" or "Strings" which needs to be drawn.
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
            } else if (sources[i] instanceof String) {
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
                bitmapDrawer.bitmapShader.setLocalMatrix(getLocalMatrix(bitmapDrawer.bitmap, getDrawingType(i, sourceObjects.size())));
            }
        }
    }

    private DrawingType getDrawingType(int position, int totalItems) {
        switch (totalItems) {
            case 4:
                return DrawingType.QUARTER_CIRCLE;

            case 3:
                if (position == 0) return DrawingType.HALF_CIRCLE;
                else return DrawingType.QUARTER_CIRCLE;

            case 2:
                return DrawingType.HALF_CIRCLE;

            default:
            case 1:
                return DrawingType.FULL_CIRCLE;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        BitmapDrawer drawer = (BitmapDrawer) sourceObjects.get(0);
        mPaint.setShader(drawer.bitmapShader);
        canvas.drawCircle(mRect.centerX(), mRect.centerY(), mRect.width() / 2, mPaint);

        //Draw border
        if (mBorderWidth > 0) canvas.drawCircle(mRect.centerX(), mRect.centerY(), mRect.width() / 2 + mBorderWidth / 2 - 1, mBorderPaint);
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

    public Matrix getLocalMatrix(Bitmap bitmap, DrawingType drawingType) {
        Matrix matrix = new Matrix();
        switch (scaleType) {
            case CENTER_CROP:
                if (drawingType == DrawingType.FULL_CIRCLE) {
                    float scale;
                    if (bitmap.getHeight() > bitmap.getWidth()) {
                        //Portrait
                        scale = mRect.width() / bitmap.getWidth();
                        matrix.setScale(scale, scale);
                        //Portrait we don't want to translate to middle, since most of the faces are in top area, not in center
                        matrix.setScale(scale, scale);
                        matrix.postTranslate(mBorderWidth, mBorderWidth);
                    } else {
                        //Landscape
                        scale = mRect.height() / bitmap.getHeight();
                        float difference = mRect.width() + 2 * mBorderWidth - bitmap.getWidth() * scale;
                        matrix.setScale(scale, scale);
                        matrix.postTranslate(difference / 2, mBorderWidth);
                    }
                }
        }
        return matrix;
    }
}