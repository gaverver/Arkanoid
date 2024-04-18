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

public class GameLevelTwo extends View {
    Context context;
    float ballX, ballY, paddleX, paddleY, oldPaddleX, oldX;
    Velocity ballVelocity = new Velocity(20, 20);
    Handler handler;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint blockPaint = new Paint();
    int score = 0, width, height, ballWidth, ballHeight, numBlocks = 0, brokenBlocks = 0;
    Bitmap ball, paddle;
    boolean gameOver = false, showAgain = true;
    Random random;
    Paint statsPaint = new Paint();
    static final int STATS_HEIGHT = 60;
    List<Block> levelBlocks = new ArrayList<Block>();

    public GameLevelTwo(Context context) {
        super(context);
        this.context = context;
        this.statsPaint.setColor(Color.argb(255, 176, 216, 230));
        this.handler = new Handler();
        this.ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        this.paddle = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);
        this.runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        this.textPaint.setColor(Color.BLUE);
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
        this.ballY = (float)this.height/3;
        this.paddleY = (float)height * 4/5;
        this.paddleX = (float)this.width / 2 - (float)this.paddle.getWidth() / 2;
        this.ballWidth = ball.getWidth();
        this.ballHeight = ball.getHeight();
        initializeBlocks();
    }

    private void initializeBlocks() {
        int blockWidth = this.width / 8, blockHeight = this.height / 16;
        for (int i = 0; i < 8; i++)  {
            for (int j = 0; j < 4; j++) {
                levelBlocks.add(new Block(j, i, blockWidth, blockHeight));
                this.numBlocks++;
            }
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
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        int[] rainbow = context.getResources().getIntArray(R.array.rainbow);
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        canvas.drawRect(0, 0, this.width, STATS_HEIGHT, this.statsPaint);
        Line ballMovement = new Line(this.ballX, this.ballY, this.ballX + this.ballVelocity.getDx(), this.ballY + this.ballVelocity.getDy());
        this.ballX += this.ballVelocity.getDx();
        this.ballY += this.ballVelocity.getDy();
        if ((this.ballX >= this.width - this.ball.getWidth()) || (this.ballX <= 0)) {
            this.ballVelocity.setVelocity(-this.ballVelocity.getDx(), this.ballVelocity.getDy());
        }
        if ((this.ballY >= this.height - this.ball.getHeight()) || (this.ballY <= 0)) {
            this.ballVelocity.setVelocity(this.ballVelocity.getDx(), -this.ballVelocity.getDy());
        }
        if (this.ballY > this.paddleY + this.paddle.getHeight()) {
            youLost();
        }
        if(((this.ballX + this.ball.getWidth()) >= this.paddleX) && (this.ballX <= this.paddleX + this.paddle.getWidth())
                && (this.ballY + this.ball.getHeight() >= this.paddleY) && (this.ballY + this.ball.getHeight() <= this.paddleY + this.paddle.getHeight())) {
            this.ballVelocity.setVelocity(this.ballVelocity.getDx() + 1, -1*(this.ballVelocity.getDy() + 1));
        }
        int index = 0;
        for(int i = 0; i < this.numBlocks; i++) {
            this.blockPaint.setColor(rainbow[index]);
            if (i % 8 == 7) {
                index++;
            }
            if (levelBlocks.get(i).getVisability()) {
                canvas.drawRect(levelBlocks.get(i).getColumn() * levelBlocks.get(i).getWidth() + 1, levelBlocks.get(i).getRow() * levelBlocks.get(i).getHeight() + 1 + STATS_HEIGHT
                        , levelBlocks.get(i).getColumn() * levelBlocks.get(i).getWidth() + levelBlocks.get(i).getWidth() - 1,
                        levelBlocks.get(i).getRow() * levelBlocks.get(i).getHeight() + levelBlocks.get(i).getHeight() - 1 + STATS_HEIGHT, this.blockPaint);
            }
        }
        for (int i = 0; i < this.numBlocks; i++) {
            Line[] blockLines = levelBlocks.get(i).getLinesOfRect();
            if (levelBlocks.get(i).getVisability()) {
                if (this.ballX + this.ballWidth >= levelBlocks.get(i).getColumn() * levelBlocks.get(i).getWidth()
                        && this.ballX <= levelBlocks.get(i).getColumn() * levelBlocks.get(i).getWidth() + levelBlocks.get(i).getWidth()
                        && this.ballY <= levelBlocks.get(i).getRow() * levelBlocks.get(i).getHeight() + levelBlocks.get(i).getHeight()
                        && this.ballY >= levelBlocks.get(i).getRow() * levelBlocks.get(i).getHeight()
                        && (ballMovement.intersectionWith(blockLines[0]) != null || ballMovement.intersectionWith(blockLines[1]) != null)) {
                    this.ballVelocity.setVelocity(this.ballVelocity.getDx(), -1 * (this.ballVelocity.getDy() + 1));
                    levelBlocks.get(i).setInvisible();
                    this.score += 20;
                    this.brokenBlocks++;
                    if (this.brokenBlocks == 32) {
                        youWon();
                    }
                }
                if (this.ballY + this.ballHeight >= this.levelBlocks.get(i).getRow() * this.levelBlocks.get(i).getHeight()
                        && this.ballY <= this.levelBlocks.get(i).getRow() * this.levelBlocks.get(i).getHeight() + this.levelBlocks.get(i).getHeight()
                        && this.ballX <= this.levelBlocks.get(i).getColumn() * this.levelBlocks.get(i).getWidth() + this.levelBlocks.get(i).getWidth()
                        && this.ballX >= this.levelBlocks.get(i).getColumn() * this.levelBlocks.get(i).getWidth()
                        &&(ballMovement.intersectionWith(blockLines[2]) != null || ballMovement.intersectionWith(blockLines[3]) != null)) {
                    this.ballVelocity.setVelocity(-1*(this.ballVelocity.getDx()+1), this.ballVelocity.getDy());
                    this.levelBlocks.get(i).setInvisible();
                    this.score += 20;
                    this.brokenBlocks++;
                    if (this.brokenBlocks == 32) {
                        youWon();
                    }
                }
            }
        }
        if (this.brokenBlocks == this.numBlocks) {
            this.gameOver = true;
        }
        canvas.drawBitmap(this.ball, this.ballX, this.ballY, null);
        canvas.drawBitmap(this.paddle, this.paddleX, this.paddleY, null);
        canvas.drawText("score:" + this.score, (int)(this.width/8.0), (int)(STATS_HEIGHT/1.5), this.textPaint);
        if (!this.gameOver) {
            this.handler.postDelayed(this.runnable, 30);
        }
    }


}
