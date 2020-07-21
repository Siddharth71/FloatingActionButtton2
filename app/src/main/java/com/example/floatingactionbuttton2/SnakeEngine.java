package com.example.floatingactionbuttton2;

import android.view.SurfaceView;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.Random;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class SnakeEngine  extends SurfaceView implements Runnable {
    int screenX,screenY;
    public SnakeEngine(Context context,Point Size) {
        super(context);
        Math.abs(NUM_BLOCKS_WIDE);
        Math.abs(numBlocksHigh);

        // Work out how many pixels each block is

        // How many blocks of the same size will fit into the height

        // Set the sound up
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try {

            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;
            Math.abs(NUM_BLOCKS_WIDE);
            Math.abs(numBlocksHigh);

            descriptor = assetManager.openFd("get_mouse_sound.ogg");
            eat_bob = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("death_sound.ogg");
            snake_crash = soundPool.load(descriptor, 0);
            blockSize = screenX / NUM_BLOCKS_WIDE;

            numBlocksHigh = screenY / blockSize;




        } catch (IOException e) {


        }


        // Initialize the drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();

        // If you score 200 you are rewarded with a crash achievement!
        snakeXs = new int[200];
        snakeYs = new int[200];
        newGame();
           }





    public void newGame()
    {
        snakeLength = 1;
        Math.abs(NUM_BLOCKS_WIDE);
       Math.abs(numBlocksHigh);

        snakeXs[0] = NUM_BLOCKS_WIDE / 2;
        snakeYs[0] = numBlocksHigh / 2;


        score = 0;}

        public void spawnBob() {
        Random random = new Random();
            Math.abs(NUM_BLOCKS_WIDE);
            Math.abs(numBlocksHigh);

        bobX = random.nextInt(NUM_BLOCKS_WIDE - 1) + 1;
        bobY = random.nextInt(numBlocksHigh - 1) +1 ;

        nextFrameTime = System.currentTimeMillis();
    }

























    @Override
    public void run() {

        while (isPlaying) {

            // Update 10 times a second
            if (updateRequired()) {
                update();
                draw();
            }

        }
    }

    public void pause() {
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }


    private Thread thread = null;
    private Context context;
    private SoundPool soundPool;
    private int eat_bob = -1;
    private int snake_crash = -1;


    public enum Heading {UP, RIGHT, DOWN, LEFT}

    private Heading heading = Heading.RIGHT;


    private int ScreenX;
    private int ScreenY;

    private int snakeLength;

    private int bobX;
    private int bobY;


    private int blockSize;


    private final int NUM_BLOCKS_WIDE = 40;
    private int numBlocksHigh;


    private long nextFrameTime;

    private final long FPS = 10;

    private final long MILLIS_PER_SECOND = 1000;

    private int score;


    private int[] snakeXs;
    private int[] snakeYs;


    private volatile boolean isPlaying;


    private Canvas canvas;


    private SurfaceHolder surfaceHolder;


    private Paint paint;




    private void eatBob() {

        snakeLength++;

        spawnBob();
        //add to the score
        score = score + 1;
        soundPool.play(eat_bob, 1, 1, 0, 0, 1);
    }
    private void moveSnake(){
        // Move the body
        for (int i = snakeLength; i > 1; i--) {
            // Start at the back and move it
            // to the position of the segment in front of it
            snakeXs[i] = snakeXs[i - 1];
            snakeYs[i] = snakeYs[i - 1];

            // Exclude the head because
            // the head has nothing in front of it
        }

        // Move the head in the appropriate heading
        switch (heading) {
            case UP:
                snakeYs[0]--;
                break;

            case RIGHT:
                snakeXs[0]++;
                break;

            case DOWN:
                snakeYs[0]++;
                break;

            case LEFT:
                snakeXs[0]--;
                break;
        }
    }

    private boolean detectDeath() {
        // Has the snake died?
        boolean dead = false;

        // Hit the screen edge
        if (snakeXs[0] == -1) dead = true;
        if (snakeXs[0] >= NUM_BLOCKS_WIDE) dead = true;
        if (snakeYs[0] == -1) dead = true;
        if (snakeYs[0] == numBlocksHigh) dead = true;

        // Eaten itself?
        for (int i = snakeLength - 1; i > 0; i--) {
            if ((i > 4) && (snakeXs[0] == snakeXs[i]) && (snakeYs[0] == snakeYs[i])) {
                dead = true;
            }
        }

        return dead;
    }


        public void update() {
            // Did the head of the snake eat Bob?
            if (snakeXs[0] == bobX && snakeYs[0] == bobY) {
                eatBob();
            }

            moveSnake();

            if (detectDeath()) {
                //start again
                soundPool.play(snake_crash, 1, 1, 0, 0, 1);


            }
        }

    public void draw() {
        // Get a lock on the canvas
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            // Fill the screen with Game Code School blue
            canvas.drawColor(Color.argb(255, 26, 128, 182));

            // Set the color of the paint to draw the snake white
            paint.setColor(Color.argb(255, 255, 255, 255));

            // Scale the HUD text
            paint.setTextSize(90);
            canvas.drawText("Score:" + score, 10, 70, paint);

            // Draw the snake one block at a time
            for (int i = 0; i < snakeLength; i++) {
                canvas.drawRect(snakeXs[i] * blockSize,
                        (snakeYs[i] * blockSize),
                        (snakeXs[i] * blockSize) + blockSize,
                        (snakeYs[i] * blockSize) + blockSize,
                        paint);
            }

            // Set the color of the paint to draw Bob red
            paint.setColor(Color.argb(255, 255, 0, 0));

            // Draw Bob
            canvas.drawRect(bobX * blockSize,
                    (bobY * blockSize),
                    (bobX * blockSize) + blockSize,
                    (bobY * blockSize) + blockSize,
                    paint);

            // Unlock the canvas and reveal the graphics for this frame
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }
        public boolean updateRequired() {

            // Are we due to update the frame
            if (nextFrameTime <= System.currentTimeMillis()) {
                // Tenth of a second has passed

                // Setup when the next update will be triggered
                nextFrameTime = System.currentTimeMillis() + MILLIS_PER_SECOND / FPS;

                // Return true so that the update and draw
                // functions are executed
                return true;
            }

            return false;


            }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (motionEvent.getX() >=  screenX / 2) {
                    switch(heading){
                        case UP:
                            heading = Heading.RIGHT;
                            break;
                        case RIGHT:
                            heading = Heading.DOWN;
                            break;
                        case DOWN:
                            heading = Heading.LEFT;
                            break;
                        case LEFT:
                            heading = Heading.UP;
                            break;
                    }
                } else {
                    switch(heading){
                        case UP:
                            heading = Heading.LEFT;
                            break;
                        case LEFT:
                            heading = Heading.DOWN;
                            break;
                        case DOWN:
                            heading = Heading.RIGHT;
                            break;
                        case RIGHT:
                            heading = Heading.UP;
                            break;
                    }
                }
        }
        return true;
    }}






































































































































