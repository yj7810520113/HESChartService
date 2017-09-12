package org.maomao.test.DataPersistence;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.hashids.Hashids;
import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.maomao.test.DataSource.DataSource.*;

/**
 * Created by maomao on 2017/8/8.
 * mail:yinjun104@gmail.com
 * homepage:http://www.mmcode.top
 * 第七步：
 * 计算每一天的每隔10min的模拟出行数据，
 */
public class SimulateTravelByDay {
//    从左向右，
    String[] link_id_1={"0395","3328","2328","9228","8228","9663","8663","7663","7674","8738","4028","1141","0141","3959","2959","6141","5141"};
//    从右向左
    String[] link_id_2={"0969","1969","3141","4141","2141","8041","9041","5934","4395","0763","1763","2763","1555","6041","4555","5041","1041"};
//    左边的从上到下
    String[] link_id_3={"0863","5681","6681","0329","2418","1784","0784","8593","8063","7063","2541","6918","9674","0913","9243","0344","8175","9175","6566","2759","3759",};
//    左边的从下到上
    String[] link_id_4={"5566","2532","9244","9525","9813","8243","7243","7934","7886","9063","0163","4514","4594","5594","5334","6334","8421"};
//右边的从上到下
    String[] link_id_5={"8893","6343","5343","5759","4759","7169","6169","3815","2815","7141","4959","8959","7959","4422","3422","5434","2769","7863"};
//    右边的从下到上
    String[] link_id_6={"9863","3769","9044","0241","1241","9141","2969","5525","6525","1741","1518","7869","8869","8425","7425"};


    Hashids hashids = new Hashids("minechicken");
    public static Connection con=null;
    public static PreparedStatement ps = null;
    SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat daydate=new SimpleDateFormat("yyyy-MM-dd");

    //    SimpleDateFormat time=new SimpleDateFormat("YYYY-mm-DD");
     HashMap<String,Integer> LinkMap=new HashMap<String, Integer>();
     HashMap<String,Float> LinkSpeedMap=new HashMap<String,Float>();
//     String[] link_id_1={"5566","8228","4395","4959","2776","7674","3525"};
     LinkedHashMap<String,Long> link_travel_time=new LinkedHashMap<String, Long>();
    @Test
    public void read() throws ParseException {
        readData();
        long firstDate=date.parse("2016-03-01 00:00:00").getTime();
//        3月1号到5月31号
//        System.out.println(1000*60*60*92);
        for(long t=0;t<(1000L*60*60*24*92);t+=1000L*60*60*24){
            System.out.println("111");
            predictTravelTime(firstDate+t,link_id_1,1);
//            for (HashMap.Entry<String, Long> ele : link_travel_time.entrySet()) {
//                System.out.println(ele.getKey() + ":" + date.format(new Date(ele.getValue())));
//            }
            predictTravelTime(firstDate+t,link_id_2,2);
            predictTravelTime(firstDate+t,link_id_3,3);
            predictTravelTime(firstDate+t,link_id_4,4);
            predictTravelTime(firstDate+t,link_id_5,5);
            predictTravelTime(firstDate+t,link_id_6,6);
        }
        System.out.println("222");




    }
    public void predictTravelTime(Long dayDateTime,String[] links,int link_path) throws ParseException {
//        readData1("2016-03-10");
//        读取改天的数据
        readData1(daydate.format(new Date(dayDateTime)));
//        开始算法过程
//        计算每一天每隔10min的数据
        for(int interval=0;interval<1000L*60*60*24;interval+=1000L*60*10) {
            System.out.println(date.format(new Date(dayDateTime+interval)));
//
            Long timeSpan=dayDateTime+interval;

//        starttime作为开始时间段的标示符
            Long startTime = dayDateTime+interval;
//        intervalStarttime作为真正的开始时间
            Long intervalStartTime =  dayDateTime+interval;
            for (int i = 0; i < links.length; i++) {
                float length = LinkMap.get(links[i]);
//            System.out.println(startTime+"_"+link_id_1[i]);
//            System.out.println(LinkSpeedMap.size());
                float speed = LinkSpeedMap.get((startTime + "_" + links[i]));
//            进出都在2min内
                if (speed * ((120 * 1000 - (intervalStartTime - startTime)) / 1000) > length) {
                    intervalStartTime += (long) ((length / speed) * 1000);
//                System.out.println(intervalStartTime);
                    link_travel_time.put(links[i]+"_"+timeSpan, intervalStartTime);
                    continue;
                }
//            循环lenght和intervalstarttime
                else {
//                System.out.println("1111");
                    length = length - speed * ((120 * 1000 - (intervalStartTime - startTime)) / 1000);
                    startTime = intervalStartTime = startTime + 120 * 1000;
                    if(startTime>=(dayDateTime+(1000L*60*60*24))){
//                        link_travel_time.put(link)
                        break;
                    }
                    for (; ; ) {
//                        System.out.println((startTime + "_" + links[i]));
                        speed = LinkSpeedMap.get((startTime + "_" + links[i]));
                        if (speed * ((120 * 1000 - (intervalStartTime - startTime)) / 1000) > length) {
                            intervalStartTime += (long) ((length / speed) * 1000);
                            link_travel_time.put(links[i]+"_"+timeSpan, intervalStartTime);
                            break;
                        } else {
//                            System.out.println(length);
                            length = length - speed * ((120 * 1000 - (intervalStartTime - startTime)) / 1000);
                            startTime = intervalStartTime = startTime + 120 * 1000;
                        }
                    }
                }
            }
            int exes=0;
            for (HashMap.Entry<String, Long> ele : link_travel_time.entrySet()) {
                exes++;
                updateData(daydate.format(dayDateTime),timeSpan/1000,ele.getKey().split("_")[0],date.format(new Date(ele.getValue())),link_path,exes==link_travel_time.size()?true:false);

//                System.out.println(ele.getKey() + ":" + date.format(new Date(ele.getValue())));
            }
            link_travel_time.clear();

        }

    }
    public void readData(){
        if(con==null) {
            try {
                Class.forName(DBDRIVER); //1、使用CLASS 类加载驱动程序
                con = DriverManager.getConnection(DBURL, DBUSER, DBPASS); //2、连接数据库

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ps = con.prepareStatement("select link_id,length from link_info");
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
//                Link link=new Link(rs.getString("link_id"),rs.getInt("length"));
                LinkMap.put(rs.getString("link_id"),rs.getInt("length"));
            }
//            con.commit();
            ps=null;
            con.close();
            con=null;

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void readData1(String daydate){
//        dateSet.clear();
        if(con==null) {
            try {
                Class.forName(DBDRIVER); //1、使用CLASS 类加载驱动程序
                con = DriverManager.getConnection(DBURL, DBUSER, DBPASS); //2、连接数据库

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
//            System.out.println(daydate+daytime);
            ps = con.prepareStatement("select link_id,speed,UNIX_TIMESTAMP(concat(daydate,' ',daytime)) as longtime from training_data where daydate=?");
            ps.setString(1,daydate);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
//                System.out.println((rs.getLong("longtime")+"_"+rs.getString("link_id")));
//                System.out.println(rs.getString("link_id"));
                LinkSpeedMap.put((rs.getLong("longtime")*1000+"_"+rs.getString("link_id")),rs.getFloat("speed"));
//                dateSet.add(rs.getString("link_id"));
            }
//            System.out.println(dateSet.size());
//            con.commit();
            ps=null;
            con.close();
            con=null;

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void updateData(String predict_date,Long predict_datetimespan,String link_id,String predict_arrivetime,int link_path,boolean exe){
        try {
            if(con==null) {

                try {
                    Class.forName(DBDRIVER); //1、使用CLASS 类加载驱动程序
                    con = DriverManager.getConnection(DBURL, DBUSER, DBPASS); //2、连接数据库
                    con.setAutoCommit(false);

                    ps = con.prepareStatement("insert into predict_travel_time(predict_datetimespan,predict_date,link_id,predict_arrive_time,link_path) values(FROM_UNIXTIME(?),?,?,?,?)");
//                    System.out.println("ps-------------------------------------------"+ps);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
//            System.out.println("link_id"+link_id);
//            System.out.println(ps);
            ps.setLong(1,predict_datetimespan);
            ps.setString(2,predict_date);
            ps.setString(3,link_id);
            ps.setString(4,predict_arrivetime);
            ps.setInt(5,link_path);
            ps.addBatch();
            if(exe==true) {
                ps.executeBatch();
               con.commit();

                    con.close();
                ps = null;
                   con=null;
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}


