package qfm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		////String fileName1 = "t25_q125_80_qmc.txt";	
		//MultyReturnType quartetTaxa = Routines.readQuartetC(fileName);//this method returns quartet & taxa
		//MultyReturnType quartetTaxa = Routines.readQuartetNewick(args[0]);
		//MultyReturnType quartetTaxa = Routines.readQuartetNewick("out1.txt");
		//MultyReturnType quartetTaxa = Routines.readQuartetCS(args[0]);
		MultyReturnType quartetTaxa = Routines.readQuartetC(args[0]);//this method returns quartet & taxa. I will comment out it
		//	args[0] is input file
//				Routines.printQuartet(quartetTaxa.getQuartet());
//				Routines.printTaxa(quartetTaxa.getTaxa());
//				
//				
		String s= Routines.SQP(quartetTaxa.getQuartet(), quartetTaxa.getTaxa(), 1000, 0);
		//extraTaxa = 1000. Initially, it should be (# of taxa + 1). any number is also fine. And partSatCount = 0
		
		s = s.replace("(O,", "(0,");
		s = s.replace(",O,", ",0,");
		s = s.replace(",O)", ",0)");
		System.out.println("("+s+")"+";");
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(args[1]))) {//args[1] is output file
			bw.write("("+s+")"+";");
		} catch (IOException e) {
			e.printStackTrace();
		}
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("Elapsed Time : "+ estimatedTime/60000 + " minutes");
	}

}
