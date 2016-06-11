package com.flipkart.circularImageView;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.widget.ImageView;

/**
 * Class that holds all the information to draw a bitmap.
 * <p/>
 * Created by vivek.soneja on 18/02/15.
 */
public class BitmapDrawer {
    public Bitmap bitmap;
    public Shader bitmapShader;
    public ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;
}
