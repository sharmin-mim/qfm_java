package qfm;



public class MultyReturnType {
	private Quartet quartet;
	private Taxa taxa;
	
	public MultyReturnType(Quartet quartet, Taxa taxa) {
		this.quartet = quartet;
		this.taxa = taxa;
	}
	public Quartet getQuartet() {
		return quartet;
	}

	public Taxa getTaxa() {
		return taxa;
	}

}
