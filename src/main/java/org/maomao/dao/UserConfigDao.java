package org.maomao.dao;

import com.alibaba.fastjson.JSON;
import org.maomao.model.ScreenConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/5/5.
 */
@Repository("adminConfigDao")
public class UserConfigDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //添加配置文件至数据库中，若配置文件存在，则更新配置文件（是否存在，根据userName和screenName确定）
    public boolean userLoginDao(String userName,String password){
        int i=jdbcTemplate.queryForObject("select count(*) from userconfig where username=? and password=?",new String[]{userName,password},Integer.class);
        System.out.println("是否有该用户"+i);
        if(i==0){
            return false;
        }
        else{
            return true;
        }
    }
}
