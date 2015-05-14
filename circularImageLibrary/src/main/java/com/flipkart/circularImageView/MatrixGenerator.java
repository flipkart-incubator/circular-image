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
                switch (drawingType) {
                    case QUARTER_CIRCLE:
                        setMatrixForQuarterCircle(matrix, bitmap, drawingType);
                        break;
                    case HALF_CIRCLE:
                        setMatrixForHalfCircle(matrix, bitmap, drawingType);
                        break;
                    case FULL_CIRCLE:
                        setMatrixForFullCircle(matrix, bitmap, drawingType);
                        break;
                }
        }
        return matrix;
    }

    private void setMatrixForHalfCircle(Matrix matrix, Bitmap bitmap, DrawerHelper.DrawingType drawingType) {
        float scale;
        if (bitmap.getWidth() < bitmap.getHeight()) {
            //Portrait
            scale = mRect.width() / bitmap.getWidth();
            matrix.setScale(scale, scale);

            float shift;
            if(drawingType.getPosition() == 1) shift = (bitmap.getWidth() * scale  - mRect.width() / 2) * -1 / 2;
            else shift = (bitmap.getWidth() * scale  - mRect.width() / 2) / 2;

            //Portrait we don't want to translate to middle, since most of the faces are in top area, not in center
            matrix.postTranslate(mRect.left + shift + mBorderWidth, mRect.top + mBorderWidth);
        } else {
            //Landscape
            scale = mRect.height() / bitmap.getHeight();
            float difference;
            if (drawingType.getPosition() == 1) difference = (bitmap.getWidth() * scale  - mRect.width() / 2) * -1 / 2 + mBorderWidth;
            else difference = (bitmap.getWidth() * scale  - 3 * mRect.width() / 2) * -1 / 2 + mBorderWidth;
            matrix.setScale(scale, scale);
            matrix.postTranslate(mRect.left + difference, mRect.top + mBorderWidth);
        }
    }

    private void setMatrixForFullCircle(Matrix matrix, Bitmap bitmap, DrawerHelper.DrawingType drawingType) {
        float scale;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            //Portrait
            scale = mRect.width() / bitmap.getWidth();
            matrix.setScale(scale, scale);
            //Portrait we don't want to translate to middle, since most of the faces are in top area, not in center
            matrix.setScale(scale, scale);
            matrix.postTranslate(mRect.left + mBorderWidth, mRect.top + mBorderWidth);
        } else {
            //Landscape
            scale = mRect.height() / bitmap.getHeight();
            float difference = mRect.width() + 2 * mBorderWidth - bitmap.getWidth() * scale;
            matrix.setScale(scale, scale);
            matrix.postTranslate(mRect.left + difference / 2, mRect.top + mBorderWidth);
        }
    }

    private void setMatrixForQuarterCircle(Matrix matrix, Bitmap bitmap, DrawerHelper.DrawingType drawingType) {
        float scale;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            //Portrait
            scale = mRect.width() / (2 * bitmap.getWidth());
            matrix.setScale(scale, scale);
            //Portrait we don't want to translate to middle, since most of the faces are in top area, not in center
            float translateX = 0, translateY = 0;
            switch (drawingType.getPosition()) {
                case 1:
                    translateX = mBorderWidth;
                    translateY = mBorderWidth;
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
            scale = mRect.height() / (2 * bitmap.getHeight());
            float translateX = 0, translateY = 0;
            switch (drawingType.getPosition()) {
                case 1:
                    translateX = (mRect.width() / 2 + 2 * mBorderWidth - bitmap.getWidth() * scale) / 2;
                    translateY = mBorderWidth;
                    break;

                case 2:
                    translateX = (mRect.width() / 2 + 2 * mBorderWidth - bitmap.getWidth() * scale) / 2;
                    translateY = mRect.height() / 2 + mBorderWidth;
                    break;

                case 3:
                    translateX = (3 * mRect.width() / 2 + 2 * mBorderWidth - bitmap.getWidth() * scale) / 2;
                    translateY = mBorderWidth;
                    break;

                case 4:
                    translateX = (3 * mRect.width() / 2 + 2 * mBorderWidth - bitmap.getWidth() * scale) / 2;
                    translateY = mBorderWidth + mRect.height() / 2;
                    break;
            }

            matrix.setScale(scale, scale);
            matrix.postTranslate(mRect.left + translateX, mRect.top + translateY);
        }
    }

    public Matrix generateMatrix(RectF containerRect, RectF badgeRect, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        float scale;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            //Portrait
            scale = badgeRect.width() / bitmap.getWidth();
            matrix.setScale(scale, scale);
            //Portrait we don't want to translate to middle, since most of the faces are in top area, not in center
            matrix.setScale(scale, scale);
            matrix.postTranslate(mBorderWidth + containerRect.width() - badgeRect.width(), mBorderWidth + containerRect.height() - badgeRect.height());
        } else {
            //Landscape
            scale = badgeRect.height() / bitmap.getHeight();
            float difference = badgeRect.width() + 2 * mBorderWidth - bitmap.getWidth() * scale;
            matrix.setScale(scale, scale);
            matrix.postTranslate(difference / 2 + containerRect.width() - badgeRect.width(), mBorderWidth + containerRect.height() - badgeRect.height());
        }
        return matrix;
    }
}
