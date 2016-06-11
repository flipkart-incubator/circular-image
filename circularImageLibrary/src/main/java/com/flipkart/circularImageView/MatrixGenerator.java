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
                        setMatrixForFullCircle(scaleType, matrix, bitmap);
                        break;
                }
                break;
            case FIT_CENTER:
                setMatrixForFullCircle(scaleType, matrix, bitmap);
                break;
        }
        return matrix;
    }

    //noinspection SuspiciousNameCombination
    private void setMatrixForHalfCircle(Matrix matrix, Bitmap bitmap, DrawerHelper.DrawingType drawingType) {
        float scale;
        if (bitmap.getWidth() < bitmap.getHeight()) {
            //Portrait
            scale = mRect.width() / bitmap.getWidth();
            matrix.setScale(scale, scale);

            float shift;
            if (drawingType.getPosition() == 1)
                shift = (bitmap.getWidth() * scale - mRect.width() / 2) * -1 / 2;
            else shift = (bitmap.getWidth() * scale - mRect.width() / 2) / 2;

            //Portrait we don't want to translate to middle, since most of the faces are in top area, not in center
            matrix.postTranslate(mRect.left + shift + mBorderWidth, mRect.top + mBorderWidth);
        } else {
            //Landscape
            scale = mRect.height() / bitmap.getHeight();
            float difference;
            if (drawingType.getPosition() == 1)
                difference = (bitmap.getWidth() * scale - mRect.width() / 2) * -1 / 2 + mBorderWidth;
            else
                difference = (bitmap.getWidth() * scale - 3 * mRect.width() / 2) * -1 / 2 + mBorderWidth;
            matrix.setScale(scale, scale);
            matrix.postTranslate(mRect.left + difference, mRect.top + mBorderWidth);
        }
    }

    private void setMatrixForFullCircle(ImageView.ScaleType scaleType, Matrix matrix, Bitmap bitmap) {
        float scale;
        if (scaleType == ImageView.ScaleType.CENTER_CROP) {
            if (bitmap.getHeight() > bitmap.getWidth()) {
                //Portrait
                scale = (mRect.width()) / bitmap.getWidth();
                matrix.setScale(scale, scale);
                //Portrait we don't want to translate to middle, since most of the faces are in top area, not in center
                matrix.setScale(scale, scale);
                matrix.postTranslate(mRect.left, mRect.top);
            } else {
                //Landscape
                scale = (mRect.height()) / bitmap.getHeight();
                float difference = mRect.width() - bitmap.getWidth() * scale;
                matrix.setScale(scale, scale);
                matrix.postTranslate(mRect.left + difference / 2, mRect.top);
            }
        } else if (scaleType == ImageView.ScaleType.FIT_CENTER) {
            RectF bitmapRect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
            float divisor = 1.414f; //sqrt of 2 because inner square fitting inside circle has a side of circle's diameter divided by sqrt(2)
            float sideDimension =  (Math.max(mRect.width(), mRect.height()) / divisor); // side of the inner square
            float remainingSpace = (Math.max(mRect.width(), mRect.height()) - sideDimension); // empty space used for center aligning
            RectF newRect = new RectF(mRect.left + (remainingSpace / 2f), mRect.top + (remainingSpace / 2f), mRect.left + (remainingSpace / 2f) + sideDimension, mRect.top + (remainingSpace / 2f) + sideDimension);
            matrix.setRectToRect(bitmapRect, newRect, Matrix.ScaleToFit.CENTER);
        }
    }


    @SuppressWarnings("SuspiciousNameCombination")
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
            matrix.postTranslate(mRect.left + translateX, mRect.top + translateY);
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
            matrix.postTranslate(containerRect.left + mBorderWidth + containerRect.width() - badgeRect.width(), containerRect.top + mBorderWidth + containerRect.height() - badgeRect.height());
        } else {
            //Landscape
            scale = badgeRect.height() / bitmap.getHeight();
            float difference = badgeRect.width() + 2 * mBorderWidth - bitmap.getWidth() * scale;
            matrix.setScale(scale, scale);
            matrix.postTranslate(containerRect.left + difference / 2 + containerRect.width() - badgeRect.width(), containerRect.top + mBorderWidth + containerRect.height() - badgeRect.height());
        }
        return matrix;
    }
}
