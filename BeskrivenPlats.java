
public class BeskrivenPlats extends Platser {

	String beskrivning; 
	
	public BeskrivenPlats(String namn, String kategori, Position p, String beskrivning) {
		super(namn, kategori, p);
		this.beskrivning = beskrivning;
	}

	public String getBeskrivning() {
		return beskrivning;
	}
	
}
