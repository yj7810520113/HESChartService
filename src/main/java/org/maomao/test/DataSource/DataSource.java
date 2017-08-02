package org.maomao.test.DataSource;

/**
 * Created by maomao on 2017/8/1.
 * mail:yinjun104@gmail.com
 * homepage:http://www.mmcode.top
 */
public class DataSource {
    //驱动程序就是之前在classpath中配置的JDBC的驱动程序的JAR 包中
    public static final String DBDRIVER = "com.mysql.jdbc.Driver";
    //连接地址是由各个数据库生产商单独提供的，所以需要单独记住
    public static final String DBURL = "jdbc:mysql://localhost:3306/minechicken_road";
    //连接数据库的用户名
    public static final String DBUSER = "root";
    //连接数据库的密码
    public static final String DBPASS = "";
}
