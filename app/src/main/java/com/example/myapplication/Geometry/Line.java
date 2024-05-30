package com.example.myapplication.Geometry;




/**
 * Represents a line, defined by start and end point.
 *
 * @author Gavriel Shandalov
 */
public class Line {
    private Point start;
    private Point end;
    private static final double COMPARISON_THRESHOLD = 0.00001;

    /**
     * Instantiates a new Line.
     *
     * @param start the start
     * @param end   the end
     */
    public Line(Point start, Point end) {
        this.start = new Point(start.getX(), start.getY());
        this.end = new Point(end.getX(), end.getY());
    }

    /**
     * Instantiates a new Line.
     *
     * @param x1 the x 1
     * @param y1 the y 1
     * @param x2 the x 2
     * @param y2 the y 2
     */
    public Line(double x1, double y1, double x2, double y2) {
        this.start = new Point(x1, y1);
        this.end = new Point(x2, y2);
    }

    /**
     * .
     * checks if the diff between two double numbers is very small
     *
     * @param a - a double num
     * @param b - a double num
     * @return true if the diff between the number is smaller than COMPARISON_THRESHOLD, false otherwise
     */
    public static boolean doubleEquals(double a, double b) {
        return Math.abs(a - b) < COMPARISON_THRESHOLD;
    }

    /**
     * calculates the length of the line and returns it.
     *
     * @return the length of the line
     */
    public double length() {
        return this.start.distance(this.end);
    }

    /**
     * calculates the middle point and returns it.
     *
     * @return the middle point
     */
// Returns the middle point of the line
    public Point middle() {
        double midX = (this.start.getX() + this.end.getX()) / 2;
        double midY = (this.start.getY() + this.end.getY()) / 2;
        return new Point(midX, midY);
    }

    /**
     * returns the start point of the line.
     *
     * @return the start point
     */
    public Point start() {
        return new Point(this.start.getX(), this.start.getY());
    }

    /**
     * returns the end point of the line.
     *
     * @return the end point
     */
    public Point end() {
        return new Point(this.end.getX(), this.end.getY());
    }

    /**
     * the function returns a line when the start point has the lower x and
     * if the x are equal so the lower y.
     *
     * @return an organized line
     */
    public Line organizedLine() {
        if (this.start.getX() < this.end.getX()) {
            return new Line(this.start, this.end);
        } else if (this.start.getX() > this.end.getX()) {
            return new Line(this.end, this.start);
        } else {
            if (this.start.getY() < this.end.getY()) {
                return new Line(this.start, this.end);
            } else {
                return new Line(this.end, this.start);
            }
        }
    }

    /**
     * Checks if the two lines are intersecting or not. returns true if they
     * are, false if not.
     *
     * @param other the other line we check intersection with
     * @return true if the lines intersect, false otherwise
     */
    public boolean isIntersecting(Line other) {
        if (other == null) {
            return false;
        }
        // creates organized lines by x and y, easier to check
        Line temp1 = this.organizedLine();
        Line temp2 = other.organizedLine();
        // returns true if the lines are equal
        if (temp1.start.getX() == temp2.start.getX() && temp1.end.getX()
                == temp2.end.getX()
                && temp1.start.getY() == temp2.start.getY() && temp1.end.getY()
                == temp2.end.getY()) {
            return true;
        }
        double slope1 = 0, slope2 = 0, factor1 = 0, factor2 = 0;
        double xMeeting = 0, yMeeting = 0;
        boolean isFirstSlopeExist = false, isSecondSlopeExist = false;
        // checks if the slope of the line exists
        if (temp1.start.getX() - temp1.end.getX() != 0) {
            // calculates the slope with the formula (y2-y1)/(x2-x1)
            slope1 = (temp1.start.getY() - temp1.end.getY())
                    / (temp1.start.getX() - temp1.end.getX());
            isFirstSlopeExist = true;
            // calculates the b in y=mx+b with this formula
            factor1 = temp1.start.getY() - slope1 * (temp1.start.getX());
        }
        // checks if the slope of the line exists
        if (temp2.start.getX() - temp2.end.getX() != 0) {
            // calculates the slope with the formula (y2-y1)/(x2-x1)
            slope2 = (temp2.start.getY() - temp2.end.getY())
                    / (temp2.start.getX() - temp2.end.getX());
            isSecondSlopeExist = true;
            // calculates the b in y=mx+b with this formula
            factor2 = temp2.start.getY() - slope2 * (temp2.start.getX());
        }
        // a condition when the slope of both lines exist
        if (isFirstSlopeExist && isSecondSlopeExist) {
            // if the lines parallel and the b is different, so no intersection
            if (slope1 == slope2 && factor1 != factor2) {
                return false;
            }
            if (slope1 == slope2) {
                // if the end is smaller than the start they don't intersect
                if (temp1.end.getX() < temp2.start.getX() || temp2.end.getX()
                        < temp1.start.getX()) {
                    return false;
                }
            }
            /*
            calculate the xMeeting solving two equations. y=m1x+b1, y=m2x+b2
            m1x+b1=m2x+b2 --> x = (b2-b1)/(m1-m2)
             */
            xMeeting = (factor2 - factor1) / (slope1 - slope2);
            // checks if the meeting is in the range for both lines
            if ((xMeeting > temp1.start.getX() && xMeeting > temp1.end.getX())
                    || (xMeeting < temp1.start.getX()
                    && xMeeting < temp1.end.getX())) {
                return false;
            }
            return (!(xMeeting > temp2.start.getX())
                    || !(xMeeting > temp2.end.getX()))
                    && (!(xMeeting < temp2.start.getX())
                    || !(xMeeting < temp2.end.getX()));
        }
        // a case which the first slope doesn't exist and the second does
        if (!isFirstSlopeExist && isSecondSlopeExist) {
            // a case when the slope of the second line is 0
            if (slope2 == 0) {
                if (temp2.end.getY() < temp1.start.getY()) {
                    return false;
                }
                if (temp2.start.getY() > temp1.end.getY()) {
                    return false;
                }
            }
            // calculates the x and y Meeting
            xMeeting = temp1.start.getX();
            yMeeting = slope2 * xMeeting + factor2;
            //check if the meeting in the range of the start,end of both lines
            return (xMeeting <= temp2.end.getX()
                    && xMeeting >= temp2.start.getX())
                    || (xMeeting <= temp2.start.getX()
                    && xMeeting >= temp2.end.getX())
                    && (yMeeting >= temp1.start.getY()
                    && yMeeting <= temp1.end.getY())
                    || (yMeeting <= temp1.start.getY()
                    && yMeeting >= temp1.end.getY());
        }
        // a case when the first slope exists and the second doesn't
        if (isFirstSlopeExist && !isSecondSlopeExist) {
            // checks the case when the first slope is equals 0
            if (slope1 == 0) {
                if (temp2.end.getY() < temp1.start.getY()) {
                    return false;
                }
                if (temp2.start.getY() > temp1.end.getY()) {
                    return false;
                }
            }
            // calculates the x,y meeting of the point
            xMeeting = temp2.start.getX();
            yMeeting = slope1 * xMeeting + factor1;
            // check if meeting is in the range of the start,end of both lines
            if ((xMeeting <= temp1.end.getX() && xMeeting >= temp1.start.getX())
                    || (xMeeting <= temp1.start.getX()
                    && xMeeting >= temp1.end.getX())
                    && (yMeeting >= temp2.start.getY()
                    && yMeeting <= temp2.end.getY())
                    || (yMeeting <= temp2.start.getY()
                    && yMeeting >= temp2.end.getY())) {
                return true;
            }
            return false;
        }
        // a case when both slopes don't exist
        if (temp1.start.getX() != temp2.start.getX()) {
            return false;
        } else {
            if (temp1.start.getY() == temp2.end.getY()) {
                return true;
            }
            if (temp2.start.getY() == temp1.end.getY()) {
                return true;
            }
            return false;
        }
    }

    /**
     * checks if two lines are intersecting and returns the intersection
     * point if there is more than one ro zero it returns null.
     *
     * @param other the other line we check intersection with
     * @return the intersection point if the lines intersect and null otherwise
     */
    public Point intersectionWith(Line other) {
        if (other == null) {
            return null;
        }
        // creates organized lines by x and y, easier to check
        Line temp1 = this.organizedLine();
        Line temp2 = other.organizedLine();
        // checks if both lines are equal. infinite intersection the null
        if (temp1.start.getX() == temp2.start.getX()
                && temp1.end.getX() == temp2.end.getX()
                && temp1.start.getY() == temp2.start.getY()
                && temp1.end.getY() == temp2.end.getY()) {
            return null;
        }
        double slope1 = 0, slope2 = 0, factor1 = 0, factor2 = 0;
        double xMeeting = 0, yMeeting = 0;
        boolean isFirstSlopeExist = false, isSecondSlopeExist = false;
        // checks if the slope of the line exists
        if (temp1.start.getX() - temp1.end.getX() != 0) {
            // calculates the slope with the formula (y2-y1)/(x2-x1)
            slope1 = (temp1.start.getY() - temp1.end.getY())
                    / (temp1.start.getX() - temp1.end.getX());
            isFirstSlopeExist = true;
            // calculates the b in y=mx+b with this formula
            factor1 = temp1.start.getY() - slope1 * (temp1.start.getX());
        }
        // checks if the slope of the line exists
        if (temp2.start.getX() - temp2.end.getX() != 0) {
            // calculates the slope with the formula (y2-y1)/(x2-x1)
            slope2 = (temp2.start.getY() - temp2.end.getY())
                    / (temp2.start.getX() - temp2.end.getX());
            isSecondSlopeExist = true;
            // calculates the b in y=mx+b with this formula
            factor2 = temp2.start.getY() - slope2 * (temp2.start.getX());
        }
        // a case when both slopes exist
        if (isFirstSlopeExist && isSecondSlopeExist) {
            boolean intersect = true;
            // if the lines parallel and the b is different, so no intersection
            if (slope1 == slope2 && factor1 != factor2) {
                return null;
            }
            // checks if the lines are the same
            if (slope1 == slope2) {
                // checks if there is only one intersection, start=end
                if (temp1.start.getX() == temp2.end.getX()) {
                    return new Point(temp1.start.getX(), temp1.start.getY());
                }
                if (temp1.end.getX() == temp2.start.getX()) {
                    return new Point(temp2.start.getX(), temp2.start.getY());
                }
                // otherwise there are 0 or infinite so return null
                return null;
            }
            /*
            calculate the xMeeting solving two equations. y=m1x+b1, y=m2x+b2
            m1x+b1=m2x+b2 --> x = (b2-b1)/(m1-m2)
             */
            xMeeting = (factor2 - factor1) / (slope1 - slope2);
            yMeeting = slope2 * xMeeting + factor2;
            // checks if the x isn't in the range of the lines
            if ((xMeeting > temp1.start.getX() && xMeeting > temp1.end.getX())
                    || (xMeeting < temp1.start.getX()
                    && xMeeting < temp1.end.getX())) {
                intersect = false;
            }
            if ((xMeeting > temp2.start.getX() && xMeeting > temp2.end.getX())
                    || (xMeeting < temp2.start.getX()
                    && xMeeting < temp2.end.getX())) {
                intersect = false;
            }
            // checks if the intersection is in the start/end of the lines
            if ((doubleEquals(xMeeting, temp1.start.getX())
                    && (doubleEquals(xMeeting, temp2.end.getX())
                    || doubleEquals(xMeeting, temp2.start.getX())))
                    || (doubleEquals(xMeeting, temp1.end.getX())
                    && (doubleEquals(xMeeting, temp2.end.getX())
                    || doubleEquals(xMeeting, temp2.start.getX())))) {
                return new Point(xMeeting, yMeeting);
            }
            if (!intersect) {
                return null;
            }
            return new Point(xMeeting, yMeeting);
        }
        // a case when the first slope doesn't exist and the second does
        if (!isFirstSlopeExist && isSecondSlopeExist) {
            // checks the case when the second line in y=b
            if (slope2 == 0) {
                if (temp2.end.getY() < temp1.start.getY()) {
                    return null;
                }
                if (temp2.start.getY() > temp1.end.getY()) {
                    return null;
                }
            }
            xMeeting = temp1.start.getX();
            yMeeting = slope2 * xMeeting + factor2;
            // checks if the meeting is in the range of the lines
            if (xMeeting <= temp2.end.getX() && xMeeting >= temp2.start.getX()
                    && yMeeting <= temp1.end.getY() && yMeeting >= temp1.start.getY()) {
                return new Point(xMeeting, yMeeting);
            }
            return null;
        }
        // a case when the second slope doesn't exist and the first does
        if (isFirstSlopeExist && !isSecondSlopeExist) {
            // checks the case when the first line in y=b
            if (slope1 == 0) {
                if (temp2.end.getY() < temp1.start.getY()) {
                    return null;
                }
                if (temp2.start.getY() > temp1.end.getY()) {
                    return null;
                }
            }
            xMeeting = temp2.start.getX();
            yMeeting = slope1 * xMeeting + factor1;
            // checks if the meeting is in the range of the lines
            if (xMeeting <= temp1.end.getX() && xMeeting >= temp1.start.getX()
                    && yMeeting <= temp2.end.getY() && yMeeting >= temp2.start.getY()) {
                return new Point(xMeeting, yMeeting);
            }
            return null;
        }
        // a case when both slopes don't exist - checks it
        if (temp1.start.getX() == temp2.start.getX()) {
            if (temp1.start.getY() == temp2.end.getY()) {
                return new Point(temp1.start.getX(), temp1.start.getY());
            }
            if (temp2.start.getY() == temp1.end.getY()) {
                return new Point(temp2.start.getX(), temp2.start.getY());
            }
        }
        return null;
    }

    /**
     * the method checks if the lines are equal. if they are it returns true,
     * if not then false
     *
     * @param other the other line we check if equals
     * @return true is the lines are equal, false otherwise
     */
    public boolean equals(Line other) {
        Line temp1 = this.organizedLine();
        Line temp2 = other.organizedLine();
        if (temp1.start.equals(temp2.start) && temp1.end.equals(temp2.end)) {
            return true;
        }
        return false;
    }
}
