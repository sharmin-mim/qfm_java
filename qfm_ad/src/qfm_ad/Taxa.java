package qfm_ad;

import java.util.Objects;

public class Taxa {
	private String name; // node name
	private int partition; //0 = A, 1 = B
    //private int state; //0 = unmoved, 1 = moved
    //private int taxaScore;
    //private int locked;
    //Taxa tnext;

	public Taxa(String name) {
		this(name, -1);
	}
	
	 public Taxa(String name, int partition) {
			this.name = name;
			this.partition = partition;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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
