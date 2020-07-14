/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segment.level.event.detection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author nlztoo
 */
public class SegmentLevelEventDetection {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException {
        File dir = new File("C:\\Users\\nlztoo\\Documents\\\\\\7 day\\tracking table\\");
//        File dir = new File("C:\\Users\\nlztoo\\Documents\\\\\\test blockage detection\\");

        int c = 0;
        int trackingtablenumber = 1;
        for (File file : dir.listFiles()) {
            if (file.getName().contains("TRACKING")) {
                BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\nlztoo\\Documents\\\\7 day\\duration\\" + file.getName() + ".csv"));
                trackingtablenumber++;
                HashMap<String, List> BagsMap = new HashMap<String, List>();
                System.out.println(file.getName() );
                List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                for (String line : lines) {

                    String[] array2 = line.split(",");

                    String AID = array2[2];
                    String ZID = array2[3];
                    String EID = array2[4];

                    String location = AID + "-" + ZID + "-" + EID;
                    String PID = array2[5];

                    String time = array2[1];
//                    time = time.substring(1, time.length() - 1);

                    String LocationTime = location.concat("*");
                    LocationTime = LocationTime.concat(time);

//                    System.out.println("PID: "+PID);
                    if (!(PID.equals("PID"))) {
                        if (PID != null & !(PID.contains("="))) {

                            if (!(PID.equals("0") & PID.equals(""))) {
                                ///if pid is not 0
                                ////add the location and time to the list of bag
                                if (BagsMap.containsKey(PID)) {
                                    List l = BagsMap.get(PID);
                                    l.add(LocationTime);
                                    BagsMap.put(PID, l);
                                } else {
                                    c++;
                                    List<String> l = new ArrayList<String>();

                                    l.add(LocationTime);
                                    BagsMap.put(PID, l);
                                }
                            }
                        }
                    }
                }

                /////// after completeall data for all bags
                ///compute duration time for bag in each segment
                Set set1 = BagsMap.entrySet();
                Iterator itr1 = set1.iterator();

                while (itr1.hasNext()) {

                    Map.Entry entry = (Map.Entry) itr1.next();
                    String pid = (String) entry.getKey();
                    List lpid = (List) entry.getValue();

                    SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yy HH.mm.ss");
                    Map< Long, Object> msorted = new TreeMap< Long, Object>();
                    for (int i = 0; i < lpid.size() - 1; i++) {

                        /////////////////
                        //here sort time and location of case
                        String locationtime1 = (lpid.get(i).toString());
                        String location1 = locationtime1.substring(0, locationtime1.indexOf("*"));
                        String time1 = locationtime1.substring(locationtime1.indexOf("*") + 1);
                        time1 = time1.substring(0, 18);

                        Date d = f.parse(time1);
                        long millisecondss = d.getTime();

                        msorted.put(millisecondss, location1);

                    }

                    List lsortedpid = new ArrayList();
                    Set sett = msorted.entrySet();
                    Iterator itrt = sett.iterator();
                    String location2 = "";
                    String time2 = "";
                    String locationq = "";
                    String timeq = "";
                    while (itrt.hasNext()) {

                        Map.Entry entryy = (Map.Entry) itrt.next();
//                    System.out.println(entryy.getKey());
                        String time = String.valueOf(entryy.getKey());
//                      System.out.println(time);
                        String location1 = (String) entryy.getValue();
                        String locationtime = location1.concat("*");
                        locationtime = locationtime.concat(time);

                        if (!(locationq.equals("") & timeq.equals(""))) {

                            String segment = locationq.concat(":");
                            segment = segment.concat(location1);

                            long elapsed = Long.valueOf(time) - Long.valueOf(timeq);

                            String Duration = String.valueOf(elapsed);
//                           System.out.println(pid+","+segment+","+Duration+","+timeq);
                            String result = pid + "," + segment + "," + timeq + "," + Duration;
                            writer.write(result);
                            writer.newLine();

                            locationq = location1;
                            timeq = time;

                        } else {
                            locationq = location1;
                            timeq = time;
                        }

                    }
                }
                writer.close();
            }
//s.close();
        }
        System.out.println(c);
    }

}
