/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.level.events.relation.detection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author nlztoo
 */
public class SystemLevelEventsRelationDetection {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException {

        File dir = new File("C:\\Users\\nlztoo\\Documents\\\\test 2 m of data for SCM VL\\bl and high loads\\");

        int c = 0;
        int trackingtablenumber = 1;
        for (File file : dir.listFiles()) {
            if (file.getName().contains("TRACKING")) {
                BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\nlztoo\\Documents\\\\test 2 m of data for SCM VL\\related bl and hl\\" + file.getName() + ".csv"));
//               BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\nlztoo\\Documents\\\\t3 9M data\\New folder\\full_converted\\RelatedHLandBL\\10and11\\"+file.getName()+".csv"));

                HashMap<String, HashMap<String, String>> outerMap = new HashMap<String, HashMap<String, String>>();
                HashMap<String, String> innerMap = new HashMap<String, String>();
                System.out.println("file.getName() :" + file.getName());
                HashMap<String, String> inputmap = new HashMap<String, String>();

                List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                int inputcounter = 0;
                for (String line : lines) {
                    inputcounter++;

                    inputmap.put(String.valueOf(inputcounter), line);
//  System.out.println("...input"+line);
                }

                //check each event in inputmap by the next one, if there is any relation or not?
                //gharar nis hame ro begarde . avali az 1 mire ta akar dovomi dg nabayad avali ro check kone pas az 2 mire ta kahar
                int index = 1;
                for (int i = index; i <= inputcounter; i++) {
                    // get mainevent to compare with all of the otherevents after that(index+1)
                    String mainevent = inputmap.get(String.valueOf(i));
//            System.out.println(i);
                    String maineventtype = mainevent.substring(0, mainevent.indexOf(","));
                    String a1 = mainevent.substring(mainevent.indexOf(",") + 1);
                    String mainsegid = a1.substring(0, a1.indexOf(","));
                    String b1 = a1.substring(a1.indexOf(",") + 1);
                    String mainstart = b1.substring(0, b1.indexOf(","));
//            System.out.println(mainstart);
                    String startday = mainstart.substring(0, mainstart.indexOf("-"));
//            System.out.println(startday);
                    String mainfinish = b1.substring(b1.indexOf(",") + 1);
                    ///find first areaid and the second
//            String mainfirstareaid = mainsegid.substring(0, mainsegid.indexOf("-"));
//            String mainsecondareaid = mainsegid.substring(mainsegid.indexOf(":") + 1);
//            mainsecondareaid = mainsecondareaid.substring(0, mainsecondareaid.indexOf("-"));
                    String mainsegfirstnode = mainsegid.substring(0, mainsegid.indexOf(":"));
                    String mainsegsecondnode = mainsegid.substring(mainsegid.indexOf(":") + 1);
                    ///
//            mainsegid = mainsegid.replaceAll("-", "_");
//            mainsegid = mainsegid.replaceAll(":", "s");
//            String s = "s";
//            mainsegid = s.concat(mainsegid);
                    for (int j = 1; j <= inputcounter; j++) {
                        String otherevent = inputmap.get(String.valueOf(j));
                        String othereventtype = otherevent.substring(0, otherevent.indexOf(","));
                        String a = otherevent.substring(otherevent.indexOf(",") + 1);
                        String othersegid = a.substring(0, a.indexOf(","));
                        String b = a.substring(a.indexOf(",") + 1);
                        String otherstart = b.substring(0, b.indexOf(","));
//                  System.out.println(otherstart);
                        String ostartday = otherstart.substring(0, otherstart.indexOf("-"));
//            System.out.println(ostartday);
                        String otherfinish = b.substring(b.indexOf(",") + 1);
                        ///find first areaid and the second

                        String othersegfirstnode = othersegid.substring(0, othersegid.indexOf(":"));
                        String othersegsecondnode = othersegid.substring(othersegid.indexOf(":") + 1);
                        ///
                        if ((ostartday.equals(startday))) {
                            if (!(mainsegid.equals(othersegid))) {

//
//                othersegid = s.concat(othersegid);
                                //// check each event by other events
                                // at first check if the time is close
                                int precedes = 0;
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
                                Date d1 = sdf.parse(mainfinish);
                                Date d2 = sdf.parse(otherstart);
                                long elapsed = d2.getTime() - d1.getTime();
                                if (elapsed > 0) {
                                    //it is precedes
                                    precedes = 1;
                                }
                                Date d3 = sdf.parse(mainstart);
                                Date d4 = sdf.parse(otherfinish);
                                long elapsed2 = d3.getTime() - d4.getTime();
                                if (elapsed2 > 0) {
                                    //it is is preceded by
                                    precedes = 1;
                                }
                                // then check the relation
                                String relationtype = "";
                                if (precedes == 0) {
                                    ///check if at least one of the areaides is same
                                    //   if ((mainfirstareaid.equals(otherfirstareaid)) || (mainfirstareaid.equals(othersecondareaid)) || (mainsecondareaid.equals(otherfirstareaid)) || (mainsecondareaid.equals(othersecondareaid))) {
                                    if ((mainsegfirstnode.equals(othersegfirstnode)) || (mainsegfirstnode.equals(othersegsecondnode)) || (mainsegsecondnode.equals(othersegfirstnode)) || (mainsegsecondnode.equals(othersegsecondnode))) {

                                        // it is not precedes so I can check the type of reltion
                                        elapsed = d4.getTime() - d1.getTime();
                                        elapsed2 = d2.getTime() - d3.getTime();
                                        if (elapsed > 0 & elapsed2 > 0) {
                                            relationtype = "overlaps";
                                            String mstart = mainstart;
//                              System.out.println(mstart.indexOf("."));
                                            mstart = mstart.substring(0, mstart.indexOf("."));
                                            String mfinish = mainfinish;
                                            mfinish = mfinish.substring(0, mfinish.indexOf("."));
                                            String ostart = otherstart;
                                            ostart = ostart.substring(0, ostart.indexOf("."));
                                            String ofinish = otherfinish;
                                            ofinish = ofinish.substring(0, ofinish.indexOf("."));

                                            String result1 = mainsegid + "," + mstart + "," + mfinish + "," + maineventtype + "*" + othersegid + "," + ostart + "," + ofinish + "," + othereventtype;
                                            writer.write(result1);
                                            writer.newLine();
                                        }

                                        //contains mainstart< otherstart and mainfinish> otherfinish
                                        elapsed = d2.getTime() - d3.getTime();
                                        elapsed2 = d1.getTime() - d4.getTime();
                                        if (elapsed > 0 & elapsed2 > 0) {
                                            relationtype = "contains";
                                            String mstart = mainstart;
//                  System.out.println(mstart.indexOf("."));
                                            mstart = mstart.substring(0, mstart.indexOf("."));
                                            String mfinish = mainfinish;

                                            mfinish = mfinish.substring(0, mfinish.indexOf("."));
                                            String ostart = otherstart;
                                            ostart = ostart.substring(0, ostart.indexOf("."));
                                            String ofinish = otherfinish;
                                            ofinish = ofinish.substring(0, ofinish.indexOf("."));

                                            String result2 = mainsegid + "," + mstart + "," + mfinish + "," + maineventtype + "*" + othersegid + "," + ostart + "," + ofinish + "," + othereventtype;
//    String result2=mainsegid + "," + mstart+","+mfinish +"*" + othersegid + "," + ostart+","+ofinish ;
                                            writer.write(result2);
                                            writer.newLine();
                                        }

                                    }
                                }
                            }
                        }
                    }
//            index++;

                }
                writer.close();
            }

        }
    }

}
