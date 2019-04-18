import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Interface extends JFrame {

	Map<Position, Platser> platsersPositioner = new HashMap<>();
	Map<String, ArrayList<Platser>> platsersKategorier = new HashMap<>();
	Map<String, ArrayList<Platser>> platsersNamn = new HashMap<>();
	ArrayList<Platser> markeradePlatser = new ArrayList<Platser>();

	JRadioButton namedKnapp;
	JRadioButton describedKnapp;
	String[] panelOptions = { "New", "Load Places", "Save", "Exit" };
	JMenuBar options = new JMenuBar();
	private boolean kartaLaddad = false;
	private Karta nuvarandeKarta = null;
	private JList<String> kategoriFält;
	public MusLyss musLyss = new MusLyss();
	private String[] kategorier = { "Bus", "Train", "Underground" };
	private JTextField sökFält;
	private boolean changeMade = false;
	private JScrollPane sp;

	public Interface() {
		super("Inlupp 2:");

		JPanel övre = new JPanel(new BorderLayout());
		add(övre, BorderLayout.NORTH);

		JMenu label = new JMenu("Settings");
		options.add(label);
		JMenuItem ny = new JMenuItem("New");
		ny.addActionListener(new NyKartaLyss());
		JMenuItem load = new JMenuItem("Load");
		load.addActionListener(new LaddaLyss());
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new SparaLyss());
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ExitLyss());
		label.add(ny);
		label.add(load);
		label.add(save);
		label.add(exit);
		
		övre.add(options, BorderLayout.NORTH);
		
		

		
		JPanel knappRad = new JPanel();
		knappRad.setLayout(new BoxLayout(knappRad, BoxLayout.X_AXIS));
		övre.add(knappRad, BorderLayout.SOUTH);
		JButton newKnapp = new JButton("New");
		newKnapp.addActionListener(new NewLyss());
		knappRad.add(newKnapp, BorderLayout.SOUTH);

		namedKnapp = new JRadioButton("Named", true);
		describedKnapp = new JRadioButton("Described");

		JPanel bgWrapper = new JPanel();
		bgWrapper.setLayout(new BoxLayout(bgWrapper, BoxLayout.Y_AXIS));

		bgWrapper.add(namedKnapp);
		bgWrapper.add(describedKnapp);
		ButtonGroup bg = new ButtonGroup();
		bg.add(namedKnapp);
		bg.add(describedKnapp);
		knappRad.add(bgWrapper);

		sökFält = new JTextField("Search");
		sökFält.setMaximumSize(new Dimension(100, 20));

		knappRad.add(sökFält);

		JButton sökKnapp = new JButton("Search");
		sökKnapp.addActionListener(new SearchLyss());
		knappRad.add(sökKnapp);

		JButton gömKnapp = new JButton("Hide");
		knappRad.add(gömKnapp);
		gömKnapp.addActionListener(new GömLyss());

		JButton taBortKnapp = new JButton("Remove");
		knappRad.add(taBortKnapp);
		taBortKnapp.addActionListener(new removeLyss());

		JButton koordinatKnapp = new JButton("Coordinates");
		knappRad.add(koordinatKnapp);
		koordinatKnapp.addActionListener(new KoordinatLyss());

		JPanel högra = new JPanel();
		högra.setLayout(new BoxLayout(högra, BoxLayout.Y_AXIS));
		add(högra, BorderLayout.EAST);

		kategoriFält = new JList<String>(kategorier);
		kategoriFält.setMaximumSize(new Dimension(200, 400));
		högra.add(kategoriFält, BorderLayout.EAST);
		kategoriFält.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		kategoriFält.addListSelectionListener(new KategoriLyss());

		JButton gömKategoriKnapp = new JButton("Hide Category");
		högra.add(gömKategoriKnapp);
		gömKategoriKnapp.addActionListener(new GömLyss());

		platsersKategorier.put("Bus", new ArrayList<Platser>());
		platsersKategorier.put("Train", new ArrayList<Platser>());
		platsersKategorier.put("None", new ArrayList<Platser>());
		platsersKategorier.put("Underground", new ArrayList<Platser>());

		setSize(900, 600);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent we) {

				if (changeMade == true) {
					String ObjButtons[] = { "Yes", "No" };

					int PromptResult = JOptionPane.showOptionDialog(null,
							"Det finns osparade ändringar, vill du avsluta?", " ", JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
					if (PromptResult == 0) {
						System.exit(0);
					}

				} else
					System.exit(0);
			}
		});

	}

	public void nyKarta(String namn) {

		if (kartaLaddad == true) {
			remove(sp);
		}
		nuvarandeKarta = new Karta(namn);
		sp = new JScrollPane(nuvarandeKarta);
		add(sp, BorderLayout.CENTER);
		validate();
		repaint();
		kartaLaddad = true;
	}

	public void nyPlats(Position pos) {

		Platser nyPlats = null;
		String namn = null;
		String kategori = kategoriFält.getSelectedValue();
		if (kategori == null) {
			kategori = "None";
		}

		if (namedKnapp.isSelected()) {
			try {
				namn = JOptionPane.showInputDialog("Namn:");
				nyPlats = new NamngivenPlats(namn, kategori, pos);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(Interface.this, "Fel inmatning!");
			}

		}

		if (describedKnapp.isSelected()) {
			BeskrivenForm form = new BeskrivenForm();
			int formSvar = JOptionPane.showConfirmDialog(Interface.this, form);
			if (formSvar != JOptionPane.OK_OPTION)
				return;
			else {
				try {
					namn = form.getNamn();
					String beskrivning = form.getBeskrivning();
					nyPlats = new BeskrivenPlats(namn, kategori, pos, beskrivning);

				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(Interface.this, "Fel inmatning!");
				}

			}
		}
		if (nyPlats != null) {
			Platser p = nyPlats;
			if (!platsersPositioner.equals(p)) {
				p.addMouseListener(new BrickLyss());
				nuvarandeKarta.add(p);
				nuvarandeKarta.validate();
				nuvarandeKarta.repaint();
				platsersKategorier.get(p.getKategori()).add(p);
				platsersPositioner.put(p.getPosition(), p);
				changeMade = true;
				if(platsersNamn.containsKey(p.getNamn())) {
					platsersNamn.get(p.getNamn()).add(p);
				}
					else { 
						ArrayList<Platser> list = new ArrayList<Platser>();
						platsersNamn.put(p.getNamn(), list);
						platsersNamn.get(p.getNamn()).add(p);
				}
			} else
				JOptionPane.showMessageDialog(Interface.this, "Positionen finns redan!");

		}

	}

	public void taBortMarkerat() {
		for (Platser p : markeradePlatser) {
			gömOchAvmarkera(p);
			taBort(p);
		}
	}

	public void läggTillNy(Platser p) {
		if (p != null) {

			if (!platsersPositioner.equals(p)) {
				p.addMouseListener(new BrickLyss());
				nuvarandeKarta.add(p);
				nuvarandeKarta.validate();
				nuvarandeKarta.repaint();
				platsersKategorier.get(p.getKategori()).add(p);
				platsersPositioner.put(p.getPosition(), p);
				changeMade = true;
				if(platsersNamn.containsKey(p.getNamn())) {
					platsersNamn.get(p.getNamn()).add(p);
				}
					else { 
						ArrayList<Platser> list = new ArrayList<Platser>();
						platsersNamn.put(p.getNamn(), list);
						platsersNamn.get(p.getNamn()).add(p);
				}
					
			} else
				JOptionPane.showMessageDialog(Interface.this, "Positionen finns redan!");

		}

	}

	public void taBortKoordinat() {

	}

	public void gömOchAvmarkera(Platser p) {
		p.avMarkera();
		p.inteSynlig();
		markeradePlatser.remove(p);

	}
	
	public void Avmarkera(Platser p) {
		p.avMarkera();
		markeradePlatser.remove(p);

	}

	public void avMarkeraOchGömAlla() {

		for (Platser p : markeradePlatser) {
			p.avMarkera();
			p.inteSynlig();
		}
		markeradePlatser.clear();
	}
	
	public void avMarkeraAlla() {
		for (Platser p : markeradePlatser) {
			p.avMarkera();
		}
		markeradePlatser.clear();
	}

	public void taBort(Platser p) {
		String namn = p.getNamn();
		String kategori = p.getKategori();
		Position pos = p.getPosition();
		p.inteSynlig();
		p.avMarkera();
		platsersKategorier.get(kategori).remove(p);
		platsersPositioner.remove(pos, p);
		platsersNamn.get(namn).remove(p);
		if(platsersNamn.get(namn).isEmpty()){
			ArrayList<Platser> list = platsersNamn.get(namn);
			platsersNamn.remove(namn, list);
		}
	}

	public void taBortAlla() {
		ArrayList<Platser> list = new ArrayList<Platser>(platsersPositioner.values());
		for(Platser p : list) {
			taBort(p);
		}

	}

	class removeLyss implements ActionListener {

		public void actionPerformed(ActionEvent ave) {
			for (Platser p : markeradePlatser) {
				taBort(p);
			}
			markeradePlatser.clear();

		}
	}

	class NewLyss implements ActionListener {

		public void actionPerformed(ActionEvent ave) {

			if (nuvarandeKarta != null) {
				nuvarandeKarta.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				nuvarandeKarta.addMouseListener(musLyss);
			} else
				JOptionPane.showMessageDialog(Interface.this, "Ladda in en karta!");
		}
	}

	class GömLyss implements ActionListener {

		public void actionPerformed(ActionEvent ave) {
			avMarkeraOchGömAlla();
			}
	}

	class SearchLyss implements ActionListener {

		public void actionPerformed(ActionEvent ave) {
			if (!sökFält.getText().equals("Search") && !sökFält.getText().equals(null)) {

				String namn = sökFält.getText();

				if (platsersNamn.containsKey(namn)) {
					ArrayList<Platser> list = platsersNamn.get(namn);
					avMarkeraAlla();

					for(Platser p : list) {
					p.markera();
					p.synlig();
					markeradePlatser.add(p);
					}
				} else
					JOptionPane.showMessageDialog(Interface.this, "Ingen plats med det namnet");

			} else
				JOptionPane.showMessageDialog(Interface.this, "Fel inmatning!");
		}

	}

	class KoordinatLyss implements ActionListener {

		public void actionPerformed(ActionEvent ave) {

			KoordinatForm form = new KoordinatForm();

			int formSvar = JOptionPane.showConfirmDialog(Interface.this, form);
			if (formSvar != JOptionPane.OK_OPTION)
				return;
			else {
				try {
					int x = form.getXCor();
					int y = form.getYCor();
					Position xy = new Position(x, y);

					if (platsersPositioner.containsKey(xy)) {
						Platser p = platsersPositioner.get(xy);
						avMarkeraAlla();
						p.markera();
						p.synlig();
						markeradePlatser.add(p);
					} else
						JOptionPane.showMessageDialog(Interface.this, "Koordinat finns inte!");
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(Interface.this, "Fel inmatning!");

				}

			}

		}

	}

	class ExitLyss implements ActionListener {

		public void actionPerformed(ActionEvent ave) {

			if (changeMade == true) {
				String ObjButtons[] = { "Yes", "No" };

				int PromptResult = JOptionPane.showOptionDialog(null, "Det finns osparade ändringar, vill du avsluta?",
						" ", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
				if (PromptResult == 0) {
					System.exit(0);
				}

			} else
				System.exit(0);
		}
	}

	class SparaLyss implements ActionListener {

		public void actionPerformed(ActionEvent ave) {
			JFileChooser jfc = new JFileChooser(".");
			int svar = jfc.showOpenDialog(Interface.this);
			if (svar == JFileChooser.APPROVE_OPTION) {

				try {
					File fil = jfc.getSelectedFile();
					String långNamn = fil.getAbsolutePath();
					FileWriter utfil = new FileWriter(långNamn);
					PrintWriter out = new PrintWriter(utfil);

					for (Platser p : platsersPositioner.values()) {
						if (p instanceof BeskrivenPlats) {
							BeskrivenPlats beskriven = (BeskrivenPlats) p;
							out.println("Described" + "," + beskriven.getKategori() + ","
									+ beskriven.getPosition().getXCor() + "," + beskriven.getPosition().getYCor() + ","
									+ beskriven.getNamn() + "," + beskriven.getBeskrivning());
						} else
							out.println("Namned," + p.getKategori() + "," + p.getPosition().getXCor() + ","
									+ p.getPosition().getYCor() + "," + p.getNamn());
					}
					out.close();
					utfil.close();
					changeMade = false;
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(Interface.this, "File kan ej öppnas");
				} catch (IOException e) {
					JOptionPane.showMessageDialog(Interface.this, "File kan inte öppnas");
				}
			}

		}

	}

	class NyKartaLyss implements ActionListener {

		public void actionPerformed(ActionEvent ave) {
		
		if (changeMade == false) {
			JFileChooser jfc = new JFileChooser(".");
			int svar = jfc.showOpenDialog(Interface.this);
			if (svar == JFileChooser.APPROVE_OPTION) {
				File fil = jfc.getSelectedFile();
				String långNamn = fil.getAbsolutePath();
				nyKarta(långNamn);
			}
			} else {
			    String ObjButtons[] = {"Yes","No"};
			    
			    int PromptResult = JOptionPane.showOptionDialog(
			    		null, 
			    		"Det finns osparade ändringar, vill du byta karta?", " ", 
			        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, 
			        ObjButtons,ObjButtons[1]);
			    if(PromptResult==0)
			    {
			    taBortAlla();
				JFileChooser jfc = new JFileChooser(".");
					int svar = jfc.showOpenDialog(Interface.this);
					if (svar == JFileChooser.APPROVE_OPTION) {
						File fil = jfc.getSelectedFile();
						String långNamn = fil.getAbsolutePath();
						nyKarta(långNamn);
			    }
			    
		}

		}
	}
	}
	
	class LaddaLyss implements ActionListener {

		public void actionPerformed(ActionEvent ave) {

			boolean notSaved = true;
			while (notSaved == true) {
				
			if (changeMade == false) {
			taBortAlla();
			JFileChooser jfc = new JFileChooser(".");
			int svar = jfc.showOpenDialog(Interface.this);
			if (svar == JFileChooser.APPROVE_OPTION) {

				try {
					File fil = jfc.getSelectedFile();
					String långNamn = fil.getAbsolutePath();
					FileReader infil = new FileReader(långNamn);
					BufferedReader in = new BufferedReader(infil);
					String line;
					while ((line = in.readLine()) != null) {
						String[] tokens = line.split(",");
						String typ = tokens[0];
						String kategori = tokens[1].replace("\n", "").replace("\r", "");
						;
						int xCor = Integer.parseInt(tokens[2]);
						int yCor = Integer.parseInt(tokens[3]);
						String namn = tokens[4];
						Position p = new Position(xCor, yCor);

						if (typ.equals("Described")) {
							String beskrivning = tokens[5];
							BeskrivenPlats b = new BeskrivenPlats(namn, kategori, p, beskrivning);
							läggTillNy(b);
						} else {

							NamngivenPlats n = new NamngivenPlats(namn, kategori, p);
							läggTillNy(n);
						}

					}
					in.close();
					infil.close();
					changeMade = true;
					notSaved = false;

				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(Interface.this, "Fil kan ej öppnas");
				} catch (IOException e) {
					JOptionPane.showMessageDialog(Interface.this, "Fil kan inte öppnas");
				}
			} else notSaved = false;
			} else { 			    
				String ObjButtons[] = {"Yes","No"}; 
		    int PromptResult = JOptionPane.showOptionDialog(
		    		null, 
		    		"Det finns osparade ändringar, vill du byta karta?", " ", 
		        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, 
		        ObjButtons,ObjButtons[1]);
		    if(PromptResult==0) {
		    	changeMade = false;
		    }
		    if(PromptResult==1) {
		    	notSaved = false;
		    }
		    
			

			}

		}
		}
	}
	
	class MusLyss extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent mev) {
			int x = mev.getX();
			int y = mev.getY();
			Position lastPosition = new Position(x, y);
			nuvarandeKarta.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			nuvarandeKarta.removeMouseListener(musLyss);
			nyPlats(lastPosition);

		}
	}

	class BrickLyss extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent mev) {
			Platser p = (Platser) mev.getComponent();

			if (mev.getButton() == MouseEvent.BUTTON1) {
				if (p.getMarkerad() == false) {
					p.markera();
					markeradePlatser.add(p);
				} else {
					markeradePlatser.remove(p);
					p.avMarkera();
				}

			}

			if (mev.getButton() == MouseEvent.BUTTON3) {
				if (p instanceof NamngivenPlats) {
					JOptionPane.showMessageDialog(Interface.this,
							p.getNamn() + " (" + p.getPosition().getXCor() + " ," + p.getPosition().getYCor() + ")");
				}
				if (p instanceof BeskrivenPlats) {
					BeskrivenPlats b = (BeskrivenPlats) p;
					JOptionPane.showMessageDialog(Interface.this, b.getNamn() + " (" + b.getPosition().getXCor() + " ,"
							+ b.getPosition().getYCor() + ")" + "\n" + b.getBeskrivning());

				}

			}
		}
	}

	class KategoriLyss implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent eve) {

			for (Platser p : markeradePlatser) {
				p.avMarkera();

			}
			markeradePlatser.clear();

			String kategori = kategoriFält.getSelectedValue();
			if (!platsersKategorier.get(kategori).isEmpty()) {
				ArrayList<Platser> pList = platsersKategorier.get(kategori);
				for (Platser p : pList) {
					markeradePlatser.add(p);
					p.synlig();
					p.markera();
				}
			}
		}
	}

	public static void main(String[] args) {
		new Interface();

	}

}
