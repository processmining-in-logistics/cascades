/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.of.syetem.level.event.detection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nlztoo
 */
public class CascadeOfSyetemLevelEventDetection {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\nlztoo\\Documents\\Tue\\paper is here\\spmf\\inputfor tkg 7 days dirksuggestionnew for bl cacadescorrectalaki.txt"));
        BufferedWriter writeractivityid = new BufferedWriter(new FileWriter("C:\\Users\\nlztoo\\Documents\\Tue\\paper is here\\spmf\\inputfor tkg 7 days dirksuggestion real segment name and id for blockage cascadescorrectalaki.txt"));
//          BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\nlztoo\\Documents\\Vanderlande\\t3 9M data\\New folder\\full_converted\\EventLogWithCaseId\\10and11\\aaaaaa.csv"));
        Map<String, List<String>> nodeidactivitylinkmap = new LinkedHashMap<>();
        HashMap<String, String> inputmap = new HashMap<String, String>();

        HashMap<String, Integer> nodeidandrealnamenmap = new HashMap<String, Integer>();
        HashMap<String, String> secondnodeofrelationmap = new HashMap<String, String>();
        ArrayList nodelist = new ArrayList();
        ArrayList edgelist = new ArrayList();
        ArrayList firstnodeofrelationchecklist = new ArrayList();
        ArrayList EdgeslistForsearch = new ArrayList();
        // Scanner scanner = new Scanner(new File("C:\\Users\\nlztoo\\Documents\\Tue\\paper is here\\report\\pettern detection on conceret cascade\\related blockage and high load\\20170929_030001_BPIT3_WC_TRACKINGREPORT.CSV.csv.csv.csv"));

        //  Scanner dataScanner = null;
        int CaseID = 0;
        int k = 0;
        int t = 0;
        int countedge = 0;
        int blo = 0;

//        int inputcounter = 0;
        File dir = new File("C:\\\\Users\\\\nlztoo\\\\Documents\\\\Tue\\\\paper is here\\\\report\\\\pettern detection on conceret cascade\\\\related blockage and high load\\");

        for (File file : dir.listFiles()) {
            HashMap<Integer, List> graphmap = new HashMap<Integer, List>();
            ArrayList blolist = new ArrayList();
            ArrayList hillist = new ArrayList();
            int allcascade = 0;
            int blcascade = 0;
//        ArrayList pattern = new ArrayList();
//        Scanner scanner = new Scanner(new File("C:\\Users\\nlztoo\\Documents\\Tue\\paper is here\\report\\pettern detection on conceret cascade\\related blockage and high load\\all.csv"));
//
//        Scanner dataScanner = null;

            int inputcounter = 0;
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for (String line : lines) {

//add all events to inputmap
                // while (scanner.hasNextLine()) {
                inputcounter++;

//            dataScanner = new Scanner(scanner.nextLine());
////            dataScanner.useDelimiter(",");
//            while (dataScanner.hasNext()) {
                String input = line;
                if (input.contains("*")) {
//                System.out.println("...input"+input);
//                    edgelist.add(input);
                    inputmap.put(String.valueOf(inputcounter), input);
                    String firstnode = input.substring(0, input.indexOf("*"));
                    String secondnode = input.substring(input.indexOf("*") + 1);
                    if (!(nodelist.contains(firstnode))) {
                        nodelist.add(firstnode);
                    }
                    if (!(nodelist.contains(secondnode))) {
                        nodelist.add(secondnode);
                    }
                    if (firstnode.contains("BL")) {
                        blo++;
                        if (!(blolist.contains(firstnode))) {
                            blolist.add(firstnode);
                        }
                    }
                    if (secondnode.contains("BL")) {
                        blo++;
                        if (!(blolist.contains(secondnode))) {
                            blolist.add(secondnode);
                        }
                    }
                    if (firstnode.contains("HL")) {
//                        blo++;
                        if (!(hillist.contains(firstnode))) {
                            hillist.add(firstnode);
                        }
                    }
                    if (secondnode.contains("HL")) {
//                        blo++;
                        if (!(hillist.contains(secondnode))) {
                            hillist.add(secondnode);
                        }
                    }
                    if (!(edgelist.contains(input))) {
                        edgelist.add(input);
                    }
                }
            }

            System.out.println("bl node  : " + blo);
            System.out.println("bllist size node  : " + blolist.size());
            System.out.println("hillist size node  : " + hillist.size());

            for (int i = 0; i < nodelist.size(); i++) {
                String node = (String) nodelist.get(i);
                ArrayList relatednodelist = new ArrayList();
                ArrayList relatededgelist = new ArrayList();
                ArrayList graphlist = new ArrayList();
                nodelist.remove(node);
// System.out.println("new node : "+node );
//              EdgeslistForsearch = edgelist;
                ///find all related nodes and remove from node list
                relatededgelist = FindRelatedNode(edgelist, node);

                if (relatededgelist.size() > 0) {
//                System.out.println("%%%");

                    int j = 0;
//                    String searchnode = BLnode;
                    while (relatededgelist.size() > 0) {
//            for (int j = 0; j < relatededgelist.size(); j++) {
//                 System.out.println(relatededgelist.size());

                        String edge = (String) relatededgelist.get(j);
                        graphlist.add(edge);
//                j++;
                        relatededgelist.remove(edge);
                        String firstnode = edge.substring(0, edge.indexOf("*"));
                        String secondnode = edge.substring(edge.indexOf("*") + 1);
                        nodelist.remove(secondnode);
                        nodelist.remove(firstnode);
                        edgelist.remove(edge);
//                     System.out.println(edge);
                        if (firstnode.equals(node)) {
//                    relatednodelist.add(secondnode);
//                    relatededgelist.add(edge);

                            ArrayList newedgerelatedlist = FindRelatedNode(edgelist, secondnode);
                            for (int w = 0; w < newedgerelatedlist.size(); w++) {

                                String reledge = (String) newedgerelatedlist.get(w);
                                String ftnode = reledge.substring(0, reledge.indexOf("*"));
                                String snode = reledge.substring(reledge.indexOf("*") + 1);
                                if (!(relatededgelist.contains(reledge))) {
                                    relatededgelist.add(reledge);
                                }
                                if ((edgelist.contains(reledge))) {
                                    edgelist.remove(reledge);
                                }
                                if ((nodelist.contains(snode))) {
                                    nodelist.remove(snode);
                                }
                                if ((nodelist.contains(ftnode))) {
                                    nodelist.remove(ftnode);
                                }

                            }
                            node = secondnode;
                        }
                        if (secondnode.equals(node)) {
//                    relatednodelist.add(firstnode);
//                    relatededgelist.add(edge);
//                    nodelist.remove(firstnode);
//                    edgelist.remove(edge);
                            ArrayList newedgerelatedlist = FindRelatedNode(edgelist, firstnode);
                            for (int w = 0; w < newedgerelatedlist.size(); w++) {

                                String reledge = (String) newedgerelatedlist.get(w);
                                String ftnode = reledge.substring(0, reledge.indexOf("*"));
                                String snode = reledge.substring(reledge.indexOf("*") + 1);
                                if (!(relatededgelist.contains(reledge))) {
                                    relatededgelist.add(reledge);
                                }
                                if ((edgelist.contains(reledge))) {
                                    edgelist.remove(reledge);
                                }
                                if ((nodelist.contains(snode))) {
                                    nodelist.remove(snode);
                                }
                                if ((nodelist.contains(ftnode))) {
                                    nodelist.remove(ftnode);
                                }

                            }
                            node = firstnode;
                        }

                    }
                }
                ////add case to the mapofgraph

//            System.out.println(CaseID);
//            System.out.println(relatededgelist.size());
                if (graphlist.size() > 1) {
                    int bl = 0;
                    allcascade++;
                    for (int ii = 0; ii < graphlist.size(); ii++) {
                        String ed = (String) graphlist.get(ii);
                        if (ed.contains("BL")) {
//                          System.out.println("bl : "+ed);
                            bl = 1;
                        }
                    }
                    if (bl == 1) {
                        blcascade++;
//                      for (int w = 0; w < graphlist.size(); w++) {
//    
//             System.out.println(graphlist.get(w));
//         }
                        graphmap.put(CaseID, graphlist);
                        CaseID++;
                        bl = 0;
                    }
                }
//             System.out.println(graphlist.size());
// for (int w = 0; w < graphlist.size(); w++) {
//    
//             System.out.println(graphlist.get(w));
//         }
//         System.out.println("___________________________________");
            }
            System.out.println("all cascades : " + allcascade);
            System.out.println("blockage cascades : " + blcascade);
            System.out.println("___________________________________");

//         
//         
            for (Map.Entry<Integer, List> entry : graphmap.entrySet()) {
                ArrayList graphnodelist = new ArrayList();
                ArrayList graphedgelist = new ArrayList();
                HashMap<String, Integer> nodeidandlabelforedgesmap = new HashMap<String, Integer>();
                writer.write("t # " + t);
                writer.newLine();
//            System.out.println("t # " + t);
                t++;
                List listgraph = entry.getValue();
                for (int i = 0; i < listgraph.size(); i++) {
                    Integer firstnodeid;
                    Integer secondnodeid;
                    String firstouttype;
                    String secouttype;
                    int firstnodeidnotlabel;
                    int secondnodeidnotlabel;
                    String edge = (String) listgraph.get(i);
                    String ftnode = edge.substring(0, edge.indexOf("*"));
                    String firstnodesegment = ftnode.substring(0, ftnode.indexOf(","));
                    if (ftnode.contains("BL")) {
                        firstouttype = "BL";
                    } else {
                        firstouttype = "HL";
                    }
                    String firstnodesegmenttype = firstnodesegment.concat("*");
                    firstnodesegmenttype = firstnodesegmenttype.concat(firstouttype);

                    ///add node to realnameidmap
                    if (nodeidandrealnamenmap.containsKey(firstnodesegmenttype)) {
                        firstnodeid = nodeidandrealnamenmap.get(firstnodesegmenttype);
                    } else {
                        firstnodeid = nodeidandrealnamenmap.size() + 1;
                        nodeidandrealnamenmap.put(firstnodesegmenttype, firstnodeid);
                        writeractivityid.write(firstnodesegmenttype + "," + firstnodeid);
                        writeractivityid.newLine();
                    }
//                 System.out.println("firstnodesegmenttype " + firstnodesegmenttype+" id: "+firstnodeid);
                    if (nodeidandlabelforedgesmap.containsKey(String.valueOf(firstnodeid))) {
                        firstnodeidnotlabel = nodeidandlabelforedgesmap.get(String.valueOf(firstnodeid));
                    } else {
                        nodeidandlabelforedgesmap.put(String.valueOf(firstnodeid), k);
                        firstnodeidnotlabel = k;
                        k++;
                    }

//                int firstnodeidnotlabel=k;
//                k++;
                    String firstnode = "v ";
                    firstnode = firstnode.concat(String.valueOf(firstnodeidnotlabel));
                    firstnode = firstnode.concat(" ");
                    firstnode = firstnode.concat(String.valueOf(firstnodeid));

                    String snode = edge.substring(edge.indexOf("*") + 1);
                    String secondnodesegment = snode.substring(0, snode.indexOf(","));
                    if (snode.contains("BL")) {
                        secouttype = "BL";
                    } else {
                        secouttype = "HL";
                    }
                    String secondnodesegmenttype = secondnodesegment.concat("*");
                    secondnodesegmenttype = secondnodesegmenttype.concat(secouttype);

//                int secondnodeidnotlabel=k;
//                k++;
                    ///add node to realnameidmap
                    if (nodeidandrealnamenmap.containsKey(secondnodesegmenttype)) {
                        secondnodeid = nodeidandrealnamenmap.get(secondnodesegmenttype);
                    } else {
                        secondnodeid = nodeidandrealnamenmap.size() + 1;
                        nodeidandrealnamenmap.put(secondnodesegmenttype, secondnodeid);
                        writeractivityid.write(secondnodesegmenttype + "," + secondnodeid);
                        writeractivityid.newLine();
                    }

                    if (nodeidandlabelforedgesmap.containsKey(String.valueOf(secondnodeid))) {
                        secondnodeidnotlabel = nodeidandlabelforedgesmap.get(String.valueOf(secondnodeid));
                    } else {
                        nodeidandlabelforedgesmap.put(String.valueOf(secondnodeid), k);
                        secondnodeidnotlabel = k;
                        k++;
                    }

                    String secondnode = "v ";
                    secondnode = secondnode.concat(String.valueOf(secondnodeidnotlabel));
                    secondnode = secondnode.concat(" ");
                    secondnode = secondnode.concat(String.valueOf(secondnodeid));

                    String graphedge = "e ";
                    graphedge = graphedge.concat(String.valueOf(firstnodeidnotlabel));
                    graphedge = graphedge.concat(" ");
                    graphedge = graphedge.concat(String.valueOf(secondnodeidnotlabel));
                    String edgelabel = (String.valueOf(firstnodeid)).concat(String.valueOf(secondnodeid));
//                graphedge= graphedge.concat(" "+edgelabel);
                    graphedge = graphedge.concat(" 1");
                    if (!(graphnodelist.contains(firstnode))) {
                        graphnodelist.add(firstnode);
                    }
                    if (!(graphnodelist.contains(secondnode))) {
                        graphnodelist.add(secondnode);
                    }
                    if (!(graphedgelist.contains(graphedge))) {
                        graphedgelist.add(graphedge);
                    }

//                System.out.println(listgraph.get(i));
                }
                for (int i = 0; i < graphnodelist.size(); i++) {

//                  System.out.println(graphnodelist.get(i));
                    writer.write((String.valueOf(graphnodelist.get(i))));
                    writer.newLine();
//                  k++;
                }

                for (int i = 0; i < graphedgelist.size(); i++) {
//                  System.out.println(graphedgelist.get(i));
                    writer.write((String.valueOf(graphedgelist.get(i))));
                    writer.newLine();
                }

                System.out.println();
            }
//        
        }
        writer.close();
        writeractivityid.close();
    }

    public static ArrayList FindRelatedNode(List edgelist, String node) {
        ArrayList relatedlist = new ArrayList();
        for (int i = 0; i < edgelist.size(); i++) {
            String reledge = (String) edgelist.get(i);

            String firstnode = reledge.substring(0, reledge.indexOf("*"));
            String secondnode = reledge.substring(reledge.indexOf("*") + 1);

            if ((secondnode.equals(node)) || ((firstnode.equals(node)))) {
                //if one of the nodes in edge is the node we are looking for related for that

                if (!(relatedlist.contains(reledge))) {
                    relatedlist.add(reledge);
                }
//                   
            }
        }

        return relatedlist;
    }

}
