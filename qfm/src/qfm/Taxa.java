package qfm;



public class Taxa {
	private String name; // node name
    private int partition; //0 = A, 1 = B
    private int state; //0 = unmoved, 1 = moved
    private int taxaScore;
    private int locked;
    Taxa tnext;
    
	public Taxa() {
		this("");
	}

	public Taxa(String name) {
		this(name, -1);
	}
	
	public Taxa(String name, int partition) {
		this(name, partition, 0, 0);
	}

	public Taxa(String name, int partition, int state, int locked) {
		this.name = name;
		this.partition = partition;
		this.state = state;
		this.taxaScore = 0;
		this.locked = locked;
		this.tnext = null;
	}

	public String getName() {
		return name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getLocked() {
		return locked;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setTaxaScore(int taxaScore) {
		this.taxaScore = taxaScore;
	}

	public int getPartition() {
		return partition;
	}
	
	
    

}
