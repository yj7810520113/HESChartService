package org.maomao.test.DataPersistence;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.hashids.Hashids;
import org.junit.Test;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.*;

import static org.maomao.test.DataSource.DataSource.*;

/**
 * Created by maomao on 2017/8/1.
 * mail:yinjun104@gmail.com
 * homepage:http://www.mmcode.top
 * 第一步
 */
public class SaveLinkInfo {
    Hashids hashids = new Hashids("MineChicken",0);
    public static Connection con=null;
    public static String sql="insert into link_info(link_id,length,width,link_class) values(?,?,?,?)";
    public static PreparedStatement ps = null;

    @Test
    public void read(){
        int lineCount=0;
        Set<String> s=new HashSet<String>();
        try {
            List<String> lines=Files.readAllLines(Paths.get("C:\\Users\\Administrator\\Desktop\\maomao\\tianchi\\数据集\\gy_contest_link_info.txt"));
            System.out.println(lines.size());
            for(lineCount=1;lineCount<lines.size();lineCount++){
                String[] ele=lines.get(lineCount).split(";");
//                System.out.println(ele[0].replaceAll("\\d{9}(\\d{4})\\d{6}","$1"));
                saveData(ele[0].replaceAll("\\d{9}(\\d{4})\\d{6}","$1"),Integer.parseInt(ele[1]),Integer.parseInt(ele[2]),Integer.parseInt(ele[3]),(lineCount%1000==0||lineCount==lines.size()-1)?true:false);
            }
            System.out.println(s.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void saveData(String link_id,int length,int width,int link_class,boolean exe){
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
            ps.setInt(2,length);
            ps.setInt(3,width);
            ps.setInt(4,link_class);
            ps.addBatch();
            if(exe==true){
                ps.executeBatch();
                con.commit();
                ps=null;
                con.close();

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}


