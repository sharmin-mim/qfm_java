package qfm_ad;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Main {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
//		//String fileName = "t25_q125_80_qmc.txt";
//		//String fileName = "27_ecoli.txt";
//		//String fileName = "sample.txt";
//		//String s= Routines.readQuartetQMC("sample300.txt");
		//String s= Routines.readQuartetQMC("27_ecoli.txt");
		String s= Routines.readQuartetQMC(args[0]);
		//String s = Routines.newickQuartetWeightAsFrequency(args[0]);
		System.out.println(s);
		//try(BufferedWriter bw = new BufferedWriter(new FileWriter("gene101Out.txt"))) {//args[1] is output file
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(args[1]))) {//args[1] is output file
			bw.write(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("Total Running Time : "+ estimatedTime/1000 + " seconds");

		
	}

}
