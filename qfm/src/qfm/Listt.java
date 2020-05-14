package qfm;



public class Listt {
	private String taxaToMove;
	private int val;		
	private int sat;
	private int vat;
	private int def;
	private int bel0w;
	private int part; // part means partition. part = 0 means partA; part = 1 means partB;
	Listt next;
	Listt prev;
	
	public Listt() {
		this("", -1, -1, -1, -1, 0, -1);
	}
	

	public Listt(String taxaToMove, int val, int part) {
		this(taxaToMove, val, -1, -1, -1, 0, part);
	}


	public Listt(String taxaToMove, int val, int sat, int vat, int def, int bel0w, int part) {
		super();
		this.taxaToMove = taxaToMove;
		this.val = val;
		this.sat = sat;
		this.vat = vat;
		this.def = def;
		this.bel0w = bel0w;
		this.part = part;
		this.next = null;
		this.prev = null;
	}

	public String getTaxaToMove() {
		return taxaToMove;
	}

	public int getVal() {
		return val;
	}

	public int getSat() {
		return sat;
	}

	public int getVat() {
		return vat;
	}

	public int getDef() {
		return def;
	}

	public int getBel0w() {
		return bel0w;
	}

	public int getPart() {
		return part;
	}

	

}
