package lab9;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Polyline extends MouseAdapter implements Serializable {
	private static final long serialVersionUID = 2L;
	
	private ArrayList<Point> mPts = new ArrayList<Point>();
	private MyDrawPanel panel = null;
	private Point tempP = null;
	private Polyline templine = null;
	
	public void clear(MyDrawPanel panel) {
		// ...
		mPts = new ArrayList<Point>();
		panel.cleanPanel();
	}
	
	public void setpanelcheck(MyDrawPanel Draw) {
		panel = Draw;
	}
	public void closed() {
		Point p1 = new Point(0, 0);
		mPts.add(p1);
	}
	
	public int getNumPts() {
		// ...
		return mPts.size();
	} 

	public Point getPoint(int i) {
		// ...
		return mPts.get(i);
	}
	
	public void printPoint() {
		for(int i = 0; i < this.getNumPts(); i++) {
			System.out.println(this.getPoint(i).getX() + this.getPoint(i).getY());
		}
	}
	
	public void clone(Polyline p) {
		for(int i = 0; i < p.getNumPts(); i++) {
			double x = (double)p.getPoint(i).getX();
			double y = (double)p.getPoint(i).getY();
			Point z = new Point(x, y);
			mPts.add(z);
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		tempP = null;
	}
	
	public Polyline Dragged(double x, double y) {
		if(mPts.size() > 0)
			if(mPts.get(mPts.size() - 1).getX() == 0 && mPts.get(mPts.size() - 1).getY() == 0)	return null;

		if((0 <= x) && (x <= 690) && (0 <= y) && (y <= 699)) {
			if(tempP != null) {
				tempP.setX(x);
				tempP.setY(y);
				templine = panel.checkline(tempP.getX(), tempP.getY());
				if(templine.getPoint(templine.getNumPts() - 1).getX() == 0) 	return templine;
			}
			else{
				if(mPts.size() > 0) {
					mPts.get(mPts.size() - 1).setX(x);
					mPts.get(mPts.size() - 1).setY(y);
					return null;
				}
			}
		}
		return null;
	}
	
	public void Pressed(double x, double y) {
		if(mPts.size() > 0)
			if(mPts.get(mPts.size() - 1).getX() == 0 && mPts.get(mPts.size() - 1).getY() == 0)	return;
		
		tempP = panel.checkPoint(x, y);
		if((0 <= x) && (x <= 690) && (0 <= y) && (y <= 699)) {
			if(tempP != null) return;
			else {
				Point p = new Point(x, y);
				mPts.add(p);
			}
		}
	}
}