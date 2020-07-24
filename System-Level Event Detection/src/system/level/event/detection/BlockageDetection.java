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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 *
 * @author nlztoo
 */
public class BlockageDetection {

    public int check(List inervalueList, String segmentid, BufferedWriter writer) throws FileNotFoundException, ParseException, IOException {
        Map zscoremap = new HashMap();
        Map segpidstarttimemap = new HashMap();
///put all duration of segment in an array numList

        int i = 0;

        List<String> numList = new ArrayList<String>();
        List<String> madList = new ArrayList<String>();

        int countpid = 0;
////////        while (itr.hasNext()) {
        for (Object st : inervalueList) {

            String segiddurationtime = (String) st;

            String segid = segiddurationtime.substring(0, segiddurationtime.indexOf("#"));
            String duration = segiddurationtime.substring(segiddurationtime.indexOf("#") + 1, segiddurationtime.indexOf(","));
////////            String duration = durationtime.substring(0, durationtime.indexOf(","));
            String starttimee = segiddurationtime.substring(segiddurationtime.indexOf(",") + 1);
            if (segid.equals(segmentid)) {
//    System.out.println(duration);
                numList.add(duration);
                segpidstarttimemap.put(countpid, segiddurationtime);
                countpid++;

            }
        }

        int size = numList.size();
//numList.sort(Comparator.naturalOrder());
        Collections.sort(numList);
        int[] numArray = new int[size];
        double[] MADArray = new double[size];
        int h = 0;
        for (String st : numList) {
            numArray[h] = Integer.valueOf(st);
            h++;

        }
        Arrays.sort(numArray);
        for (i = 0; i < size; i++) {
// System.out.println(numArray[i]);
        }
        double median;
        if (numArray.length % 2 == 0) {
////            System.out.println(numArray[numArray.length / 2]);
            median = ((double) numArray[numArray.length / 2] + (double) numArray[numArray.length / 2 + 1]) / 2;
        } else {
            median = (double) numArray[numArray.length / 2];
        }
//        System.out.println("segmentid :" + segmentid);
//        System.out.println("median :" + median);
/////now i have median and can compute MAD
/// median of ( diffrence of variable and median)
        for (i = 0; i < size; i++) {

            double diff = numArray[i] - median;
            if (diff < 0) {
                diff = diff * -1;
            }
            madList.add(String.valueOf(diff));
//              System.out.println("diff :" + diff);
        }
////        Arrays.sort(MADArray);
        Collections.sort(madList);

        int k = 0;
        for (String st : madList) {
            MADArray[k] = Double.valueOf(st);
            k++;

        }
        Arrays.sort(MADArray);
        double MAD;
        if (MADArray.length % 2 == 0) {
            MAD = ((double) MADArray[MADArray.length / 2] + (double) MADArray[MADArray.length / 2 + 1]) / 2;
        } else {
            MAD = (double) MADArray[MADArray.length / 2];
        }


/////now i have mad and I can calcute z score
        if (MAD != 0.0) {

            ///////////////compute zscore averag for threshold in finding outliers
            ///////////// check if the duartion is an outlier or not
            Set set1 = segpidstarttimemap.entrySet();
            Iterator itr1 = set1.iterator();

            while (itr1.hasNext()) {

                //Converting to Map.Entry so that we can get key and value separately  
                Map.Entry entry = (Map.Entry) itr1.next();
////////////////            String durationtime = (String) entry.getValue();

////        System.out.println(entry.getKey()+","+entry.getValue()); 
                String segiddurationtime = (String) entry.getValue();
////            System.out.println(entry.getKey() + "," + entry.getValue());
                String segid = segiddurationtime.substring(0, segiddurationtime.indexOf("#"));
                String duration = segiddurationtime.substring(segiddurationtime.indexOf("#") + 1, segiddurationtime.indexOf(","));
////////            String duration = durationtime.substring(0, durationtime.indexOf(","));
                String starttime = segiddurationtime.substring(segiddurationtime.indexOf(",") + 1);
                if (segid.equals(segmentid)) {

//////////////            String duration = durationtime.substring(0, durationtime.indexOf(","));
//            System.out.println("duration :" + duration);
                    Date date = new Date(Long.valueOf(starttime));
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
                    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String dateFormatted = formatter.format(date);
//                     System.out.println("starttime :" + dateFormatted);
                    int durationint = Integer.valueOf(duration);
//                System.out.println("MAD :" + MAD);
                    double zscore = (0.6745 * (durationint - median)) / MAD;

////          zscoremap.put(entry.getKey(), zscore);
                    if (zscore > 15) {
                        zscoremap.put(entry.getKey(), 1);
// System.out.println("zscore :" + zscore);
                    } else {
                        zscoremap.put(entry.getKey(), 0);
//                    System.out.println("zscore :" + zscore);
                    }

                }
            }

        }

        //// now i have z score and pid in a map
//have delay window!!
/// start window if see an outlier(value 1 in zscore map)
// end window if wee an normal bag

/////for each blockage there is an windows that opens when see the first outlier in duration and cloase by seeing a non outlier duration
        Set setz = zscoremap.entrySet();
        Iterator itrz = setz.iterator();
        int start = 0;
        int end = 0;
        String previousendtime = "";
        String startwindow = "";
        String endwindow = "";
        int blockagesize = 0;
        while (itrz.hasNext()) {
            //Converting to Map.Entry so that we can get key and value separately  
            Map.Entry entry = (Map.Entry) itrz.next();
            int outlierornot = (int) entry.getValue();

            String segiddurationtime = (String) segpidstarttimemap.get(entry.getKey());

            String segid = segiddurationtime.substring(0, segiddurationtime.indexOf("#"));
            String duration = segiddurationtime.substring(segiddurationtime.indexOf("#") + 1, segiddurationtime.indexOf(","));
            String starttime = segiddurationtime.substring(segiddurationtime.indexOf(",") + 1);
            long finishtime = Long.valueOf(starttime) + Long.valueOf(duration);
            if (outlierornot == 1) {
                ///save previus outlier for closing the window
                previousendtime = String.valueOf(finishtime);
                blockagesize++;
                if (start == 0) {

                    Date date = new Date(Long.valueOf(starttime));
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
                    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String dateFormatted = formatter.format(date);

// System.out.println(dateFormatted);
                    startwindow = dateFormatted;
                    start = 1;
                }
            }
            if (outlierornot == 0) {
                if (end == 0 & start == 1) {

                    Date date = new Date(Long.valueOf(previousendtime));
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
                    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String dateFormatted = formatter.format(date);

                    endwindow = dateFormatted;
                    if (blockagesize > 1) {
// System.out.println("BL,"+ segmentid+"," + startwindow + "," + endwindow);
                        String result = "BL," + segmentid + "," + startwindow + "," + endwindow;
                        writer.write(result);
                        writer.newLine();
                    }
//////System.out.println("Delay" + startwindow + "-" + endwindow);
                    start = 0;
                    end = 0;
                    blockagesize = 0;
                }
            }

        }

        return 0;

    }
}
