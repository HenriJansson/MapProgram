import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class KoordinatForm extends JPanel{

	private JTextField xKorField = new JTextField(5);
	private JTextField yKorField = new JTextField(5);

	
	public KoordinatForm() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel rad1 = new JPanel();
		add(rad1);
		rad1.add(new JLabel("X Koordinat:"));
		rad1.add(xKorField);
		JPanel rad2 = new JPanel();
		add(rad2);
		rad2.add(new JLabel("Y Koordinat:"));
		rad2.add(yKorField);
	}


	public int getXCor() {
		return Integer.parseInt(xKorField.getText());
	}
	
	public int getYCor() {
		return Integer.parseInt(yKorField.getText());
	}
	
	
}