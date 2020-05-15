package qfm;



public class MultyReturnType {
	private Quartet quartet;
	private Taxa taxa;
	private int anyCount;
	
	public MultyReturnType(Quartet quartet, Taxa taxa) {
		this(quartet, taxa, 0);
	}
	
	public MultyReturnType(Taxa taxa, int anyCount) {
		this(null, taxa, anyCount);
	}
	
	public MultyReturnType(Quartet quartet, Taxa taxa, int anyCount) {
		this.quartet = quartet;
		this.taxa = taxa;
		this.anyCount = anyCount;
	}

	public Quartet getQuartet() {
		return quartet;
	}

	public Taxa getTaxa() {
		return taxa;
	}

	public int getAnyCount() {
		return anyCount;
	}


	

}
