package org.maomao.test.DataPersistence;

import org.hashids.Hashids;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.maomao.test.DataSource.DataSource.*;

/**
 * Created by maomao on 2017/8/2.
 * mail:yinjun104@gmail.com
 * homepage:http://www.mmcode.top
 * 第四步
 */
public class CalcAverSpeed {
    Hashids hashids = new Hashids("minechicken");
    public static Connection con=null;
    public static String sql="update training_data  set length=?,width=? where link_id=?";
    public static PreparedStatement ps = null;
    SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    SimpleDateFormat time=new SimpleDateFormat("YYYY-mm-DD");

    @Test
    public void read(){
        int lineCount=0;
        try {
            List<String> lines=Files.readAllLines(Paths.get("C:\\Users\\Administrator\\Desktop\\maomao\\tianchi\\数据集\\gy_contest_link_info.txt"));
            System.out.println(lines.size());
            for(lineCount=1;lineCount<lines.size();lineCount++){
                String[] ele=lines.get(lineCount).split(";");
//                System.out.println("date"+date.format(ele[2].replaceAll("[\\[\\)]","").split(",")[0]));
                saveData(ele[0].replaceAll("\\d{9}(\\d{4})\\d{6}","$1"),Integer.parseInt(ele[1]),Integer.parseInt(ele[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void saveData(String link_id,int length,int width){
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
            ps.setString(3,link_id);
            ps.setInt(1,length);
            ps.setInt(2,width);
            ps.addBatch();
            long start=(new Date()).getTime();
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


