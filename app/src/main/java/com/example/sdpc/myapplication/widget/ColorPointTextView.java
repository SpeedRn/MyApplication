
package com.example.sdpc.myapplication.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class ColorPointTextView extends TextView {

    private class Particle {
        float x;
        float y;
        int color;
        float alpha;
        float radius;

        public void advance(float factor, Random random, float canvasW, float canvasH) {
            x = x + factor * random.nextInt((int) canvasW) * (random.nextFloat() - 0.5f);
            y = y + factor * random.nextInt((int) canvasH / 2);
            radius = radius - factor * random.nextInt(2);
            alpha = (1f - factor) * (1 + random.nextFloat());
        }
    }

    abstract class ParticleAnimator extends ValueAnimator {
        abstract void draw(View target, Canvas canvas);
    }

    class CrushAnimator extends ParticleAnimator {

        Particle[][] mParticles;
        Paint mPaint;
        float targetX;
        float targetY;
        float targetW;
        float targetH;

        public CrushAnimator(float targetX, float targetY, float targetW, float targetH) {
            setFloatValues(0.0f, 1.0f);
            setDuration(1500);
            mPaint = new Paint();

            this.targetX = targetX;
            this.targetY = targetY;
            this.targetW = targetW;
            this.targetH = targetH;

            generateParticle();
        }

        void generateParticle() {
            int colorPointHeigthParts = (int) (colorPointRadius / particleRadius);
            int colorPointWidthParts = (int) (colorPointRadius / particleRadius);
            mParticles = new Particle[colorPointHeigthParts][colorPointWidthParts];
            for (int i = 0; i < colorPointHeigthParts; i++) {
                for (int j = 0; j < colorPointWidthParts; j++) {
                    Particle particle = new Particle();
                    particle.radius = particleRadius;
                    particle.color = pointColor;
                    particle.alpha = 1.0f;
                    particle.x = j * particleRadius + particleRadius + colorPointX;
                    particle.y = i * particleRadius + particleRadius + colorPointY;
                    mParticles[i][j] = particle;
                }
            }
        }

        @Override
        void draw(View target, Canvas canvas) {
            if (!isStarted()) {
                return;
            }
            for (Particle[] heigthParts : mParticles) {
                for (Particle particle : heigthParts) {
                    particle.advance((Float) getAnimatedValue(), random, targetW, targetH);
                    mPaint.setColor(particle.color);
                    mPaint.setAlpha((int) (Color.alpha(particle.color) * particle.alpha));
                    canvas.drawCircle(particle.x, particle.y, particle.radius, mPaint);
                }
            }
            target.invalidate();
        }
    }

    private int pointColor;
    private float colorPointX;
    private float colorPointY;
    private float colorPointRadius;
    private float particleRadius;
    private boolean showPoint;
    private ParticleAnimator particleAnimator;
    private Random random;
    private Paint colorPointPaint;

    public ColorPointTextView(Context context) {
        this(context, null);
    }

    public ColorPointTextView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ColorPointTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        particleRadius = 1;
        pointColor = Color.RED;
        colorPointRadius = 5;
        colorPointX = colorPointRadius;
        colorPointY = colorPointRadius;
        random = new Random();
    }

    protected void setColorPointLocation(float x, float y) {
        this.colorPointX = x;
        this.colorPointY = y;
    }

    protected void setColorPointRadius(float radius) {
        this.colorPointRadius = radius;
    }

    protected void setColorPointColor(int color) {
        this.pointColor = color;
    }

    protected void showColorPoint() {
        showPoint = true;
        if (particleAnimator != null) {
            particleAnimator.cancel();
        }
        invalidate();
    }

    protected void hideColorPoint() {
        showPoint = false;
        invalidate();
    }

    protected void crushColorPoint() {
        if (showPoint) {
            showPoint = false;
            if (particleAnimator == null) {
                particleAnimator = new CrushAnimator(getX(), getY(), getWidth(), getHeight());
            }
            particleAnimator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    particleAnimator = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    particleAnimator = null;
                }
            });
            invalidate();
            particleAnimator.start();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (showPoint) {
            if (colorPointPaint == null) {
                colorPointPaint = new Paint();
                colorPointPaint.setColor(pointColor);
                colorPointPaint.setAntiAlias(true);
            }
            canvas.drawCircle(colorPointX, colorPointY, colorPointRadius, colorPointPaint);
        }
        if (particleAnimator != null) {
            particleAnimator.draw(this, canvas);
        }
    }
}
