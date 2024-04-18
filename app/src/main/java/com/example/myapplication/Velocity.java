package com.example.myapplication;

public class Velocity {
    private double dx;
    private double dy;

    /**
     * .
     * the function gets two doubles and creates a new object from type Velocity
     *
     * @param dx - the dx value of the Velocity
     * @param dy - the dy value of the Velocity
     */
    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * .
     * return the value of dx of this Velocity
     *
     * @return the dx value of the Velocity
     */
    public double getDx() {
        return this.dx;
    }

    /**
     * .
     * return the value of dy of this Velocity
     *
     * @return the dy value of the Velocity
     */
    public double getDy() {
        return this.dy;
    }

    /**
     * Sets velocity to a given values of dx and dy.
     *
     * @param dx the dx we change the value to
     * @param dy the dy we change the value to
     */
    public void setVelocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
}