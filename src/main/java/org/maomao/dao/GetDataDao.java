package org.maomao.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by maomao on 2017/8/24.
 * mail:yinjun104@gmail.com
 * homepage:http://www.mmcode.top
 */
@Repository("getDataDao")
public class GetDataDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List getPredictData(String dateTime){
        return this.jdbcTemplate.queryForList("select predict_datetimespan,link_id ,predict_arrive_time ,link_path  from predict_travel_time where predict_date=?",new String[]{dateTime});
    }
    public List getRoadStatus(String dateTIme){
        return this.jdbcTemplate.queryForList("select link_id ,concat(daydate,' ',daytime) as daydatetime ,speed_offset  from training_data where daydate=?",new String[]{dateTIme});
    }
    public List getRoadDay(String link,String dateTIme){
        return this.jdbcTemplate.queryForList("select link_id ,concat(daydate,' ',daytime) as daydatetime ,speed_offset  from training_data where link_id=? and daydate=?",new String[]{link,dateTIme});
    }
}
