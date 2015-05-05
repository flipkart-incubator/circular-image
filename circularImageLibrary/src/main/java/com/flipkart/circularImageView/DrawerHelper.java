package com.flipkart.circularImageView;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.List;

/**
 * Draws images in multiple formats (shapes)
 * <p/>
 * Created by vivek.soneja on 18/02/15.
 */
public class DrawerHelper {
    private RectF mRect;
    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mBackgroundPaint;
    private List<Object> sourceObjects;

    public enum DrawingType {
        QUARTER_CIRCLE, HALF_CIRCLE, FULL_CIRCLE;

        int position;

        public void setPosition(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }

    public DrawerHelper(RectF mRect, Paint mPaint, Paint mTextPaint, Paint mBackgroundPaint, List<Object> sourceObjects) {
        this.mRect = mRect;
        this.mPaint = mPaint;
        this.mTextPaint = mTextPaint;
        this.mBackgroundPaint = mBackgroundPaint;
        this.sourceObjects = sourceObjects;
    }

    public DrawingType getDrawingType(int position, int totalItems) {
        switch (totalItems) {
            case 4:
                DrawingType quarterCircle = DrawingType.QUARTER_CIRCLE;
                quarterCircle.setPosition(position + 1);
                return quarterCircle;

            case 3:
                if (position == 0) {
                    DrawingType halfCircle = DrawingType.HALF_CIRCLE;
                    halfCircle.setPosition(position + 1);
                    return halfCircle;
                } else {
                    DrawingType quarterCircle3 = DrawingType.QUARTER_CIRCLE;
                    quarterCircle3.setPosition(position + 2);
                    return quarterCircle3;
                }

            case 2:
                DrawingType halfCircle = DrawingType.HALF_CIRCLE;
                halfCircle.setPosition(position + 1);
                return halfCircle;

            default:
            case 1:
                DrawingType fullCircle = DrawingType.FULL_CIRCLE;
                fullCircle.setPosition(position + 1);
                return fullCircle;
        }
    }

    public void drawComplexCircle(Canvas canvas) {
        int size = sourceObjects.size();
        for (int i = 0; i < size; i++) {
            Object sourceObject = sourceObjects.get(i);
            DrawingType drawingType = getDrawingType(i, size);
            switch (drawingType) {
                case QUARTER_CIRCLE:
                    drawQuarter(canvas, drawingType.getPosition(), sourceObject);
                    break;

                case HALF_CIRCLE:
                    drawHalfCircle(canvas, drawingType.getPosition(), sourceObject);
                    break;

                case FULL_CIRCLE:
                    drawFullCircleImage(canvas, sourceObject);
                    break;
            }
        }
    }

    public void drawFullCircleImage(Canvas canvas, Object sourceObject) {
        //Single Object, Draw one big circle of either text or image
        if (sourceObject instanceof BitmapDrawer) {
            //Its a image
            BitmapDrawer drawer = (BitmapDrawer) sourceObject;
            mPaint.setShader(drawer.bitmapShader);
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), mRect.width() / 2, mPaint);
        } else if (sourceObject instanceof TextDrawer) {
            //Its a text, write text
            TextDrawer textDrawer = (TextDrawer) sourceObject;
            String message = textDrawer.getText();
            mBackgroundPaint.setColor(textDrawer.getBackgroundColor());
            mTextPaint.setColor(textDrawer.getTextColor());
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), mRect.width() / 2, mBackgroundPaint);
            canvas.drawText(message, 0, message.length() > 2 ? 2 : message.length(), mRect.centerX(), mRect.centerY() - ((mTextPaint.descent() + mTextPaint
                    .ascent()) / 2), mTextPaint);
        }
    }

    /**
     * Draw a half circle.
     *
     * @param canvas     Canvas on which half circle needs to be drawn.
     * @param halfNumber Which half (1 or 2) needs to be drawn
     */
    public void drawHalfCircle(Canvas canvas, int halfNumber, Object sourceObject) {
        if (sourceObject instanceof BitmapDrawer) {
            //Its a bitmap
            BitmapDrawer drawer = (BitmapDrawer) sourceObject;
            mPaint.setShader(drawer.bitmapShader);
            if (halfNumber == 1) {
                canvas.drawArc(mRect, 90, 180, false, mPaint);
            } else {
                canvas.drawArc(mRect, 270, 180, false, mPaint);
            }
        } else if (sourceObject instanceof TextDrawer) {
            //Its a text
            TextDrawer textDrawer = (TextDrawer) sourceObject;
            String message = textDrawer.getText();
            mBackgroundPaint.setColor(textDrawer.getBackgroundColor());
            mTextPaint.setColor(textDrawer.getTextColor());
            float previousTextSize = mTextPaint.getTextSize();
            mTextPaint.setTextSize(previousTextSize * 0.7f);
            if (halfNumber == 1) {
                canvas.drawArc(mRect, 90, 180, false, mBackgroundPaint);
                canvas.drawText(message, 0, message.length() > 2 ? 2 : message.length(), mRect.centerX() / 2, mRect.centerY() - ((mTextPaint.descent() + mTextPaint.ascent()) / 2), mTextPaint);
            } else {
                canvas.drawArc(mRect, 270, 180, false, mBackgroundPaint);
                canvas.drawText(message, 0, message.length() > 2 ? 2 : message.length(), 3 * mRect.centerX() / 2, mRect.centerY() - ((mTextPaint.descent() + mTextPaint.ascent()) / 2), mTextPaint);
            }

            //Restore the textSize, once the drawing is done, so that new drawing can scale appropriately
            mTextPaint.setTextSize(previousTextSize);
        }
    }

    /**
     * Draws a quarter on Canvas.
     *
     * @param canvas        Canvas on which quarter needs to be drawn.
     * @param quarterNumber Quarter number that needs to be drawn (1, 2, 3 or 4)
     */
    private void drawQuarter(Canvas canvas, int quarterNumber, Object sourceObject) {
        if (sourceObject instanceof BitmapDrawer) {
            //Its a bitmap
            BitmapDrawer drawer = (BitmapDrawer) sourceObject;
            mPaint.setShader(drawer.bitmapShader);
            switch (quarterNumber) {
                case 1:
                    canvas.drawArc(mRect, 180, 90, true, mPaint);
                    break;
                case 2:
                    canvas.drawArc(mRect, 90, 90, true, mPaint);
                    break;
                case 3:
                    canvas.drawArc(mRect, 270, 90, true, mPaint);
                    break;
                case 4:
                    canvas.drawArc(mRect, 0, 90, true, mPaint);
                    break;
            }
        } else if (sourceObject instanceof TextDrawer) {
            //Its a text
            TextDrawer textDrawer = (TextDrawer) sourceObject;
            String message = textDrawer.getText();
            mBackgroundPaint.setColor(textDrawer.getBackgroundColor());
            mTextPaint.setColor(textDrawer.getTextColor());
            float previousTextSize = mTextPaint.getTextSize();
            mTextPaint.setTextSize(previousTextSize * 0.45f);

            switch (quarterNumber) {
                case 1:
                    canvas.drawArc(mRect, 180, 90, true, mBackgroundPaint);
                    canvas.drawText(message, 0, message.length() > 2 ? 2 : message.length(), mRect.centerX() * 0.6f, mRect.centerY() * 0.64f - ((mTextPaint.descent() + mTextPaint.ascent()) / 2),
                            mTextPaint);
                    break;
                case 2:
                    canvas.drawArc(mRect, 90, 90, true, mBackgroundPaint);
                    canvas.drawText(message, 0, message.length() > 2 ? 2 : message.length(), mRect.centerX() * 0.6f, mRect.centerY() * 1.35f - ((mTextPaint.descent() + mTextPaint.ascent()) / 2),
                            mTextPaint);
                    break;
                case 3:
                    canvas.drawArc(mRect, 270, 90, true, mBackgroundPaint);
                    canvas.drawText(message, 0, message.length() > 2 ? 2 : message.length(), mRect.centerX() * 1.4f, mRect.centerY() * 0.64f - ((mTextPaint.descent() + mTextPaint.ascent()) / 2),
                            mTextPaint);
                    break;
                case 4:
                    canvas.drawArc(mRect, 0, 90, true, mBackgroundPaint);
                    canvas.drawText(message, 0, message.length() > 2 ? 2 : message.length(), mRect.centerX() * 1.4f, mRect.centerY() * 1.35f - ((mTextPaint.descent() + mTextPaint.ascent()) / 2),
                            mTextPaint);
                    break;
            }

            //Restore the textSize, once the drawing is done, so that new drawing can scale appropriately
            mTextPaint.setTextSize(previousTextSize);
        }
    }
}
