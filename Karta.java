import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Karta extends JPanel {
	private JPanel karta;
	private ImageIcon bg;
	private Position lastPosition;

	
	public Karta(String filPlats){
		bg = new ImageIcon(filPlats);
		setLayout(null);
		karta = this;
		karta.repaint();
		Dimension d = new Dimension(bg.getIconWidth(), bg.getIconHeight());
		setPreferredSize(d);

	}
	
	protected void paintComponent(Graphics g){
		super.paintComponents(g);
		g.drawImage(bg.getImage(), 0, 0, bg.getIconWidth(), bg.getIconHeight(), this);
	}
	
	public Position getlastPos() {
		return lastPosition;
	}
	

	
	

		
		
	}
