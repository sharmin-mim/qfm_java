package qfm_ad;

import java.util.Objects;

public class Quartet {
	private Taxa t1;
	private Taxa t2;
	private Taxa t3;
	private Taxa t4;
	private String status;
	private int qFrequency;
	private boolean increaseFrequency;
	public Quartet(Taxa t1, Taxa t2, Taxa t3, Taxa t4) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
		this.t4 = t4;
		this.qFrequency = 1;
		this.increaseFrequency = true;
		this.status = "";
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
	
	public void setT1(Taxa t1) {
		this.t1 = t1;
	}
	public void setT2(Taxa t2) {
		this.t2 = t2;
	}
	public void setT3(Taxa t3) {
		this.t3 = t3;
	}
	public void setT4(Taxa t4) {
		this.t4 = t4;
	}
	public int getQFrequency() {
		return qFrequency;
	}

	public void setQFrequency(int qFrequency) {
		this.qFrequency = qFrequency;
	}
	

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isIncreaseFrequency() {
		return increaseFrequency;
	}
	public void setIncreaseFrequency(boolean increaseFrequency) {
		this.increaseFrequency = increaseFrequency;
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
			if (other.isIncreaseFrequency()) {
				other.setQFrequency(other.getQFrequency()+1);
			}
			return true;
		} else {
			return false;

		}
	
	}





	
	

}
