package org.maomao.dao;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.maomao.model.ScreenConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/5/5.
 */
@Repository("screenConfigDao")
public class ScreenConfigDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addScreenConfig(String userName,String screenName,String gridConfig,String hescList,String hashUrl){
        System.out.println(jdbcTemplate);
        jdbcTemplate.update("insert into screenConfig(userName,screenName,gridConfig,hescList,hashUrl) values(?,?,?,?,?)", userName,screenName,gridConfig,hescList,hashUrl);
    }
    public Object findScreenConfig(String userName,String screenName){
        System.out.println("select gridConfig,hescList from screenconfig where username='"+userName+"' and screenName='"+screenName+"'");
            return jdbcTemplate.queryForObject("select gridConfig,hescList from screenconfig where username='"+userName+"' and screenName='"+screenName+"'",ScreenConfigModel.class);
    }
}
