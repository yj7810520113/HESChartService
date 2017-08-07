package org.maomao.test.DataPersistence;

import org.hashids.Hashids;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.maomao.test.DataSource.DataSource.*;

/**
 * Created by maomao on 2017/8/2.
 * mail:yinjun104@gmail.com
 * homepage:http://www.mmcode.top
 * 第四步
 */
public class UpdateTrainingData_SpeedOffset {
    Hashids hashids = new Hashids("minechicken");
    public static Connection con=null;
    public static String sql="update training_data  set speed_offset=? where link_id=?";
    public static PreparedStatement ps = null;
    SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    SimpleDateFormat time=new SimpleDateFormat("YYYY-mm-DD");
     HashMap<String,Float> LinkIDSpeed=new HashMap<String, Float>();
    @Test
    public void read(){
        readData();
        for(Map.Entry<String,Float> ele:LinkIDSpeed.entrySet()){
            updateData(ele.getKey(),ele.getValue());
        }



    }
    public void readData(){
        if(con==null) {
            try {
                Class.forName(DBDRIVER); //1、使用CLASS 类加载驱动程序
                con = DriverManager.getConnection(DBURL, DBUSER, DBPASS); //2、连接数据库
                ps = con.prepareStatement("select link_id,speed from link_normal_speed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
                LinkIDSpeed.put(rs.getString("link_id"),rs.getFloat("speed"));
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
    public void updateData(String link_id,float speed_aver){
        if(con==null) {
            try {
                Class.forName(DBDRIVER); //1、使用CLASS 类加载驱动程序
                con = DriverManager.getConnection(DBURL, DBUSER, DBPASS); //2、连接数据库
                ps = con.prepareStatement(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ps.setString(1,link_id);
            ps.setFloat(2,speed_aver);
            long start=new Date().getTime();
            ps.execute();
//            con.commit();
            ps=null;
            con.close();
            con=null;
            System.out.println(link_id+":"+((new Date()).getTime()-start)/1000+"s");

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}


