import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BeskrivenForm extends JPanel{

	private JTextField nameField = new JTextField(10);
	private JTextField beskrivningField = new JTextField(5);

	
	public BeskrivenForm() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel rad1 = new JPanel();
		add(rad1);
		rad1.add(new JLabel("Namn: "));
		rad1.add(nameField);
		JPanel rad2 = new JPanel();
		add(rad2);
		rad2.add(new JLabel("Beskrivning:"));
		rad2.add(beskrivningField);
	}


	public String getNamn() {
		return nameField.getText();
	}
	
	public String getBeskrivning() {
		return beskrivningField.getText();
	}
	
	
}