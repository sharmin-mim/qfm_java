package qfm;



public class Quartet {
	private String q1;
	private String q2;
	private String q3;
	private String q4;
	private int quartet_id;
	public String status;
	private int qFrequency;
	private int modified;
	Quartet qnext;
	
	public Quartet() {
		this("", "", "", "", -1);
	}

	public Quartet(String q1, String q2, String q3, String q4, int quartet_id) {
		
		this.q1 = q1;
		this.q2 = q2;
		this.q3 = q3;
		this.q4 = q4;
		this.quartet_id = quartet_id;
		this.status = "";
		this.qFrequency = 1;
		this.modified = 0;
		this.qnext = null;
	}
	

	public String getQ1() {
		return q1;
	}

	public String getQ2() {
		return q2;
	}

	public String getQ3() {
		return q3;
	}

	public String getQ4() {
		return q4;
	}

	public int getQuartet_id() {
		return quartet_id;
	}

	public int getQFrequency() {
		return qFrequency;
	}

	public void setQFrequency(int qFrequency) {
		this.qFrequency = qFrequency;
	}
	
	public void increase_count() {
		this.qFrequency += 1;
		
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setModified(int modified) {
		this.modified = modified;
	}

	public void setQ1(String q1) {
		this.q1 = q1;
	}

	public void setQ2(String q2) {
		this.q2 = q2;
	}

	public void setQ3(String q3) {
		this.q3 = q3;
	}

	public void setQ4(String q4) {
		this.q4 = q4;
	}

	public void setQuartet_id(int quartet_id) {
		this.quartet_id = quartet_id;
	}
	

}
