package com.flipkart.circularImageView;

import android.graphics.Bitmap;

/**
 * Holds the information to draw a text.
 * <p/>
 * Created by vivek.soneja on 22/02/15.
 */
public class IconDrawer {
    private Bitmap icon;
    private float margin;
    private int backgroundColor;

    //Setters
    public IconDrawer (Bitmap icon, int backgroundColor) {
        this.icon = icon;
        this.backgroundColor = backgroundColor;
    }

    //Getters
    public int getBackgroundColor() {
        return backgroundColor;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public float getMargin() {
        return margin;
    }

    /**
     * @param margin Margin to be left outside the icon
     * @return IconDrawer for chaining
     */
    public IconDrawer setMargin(float margin) {
        this.margin = margin;
        return this;
    }
}
