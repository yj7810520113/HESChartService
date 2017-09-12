package org.maomao.controller.ajax;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.maomao.dao.GetDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;


/**
 * Created by maomao on 2017/8/24.
 * mail:yinjun104@gmail.com
 * homepage:http://www.mmcode.top
 */
@Controller
@RequestMapping(value = "ajax/")
public class GetDataController {
    @Autowired
    private GetDataDao getDataDao;
    @RequestMapping(value = "getpredictdata/{datetime}")
    @ResponseBody
    public Object getPredictData(@PathVariable("datetime") String dateTime){
        return getDataDao.getPredictData(dateTime);
    }
    @RequestMapping(value = "getroadstatus/{datetime}")
    @ResponseBody
    public List getRoadStatus(@PathVariable("datetime") String dateTime){
        long start=new Date().getTime();
        List obj=getDataDao.getRoadStatus(dateTime);
        System.out.println((new Date().getTime()-start));
        return obj;
    }
    @RequestMapping(value = "getroadday/{link}/{datetime}")
    @ResponseBody
    public List getRoadStatus(@PathVariable("link") String link,@PathVariable("datetime") String dateTime){
        long start=new Date().getTime();
        List obj=getDataDao.getRoadDay(link,dateTime);
        System.out.println((new Date().getTime()-start));
        return obj;
    }
}
