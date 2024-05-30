package com.example.myapplication;

import com.example.myapplication.Geometry.Line;

public class Block {
    private boolean isVisible;
    private int row, column, width, height;
    public Block(int row, int column, int width, int height) {
        this.isVisible = true;
        this.row = row;
        this.column = column;
        this.width = width;
        this.height = height;
    }
    public void setInvisible() {
        this.isVisible = false;
    }
    public boolean getVisability() {
        return this.isVisible;
    }
    public int getColumn() {
        return this.column;
    }
    public int getHeight() {
        return this.height;
    }
    public int getRow() {
        return this.row;
    }
    public int getWidth() {
        return this.width;
    }
    public Line[] getLinesOfRect() {
        Line[] lines = new Line[4];
        lines[0] = new Line(this.column * this.width, this.row * this.height + 60, this.column * this.width + this.height,this.row * this.height + 60); //up
        lines[1] = new Line(this.column * this.width, this.row * this.height + this.height + 60, this.column * this.width + this.height,this.row * this.height + this.height + 60); //down
        lines[2] = new Line(this.column * this.width, this.row * this.height + 60, this.column * this.width, this.row * this.height + this.height + 60); //left
        lines[3] = new Line(this.column * this.width + this.width, this.row * this.height + 60, this.column * this.width + this.width, this.row * this.height + this.height + 60); //right
        return lines;
    }
}
