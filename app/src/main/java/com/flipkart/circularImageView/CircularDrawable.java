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
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.flipkart.circularImageView.DrawerHelper.DrawingType;

class CircularDrawable extends Drawable {
    private final static boolean USE_THIN_FONT = false;

    private final RectF mRect;
    private float mBorderWidth;
    private int mBorderColor;
    private final Paint mPaint;
    private final Paint mBorderPaint;
    private final Paint mTextPaint;
    private final Paint mBackgroundPaint;
    private DrawerHelper drawerHelper;

    //Objects (Text or Bitmap) that shall be drawn in CircleDrawable
    private List<Object> sourceObjects = new ArrayList();

    private ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;

    public CircularDrawable() {
        //Initializing all the variables during construction, in order to avoid initializations during draw operation.
        mRect = new RectF();

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

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(100);
        mTextPaint.setLinearText(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextLocale(Locale.ENGLISH);
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
                bitmapDrawer.bitmapShader.setLocalMatrix(getLocalMatrix(bitmapDrawer.bitmap, drawerHelper.getDrawingType(i, sourceObjects.size())));
            }
        }

        //Set Text size for drawing text
        mTextPaint.setTextSize((bounds.height() - 2 * mBorderWidth) * 0.4f);
    }

    @Override
    public void draw(Canvas canvas) {
        drawerHelper.drawComplexCircle(canvas);

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
        return new MatrixGenerator(mRect, mBorderWidth).generateMatrix(scaleType, bitmap, drawingType);
    }
}