package qfm;



public class Main {

	public static void main(String[] args) {
		String fileName = "sample.txt";
		//String fileName1 = "t25_q125_80_qmc.txt";	
		MultyReturnType quartetTaxa = Routines.readQuartet(fileName);//this method returns quartet & taxa
		//MultyReturnType quartetTaxa = Routines.readQuartet(args[0]);//this method returns quartet & taxa
		//	args[0] is input file
				Routines.printQuartet(quartetTaxa.getQuartet());
				Routines.printTaxa(quartetTaxa.getTaxa());
				
//				
//		String s= Routines.SQP(quartetTaxa.getQuartet(), quartetTaxa.getTaxa());
//		s = s.replace("(O,", "(0,");
//		s = s.replace(",O,", ",0,");
//		s = s.replace(",O)", ",0)");
//		System.out.println("("+s+")"+";");
//		try(BufferedWriter bw = new BufferedWriter(new FileWriter(args[1]))) {//args[1] is output file
//			bw.write("("+s+")"+";");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
