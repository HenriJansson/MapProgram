import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JComponent;


public class Platser extends JComponent {

	private String namn;
	private String kategori;
	private boolean synlig;
	private Position pos;
	private boolean markerad = false;
	
	public Platser(String namn, String kategori, Position p) {
		
		setBounds(p.getXCor()-10, p.getYCor()-20, 30, 30);
		this.namn = namn;
		this.kategori = kategori;
		pos = p;
		synlig = true;
		p.setPlats(this);

	}
	
	protected void paintComponent(Graphics g) {
	    int xpoints[] = {0, 10, 20};
	    int ypoints[] = {0, 20, 0};
	    int npoints = 3;
	    Polygon p = new Polygon(xpoints, ypoints, npoints);
	    
		g.setColor(setCol());
		if (synlig) {
		if (markerad) {
			g.fillPolygon(p);
			g.setColor(Color.YELLOW);
			g.drawPolygon(p);
		}
		else g.fillPolygon(p);

	}
	}
	
	public void synlig() {
		synlig = true;
		validate();
		repaint();
	}
	
	public String getKategori() {
		return kategori;
	}
	
	public Position getPosition() {
		return pos;
	}
	
	public void inteSynlig() {
		synlig = false;
		validate();
		repaint();
	}
	
	public void markera() {
		markerad = true;
		validate();
		repaint();
	}
	
	public Color setCol() {
		if( kategori.equals("Bus"))
			return Color.RED;
		if (kategori.equals("Underground"))
			return Color.BLUE;
		if (kategori.equals("Train"))
			return Color.GREEN;
		else {
			return Color.BLACK;
			}
	}
	
	public String getNamn() {
		return namn;
	}
	
	public boolean getMarkerad() {
		return markerad;
		
	}
	
	public void avMarkera() {
		markerad = false;
		validate();
		repaint();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Platser) {
			Platser p = (Platser)other;
			return pos.equals(p.getPosition());
		}
		return false;	
	}
	@Override
	public int hashCode() {
		return pos.getXCor() * 10000 + pos.getYCor();
	}
	

}
