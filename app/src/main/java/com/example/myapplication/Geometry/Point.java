package com.example.myapplication.Geometry;

public class Point {
    private double x;
    private double y;
    private static final double COMPARISON_THRESHOLD = 0.00001;

    /**
     * .
     * the function gets two doubles and creates a new object from type Point
     *
     * @param x - the x value of the point
     * @param y - the y value of the point
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public void setX(int x) {
        this.x += x;
    }
    public void setY(int y) {
        this.y += y;
    }

    /**
     * Instantiates a new Point.
     *
     * @param p the point we copy the value from
     */
    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    /**
     * .
     * checks if the diff between two double numbers is very small
     *
     * @param a - a double num
     * @param b - a double num
     * @return true if the diff between the number is smaller than
     * COMPARISON_THRESHOLD, false otherwise
     */
    public static boolean doubleEquals(double a, double b) {
        return Math.abs(a - b) < Point.COMPARISON_THRESHOLD;
    }

    /**
     * .
     * the function calculates and returns the distance between two points
     *
     * @param other - a second point
     * @return the distance between the two points
     */
    public double distance(Point other) {
        return Math.sqrt((this.x - other.x) * (this.x - other.x)
                + (this.y - other.y) * (this.y - other.y));
    }

    /**
     * .
     * checks if the two points are equal and return true if they are, false
     * if not
     *
     * @param other - a second point
     * @return true if the points are equals(x and y are equal) otherwise false
     */
    public boolean equals(Point other) {
        //checks if the input is invalid
        if (other == null) {
            return false;
        }
        if (doubleEquals(this.x, other.x) && doubleEquals(this.y, other.y)) {
            return true;
        }
        return false;
    }

    /**
     * .
     * return the value of x of this point
     *
     * @return the x value of the point
     */
    public double getX() {
        return this.x;
    }

    /**
     * .
     * return the value of y of this point
     *
     * @return the y value of the point
     */
    public double getY() {
        return this.y;
    }
}
