package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.myapplication.Geometry.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLevelThree extends View {
    Context context;
    float ballX, ballY, paddleX, paddleY, oldPaddleX, oldX;
    Velocity ballVelocity = new Velocity(25, 25);
    Handler handler;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint blockPaint = new Paint();
    Paint statsPaint = new Paint();
    int score = 0, width, height, ballWidth, ballHeight, numBlocks = 0, brokenBlocks = 0;
    Bitmap ball, paddle;
    boolean gameOver = false, showAgain = true;;
    Random random;
    List<Block> levelBlocks = new ArrayList<Block>();
    static final int STATS_HEIGHT = 60;
    public GameLevelThree(Context context) {
        super(context);
        this.context = context;
        this.handler = new Handler();
        this.statsPaint.setColor(Color.argb(255, 176, 216, 230));
        this.ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        this.paddle = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);
        this.runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        this.textPaint.setColor(Color.GREEN);
        this.textPaint.setTextSize(50);
        this.textPaint.setTextAlign(Paint.Align.LEFT);
        this.blockPaint.setColor(Color.argb(255, 249, 129, 0));
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.width = size.x;
        this.height = size.y;
        this.random = new Random();
        this.ballX = random.nextInt(width - 50);
        this.ballY = (float)this.height/2;
        this.paddleY = (float)height * 4/5;
        this.paddleX = (float)this.width / 2 - (float)this.paddle.getWidth() / 2;
        this.ballWidth = ball.getWidth();
        this.ballHeight = ball.getHeight();
        initializeBlocks();
    }

    private void initializeBlocks() {
        int blockWidth = this.width / 8, blockHeight = this.height / 16;
        for (int j = 0; j < 5; j++)  {
            for (int i = 0; i < 8; i++) {
                levelBlocks.add(new Block(j, i, blockWidth, blockHeight));
                this.numBlocks++;
            }
        }
    }
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        int[] rainbow = context.getResources().getIntArray(R.array.rainbow);
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        Line ballMovement = new Line(this.ballX, this.ballY, this.ballX + this.ballVelocity.getDx(), this.ballY + this.ballVelocity.getDy());
        canvas.drawRect(0, 0, this.width, STATS_HEIGHT, this.statsPaint);

        if ((this.ballX >= this.width - this.ball.getWidth()) || (this.ballX <= 0)) {
            this.ballVelocity.setVelocity(-this.ballVelocity.getDx(), this.ballVelocity.getDy());
        }
        if ((this.ballY >= this.height - this.ball.getHeight()) || (this.ballY <= STATS_HEIGHT)) {
            this.ballVelocity.setVelocity(this.ballVelocity.getDx(), -this.ballVelocity.getDy());
        }

        if (this.ballY > this.paddleY + this.paddle.getHeight()) {
            youLost();
        }
        if (((this.ballX + this.ball.getWidth()) >= this.paddleX) && (this.ballX <= this.paddleX + this.paddle.getWidth())
                && (this.ballY + this.ball.getHeight() >= this.paddleY) && (this.ballY + this.ball.getHeight() <= this.paddleY + this.paddle.getHeight())) {
            this.ballVelocity.setVelocity(this.ballVelocity.getDx() + 1, -1 * (this.ballVelocity.getDy() + 1));
        }

        canvas.drawBitmap(this.ball, this.ballX, this.ballY, null);
        canvas.drawBitmap(this.paddle, this.paddleX, this.paddleY, null);

        int index = 0;
        Paint linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(3);

        for (int i = 0; i < this.numBlocks; i++) {
            this.blockPaint.setColor(rainbow[index]);
            if (i % 8 == 7) {
                index++;
            }
            if (levelBlocks.get(i).getVisability()) {
                float left = levelBlocks.get(i).getColumn() * levelBlocks.get(i).getWidth() + 1;
                float top = levelBlocks.get(i).getRow() * levelBlocks.get(i).getHeight() + 1 + STATS_HEIGHT;
                float right = levelBlocks.get(i).getColumn() * levelBlocks.get(i).getWidth() + levelBlocks.get(i).getWidth() - 1;
                float bottom = levelBlocks.get(i).getRow() * levelBlocks.get(i).getHeight() + levelBlocks.get(i).getHeight() - 1 + STATS_HEIGHT;

                canvas.drawRect(left, top, right, bottom, this.blockPaint);
                canvas.drawLine(left, top, right, top, linePaint); // Top line
                canvas.drawLine(left, bottom, right, bottom, linePaint); // Bottom line
                canvas.drawLine(left, top, left, bottom, linePaint); // Left line
                canvas.drawLine(right, top, right, bottom, linePaint); // Right line
            }
        }

        canvas.drawText("score:" + this.score, (int) (this.width / 8.0), (int) (STATS_HEIGHT / 1.5), this.textPaint);

        int minBlockIndex = -1, interIndex = -1;
        com.example.myapplication.Geometry.Point minPoint = null;
        com.example.myapplication.Geometry.Point current = new com.example.myapplication.Geometry.Point(this.ballX, this.ballY);

        for (int i = 0; i < this.numBlocks; i++) {
            if (levelBlocks.get(i).getVisability()) {
                Line[] blockLines = levelBlocks.get(i).getLinesOfRect();
                com.example.myapplication.Geometry.Point intersectionPoint;

                for (int j = 0; j < 4; j++) {
                    intersectionPoint = ballMovement.intersectionWith(blockLines[j]);
                    if (intersectionPoint != null) {
                        if (minPoint == null || intersectionPoint.distance(current) < minPoint.distance(current)) {
                            minPoint = intersectionPoint;
                            minBlockIndex = i;
                            interIndex = j;
                        }
                    }
                }
            }
        }

        if (minPoint != null && minBlockIndex != -1) {

            switch (interIndex) {
                case 0:
                    this.ballX = (float) minPoint.getX();
                    this.ballY = (float) minPoint.getY() - ballHeight / 2;
                    this.ballVelocity.setVelocity(this.ballVelocity.getDx(), -1 * (this.ballVelocity.getDy() + 1));
                    break;
                case 1:
                    this.ballX = (float) minPoint.getX();
                    this.ballY = (float) minPoint.getY() + ballHeight / 2;
                    this.ballVelocity.setVelocity(this.ballVelocity.getDx(), -1 * (this.ballVelocity.getDy() + 1));
                    break;
                case 2:
                    this.ballX = (float) minPoint.getX() - ballWidth / 2;
                    this.ballY = (float) minPoint.getY();
                    this.ballVelocity.setVelocity(-1 * (this.ballVelocity.getDx() + 1), this.ballVelocity.getDy());
                    break;
                case 3:
                    this.ballX = (float) minPoint.getX() + ballWidth / 2;
                    this.ballY = (float) minPoint.getY();
                    this.ballVelocity.setVelocity(-1 * (this.ballVelocity.getDx() + 1), this.ballVelocity.getDy());
                    break;
            }
            this.levelBlocks.get(minBlockIndex).setInvisible();
            this.score += 20;
            this.brokenBlocks++;
            if (this.brokenBlocks == 40) {
                youWon();
            }
        }
        minPoint = null;
        Line ballMovement2 = new Line(this.ballX, this.ballY, this.ballX + this.ballVelocity.getDx(), this.ballY + this.ballVelocity.getDy());
        for (int i = 0; i < this.numBlocks; i++) {
            if (levelBlocks.get(i).getVisability()) {
                Line[] blockLines = levelBlocks.get(i).getLinesOfRect();
                com.example.myapplication.Geometry.Point intersectionPoint;

                for (int j = 0; j < 4; j++) {
                    intersectionPoint = ballMovement2.intersectionWith(blockLines[j]);
                    if (intersectionPoint != null) {
                        if (minPoint == null || intersectionPoint.distance(current) < minPoint.distance(current)) {
                            minPoint = intersectionPoint;
                        }
                    }
                }
            }
        }
        if (minPoint == null) {
            this.ballX += this.ballVelocity.getDx();
            this.ballY += this.ballVelocity.getDy();
        }
        if (this.brokenBlocks == this.numBlocks) {
            this.gameOver = true;
        }

        if (!this.gameOver) {
            this.handler.postDelayed(this.runnable, 30);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX(), touchY = event.getY();
        if (touchY >= this.paddleY) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                this.oldX = event.getX();
                this.oldPaddleX = this.paddleX;
            }
            if (action == MotionEvent.ACTION_MOVE) {
                float shift = this.oldX - touchX;
                float newPaddleX = this.oldPaddleX - shift;
                if (newPaddleX <= 0) {
                    this.paddleX = 0;
                } else if (newPaddleX >= this.width - this.paddle.getWidth()) {
                    this.paddleX = this.width - this.paddle.getWidth();
                } else {
                    this.paddleX = newPaddleX;
                }
            }
        }
        return true;
    }

    private void youLost() {
        if (showAgain) {
            this.handler.removeCallbacksAndMessages(null);
            Intent intent = new Intent(this.context, LosingScreen.class);
            intent.putExtra("score", this.score);
            this.context.startActivity(intent);
            ((Activity) this.context).finish();
            this.showAgain = false;
        }
    }
    private void youWon() {
        if (showAgain) {
            this.handler.removeCallbacksAndMessages(null);
            Intent intent = new Intent(this.context, YouWonScreen.class);
            intent.putExtra("score", this.score);
            this.context.startActivity(intent);
            ((Activity) this.context).finish();
            this.showAgain = false;
        }
    }
}
