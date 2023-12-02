/*
 * Author: Sharmin Akter Mim
 * Code for QFM-FI
 * */
package qfm_fi;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import phylonet.tree.io.ParseException;
import phylonet.tree.model.sti.STITree;




public class Routines {

    public static String readQuartetQMC(String fileName) { // count will be done at the time of reading

        Map<String, Taxa> taxList = new HashMap<String, Taxa>();
        LinkedHashSet<Quartet> quartetList = new LinkedHashSet<Quartet>(251100000);

        long startTime = System.currentTimeMillis();
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))) {
            int qc = 0;
            while (scanner.hasNext()) {

                String singleQuartet = scanner.next();
                String[] qq = singleQuartet.split(",|\\||:");// For both quartet format q1,q2|q3,q4 and q1,q2|q3,q4:weight

                Taxa[] t = new Taxa[4];
                for (int i = 0; i < 4; i++) {
                    if (taxList.containsKey(qq[i])) {
                        t[i] = taxList.get(qq[i]);
                    } else {
                        t[i] = new Taxa(qq[i]);
                        taxList.put(qq[i], t[i]);
                    }
                }

//                if (quartetList.contains(new Quartet(t[1], t[0], t[2], t[3]))) {
//                    qc++;
//                } else {
                    quartetList.add(new Quartet(t[0], t[1], t[2], t[3]));
                    qc++;
//                }
            }
            System.out.println("number of quartet = " + qc);
            System.out.println("number of unique quartet = " + quartetList.size());
            System.out.println("number of Taxa = " + taxList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Quartet Reading Time : " + estimatedTime / 1000 + " seconds");
        ArrayList<Quartet> qr = new ArrayList<Quartet>(quartetList.size());
        qr.addAll(quartetList);

        quartetList.clear();
        qr.sort(Comparator.comparing(Quartet::getQFrequency, Collections.reverseOrder()));
        long sortingTime = System.currentTimeMillis() - startTime - estimatedTime;
        System.out.println("Sorting Time : " + sortingTime / 1000 + " seconds");

        LinkedHashSet<Taxa> taxaList = new LinkedHashSet<Taxa>(taxList.values());
        taxList.clear();
        String s = SQP(qr, taxaList, 1000, 0);

        if (s == null) {
            s = "null";
        }

        return s;
    }

    public static String newickQuartetWeightAsFrequency(String fileName) {
        //Quartet Format: ((a,b),(c,d)); Frequency
        // If frequency is already counted or weight represents frequency 
        //and quartet is in newick format i.e ((a,b),(c,d)); Frequency
        //Then use this function.

        ArrayList<Quartet> qr = new ArrayList<Quartet>();
        Map<String, Taxa> taxList = new HashMap<String, Taxa>();

        long startTime = System.currentTimeMillis();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String loc;// int qc = 0;
            while ((loc = br.readLine()) != null) {
                if (loc.equals("")) {
                    //remove extra new line/white space
                    continue;
                }
                //System.out.println(loc);
                String[] qq = loc.split("\\(\\(|,|\\),\\(|\\)\\)|; ");

                int frequency = Integer.parseInt(qq[6]); //for qfm, frequency means number of quartet. Frequency must be integer
                Taxa[] t = new Taxa[4];

                for (int i = 0; i < 4; i++) {
                    int newIndex = i + 1;
                    if (taxList.containsKey(qq[newIndex])) {
                        t[i] = taxList.get(qq[newIndex]);
                    } else {
                        t[i] = new Taxa(qq[newIndex]);
                        taxList.put(qq[newIndex], t[i]);
                    }
                }
                qr.add(new Quartet(t[0], t[1], t[2], t[3], frequency));
            }

            System.out.println("number of quartet = " + qr.size());
            System.out.println("number of Taxa = " + taxList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Quartet Reading Time : " + estimatedTime / 1000 + " seconds");
        qr.sort(Comparator.comparing(Quartet::getQFrequency, Collections.reverseOrder()));
        long sortingTime = System.currentTimeMillis() - startTime - estimatedTime;
        System.out.println("Sorting Time : " + sortingTime / 1000 + " seconds");

        LinkedHashSet<Taxa> taxaList = new LinkedHashSet<Taxa>(taxList.values());
        taxList.clear();
        String s = SQP(qr, taxaList, 1000, 0);

        if (s == null) {
            s = "null";
        }

        return s;
    }

    public static String readSVDquartet(String fileName) {

        Map<String, Taxa> taxList = new HashMap<String, Taxa>();
        ArrayList<Quartet> qr = new ArrayList<Quartet>();
        long startTime = System.currentTimeMillis();
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))) {
            while (scanner.hasNext()) {

                String singleQuartet = scanner.next();
                String[] qq = singleQuartet.split(",|\\||:");// For both quartet format q1,q2|q3,q4 and q1,q2|q3,q4:weight

                Taxa[] t = new Taxa[4];
                for (int i = 0; i < 4; i++) {

                    if (taxList.containsKey(qq[i])) {
                        t[i] = taxList.get(qq[i]);
                    } else {
                        t[i] = new Taxa(qq[i]);
                        taxList.put(qq[i], t[i]);
                    }

                }
                qr.add(new Quartet(t[0], t[1], t[2], t[3]));

            }
            System.out.println("number of unique quartet = " + qr.size());
            System.out.println("number of Taxa = " + taxList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Quartet Reading Time : " + estimatedTime / 1000 + " seconds");
        LinkedHashSet<Taxa> taxaList = new LinkedHashSet<Taxa>(taxList.values());
        taxList.clear();
        String s = SQP(qr, taxaList, 1000, 0);
        if (s == null) {
            s = "null";
        }

        return s;

    }

    public static String SQP(ArrayList<Quartet> quartetMap, LinkedHashSet<Taxa> taxaList, int extraTaxa, int partSatCount) {

        int taxacount = taxaList.size();
        String s, extra = "extra";

        if (taxacount == 0) {
            s = ("");

            return s;
        }
        if (quartetMap.isEmpty() || taxacount < 4) {
            s = depthOneTree(taxaList);
        } else {
            MultiReturnType mrt = FM(taxaList, quartetMap);
            LinkedHashSet<Taxa> partA = mrt.getPartA();
            LinkedHashSet<Taxa> partB = mrt.getPartB();

            extra = "extra" + extraTaxa;
            extraTaxa++;
            for (Taxa taxa : partA) {

                taxa.relaventQIDs.clear();
            }
            for (Taxa taxa : partB) {
                taxa.relaventQIDs.clear();
            }
            Taxa extraA = new Taxa(extra, 0);
            Taxa extraB = new Taxa(extra, 1);
            partA.add(extraA);// add extra taxa to partition A
            partB.add(extraB);// add extra taxa to parttion B

            LinkedHashSet<Quartet> quartetSetA = new LinkedHashSet<Quartet>();
            LinkedHashSet<Quartet> quartetSetB = new LinkedHashSet<Quartet>();

            for (Quartet q : quartetMap) {
                byte c = q.status;

                if (c == 4 || c == 3) {
                    if (c == 4) {

                        if (q.t1.getPartition() == 0) {
                            quartetSetA.add(q);

                        } else {
                            quartetSetB.add(q);
                        }
                    } else {
                        int dcount = q.t1.getPartition() + q.t2.getPartition() + q.t3.getPartition();

                        if (dcount > 1) {
                            if (q.t1.getPartition() == 0) {
                                q.t1 = extraB;
                            } else if (q.t2.getPartition() == 0) {
                                q.t2 = extraB;
                            } else if (q.t3.getPartition() == 0) {
                                q.t3 = extraB;
                            } else {
                                q.t4 = extraB;
                            }

//                            if (quartetSetB.contains(new Quartet(q.t2, q.t1, q.t3, q.t4, q.getQFrequency()))) {
//
//                            } else if (quartetSetB.contains(new Quartet(q.t1, q.t2, q.t4, q.t3, q.getQFrequency()))) {
//
//                            } else if (quartetSetB.contains(new Quartet(q.t2, q.t1, q.t4, q.t3, q.getQFrequency()))) {
//
//                            } else if (quartetSetB.contains(new Quartet(q.t3, q.t4, q.t1, q.t2, q.getQFrequency()))) {
//
//                            } else if (quartetSetB.contains(new Quartet(q.t3, q.t4, q.t2, q.t1, q.getQFrequency()))) {
//
//                            } else if (quartetSetB.contains(new Quartet(q.t4, q.t3, q.t1, q.t2, q.getQFrequency()))) {
//
//                            } else if (quartetSetB.contains(new Quartet(q.t4, q.t3, q.t2, q.t1, q.getQFrequency()))) {
//
//                            } else {
                                quartetSetB.add(q);

//                            }

                        } else {
                            if (q.t1.getPartition() == 1) {
                                q.t1 = extraA;
                            } else if (q.t2.getPartition() == 1) {
                                q.t2 = extraA;
                            } else if (q.t3.getPartition() == 1) {
                                q.t3 = extraA;
                            } else {
                                q.t4 = extraA;
                            }

//                            if (quartetSetA.contains(new Quartet(q.t2, q.t1, q.t3, q.t4, q.getQFrequency()))) {
//                                
//                            } else if (quartetSetA.contains(new Quartet(q.t1, q.t2, q.t4, q.t3, q.getQFrequency()))) {
//
//                            } else if (quartetSetA.contains(new Quartet(q.t2, q.t1, q.t4, q.t3, q.getQFrequency()))) {
//
//                            } else if (quartetSetA.contains(new Quartet(q.t3, q.t4, q.t1, q.t2, q.getQFrequency()))) {
//
//                            } else if (quartetSetA.contains(new Quartet(q.t3, q.t4, q.t2, q.t1, q.getQFrequency()))) {
//
//                            } else if (quartetSetA.contains(new Quartet(q.t4, q.t3, q.t1, q.t2, q.getQFrequency()))) {
//
//                            } else if (quartetSetA.contains(new Quartet(q.t4, q.t3, q.t2, q.t1, q.getQFrequency()))) {
//
//                            } else {
                                quartetSetA.add(q);

//                            }
                        }

                    }
                }
            }
            quartetMap.clear();

            ArrayList<Quartet> qrA = new ArrayList<Quartet>(quartetSetA);
            quartetSetA.clear();
            qrA.sort(Comparator.comparing(Quartet::getQFrequency, Collections.reverseOrder()));

            ArrayList<Quartet> qrB = new ArrayList<Quartet>(quartetSetB);
            quartetSetB.clear();
            qrB.sort(Comparator.comparing(Quartet::getQFrequency, Collections.reverseOrder()));

            if (extraTaxa == 1001) {
                final int ext = extraTaxa, psc = partSatCount;
                CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> SQP(qrA, partA, ext, psc));
                String s2 = SQP(qrB, partB, extraTaxa, partSatCount);
                String s1 = null;
                try {
                    s1 = cf.get();
                } catch (InterruptedException e) {
                    
                } catch (ExecutionException e) {
                    
                }

                s = mergeUsingJAR(s1, s2, extra);
            } else {
                String s1 = SQP(qrA, partA, extraTaxa, partSatCount);
                String s2 = SQP(qrB, partB, extraTaxa, partSatCount);
                s = mergeUsingJAR(s1, s2, extra);

            }
        }

        return s;
    }

    private static String merge(String s1, String s2, String extra) {

        s1 = reroot(s1, extra);
        s2 = reroot(s2, extra);
        if (s1 != null && s2 != null) {
            return s1 + "," + s2;
        } else if (s1 == null && s2 != null) {
            return s2;
        } else if (s1 != null && s2 == null) {
            return s1;
        } else {
            return null;
        }
    }

    private static String reroot(String s, String extra) {

        boolean brkt;
        brkt = bracket(s);
        if (!brkt) {
            s = "(" + s + ")";

        }
        //perl script consider 0 as false. so we have to replace zero
        s = s.replace("(0,", "(O,");
        s = s.replace(",0,", ",O,");
        s = s.replace(",0)", ",O)");
        String fileName = "qfm11011.tmp";
        String ss = null;

        synchronized (fileName) {
            try {
                String cmd = "perl reroot_tree_new.pl -t " + s + ";" + " -r " + extra + " -o " + fileName;
                Process p = Runtime.getRuntime().exec(cmd);
                p.waitFor();
                p.destroy();
            } catch (Exception e) {

                e.printStackTrace();
                System.out.println("Problem in cmd");
            }
            //reading from temp file

            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                ss = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (ss != null) {
            ss = ss.replace(":", "");
            ss = ss.replace(";", "");
            int pos1 = ss.indexOf(extra);
            ss = ss.replace(extra, "");

            int start = 0, i = 0, ob = 0, end = 0, cb = 0;
            String mystring;
            boolean removebr = false;
            if (ss.charAt(pos1 - 1) == '(' && ss.charAt(pos1) == ',') {

                try {

                    ss = ss.substring(0, pos1) + ss.substring(pos1 + 1);
                    start = pos1 - 1;

                    i = start;
                    ob = 0;
                    while (true) {
                        if (ss.charAt(i) == '(') {
                            ob++;
                        } else if (ss.charAt(i) == ')') {
                            ob--;
                            if (ob == 0) {
                                end = i;
                                break;
                            }

                        }
                        i++;
                    }
                } catch (Exception e) {

                }
                mystring = ss.substring(start, end + 1);
                removebr = balance(mystring);
                if (removebr) {
                    try {

                        ss = ss.substring(0, end) + ss.substring(end + 1);
                        ss = ss.substring(0, start) + ss.substring(start + 1);

                    } catch (Exception e) {

                    }

                }

            } else if (ss.charAt(pos1 - 1) == ',' && ss.charAt(pos1) == ')') {

                try {
                    ss = ss.substring(0, pos1 - 1) + ss.substring(pos1);

                    end = pos1 - 1;
                    i = end;
                    cb = 0;
                    while (true) {
                        if (ss.charAt(i) == ')') {
                            cb++;
                        } else if (ss.charAt(i) == '(') {
                            cb--;
                            if (cb == 0) {
                                start = i;
                                break;
                            }
                        }
                        i--;
                    }
                } catch (Exception e) {

                }
                mystring = ss.substring(start, end + 1);
                removebr = balance(mystring);
                if (removebr) {
                    try {

                        ss = ss.substring(0, end) + ss.substring(end + 1);
                        ss = ss.substring(0, start) + ss.substring(start + 1);

                    } catch (Exception e) {

                    }

                }

            } else if (ss.charAt(pos1 - 1) == ',' && ss.charAt(pos1) == ',') {

                try {
                    ss = ss.substring(0, pos1 - 1) + ss.substring(pos1);
                } catch (Exception e) {
                }
                if (ss.charAt(pos1 - 2) == '(' && ss.charAt(pos1) == ')') {
                    try {
                        ss = ss.substring(0, pos1) + ss.substring(pos1 + 1);
                        ss = ss.substring(0, pos1 - 2) + ss.substring(pos1 - 1);
                    } catch (Exception e) {
                    }
                }

            }
        }

        return ss;
    }

    private static boolean balance(String s) {

        int i, l, ob = 0, com = 0;
        l = s.length();

        for (i = 1; i < l - 1; i++) {

            if (s.charAt(i) == '(') {
                ob++;
            } else if (s.charAt(i) == ')') {
                ob--;
            } else if (s.charAt(i) == ',') {
                com = 1;
            }
            if (ob < 0) {
                return false; // don't remove bracket;
            }
        }
        if ((s.charAt(1) == '(' && s.charAt(l - 2) == ')' && ob == 0) || com == 0) {
            return true; //remove unnecessary bracket
        } else {
            return false;
        }

    }

    private static boolean bracket(String s) {
        int l;
        l = s.length();
        if (s.charAt(0) != '(') {
            return false;
        }
        if (s.charAt(l - 1) != ')') {
            return false;
        }

        for (int i = 1; i < l - 1; i++) {
            if (s.charAt(i) == '(' || s.charAt(i) == ')') {
                return false;
            }
        }
        return true;

    }

    private static MultiReturnType FM(LinkedHashSet<Taxa> taxaList, ArrayList<Quartet> quartetMap) {
        LinkedHashSet<Taxa> partA = new LinkedHashSet<Taxa>();
        LinkedHashSet<Taxa> partB = new LinkedHashSet<Taxa>();

        for (Quartet quartet : quartetMap) {
            int tcount = 0;
            if (taxaList.isEmpty()) {
                break;
            } else {
                if (taxaList.contains(quartet.t1)) {
                    taxaList.remove(quartet.t1);
                    tcount++;
                }
                if (taxaList.contains(quartet.t2)) {
                    taxaList.remove(quartet.t2);
                    tcount++;
                }
                if (taxaList.contains(quartet.t3)) {
                    taxaList.remove(quartet.t3);
                    tcount++;
                }
                if (taxaList.contains(quartet.t4)) {
                    taxaList.remove(quartet.t4);
                    tcount++;
                }
            }
            if (tcount > 0) {
                int a = -1, b = -1, c = -1, d = -1;
                if (partA.contains(quartet.t1)) {
                    a = 0;
                } else if (partB.contains(quartet.t1)) {
                    a = 1;
                }

                if (partA.contains(quartet.t2)) {
                    b = 0;
                } else if (partB.contains(quartet.t2)) {
                    b = 1;
                }

                if (partA.contains(quartet.t3)) {
                    c = 0;
                } else if (partB.contains(quartet.t3)) {
                    c = 1;
                }

                if (partA.contains(quartet.t4)) {
                    d = 0;
                } else if (partB.contains(quartet.t4)) {
                    d = 1;
                }

                if (a == -1 && b == -1 && c == -1 && d == -1) //all new
                {
                    ///This section can create problem in multithreading
                    quartet.t1.setPartition(0);
                    partA.add(quartet.t1);
                    quartet.t2.setPartition(0);
                    partA.add(quartet.t2);
                    quartet.t3.setPartition(1);
                    partB.add(quartet.t3);
                    quartet.t4.setPartition(1);
                    partB.add(quartet.t4);
                } else {
                    if (a == -1) {
                        if (b != -1) {
                            if (b == 0) {
                                quartet.t1.setPartition(0);
                                partA.add(quartet.t1);
                                a = 0;// ca++;
                            } else {
                                quartet.t1.setPartition(1);
                                partB.add(quartet.t1);
                                a = 1; //cb++;
                            }
                        } else if (c != -1) {
                            if (c == 1) {
                                quartet.t1.setPartition(0);
                                partA.add(quartet.t1);
                                a = 0;// ca++;
                                quartet.t2.setPartition(0);
                                partA.add(quartet.t2);
                                b = 0;
                            } else {
                                quartet.t1.setPartition(1);
                                partB.add(quartet.t1);
                                a = 1;// cb++;
                                quartet.t2.setPartition(1);
                                partB.add(quartet.t2);
                                b = 1;
                            }
                        } else if (d != -1) {
                            if (d == 1) {
                                quartet.t1.setPartition(0);
                                partA.add(quartet.t1);
                                a = 0; //ca++;
                                quartet.t2.setPartition(0);
                                partA.add(quartet.t2);
                                b = 0;
                            } else {
                                quartet.t1.setPartition(1);
                                partB.add(quartet.t1);
                                a = 1;// cb++;
                                quartet.t2.setPartition(1);
                                partB.add(quartet.t2);
                                b = 1;
                            }
                        }

                    }
                    if (b == -1) {
                        if (a == 0) {
                            quartet.t2.setPartition(0);
                            partA.add(quartet.t2);// b=0; 
                        } else {
                            quartet.t2.setPartition(1);
                            partB.add(quartet.t2); //b=1; 
                        }
                    }
                    if (c == -1) {
                        if (d != -1) {
                            if (d == 0) {
                                quartet.t3.setPartition(0);
                                partA.add(quartet.t3); //c=0; 
                            } else {
                                quartet.t3.setPartition(1);
                                partB.add(quartet.t3); //c=1; 
                            }
                        } else {
                            if (a == 1) {
                                quartet.t3.setPartition(0);
                                partA.add(quartet.t3);// c=0;
                                quartet.t4.setPartition(0);
                                partA.add(quartet.t4);
                                d = 0;
                            } else {
                                quartet.t3.setPartition(1);
                                partB.add(quartet.t3);// c=1;
                                quartet.t4.setPartition(1);
                                partB.add(quartet.t4);
                                d = 1;
                            }
                        }
                    }
                    if (d == -1) {
                        if (c == 0) {
                            quartet.t4.setPartition(0);
                            partA.add(quartet.t4); //ca++;
                        } else {
                            quartet.t4.setPartition(1);
                            partB.add(quartet.t4); //cb++;
                        }
                    }

                }
            }

        }
        int c = 0;
        int ca = partA.size(), cb = partB.size();
        if (!taxaList.isEmpty()) {

            for (Taxa taxa : taxaList) {
                if (ca < cb) {
                    c = 2;
                } else if (cb < ca) {
                    c = 1;
                } else {
                    c++;
                }

                if (c % 2 == 0) {
                    taxa.setPartition(0);
                    partA.add(taxa);
                    ca++;
                } else {
                    taxa.setPartition(1);
                    partB.add(taxa);
                    cb++;
                }

            }
        }

        return MFM_algo(partA, partB, quartetMap);
    }

    private static int countSatisfiedQuartets(ArrayList<Quartet> quartetMap) {
        int[] sat = {0};
        quartetMap.parallelStream().forEach(quartet -> {
            if (quartet.status == 2) {

                sat[0] += quartet.getQFrequency();

            }
        });

        return sat[0];
    }

    private static void printQuartet(LinkedHashSet<Quartet> quartetList) {
        System.out.println("**********************Quartet List********************************");
        for (Quartet quartets : quartetList) {

            System.out.println("quartet" + " : " + quartets.t1.name + "," + quartets.t2.name + "|"
                    + quartets.t3.name + "," + quartets.t4.name + ":" + quartets.getQFrequency() + "->"
                    + " -> " + quartets.status + "\n");
        }
        System.out.println("*********************End of Printing Quartet**********************");

    }

    private static void printTaxa(LinkedHashSet<Taxa> taxaList) {
        System.out.println("**********************Taxa List********************************");
        for (Taxa taxa : taxaList) {
            System.out.print(taxa.name + "->");
        }
        System.out.println("end");

    }

    private static String depthOneTree(LinkedHashSet<Taxa> taxaList) {
        String s = "(";
        int taxaCount = taxaList.size(), count = 0;
        for (Taxa taxa : taxaList) {
            s += taxa.name;
            count++;
            if (count == taxaCount) {
                s += ")";
            } else {
                s += ",";
            }
        }

        return s;
    }
    /////////////Modifying Gain//////////////////

    private static MultiReturnType MFM_algo(LinkedHashSet<Taxa> partA, LinkedHashSet<Taxa> partB,
            ArrayList<Quartet> quartetMap) {
        //String taxaToMove = null;
        boolean loopAgain = true;
        for (int quartetID = 0; quartetID < quartetMap.size(); quartetID++) {
            Quartet q = quartetMap.get(quartetID);
            q.initiallyFillUpRelaventQIDs(quartetID);
        }

        while (loopAgain) {
            boolean iterationMore = true;
            int iteration = 0;
            int ca = partA.size();
            int cb = partB.size();
            // at the begining of this loop(loopAgain) ca and cb are size of partA and part. But later it is hypothetically true.
            // because I only remove taxa from these partitions but I dont add them in opposite partition. I add the removed taxa in movedList

            int arraysize = ca + cb;
            List<GainList> movedList;
            if (arraysize > 0) {
                movedList = new ArrayList<GainList>(arraysize);
            } else {
                movedList = new ArrayList<GainList>();
            }

            int prevScore = 0, prevS = 0, prevV = 0;//, prevD = 0;

            while (iterationMore) {
                iteration++;
                int[] score;
                if (iteration == 1) {
                    score = iCalculateScore(quartetMap);
                    prevScore = score[0];//partition score
                    prevS = score[1];//noOfSat
                    prevV = score[2];//noOfVat
                }

                //////////////////parrallel stream///////////////////
                final int prevSF = prevS;
                final int prevVF = prevV;
                final int prevScoreF = prevScore;
                partA.parallelStream().forEach(taxa -> {

                    taxa.mCalculateScore(prevSF, prevVF, prevScoreF);
                });
                partB.parallelStream().forEach(taxa -> {
                    taxa.mCalculateScore(prevSF, prevVF, prevScoreF);
                });
                int partitionIndex = 0;
                for (Taxa taxa : partA) {
                    partitionIndex++;
                    taxa.partitionIndex = partitionIndex;
                }
                partitionIndex = 0;
                for (Taxa taxa : partB) {
                    partitionIndex++;
                    taxa.partitionIndex = partitionIndex;
                }

                Taxa taxa_to_move = new Taxa("", -1);

                Taxa maxGainTaxaPartA = null;
                if (!partA.isEmpty() && ca > 2) {
                    maxGainTaxaPartA = partA.stream().max(Comparator.comparing(Taxa::getVal).thenComparing(Taxa::getSat)).get();
                }
                // adding below code after calculating gain of all taxa of partB
                Taxa maxGainTaxaPartB = null;
                if (!partB.isEmpty() && cb > 2) {
                    maxGainTaxaPartB = partB.stream().max(Comparator.comparing(Taxa::getVal).thenComparing(Taxa::getSat)).get();
                }

                if (maxGainTaxaPartA != null && maxGainTaxaPartB != null) {
                    if (maxGainTaxaPartA.getVal() > maxGainTaxaPartB.getVal()) {
                        taxa_to_move = maxGainTaxaPartA;
                    } else if (maxGainTaxaPartA.getVal() < maxGainTaxaPartB.getVal()) {
                        taxa_to_move = maxGainTaxaPartB;
                    } else { //in that case maxGainTaxaPartA.getVal() == maxGainTaxaPartB.getVal()
                        if (maxGainTaxaPartA.getSat() > maxGainTaxaPartB.getSat()) {
                            taxa_to_move = maxGainTaxaPartA;
                        } else if (maxGainTaxaPartA.getSat() < maxGainTaxaPartB.getSat()) {
                            taxa_to_move = maxGainTaxaPartB;
                        } else {//in that case (maxGainTaxaPartA.getSat() == maxGainTaxaPartB.getSat()) {
                            if (maxGainTaxaPartA.partitionIndex <= maxGainTaxaPartB.partitionIndex) {
                                taxa_to_move = maxGainTaxaPartA;
                            } else {
                                taxa_to_move = maxGainTaxaPartB;
                            }
                        }
                    }

                } else if (maxGainTaxaPartA != null) {
                    taxa_to_move = maxGainTaxaPartA;
                } else if (maxGainTaxaPartB != null) {
                    taxa_to_move = maxGainTaxaPartB;
                }

                prevS = taxa_to_move.getSat();//score[1];//noOfSat
                prevV = taxa_to_move.getVat();//score[2];//noOfVat
                // prevD = taxa_to_move.getDef();//score[3];//noOfDef
                ////////////////
                int glPart = taxa_to_move.getPartition();
                prevScore = prevS - prevV;//partition score
                if (glPart != -1) {
                    taxa_to_move.locked = true;
//					System.out.println("Moved Taxa = "+taxa_to_move.name+" maxgain = "+taxa_to_move.getVal()+" svdTablesize = "+
//							taxa_to_move.relaventQIDs.size());
                    if (glPart == 1) {
                        taxa_to_move.setPartition(0);
                        partB.remove(taxa_to_move);
                        cb--;
                        ca++;
                        //partA.add(taxa_to_move);
                    } else {
                        taxa_to_move.setPartition(1);
                        partA.remove(taxa_to_move);
                        ca--;
                        cb++;
                        //partB.add(taxa_to_move);
                    }
                    //taxa_to_move.setLocked(true);
//					t.setLocked(true);
                    //movedList.add(new GainList(taxa_to_move, maxgain));
                    movedList.add(new GainList(taxa_to_move, taxa_to_move.getVal()));
                    ////////////Extracting quartets which have moved_taxa

                    //rQuartetList.clear();
                    ////////////////Parallel Stream//////////////
                    final Taxa final_taxa_to_move = taxa_to_move;
                    taxa_to_move.relaventQIDs.parallelStream().forEach(qid -> {
                        Quartet q = quartetMap.get(qid);
                        q.updateSatVatCalculation();
                    });
                } else {
                    iterationMore = false;
                }

            }///no more iteration

            int cumulativeGain = 0, gainMax = 0;
            int backIndex = -1, bi = 0;
            boolean isMove = false;
            for (GainList ml : movedList) {
                cumulativeGain += ml.val;
                if (cumulativeGain >= gainMax) {
                    gainMax = cumulativeGain;
                    backIndex = bi;
                    isMove = true;
                }
                bi++;
            }

            if (isMove) {
                for (int i = backIndex + 1; i < movedList.size(); i++) {
                    Taxa moveTaxa = movedList.get(i).taxa;
                    moveTaxa.resetTaxa();

                    if (moveTaxa.getPartition() == 1) {
                        moveTaxa.setPartition(0);
                        partA.add(moveTaxa);                      
                    } else {
                        moveTaxa.setPartition(1);
                        partB.add(moveTaxa);
                    }

                }
                for (int i = backIndex; i >= 0; i--) {
                    Taxa moveTaxa = movedList.get(i).taxa;
                    moveTaxa.resetTaxa();

                    if (moveTaxa.getPartition() == 1) {
                        partB.add(moveTaxa);
                    } else {
                        partA.add(moveTaxa);
                    }

                }
            }

            if (gainMax <= 0) {
                loopAgain = false;
            }

        }//no more loop

        return new MultiReturnType(partA, partB);
    }

    private static int getChunkStartInclusive(final int chunk,
            final int chunkSize) {
        return chunk * chunkSize;
    }

    private static int getChunkEndExclusive(final int chunk, final int chunkSize,
            final int nElements) {
        final int end = (chunk + 1) * chunkSize;
        if (end > nElements) {
            return nElements;
        } else {
            return end;
        }
    }

    private static int[] iCalculateScore(ArrayList<Quartet> quartetMap) {
        //initial_calculate_score
        int[] scores = {0, 0, 0, 0};
        final int nElements = quartetMap.size();
        if (nElements > 5_00_000) {
            final int nChunks = 4;
            final int chunkSize = (nElements + nChunks - 1) / nChunks;
            List<Integer> chunk = new ArrayList<Integer>();
            chunk.add(0);
            chunk.add(1);
            chunk.add(2);
            chunk.add(3);
            chunk.parallelStream().forEach(chunkID -> {
                for (int qid = getChunkStartInclusive(chunkID, chunkSize); qid < getChunkEndExclusive(chunkID, chunkSize, nElements); qid++) {
                    Quartet q = quartetMap.get(qid);
                    q.initialSatVatCalculation();
                    byte qStat = q.status;
                    if (qStat == 2) {
                        scores[1] = scores[1] + q.getQFrequency();//number of satisfied quartet, s
                    } else if (qStat == 1) {
                        scores[2] = scores[2] + q.getQFrequency();//number of violated quartet, v
                    } else if (qStat == 3) {
                        scores[3] = scores[3] + q.getQFrequency();//number of deferred quartet, d
                    }
                }
            });

        } else {
            for (Quartet q : quartetMap) {
                q.initialSatVatCalculation();
                byte qStat = q.status;
                if (qStat == 2) {
                    scores[1] = scores[1] + q.getQFrequency();//number of satisfied quartet, s
                } else if (qStat == 1) {
                    scores[2] = scores[2] + q.getQFrequency();//number of violated quartet, v
                } else if (qStat == 3) {
                    scores[3] = scores[3] + q.getQFrequency();//number of deferred quartet, d
                }
            }
        }

        scores[0] = (scores[1] - scores[2]);//partitionScore = (s-v);

        return scores;

    }

    private static byte iCheckQuartet(Quartet q) {
        byte qstat = iCheckQuartet2(q.t1.getPartition(), q.t2.getPartition(), q.t3.getPartition(), q.t4.getPartition());
        q.status = qstat;
        return qstat;

    }

    public static byte iCheckQuartet2(int a, int b, int c, int d) {
        byte qstat;

        if (a == b && c == d && b == c) // totally on one side
        {
            qstat = 4;
        } else if (a == b && c == d) //satisfied
        {
            qstat = 2;
        } else if ((a == c && b == d) || (a == d && b == c)) // violated
        {
            qstat = 1;

        } else //deffered
        {
            qstat = 3;
        }

        return qstat;

    }
    //////////////////////////////

    private static String mergeUsingJAR(String s1, String s2, String extra) {

        if (s1.equals("")) {
            return s2;
        }
        if (s2.equals("")) {
            return s1;
        }

        String rootedS1 = rerootTreeUsingJARAndProcessing(s1, extra);
        String rootedS2 = rerootTreeUsingJARAndProcessing(s2, extra);

        String mergedTree = "(" + rootedS1 + "," + rootedS2 + ");";

        return mergedTree;

    }

    private static String rerootTreeUsingJARAndProcessing(String newickTree, String outGroupNode) {
        STITree tree = null;
        try {
            tree = new STITree(newickTree);
            tree.rerootTreeAtNode(tree.getNode(outGroupNode));
        } catch (IOException | ParseException ex) {
            System.out.println("Error in rerootTree.JAR ... check if jar main.jar exists. Exiting.");
            System.exit(-1);
        }

        String rootedTree = tree.toNewick();

        rootedTree = rootedTree.replace(";", ""); // remove semi-colon
        rootedTree = rootedTree.substring(1, rootedTree.length() - 1); // remove first and last brackets
        rootedTree = rootedTree.replace(outGroupNode, ""); //remove outGroup Node
        rootedTree = rootedTree.substring(1); //From left, so remove first comma

        return rootedTree;

    }
}