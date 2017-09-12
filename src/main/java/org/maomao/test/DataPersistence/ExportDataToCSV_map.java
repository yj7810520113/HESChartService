package org.maomao.test.DataPersistence;

import org.junit.Test;

import java.io.File;
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
 * 第8步
 * 将每天的预测数据转换为csv文件，利用cdn读取
 */
public class ExportDataToCSV_map {
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
            File file=new File("D:/mine/map/"+daydate.format(new Date(i))+".csv");
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
            System.out.println("http://cdn.mmcode.top/cdn/mr/map/"+str+".csv");

        }
    }



    public String readData1(String daydate){
//        dateSet.clear();
        StringBuilder str=new StringBuilder();
        str.append("link_id,daydatetime,speed_offset\n");
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
            ps = con.prepareStatement("select link_id ,concat(daydate,' ',daytime) as daydatetime ,speed_offset  from training_data where daydate=?");
            ps.setString(1,daydate);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {

                str.append(rs.getString(1));
                str.append(",");
                str.append(rs.getString(2));
                str.append(",");
                str.append(rs.getString(3));
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
