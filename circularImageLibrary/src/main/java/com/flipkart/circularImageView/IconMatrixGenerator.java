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
public class IconMatrixGenerator {
    private RectF mRect;
    private float mBorderWidth;
    private float mPadding;

    public Matrix generateMatrix(ImageView.ScaleType scaleType, Bitmap bitmap, DrawerHelper.DrawingType drawingType, boolean translateVertically) {
        Matrix matrix = new Matrix();
        switch (scaleType) {
            case CENTER_CROP:
                switch (drawingType) {
                    case QUARTER_CIRCLE:
                        setMatrixForQuarterCircle(matrix, bitmap, drawingType);
                        break;
                    case HALF_CIRCLE:
                        setMatrixForHalfCircle(matrix, bitmap, drawingType, translateVertically);
                        break;
                    case FULL_CIRCLE:
                        setMatrixForFullCircle(matrix, bitmap, translateVertically);
                        break;
                }
        }
        return matrix;
    }

    public IconMatrixGenerator(RectF mRect, float mBorderWidth, float mPadding) {
        this.mRect = mRect;
        this.mBorderWidth = mBorderWidth;
        this.mPadding = mPadding;
    }

    private void setMatrixForHalfCircle(Matrix matrix, Bitmap bitmap, DrawerHelper.DrawingType drawingType, boolean translateVertically) {
        float scale;
        if (bitmap.getWidth() < bitmap.getHeight()) {
            //Portrait
            scale = (mRect.width() - 2 * mPadding) / bitmap.getWidth();
            matrix.setScale(scale, scale);

            float shift;
            if(drawingType.getPosition() == 1) shift = (bitmap.getWidth() * scale  - mRect.width() / 2) * -1 / 2;
            else shift = (bitmap.getWidth() * scale  - mRect.width() / 2) / 2;

            //Portrait we don't want to translate to middle, since most of the faces are in top area, not in center
            matrix.postTranslate(mRect.left + shift + mBorderWidth, mRect.top + mBorderWidth);
        } else {
            //Landscape
            scale = (mRect.width() / 2 - 2 * mPadding)/ bitmap.getWidth();
            float difference;
            if (drawingType.getPosition() == 1) difference = (bitmap.getWidth() * scale  - mRect.width() / 2) * -1 / 2 + mBorderWidth;
            else difference = (bitmap.getWidth() * scale  - 3 * mRect.width() / 2) * -1 / 2 + mBorderWidth;

            float dy = 0;
            if(translateVertically) {
                float verticalDifference = mRect.height() - bitmap.getHeight() * scale;
                dy = verticalDifference / 2;
            }

            matrix.setScale(scale, scale);
            matrix.postTranslate(mRect.left + difference, mRect.top + mBorderWidth + dy);
        }
    }

    private void setMatrixForFullCircle(Matrix matrix, Bitmap bitmap, boolean translateVertically) {
        float scale;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            //Portrait
            scale = (mRect.width() - 2 * mPadding)/ bitmap.getWidth();
            matrix.setScale(scale, scale);

            float dy = 0;
            if(translateVertically) {
                float difference = mRect.height() - bitmap.getHeight() * scale;
                dy = difference / 2;
            }

            matrix.setScale(scale, scale);
            matrix.postTranslate(mRect.left, mRect.top + dy);
        } else {
            //Landscape
            scale = (mRect.height() - 2 * mPadding)/ bitmap.getHeight();
            float difference = mRect.width() - bitmap.getWidth() * scale;

            float dy = 0;
            if(translateVertically) {
                float verticalDifference = mRect.height() - bitmap.getHeight() * scale;
                dy = verticalDifference / 2;
            }

            matrix.setScale(scale, scale);
            matrix.postTranslate(mRect.left + difference / 2, mRect.top + dy);
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void setMatrixForQuarterCircle(Matrix matrix, Bitmap bitmap, DrawerHelper.DrawingType drawingType) {
        float delta = mRect.width()/2 - mRect.width()/(2 * 1.414f);
        float scale;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            //Portrait
            scale = (mRect.width() - 2 * mPadding) / (2 * bitmap.getWidth());
            matrix.setScale(scale, scale);
            //Portrait we don't want to translate to middle, since most of the faces are in top area, not in center
            float translateX = 0, translateY = 0;
            switch (drawingType.getPosition()) {
                case 1:
                    translateX = mBorderWidth + 100;
                    translateY = mBorderWidth + 100;
                    break;

                case 2:
                    translateX = mBorderWidth;
                    translateY = mRect.height() / 2 + mBorderWidth;
                    break;

                case 3:
                    translateX = mRect.width() / 2 + mBorderWidth;
                    translateY = mBorderWidth;
                    break;

                case 4:
                    translateX = mRect.width() / 2 + mBorderWidth;
                    translateY = mBorderWidth + mRect.height() / 2;
                    break;
            }
            matrix.postTranslate(mRect.left +translateX,mRect.top + translateY);
        } else {
            //Landscape
            scale = (mRect.height() / (2 * 1.414f) - 2 * mPadding) / ( bitmap.getHeight());
            float translateX = 0, translateY = 0;
            switch (drawingType.getPosition()) {
                case 1:
                    translateX = mPadding + delta;
                    translateY = mBorderWidth + delta + mPadding;
                    break;

                case 2:
                    translateX = mPadding + delta;
                    translateY = mRect.height() / 2 + mBorderWidth + mPadding;
                    break;

                case 3:
                    translateX = mPadding + mRect.width() / 2;
                    translateY = mBorderWidth + mPadding + delta;
                    break;

                case 4:
                    translateX = mPadding + mRect.width() / 2;
                    translateY = mBorderWidth + mPadding + mRect.height() / 2;
                    break;
            }

            matrix.setScale(scale, scale);
            matrix.postTranslate(mRect.left + translateX, mRect.top + translateY);
        }
    }
}
