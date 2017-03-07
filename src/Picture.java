
import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.awt.Dimension;
import java.awt.Rectangle;
public class Picture extends JPanel
	{
	Image image;
	
	public Picture(Image image) {
		this.image = image;
		System.out.println("graphics");
		
	}
	public Picture(String s){
		
		this.image = Toolkit.getDefaultToolkit().getImage(s);
		System.out.println("gr2");
		
		
	}
	public void paintComponent(Graphics g){
		g.drawImage(this.image, 0, 0, this);
		
	}
	/*public static void main(String args[]){
		JFrame frame = new JFrame();
		ImageIcon icon = new ImageIcon("image\\1_10.png");
		Picture panel = new Picture(icon.getImage());
		frame.getContentPane().add(panel);
		frame.setSize(300, 300);
		frame.setVisible(true);
	}*/
	public boolean imageUpdate(Image img, int flags, int x, int y, int w, int h){
		if((flags&ALLBITS)!=0){
			System.out.println("repaint!");
			repaint();
		}
		return (flags&(ALLBITS|ERROR))==0;
	}
}