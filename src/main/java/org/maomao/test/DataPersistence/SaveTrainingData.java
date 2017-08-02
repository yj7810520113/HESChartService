package org.maomao.test.DataPersistence;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.hashids.Hashids;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.maomao.test.DataSource.DataSource.*;

/**
 * Created by maomao on 2017/8/1.
 * mail:yinjun104@gmail.com
 * homepage:http://www.mmcode.top
 * 第二部
 */
public class SaveTrainingData {
    Hashids hashids = new Hashids("minechicken");
    public static Connection con=null;
    public static String sql="insert into training_data(link_id,daydate,daytime,travel_time) values(?,?,?,?)";
    public static PreparedStatement ps = null;
    SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    SimpleDateFormat time=new SimpleDateFormat("YYYY-mm-DD");

    @Test
    public void read(){
        int lineCount=0;
        try {
            List<String> lines=Files.readAllLines(Paths.get("C:\\Users\\Administrator\\Desktop\\maomao\\tianchi\\数据集\\gy_contest_link_traveltime_training_data.txt"));
            System.out.println(lines.size());
            for(lineCount=1;lineCount<lines.size();lineCount++){
                String[] ele=lines.get(lineCount).split(";");
//                System.out.println("date"+date.format(ele[2].replaceAll("[\\[\\)]","").split(",")[0]));
                saveData(ele[0].replaceAll("\\d{9}(\\d{4})\\d{6}","$1"),ele[1],ele[2].replaceAll("[\\[\\)]","").split(",")[0],Float.parseFloat(ele[3]),(lineCount%1000==0||lineCount==lines.size()-1)?true:false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void saveData(String link_id,String datetime1,String datetime2,float traver_time,boolean exe){
        if(con==null) {
            try {
                Class.forName(DBDRIVER); //1、使用CLASS 类加载驱动程序
                con = DriverManager.getConnection(DBURL, DBUSER, DBPASS); //2、连接数据库
                con.setAutoCommit(false);
                ps = con.prepareStatement(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ps.setString(1,link_id);
            ps.setString(2,datetime1);
            ps.setString(3,datetime2);
            ps.setFloat(4,traver_time);
            ps.addBatch();
            if(exe==true){
                long start=(new Date()).getTime();
                ps.executeBatch();
                con.commit();
                ps=null;
                con.close();
                con=null;
                System.out.println(((new Date()).getTime()-start)/1000+"s");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}


