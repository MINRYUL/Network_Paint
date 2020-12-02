package lab9;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;

public class MyDrawPanel extends JPanel {
	private ArrayList<Polyline> panellist = new ArrayList<Polyline>();
	
	public void setPolyLine(Polyline pline) {
		panellist.add(pline);
	}
	
	public void cleanPanel() {
		panellist = new ArrayList<Polyline>();
	}
	
	public ArrayList<Polyline> returnpanellist(MyDrawPanel panel){
		return panellist;
	}
	
	public Polyline removeline(int polylineindex) throws IndexOutOfBoundsException{
		
		if(panellist.size() == 0)	return null;
		else if(panellist.size() == (polylineindex + 1)) {
			panellist.remove(polylineindex);
			return null;
		}
		else {
			panellist.remove(polylineindex);
			return panellist.get(panellist.size() - 1);
		}
	}		
	
	public Point checkPoint(double x1, double y1) {
		for(int j = 0; j < panellist.size(); j++) {
			for(int i = 0; i < panellist.get(j).getNumPts(); i++) {
				double x = (double)panellist.get(j).getPoint(i).getX();
				double y = (double)panellist.get(j).getPoint(i).getY();
				if(((x - 4 <= x1) && (x1 <= x + 4)) && ((y - 4 <= y1) && (y1 <= y + 4))) {
					return panellist.get(j).getPoint(i);
				}
			}
		}
		return null;
	}
	
	public Polyline checkline(double x1, double y1) {
		for(int j = 0; j < panellist.size(); j++) {
			for(int i = 0; i < panellist.get(j).getNumPts(); i++) {
				double x = (double)panellist.get(j).getPoint(i).getX();
				double y = (double)panellist.get(j).getPoint(i).getY();
				if(((x - 4 <= x1) && (x1 <= x + 4)) && ((y - 4 <= y1) && (y1 <= y + 4))) {
					return panellist.get(j);
				}
			}
		}
		return null;
	}
	
	public boolean changeline(Polyline p) {
		for(int j = 0; j < panellist.size(); j++) {
			for(int a = 0; a < panellist.get(j).getNumPts(); a++){
				double x = (double)panellist.get(j).getPoint(a).getX();
				double y = (double)panellist.get(j).getPoint(a).getY();
				if(a < p.getNumPts()) {
					if((x == p.getPoint(a).getX()) && (y == p.getPoint(a).getY())) {
						for(int k = 0; k < p.getNumPts(); k++) {
							double x1 = (double)panellist.get(j).getPoint(k).getX();
							double y1 = (double)panellist.get(j).getPoint(k).getY();
							double x2 = (double)p.getPoint(k).getX();
							double y2 = (double)p.getPoint(k).getY();
							if((x1 != x2) || (y1 != y2)){
								panellist.get(j).getPoint(k).setX(x2);	
								panellist.get(j).getPoint(k).setY(y2);
								return true;
							}
						}
						return true;
					}
				}
				if(a == 2) break;
			}
		}
		return false;
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.black);
		Graphics2D	g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(3));
		
		for(int j = 0; j < panellist.size(); j++) {
			for(int i = 0; i < panellist.get(j).getNumPts(); i++) {
				double x = (double)panellist.get(j).getPoint(i).getX();
				double y = (double)panellist.get(j).getPoint(i).getY();
				if(x == 0 && y == 0)	break;
				g.fillRect((int)x, (int)y, 3, 3);
			}
			for(int i = 1; i < panellist.get(j).getNumPts(); i++) {
				double x1 = (double)panellist.get(j).getPoint(i - 1).getX();
				double y1 = (double)panellist.get(j).getPoint(i - 1).getY();
				double x2 = (double)panellist.get(j).getPoint(i).getX();
				double y2 = (double)panellist.get(j).getPoint(i).getY();
				if (x2 == 0 && y2 == 0) {
					g.drawLine((int)panellist.get(j).getPoint(0).getX(), (int)panellist.get(j).getPoint(0).getY(), (int)x1, (int)y1);
					break;
				}
				g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
			}
		}
	}
}
