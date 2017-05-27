package org.maomao.dao;

import com.alibaba.fastjson.JSON;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.maomao.model.ScreenConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2017/5/5.
 */
@Repository("screenConfigDao")
public class ScreenConfigDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //添加配置文件至数据库中，若配置文件存在，则更新配置文件（是否存在，根据userName和screenName确定）
    public void addScreenConfig(String id,String gridConfig,String hescList,String hashUrl){
        int i=jdbcTemplate.queryForObject("select count(*) from screenConfig where id="+id,new Integer[]{},Integer.class);
        System.out.println("长度为"+i);
        if(i==0){
//            jdbcTemplate.update("insert into screenConfig(userName,screenName,gridConfig,hescList,hashUrl) values(?,?,?,?,?)", userName,screenName,gridConfig,hescList,hashUrl);
        }
        else{
            jdbcTemplate.update("update  screenConfig set gridConfig=?,hescList=? where id=?",gridConfig,hescList,id);

        }
    }
    //添加配置文件至数据库中，若配置文件存在，则更新配置文件（是否存在，根据userName和screenName确定）
    public boolean updateScreenConfig(String id,String gridConfig,String hescList,String hashUrl){
           return jdbcTemplate.update("update  screenConfig set gridConfig=?,hescList=? where id=?",gridConfig,hescList,id)>0?true:false;
    }
    //保存图片
    public void updateScreenImgConfigDao(String id,String imgUrl){
        jdbcTemplate.update("update screenConfig set imgUrl=? where id=?",imgUrl,id);
    }
    public ScreenConfigModel findScreenConfig(String id, String userName){
         return jdbcTemplate.queryForObject("select gridConfig,hescList from screenconfig where id=? and userName=?",new String[]{id,userName}, new RowMapper<ScreenConfigModel>() {
                public ScreenConfigModel mapRow(ResultSet rs,int rowNum) throws SQLException {
                    ScreenConfigModel screenConfigModel=new ScreenConfigModel();
                    screenConfigModel.setGridConfig(JSON.parse(rs.getString("gridConfig")));
                    screenConfigModel.setHescList(JSON.parse(rs.getString("hescList")));
                    return screenConfigModel;
                }
            });
    }
    public ScreenConfigModel findScreenPreviewConfig(String id, String userName){
        return jdbcTemplate.queryForObject("select gridConfig,hescList from screenconfig where id=?",new String[]{id}, new RowMapper<ScreenConfigModel>() {
            public ScreenConfigModel mapRow(ResultSet rs,int rowNum) throws SQLException {
                ScreenConfigModel screenConfigModel=new ScreenConfigModel();
                screenConfigModel.setGridConfig(JSON.parse(rs.getString("gridConfig")));
                screenConfigModel.setHescList(JSON.parse(rs.getString("hescList")));
                return screenConfigModel;
            }
        });
    }
    public boolean deleteScreenConfig(String userName,String id){
        return jdbcTemplate.update("delete from screenConfig where id=? and userName=?",id,userName)>0?true:false;
    }
}
