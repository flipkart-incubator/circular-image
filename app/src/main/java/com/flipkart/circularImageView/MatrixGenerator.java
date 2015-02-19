package com.flipkart.circularImageView;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.widget.ImageView;

/**
 * Generate a Matrix to transform the given bitmap to desired destination.
 * <p/>
 * Created by vivek.soneja on 18/02/15.
 */
public class MatrixGenerator {
    private RectF mRect;
    private float mBorderWidth;

    public MatrixGenerator(RectF mRect, float mBorderWidth) {
        this.mRect = mRect;
        this.mBorderWidth = mBorderWidth;
    }

    public Matrix generateMatrix(ImageView.ScaleType scaleType, Bitmap bitmap, DrawerHelper.DrawingType drawingType) {
        Matrix matrix = new Matrix();
        switch (scaleType) {
            case CENTER_CROP:
                if (drawingType == DrawerHelper.DrawingType.FULL_CIRCLE) {
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
                } else if (drawingType == DrawerHelper.DrawingType.HALF_CIRCLE) {
                    float scale;
                    if (bitmap.getWidth() < mRect.width() / 2) {
                        //Portrait
                        scale = mRect.width() / bitmap.getWidth();
                        matrix.setScale(scale, scale);
                        //Portrait we don't want to translate to middle, since most of the faces are in top area, not in center
                        matrix.postTranslate(mBorderWidth, mBorderWidth);
                    } else {
                        //Landscape
                        scale = mRect.height() / bitmap.getHeight();
                        float difference;
                        if (drawingType.getPosition() == 1) difference = mRect.width() / 2 + 2 * mBorderWidth - bitmap.getWidth() * scale;
                        else difference = 3 * mRect.width() / 2 + 2 * mBorderWidth - bitmap.getWidth() * scale;
                        matrix.setScale(scale, scale);
                        matrix.postTranslate(difference / 2, mBorderWidth);
                    }
                }
        }
        return matrix;
    }
}
