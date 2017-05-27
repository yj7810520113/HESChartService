package org.maomao.dao;

import org.maomao.model.HomeConfigModel;
import org.maomao.model.ScreenConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2017/5/5.
 */
@Repository("homeConfigDao")
public class HomeConfigDao {
    private String hescList;
    private String gridConfig;
    @Value("#{prop.hescList}")
    public void setHescList(String hescList) {
        this.hescList = hescList;
    }
    @Value("#{prop.gridConfig}")
    public void setGridConfig(String gridConfig) {
        this.gridConfig = gridConfig;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<HomeConfigModel> HomeMyDao(String userName){
        return jdbcTemplate.query("select id,screenName, imgUrl from screenconfig where userName=? order by updateTime desc", new String[]{userName},new HomeConfigRowMapper());
    }
    public List<HomeConfigModel> HomeAllDao(){
        return jdbcTemplate.query("select id,screenName,imgUrl from screenconfig order by updateTime desc", new HomeConfigRowMapper());
    }
    public List<HomeConfigModel> HomeAllShareDao(){
        return jdbcTemplate.query("select id,screenName,imgUrl from screenconfig where share=1 order by updateTime desc", new HomeConfigRowMapper());
    }

    public boolean findAndAddScreenDao(String userName,String screenName,String description){
        System.out.println(hescList);
        boolean hasScreenName=jdbcTemplate.queryForObject("select count(*) from screenconfig where userName=? and screenName=?",new String[]{userName,screenName},Integer.class)>0?true:false;
        if(hasScreenName==false){
            jdbcTemplate.update("insert into screenconfig(userName,screenName,gridConfig,hescList,imgUrl,description) values(?,?,?,?,?,?)",userName,screenName,gridConfig,hescList,"/img/me.png",description);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean copyScreenDao(String id,String userName,String screenName,String description){
        System.out.println(hescList);
        boolean hasScreenName=jdbcTemplate.queryForObject("select count(*) from screenconfig where userName=? and screenName=?",new String[]{userName,screenName},Integer.class)>0?true:false;
        if(hasScreenName==false){
            ScreenConfigModel screenConfigModel=jdbcTemplate.queryForObject("select gridConfig,hescList,imgUrl from screenconfig where id=?",new String[]{id},new RowMapper< ScreenConfigModel>(){
                public ScreenConfigModel mapRow(ResultSet rs, int rowNum) throws SQLException{
                    ScreenConfigModel screenConfigModel=new ScreenConfigModel();
                    screenConfigModel.setGridConfig(rs.getString("gridConfig"));
                    screenConfigModel.setHescList(rs.getString("hescList"));
                    screenConfigModel.setImgUrl(rs.getString("imgUrl"));
                    return screenConfigModel;
                }
            });
            jdbcTemplate.update("insert into screenconfig(userName,screenName,gridConfig,hescList,imgUrl,description) values(?,?,?,?,?,?)",userName,screenName,screenConfigModel.getGridConfig(),screenConfigModel.getHescList(),screenConfigModel.getImgUrl(),description);
            return true;
        }
        else{
            return false;
              }
    }

    public class HomeConfigRowMapper implements RowMapper<HomeConfigModel>{
        public HomeConfigModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            HomeConfigModel homeConfigModel=new HomeConfigModel();
            homeConfigModel.setId(rs.getInt("id"));
            homeConfigModel.setScreenName(rs.getString("screenName"));
            homeConfigModel.setImgUrl(rs.getString("imgUrl"));
            return homeConfigModel;
        }
    }


}