
package com.example.sdpc.myapplication.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

/**
 *
 */
public class AlphaGradientTextView extends ColorPointTextView {

    public static final int LEFT2RIGHT = 0;
    public static final int RIGHT2LEFT = 1;
    public static final int TOP2BOTTOM = 2;
    public static final int BOTTOM2TOP = 3;

    private int mGradientDirection = -1;

    public AlphaGradientTextView(Context context) {
        this(context, null);
    }

    public AlphaGradientTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphaGradientTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0 && mGradientDirection != -1) {
            if (mGradientDirection == LEFT2RIGHT) {
                getPaint().setShader(new LinearGradient(getPaddingLeft(), 0, getWidth() - getPaddingRight() - 100, 0, getCurrentTextColor(), 0x00ffffff, TileMode.CLAMP));
            } else if (mGradientDirection == RIGHT2LEFT) {
                getPaint().setShader(new LinearGradient(getPaddingLeft() + 100, 0, getWidth() - getPaddingRight(), 0, 0x00ffffff, getCurrentTextColor(), TileMode.CLAMP));
            }
        }
    }

    public void setGraditentDirection(int direction) {
        if (direction < LEFT2RIGHT || direction > BOTTOM2TOP) {
            throw new IllegalArgumentException("direction must in a certain range.");
        }
        mGradientDirection = direction;
    }

    public void showGradient() {
        int currentColor = getCurrentTextColor();
        int currentTransparentColor = currentColor & 0x00ffffff;
        if (mGradientDirection == LEFT2RIGHT) { // 右隐
            getPaint().setShader(new LinearGradient(0, 0,getWidth()  , 0, currentColor,  currentTransparentColor, TileMode.CLAMP));
        } else if (mGradientDirection == RIGHT2LEFT) {// 左隐
            getPaint().setShader(new LinearGradient(0, 0, getWidth() , 0, currentTransparentColor, currentColor, TileMode.CLAMP));
        }
        invalidate();
    }

    public void showGradient(int gradientLength,int start){
        int currentColor = getCurrentTextColor();
        int currentTransparentColor = currentColor & 0x00ffffff;
        TextPaint tp = new TextPaint(getPaint());
        tp.linkColor = currentColor;
        if (mGradientDirection == LEFT2RIGHT) { // 右隐
            tp.setShader(new LinearGradient(start-gradientLength, 0, start , 0, currentColor,  currentTransparentColor, TileMode.CLAMP));
        } else if (mGradientDirection == RIGHT2LEFT) {// 左隐
            tp.setShader(new LinearGradient(start, 0, start+gradientLength , 0, currentTransparentColor, currentColor, TileMode.CLAMP));
        }
        getPaint().set(tp);
        invalidate();
    }

    public void hideGradient() {
        getPaint().setShader(null);
        invalidate();
    }
}
