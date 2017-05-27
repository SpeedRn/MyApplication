package com.example.sdpc.myapplication.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sdpc on 17-5-23.
 */

public class GuideView extends View {
    private Paint paint = new Paint();
    private RectF clipRect;
    private static final String TAG = "GuideView";
    private Bitmap guideBitmap;
    private Bitmap tobeDrawBitmap;

    public GuideView(Context context) {
        this(context, null);
    }

    public GuideView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        creatBitmap();
        canvas.drawBitmap(tobeDrawBitmap, 0, 0, null);

    }

    private void creatBitmap() {
        if (tobeDrawBitmap != null) {
            tobeDrawBitmap.recycle();
        }
        tobeDrawBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tobeDrawBitmap);

        if (null == clipRect || null == guideBitmap) {
            return;
        }

        paint.setColor(Color.parseColor("#7F00DD00"));
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setColor(Color.TRANSPARENT);
        canvas.drawRoundRect(clipRect,6,6,paint);

        paint.setXfermode(null);
        canvas.drawBitmap(guideBitmap, clipRect.left, clipRect.top, null);

        paint.setColor(Color.CYAN);
        paint.setTextSize(50);
        canvas.drawText("Test on Draw", 100, 100, paint);
        Log.d(TAG, "onDraw" + clipRect);

    }

    public void setClipRect(RectF clipRect) {
        this.clipRect = clipRect;
    }

    public void setGuideBitmap(Bitmap guideBitmap) {
        this.guideBitmap = guideBitmap;
    }
}
