package qfm_ad;



public class Taxa {
	private String name; // node name
	private int partition; //0 = A, 1 = B
    //private int state; //0 = unmoved, 1 = moved
    //private int taxaScore;
    private boolean locked;
    //Taxa tnext;

	public Taxa(String name) {
		this(name, -1);
	}
	
	 public Taxa(String name, int partition) {
		 this(name, partition, false);
	}
	
	 
	public Taxa(String name, int partition, boolean locked) {
		this.name = name;
		this.partition = partition;
		this.locked = locked;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public int getPartition() {
		return partition;
	}

	public void setPartition(int partition) {
		this.partition = partition;
	}
	

//	public int getState() {
//		return state;
//	}
//
//	public void setState(int state) {
//		this.state = state;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Taxa))
			return false;
		Taxa other = (Taxa) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	




	




	

	
	
	 
	
    

    


}
