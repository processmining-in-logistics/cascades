/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.level.event.detection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author nlztoo
 */
public class SystemLevelEventDetection {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, FileNotFoundException, ParseException {

//        File dir = new File("C:\\Users\\nlztoo\\Documents\\Vanderlande\\t3 9M data\\New folder\\full_converted\\all duration of segments tracking tables\\23\\");
        File dir = new File("C:\\Users\\nlztoo\\Documents\\Vanderlande\\VL commity meeting\\7 day\\duration\\");
//        File dir = new File("C:\\Users\\nlztoo\\Documents\\Vanderlande\\test 2 m of data for SCM VL\\duration\\");
        int c = 0;
        int trackingtablenumber = 1;
        for (File file : dir.listFiles()) {
            if (file.getName().contains("TRACKING")) {
             BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\nlztoo\\Documents\\Tue\\paper is here\\ICPM\\correct final result\\number of bags in blockage and blockage duration\\high load and blockage\\" + file.getName() + ".csv"));
                HashMap<String, HashMap<String, String>> outerMap = new HashMap<String, HashMap<String, String>>();
                HashMap<String, String> innerMap = new HashMap<String, String>();
                System.out.println("file.getName() :" + file.getName());

                int index = 0;
                int a = 0;

                List segidList = new ArrayList();
                List empList = new ArrayList();
                List inervalueList = new ArrayList();
////////////List latebagslist = new ArrayList();

                BlockageDetection checkBLAverage = new BlockageDetection();

                HighloadDetection checkHLPlot = new HighloadDetection();
                List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                for (String line : lines) {
                    String[] array2 = line.split(",");

                    String pid = array2[0];
                    String duration = array2[3];
                    String time = array2[2];
                    String segmentid = array2[1];

                    if (!(pid.equals("id"))) {
                        if ((!(pid == null))) {
                            if ((!(pid.contains(":")))) {

                                empList.add(pid);
                                String InnerValue = segmentid.concat("#");
                                InnerValue = InnerValue.concat(duration);
                                InnerValue = InnerValue.concat(",");
                                InnerValue = InnerValue.concat(time);

                                inervalueList.add(InnerValue);
////////////////////////////////////////////////////                outerMap.put(segmentid, innerMap);
                                if (!(segidList.contains(segmentid))) {
                                    segidList.add(segmentid);
                                }
                            }
                        }
                    }
                }

////        scanner.close();
/// for each segment check the blockage and high load
                for (Object st : segidList) {
                    String ss = (String) st;
                    checkBLAverage.check(inervalueList, (String) ss, writer);
                    checkHLPlot.check(inervalueList, ss, writer);

                }
                writer.close();

            }
        }
    }

}
