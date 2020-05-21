package qfm_ad;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class Routines {
	public static String readQuartetC(String fileName) { // count will be done at the time of reading
//		Quartet quartet = new Quartet();
//		Quartet listOfQuartet = quartet;
//		Quartet qtemp;
//		Taxa taxa = new Taxa();
//		Taxa taxa1 = new Taxa(); 
//		Taxa listOfTaxa = taxa;
		//List<Quartet> ql = new ArrayList<>();
		LinkedHashSet<Taxa> tl = new LinkedHashSet<Taxa>();
		HashSet<Quartet> ql = new HashSet<Quartet>();
		double startTime = System.currentTimeMillis();
		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))){
			int count=0;
			while (scanner.hasNext()) {
				
				String singleQuartet = scanner.next();	
				//String[] qq = singleQuartet.split(",|\\|");/// for quartet format q1,q2|q3,q4
				String[] qq = singleQuartet.split(",|\\||:");// For both quartet format q1,q2|q3,q4 and q1,q2|q3,q4:weight
				//Taxa t1, t2, t3, t4;
				Taxa t1 = new Taxa(qq[0]);
				Taxa t2 = new Taxa(qq[1]);
				Taxa t3 = new Taxa(qq[2]);
				Taxa t4 = new Taxa(qq[3]);
				tl.add(t1);
				tl.add(t2);
				tl.add(t3);
				tl.add(t4);
				
				if(ql.contains(new Quartet(t1, t2, t3, t4))) {
					
				}else if (ql.contains(new Quartet(t2, t1, t3, t4))) {
					
				}else if (ql.contains(new Quartet(t1, t2, t4, t3))) {
					
				}else if (ql.contains(new Quartet(t2, t1, t4, t3))) {
					
				}else if (ql.contains(new Quartet(t3, t4, t1, t2))) {
					
				}else if (ql.contains(new Quartet(t3, t4, t2, t1))) {
					
				}else if (ql.contains(new Quartet(t4, t3, t1, t2))) {
					
				}else if (ql.contains(new Quartet(t4, t3, t2, t1))) {
					
				}else {
					ql.add(new Quartet(t1, t2, t3, t4));
					count++;
				}
				
								
				
				
				
			}
			System.out.println("number of unique quartet = "+count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		double estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("Elapsed Time : "+ estimatedTime/1000 + " seconds");
		for (Taxa taxa : tl) {
			System.out.print(taxa.getName()+"->");
		}
		
		//ql.sort(Comparator.comparing(Quartet::getQFrequency).reversed());
		System.out.println();
//		for(Quartet quartets : ql) {
//			System.out.println(quartets.getT1().getName()+","+quartets.getT2().getName()+"|"+quartets.getT3().getName()
//					+","+quartets.getT4().getName()+":"+quartets.getQFrequency());
//		}
		System.out.println("\n elements : "+tl.size());
		
		//return new MultyReturnType(sortQuaret((listOfQuartet)), listOfTaxa);
		//return new MultyReturnType(listOfQuartet, listOfTaxa);
		return null;
		
	}

}
