package com.garethevans.church.opensongtablet.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.Choreographer;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * CryoFlowView: An animated background view for Dyslexia accessibility.
 * Renders soft, moving patterns to reduce visual stress.
 */
public class CryoFlowView extends View {

    private boolean isAnimating = false;
    private long startTime;
    private int patternMode = 0; // 0-9
    private float intensity = 1.0f;
    private float speedMultiplier = 1.0f;
    private int primaryColor = Color.BLUE;
    private int secondaryColor = Color.CYAN;
    private int backgroundColor = Color.WHITE;

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random random = new Random();
    private List<Blob> blobs = new ArrayList<>();

    private final Choreographer.FrameCallback frameCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {
            if (isAnimating) {
                invalidate();
                Choreographer.getInstance().postFrameCallback(this);
            }
        }
    };

    public CryoFlowView(Context context) {
        super(context);
        init();
    }

    public CryoFlowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        startTime = System.currentTimeMillis();
    }

    public void start() {
        if (!isAnimating) {
            isAnimating = true;
            Choreographer.getInstance().postFrameCallback(frameCallback);
        }
    }

    public void stop() {
        isAnimating = false;
    }

    public void setPatternMode(int mode) {
        this.patternMode = mode;
        resetPattern();
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public void setSpeed(float speed) {
        this.speedMultiplier = speed;
    }

    public void setColors(int primary, int secondary, int background) {
        this.primaryColor = primary;
        this.secondaryColor = secondary;
        this.backgroundColor = background;
    }

    private void resetPattern() {
        blobs.clear();
        if (patternMode == 1) { // Lava lamp blobs
            for (int i = 0; i < 5; i++) {
                blobs.add(new Blob(random.nextFloat(), random.nextFloat(), 0.15f + random.nextFloat() * 0.2f));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float elapsed = (System.currentTimeMillis() - startTime) / 1000f;
        float timeScale = elapsed * intensity * speedMultiplier;

        canvas.drawColor(backgroundColor);

        switch (patternMode) {
            case 0: drawTide(canvas, timeScale); break;
            case 1: drawLava(canvas, timeScale); break;
            case 2: drawNebula(canvas, timeScale); break;
            case 3: drawAurora(canvas, timeScale); break;
            case 4: drawClouds(canvas, timeScale); break;
            case 5: drawBreeze(canvas, timeScale); break;
            case 6: drawRipple(canvas, timeScale); break;
            case 7: drawDrift(canvas, timeScale); break;
            case 8: drawPulse(canvas, timeScale); break;
            case 9: drawOrbit(canvas, timeScale); break;
            default: drawTide(canvas, timeScale); break;
        }
    }

    private void drawTide(Canvas canvas, float elapsed) {
        float xShift = (float) Math.sin(elapsed * 0.4f) * 0.15f;
        Shader gradient = new android.graphics.LinearGradient(
                -getWidth() * 0.1f + xShift * getWidth(), 0,
                getWidth() * 1.1f + xShift * getWidth(), 0,
                new int[]{backgroundColor, primaryColor, secondaryColor, backgroundColor},
                new float[]{0f, 0.4f, 0.6f, 1f},
                Shader.TileMode.CLAMP
        );
        paint.setShader(gradient);
        paint.setAlpha(140);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        paint.setShader(null);
    }

    private void drawLava(Canvas canvas, float elapsed) {
        for (Blob blob : blobs) {
            float x = (blob.initialX + (float) Math.sin(elapsed * 0.25f + blob.seed) * 0.2f) * getWidth();
            float y = (blob.initialY + (float) Math.cos(elapsed * 0.15f + blob.seed) * 0.35f) * getHeight();
            float radius = blob.radius * getWidth();

            RadialGradient gradient = new RadialGradient(x, y, radius, 
                new int[]{primaryColor, Color.TRANSPARENT}, 
                new float[]{0f, 0.8f}, 
                Shader.TileMode.CLAMP);
            
            paint.setShader(gradient);
            paint.setAlpha((int) (110 * intensity));
            canvas.drawCircle(x, y, radius, paint);
        }
        paint.setShader(null);
    }

    private void drawNebula(Canvas canvas, float elapsed) {
        for (int i = 0; i < 3; i++) {
            float x = (0.5f + (float) Math.sin(elapsed * 0.1f + i) * 0.3f) * getWidth();
            float y = (0.5f + (float) Math.cos(elapsed * 0.08f + i) * 0.3f) * getHeight();
            float radius = (0.4f + (float) Math.sin(elapsed * 0.05f + i) * 0.1f) * getWidth();
            
            RadialGradient gradient = new RadialGradient(x, y, radius,
                new int[]{(i % 2 == 0) ? primaryColor : secondaryColor, Color.TRANSPARENT},
                new float[]{0f, 1f}, Shader.TileMode.CLAMP);
            
            paint.setShader(gradient);
            paint.setAlpha((int) (80 * intensity));
            canvas.drawCircle(x, y, radius, paint);
        }
        paint.setShader(null);
    }

    private void drawAurora(Canvas canvas, float elapsed) {
        for (int i = 0; i < 4; i++) {
            float x = (0.2f + i * 0.2f + (float) Math.sin(elapsed * 0.15f + i) * 0.05f) * getWidth();
            float h = (0.6f + (float) Math.cos(elapsed * 0.2f + i) * 0.2f) * getHeight();
            
            Shader gradient = new android.graphics.LinearGradient(
                    x, 0, x + getWidth() * 0.15f, h,
                    new int[]{primaryColor, Color.TRANSPARENT},
                    new float[]{0f, 1f}, Shader.TileMode.CLAMP
            );
            paint.setShader(gradient);
            paint.setAlpha((int) (90 * intensity));
            canvas.drawRect(x, 0, x + getWidth() * 0.15f, h, paint);
        }
        paint.setShader(null);
    }

    private void drawClouds(Canvas canvas, float elapsed) {
        for (int i = 0; i < 6; i++) {
            float x = ((elapsed * 0.05f + i * 0.2f) % 1.2f - 0.1f) * getWidth();
            float y = (0.3f + (float) Math.sin(elapsed * 0.1f + i) * 0.4f) * getHeight();
            float radius = (0.2f + (float) Math.cos(elapsed * 0.05f + i) * 0.1f) * getWidth();
            
            RadialGradient gradient = new RadialGradient(x, y, radius,
                new int[]{Color.argb(100, 255, 255, 255), Color.TRANSPARENT},
                new float[]{0f, 0.9f}, Shader.TileMode.CLAMP);
            
            paint.setShader(gradient);
            paint.setAlpha((int) (70 * intensity));
            canvas.drawCircle(x, y, radius, paint);
        }
        paint.setShader(null);
    }

    private void drawBreeze(Canvas canvas, float elapsed) {
        float angle = (float) Math.sin(elapsed * 0.1f) * 15f;
        canvas.save();
        canvas.rotate(angle, getWidth() / 2f, getHeight() / 2f);
        drawTide(canvas, elapsed * 1.2f);
        canvas.restore();
    }

    private void drawRipple(Canvas canvas, float elapsed) {
        float life = (elapsed % 4.0f) / 4.0f;
        float radius = life * getWidth() * 1.5f;
        int alpha = (int) ((1f - life) * 100 * intensity);
        
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(50f);
        paint.setColor(primaryColor);
        paint.setAlpha(alpha);
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, paint);
        paint.setStyle(Paint.Style.FILL);
    }

    private void drawDrift(Canvas canvas, float elapsed) {
        for (int i = 0; i < 8; i++) {
            float x = (float) (Math.sin(elapsed * 0.2f + i) * 0.4f + 0.5f) * getWidth();
            float y = (float) (Math.cos(elapsed * 0.3f + i * 0.5f) * 0.4f + 0.5f) * getHeight();
            paint.setColor(i % 2 == 0 ? primaryColor : secondaryColor);
            paint.setAlpha((int) (60 * intensity));
            canvas.drawCircle(x, y, 100f * intensity, paint);
        }
    }

    private void drawPulse(Canvas canvas, float elapsed) {
        int alpha = (int) ((Math.sin(elapsed * 0.5f) * 0.5f + 0.5f) * 60 * intensity);
        paint.setColor(primaryColor);
        paint.setAlpha(alpha);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    private void drawOrbit(Canvas canvas, float elapsed) {
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        for (int i = 0; i < 3; i++) {
            float dist = (0.2f + i * 0.1f) * getWidth();
            float x = cx + (float) Math.sin(elapsed * 0.2f + i) * dist;
            float y = cy + (float) Math.cos(elapsed * 0.2f + i) * dist;
            
            RadialGradient gradient = new RadialGradient(x, y, 200f * intensity,
                new int[]{secondaryColor, Color.TRANSPARENT},
                new float[]{0f, 1f}, Shader.TileMode.CLAMP);
            
            paint.setShader(gradient);
            paint.setAlpha((int) (120 * intensity));
            canvas.drawCircle(x, y, 200f * intensity, paint);
        }
        paint.setShader(null);
    }

    private static class Blob {
        float initialX, initialY, radius, seed;

        Blob(float x, float y, float r) {
            initialX = x;
            initialY = y;
            radius = r;
            seed = new Random().nextFloat() * 10f;
        }
    }
}
