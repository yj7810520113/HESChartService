package org.maomao.test.DataPersistence;

import org.hashids.Hashids;
import org.junit.Test;
import sun.util.resources.th.CalendarData_th;

import javax.sound.midi.Soundbank;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static org.maomao.test.DataSource.DataSource.*;

/**
 * Created by maomao on 2017/8/8.
 * mail:yinjun104@gmail.com
 * homepage:http://www.mmcode.top
 * 第五步：
 * 根据每个路段计算出来的平均值，补全缺失路段的数据，缺失路段数据标记为is_missing_data
 * 其中缺失数据的speed，speed_aver，默认为平均速度
 * speed_offset为0
 * 不全数据
 */
public class FillingMissingValues {
    class Link{
        private String link_id;
        private int length;
        private float speed;

        public String getLink_id() {
            return link_id;
        }

        public void setLink_id(String link_id) {
            this.link_id = link_id;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public Link(String link_id, int length, float speed){
            this.link_id=link_id;
            this.length=length;
            this.speed=speed;
        }
    }

    Hashids hashids = new Hashids("minechicken");
    public static Connection con=null;
    public static PreparedStatement ps = null;
    SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    SimpleDateFormat time=new SimpleDateFormat("YYYY-mm-DD");
     HashMap<String,Link> LinkMap=new HashMap<String, Link>();
     HashSet<String> dateSet=new HashSet<String>();
    @Test
    public void read(){
        readData();
//        for(Map.Entry<String,Link> ele:LinkMap.entrySet()){
//            updateData(ele.getKey(),ele.getValue());
//        }
        try {
            long start=date.parse("2016-03-01 00:00:00").getTime();
            long end=date.parse("2016-05-31 23:59:59").getTime();
                            System.out.println(2016+"-"+new Date(start).getMonth()+"-"+new Date(start).getDate());


            for(;start<end; start=start+1000*60*2){
                readData1(2016+"-"+(new Date(start).getMonth()+1)+"-"+new Date(start).getDate(),new Date(start).getHours()+":"+new Date(start).getMinutes()+":"+new Date(start).getSeconds());
                System.out.println(2016+"-"+(new Date(start).getMonth()+1)+"-"+new Date(start).getDate());
                System.out.println(dateSet.size());
                System.out.println(LinkMap.size());
                Iterator<String> iterator=dateSet.iterator();
                int i=0;
//                while(iterator.hasNext()){
                    for(Map.Entry<String,Link> links:LinkMap.entrySet()){
                        if(!dateSet.contains(links.getKey())){
                            i++;
                        updateData(links.getKey(),links.getValue().getSpeed(),links.getValue().getSpeed(),0,true,2016+"-"+(new Date(start).getMonth()+1)+"-"+new Date(start).getDate(),new Date(start).getHours()+":"+new Date(start).getMinutes()+":"+new Date(start).getSeconds(),(i%(LinkMap.size()-dateSet.size()))==0);
//                            System.out.println(links.getKey()+links.getValue().getSpeed()+links.getValue().getSpeed()+0+true);
                        }
                    }

//                }


//
// System.out.println(new Date(start).getYear()+"-"+new Date(start).getMonth()+"-"+new Date(start).getDay());
//                System.out.println(new Date(start).getHours()+":"+new Date(start).getMinutes()+":"+new Date(start).getSeconds());
            }
//            System.out.println(start);

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try {
            System.out.println(new Date(date.parse("2016-03-01 00:00:00").getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
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
            ps = con.prepareStatement("select link_id,length,speed from link_normal_speed");
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
                Link link=new Link(rs.getString("link_id"),rs.getInt("length"),rs.getFloat("speed"));
                LinkMap.put(rs.getString("link_id"),link);
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
    public void readData1(String daydate,String daytime){
        dateSet.clear();
        if(con==null) {
            try {
                Class.forName(DBDRIVER); //1、使用CLASS 类加载驱动程序
                con = DriverManager.getConnection(DBURL, DBUSER, DBPASS); //2、连接数据库

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            System.out.println(daydate+daytime);
            ps = con.prepareStatement("select link_id from training_data where daydate=? and daytime=?");
            ps.setString(1,daydate);
            ps.setString(2,daytime);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
//                System.out.println(rs.getString("link_id"));
                dateSet.add(rs.getString("link_id"));
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
    public void updateData(String link_id,float speed,float aver_speed,float speed_offset,boolean is_missing_data,String daydate,String daytime,boolean exe){
        try {
            if(con==null) {

                try {
                    Class.forName(DBDRIVER); //1、使用CLASS 类加载驱动程序
                    con = DriverManager.getConnection(DBURL, DBUSER, DBPASS); //2、连接数据库
                    con.setAutoCommit(false);

                    ps = con.prepareStatement("insert into training_data(link_id,speed,speed_aver,speed_offset,is_missing_data,daydate,daytime) values(?,?,?,?,?,?,?)");
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
            ps.setString(1,link_id);
            ps.setFloat(2,speed);
            ps.setFloat(3,aver_speed);
            ps.setFloat(4,speed_offset);
            ps.setBoolean(5,is_missing_data);
            ps.setString(6,daydate);
            ps.setString(7,daytime);
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


