package lab9;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

public class PolylineEditor extends MouseAdapter {
	
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	
	private MyDrawPanel draw = new MyDrawPanel();
	private Polyline line = new Polyline();
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JList<String> list;
	private Polyline tempPoint = null;
	private Polyline temp = null;
	
	private int polylineindex = -1;
	private int polylinenum = 1;
	
	public class IncomingReader implements Runnable {
		public void run() {
			try {
				while(true) {
					if((temp = (Polyline) ois.readObject()) != null) {
						if(draw.changeline(temp) == true) {
							draw.repaint();
						}
						else {
							temp.setpanelcheck(draw);
							listModel.addElement("Polyline " + (++polylinenum));
							draw.setPolyLine(temp);
							draw.addMouseListener(temp);
							draw.addMouseMotionListener(temp);
							draw.repaint();
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		tempPoint = null;
		temp = null;
		try {
			oos.writeObject(null);
			oos.flush();
		} catch (Exception ex) { 
			ex.printStackTrace(); 
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		Polyline p;
		tempPoint = new Polyline();
		p = line.Dragged(e.getX(), e.getY());
		if(p != null) {
			try {
				tempPoint.clone(p);
				oos.writeObject(tempPoint);
				oos.flush();
			} catch (Exception ex) { 
				ex.printStackTrace(); 
			}
		}
		draw.repaint();
	}
	
	public void mousePressed(MouseEvent e) {
		line.Pressed(e.getX(), e.getY());
		draw.repaint();
	}

	public PolylineEditor(Socket s, String name) {
		setUpNetworking(s);
		go(name);
		
		Thread	t = new Thread(new IncomingReader());
		t.start();
	}

	public void go(String name) {
		JFrame frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setBackground(Color.white);
		
		
		draw.setBounds(0, 0, 690, 699);

		frame.getContentPane().add(draw);
		
        listModel.addElement("Polyline " + polylinenum);
        list = new JList<String>( listModel );
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(10);
        
        JScrollPane scroller = new JScrollPane(list);
        scroller.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        JButton Delete = new JButton("Delete");
        
        scroller.setBounds(690, 10, 99, 690);
        Delete.setBounds(690, 710, 99, 50);
        panel.add(scroller);
        panel.add(Delete);
        
        Delete.addActionListener(
   			new ActionListener() {
    			public void actionPerformed(ActionEvent event) {
    				try {
    					polylineindex = list.getSelectedIndex();
    					if(listModel.getSize()==0){ 
    						polylineindex = -1;
    						return;
    					}
    					line = draw.removeline(polylineindex);
    					if(line == null) {
    						listModel.remove(polylineindex);
    						line = new Polyline();
    						line.setpanelcheck(draw);
    						listModel.addElement("Polyline " + (++polylinenum));
    						draw.setPolyLine(line);
    						draw.addMouseListener(line);
    						draw.addMouseMotionListener(line);
    					}
    					else {
    						listModel.remove(polylineindex);
    						polylineindex = -1;
    					}
    					draw.repaint();
    				}
    				catch (IndexOutOfBoundsException ex){
    					return;
    				}
    			}
    		}
    	);
        
        line.setpanelcheck(draw);
		draw.setPolyLine(line);
		draw.addMouseListener(line);
		draw.addMouseMotionListener(line);
		draw.addMouseListener((MouseListener) this);
		draw.addMouseMotionListener((MouseMotionListener) this);
		
		JButton Clear = new JButton("Clear");
		JButton Closed = new JButton("Closed Polygon");
		Clear.setBounds(20, 710, 300, 50);
		Closed.setBounds(340, 710, 300, 50);
		panel.add(Clear);
		panel.add(Closed);
		panel.setLayout(null);
		frame.getContentPane().add(panel);
		
		Clear.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					draw.cleanPanel();
					line = new Polyline();
					line.setpanelcheck(draw);
					
					polylinenum = 0;
					listModel.removeAllElements();
					listModel.addElement("Polyline " + (++polylinenum));
					
					draw.setPolyLine(line);
					draw.addMouseListener(line);
					draw.addMouseMotionListener(line);
					draw.repaint();
				}
			}
		);
		
		Closed.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					line.closed();
					draw.repaint(); 
					
					try {		
						oos.writeObject(line);
						oos.flush();
					} catch (Exception ex) { 
						ex.printStackTrace(); 
					}
	
					line = new Polyline();
					line.setpanelcheck(draw);
					listModel.addElement("Polyline " + (++polylinenum));
					draw.setPolyLine(line);
					draw.addMouseListener(line);
					draw.addMouseMotionListener(line);
				}
			}
		);
		
		BorderLayout	layout = (BorderLayout)frame.getContentPane().getLayout();
		layout.setHgap(10);
		layout.setVgap(10);

		frame.setSize(800, 800);
		frame.setVisible(true);
	}

	private boolean setUpNetworking(Socket s) {
		Runnable runthread = new IncomingReader();
		Thread chat = new Thread(runthread);
		chat.start();
		try {
			oos = new ObjectOutputStream(s.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(s.getInputStream());
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
}