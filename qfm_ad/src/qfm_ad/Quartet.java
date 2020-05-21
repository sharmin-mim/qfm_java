package qfm_ad;

import java.util.Objects;

public class Quartet {
	private Taxa t1;
	private Taxa t2;
	private Taxa t3;
	private Taxa t4;
	//private String status;
	private int qFrequency;
	public Quartet(Taxa t1, Taxa t2, Taxa t3, Taxa t4) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
		this.t4 = t4;
		this.qFrequency = 1;
	}
	
	public int getQFrequency() {
		return qFrequency;
	}

	public void setQFrequency(int qFrequency) {
		this.qFrequency = qFrequency;
	}
	public void increase_count() {
		System.out.println("increasedf");
		this.qFrequency += 1;
		
	}

	public Taxa getT1() {
		return t1;
	}

	public Taxa getT2() {
		return t2;
	}

	public Taxa getT3() {
		return t3;
	}

	public Taxa getT4() {
		return t4;
	}

	public int getqFrequency() {
		return qFrequency;
	}

	@Override
	public int hashCode() {
		return Objects.hash(t1, t2, t3, t4);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			//increase_count();
			//System.out.println("increased");
			return true;
		}
		if (!(obj instanceof Quartet))
			return false;
		Quartet other = (Quartet) obj;
		if (Objects.equals(t1, other.t1) && Objects.equals(t2, other.t2) && Objects.equals(t3, other.t3)
				&& Objects.equals(t4, other.t4)) {
			//increase_count();
			other.setQFrequency(other.getqFrequency()+1);
			//System.out.println("increasede");
			return true;
		} else {
			return false;

		}
	
	}





	
	

}
