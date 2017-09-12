package org.maomao.test.DataPersistence;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.maomao.test.DataSource.DataSource.*;

/**
 * Created by maomao on 2017/9/5.
 * mail:yinjun104@gmail.com
 * homepage:http://www.mmcode.top
 * 第7步
 * 将每天的预测数据转换为csv文件，利用cdn读取
 */
public class ExportDataToCSV_predict {
    public static Connection con=null;
    public static PreparedStatement ps = null;
    SimpleDateFormat daydate=new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void run() throws IOException {
        long start=1456761600000L;
        long end=1464710400000L;
        for(long i=start;i<end;i=i+1000*60*60*24){
            System.out.println(daydate.format(new Date(i)));
            String str=readData1(daydate.format(new Date(i)));
            File file=new File("D:/mine/predict/"+daydate.format(new Date(i))+".csv");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            out.write(str.getBytes());
            out.close();
        }
    }


//    七牛云预读url
    @Test
    public void run1(){
        long start=1456761600000L;
        long end=1464710400000L;
        for(long i=start;i<end;i=i+1000*60*60*24){
            String str=daydate.format(new Date(i));
            System.out.println("http://cdn.mmcode.top/cdn/mr/predict/"+str+".csv");

        }
    }


    public String readData1(String daydate){
//        dateSet.clear();
        StringBuilder str=new StringBuilder();
        str.append("predict_datetimespan,link_id,predict_arrive_time,link_path\n");
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
            ps = con.prepareStatement("select date_format(predict_datetimespan, '%Y-%m-%d %H:%i:%s') ,link_id ,date_format(predict_arrive_time, '%Y-%m-%d %H:%i:%s') ,link_path  from predict_travel_time where predict_date=?");
            ps.setString(1,daydate);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {

                str.append(rs.getString(1));
                str.append(",");
                str.append(rs.getString(2));
                str.append(",");
                str.append(rs.getString(3));
                str.append(",");
                str.append(rs.getInt(4));
                str.append("\n");
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
        return str.toString().substring(0,str.toString().length()-1);
    }
}
