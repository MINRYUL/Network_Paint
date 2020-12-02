package lab9;

import java.io.Serializable;

public class Point implements Serializable {
	private static final long serialVersionUID = 1L;
	private	double	posX, posY;
	public Point(double x, double y) {
		posX = x; posY = y;
	}
	public double getX() { return posX; }
	public double getY() { return posY; }
	public void setX(double x) { posX = x; }
	public void setY(double y) { posY = y; }
}
