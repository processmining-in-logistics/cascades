/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.level.event.detection;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 *
 * @author nlztoo
 */
public class HighloadDetection {

    public int check(List inervalueList, String segmentid, BufferedWriter writer) throws FileNotFoundException, ParseException, IOException {
        List<String> Segmenttimelist = new ArrayList<String>();
        HashMap<String, List> SegmentTimeMap = new HashMap<String, List>();
        String startwin = "";
        String endwin = "";
        List<String> OneminList = new ArrayList<String>();
        List<String> OneminListv = new ArrayList<String>();
        List<String> HLList = new ArrayList<String>();
        List<Integer> OneminListForAverage = new ArrayList<Integer>();

        int freq = 0;
        ///// compute average
        String starttimeofday = "";
        for (Object st1 : inervalueList) {
            String segiddurationtime1 = (String) st1;
//            System.out.println("*******"+segmentid);
            String segid1 = segiddurationtime1.substring(0, segiddurationtime1.indexOf("#"));
            String starttime1 = segiddurationtime1.substring(segiddurationtime1.indexOf(",") + 1);
            if (segid1.equals(segmentid)) {
                if (!(starttime1.equals(""))) {
//                    System.out.println(starttime1);
                    Segmenttimelist.add(starttime1);

                }

            }

        }
        String time = (Segmenttimelist.get(0));

        long startofday = Long.parseLong(time);
//        System.out.println(startofday);
        long endofday = startofday + 86400000;
        ///
        long endwintimel = (60000 + (startofday));
        long startwintimel = startofday;

        ///start a 1 min win fors
        while (endwintimel < endofday) {
            for (int i = 0; i < Segmenttimelist.size() - 1; i++) {

                long timebag = Long.parseLong(Segmenttimelist.get(i));
//                                System.out.println(timebag);
                if (startwintimel <= timebag) {
                    if (timebag < endwintimel) {
                        freq++;
//                                     System.out.println(freq);
                    }
                }
            }
            OneminListForAverage.add(freq);
            freq = 0;
            startwintimel = endwintimel;
            endwintimel = (60000 + (startwintimel));
        }

        OneminList.clear();

        ///compute max and min in list
        int max = 0;
        int min = 0;
        for (int st : OneminListForAverage) {
            if (st > max) {
                max = st;
            }
            if (st < min) {
                min = st;
            }

        }
//        System.out.println("max : " + max);
        ///threshold is 75% of max
        double threshold = max * 0.75;

        /////find highloads
        String time2 = (Segmenttimelist.get(0));

        long startofday2 = Long.parseLong(time2);
//        System.out.println(startofday);
        long endofday2 = startofday2 + 86400000;
        ///
        long endwintimel2 = (60000 + (startofday2));
        long startwintimel2 = startofday2;

        ///start a 1 min win fors
        while (endwintimel2 < endofday2) {
            for (int i = 0; i < Segmenttimelist.size() - 1; i++) {

                long timebag = Long.parseLong(Segmenttimelist.get(i));
//                                System.out.println(timebag);
                if (startwintimel2 <= timebag) {
                    if (timebag < endwintimel2) {
                        freq++;
//                                     System.out.println(freq);
                    }
                }
            }
            ////check high load here//

            if (freq >= threshold) {

                if (startwin.equals("")) {
                    startwin = String.valueOf(startwintimel2);
//                            System.out.println(OneminList.size());
                    HLList.addAll(OneminList);
//                            System.out.println(OneminList.size());
                    OneminList.clear();
                } else {
//                            System.out.println(OneminList.size());
                    HLList.addAll(OneminList);
//                            System.out.println(OneminList.size());
                    OneminList.clear();
                }

            } else {
//                                if ((!(startwin.equals("")))) {
                if (endwin.equals("") & (!(startwin.equals("")))) {
                    endwin = String.valueOf(startwintimel2);

                    Date sdate = new Date(Long.valueOf(startwin));
                    endwin = String.valueOf(endwintimel2);

                    Date edate = new Date(Long.valueOf(endwin));
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
                    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String dateFormatteds = formatter.format(sdate);
                    String edateFormatted = formatter.format(edate);
                    String startwin1 = dateFormatteds;

                    String endwin1 = edateFormatted;

                    String result = "HL," + segmentid + "," + startwin1 + "," + endwin1;
//                                    System.out.println(result);
                    writer.write(result);
                    writer.newLine();
                    startwin = "";
                    endwin = "";
                    HLList.clear();
                    OneminList.clear();
                } else {
                    OneminList.clear();
                }
            }

            freq = 0;
            startwintimel2 = endwintimel2;
            endwintimel2 = (60000 + (startwintimel2));

        }


        return 0;

    }
}
