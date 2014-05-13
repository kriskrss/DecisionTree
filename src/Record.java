import java.util.ArrayList;

public class Record {
	private ArrayList<Character> features = new ArrayList<Character>();
	private Character dependent;

	public ArrayList<Character> getFeatures() {
		return features;
	}

	public void setFeatures(ArrayList<Character> features) {
		this.features = features;
	}

	public Character getDependent() {
		return dependent;
	}

	public void setDependent(Character dependent) {
		this.dependent = dependent;
	}

}
