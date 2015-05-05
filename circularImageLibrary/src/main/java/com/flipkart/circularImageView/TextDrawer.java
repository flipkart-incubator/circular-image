package com.flipkart.circularImageView;

import android.graphics.Color;

/**
 * Holds the information to draw a text.
 * <p/>
 * Created by vivek.soneja on 22/02/15.
 */
public class TextDrawer {
    private String text;
    private int backgroundColor;
    private int textColor;

    public TextDrawer() {
        backgroundColor = Color.BLUE;
        textColor = Color.WHITE;
    }

    //Setters
    public TextDrawer setText(String text) {
        this.text = text;
        return this;
    }

    public TextDrawer setBackgroundColor(int color) {
        this.backgroundColor = color;
        return this;
    }

    public TextDrawer setTextColor(int color) {
        this.textColor = color;
        return this;
    }

    //Getters
    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public String getText() {
        return text;
    }
}
