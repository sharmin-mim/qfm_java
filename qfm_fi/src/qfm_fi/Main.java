/*
 * Author: Sharmin Akter Mim
 * Code for QFM-FI
 * */
package qfm_fi;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		String quartetType = "1";
		System.out.println("args.length = "+ args.length);
		 if (args.length >= 3) {
			 quartetType = args[2];
		 }
		String s = "";
		switch (quartetType) {
		case "1":
			System.out.println("Quartet Format: QMC i.e 1,2|3,4");
			s= Routines.readQuartetQMC(args[0]);
			break;
		case "2":
			//eta change korte hobe. non weighted newick quartet er jonno ekta function likhte hobe.
			System.out.println("Quartet Format: Newick i.e ((1,2),(3,4)); Frequency");
			//s = Routines.readNewickQuartet(args[0]);
			s = Routines.newickQuartetWeightAsFrequency(args[0]);
			break;
		case "3":
			System.out.println("Quartet Format: QMC, Quartet Generation Method: SVDquartet(qweight = none) i.e 1,2|3,4:1");
			s = Routines.readSVDquartet(args[0]);
			break;
		case "4":
			System.out.println("Quartet Format: Newick i.e ((1,2),(3,4)); Frequency");
			s = Routines.newickQuartetWeightAsFrequency(args[0]);
			break;
		default:
			s= Routines.readQuartetQMC(args[0]);
			break;
		}

		
		//String s= Routines.readQuartetQMC("r1_qrt_n500_c70_qf2.qrt");
		//String s= Routines.readQuartetQMC(args[0]);
		//String s = Routines.newickQuartetWeightAsFrequency(args[0]);
		
//		String s= Routines.readQuartetQMC("27_ecoli.txt");
//		System.out.println(s);
//		try(BufferedWriter bw = new BufferedWriter(new FileWriter("gene101Out.txt"))) {//args[1] is output file
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(args[1]))) {//args[1] is output file
			bw.write(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("Total Running Time : "+ estimatedTime/1000 + " seconds");

		
	}

}
