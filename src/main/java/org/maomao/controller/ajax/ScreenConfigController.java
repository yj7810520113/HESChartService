/**
 * Created by maomao on 2017/4/26.
 * yinjun104@qq.com
 */
package org.maomao.controller.ajax;

import com.alibaba.fastjson.JSONObject;
import org.hashids.Hashids;
import org.maomao.dao.ScreenConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by maomao on 2017/4/26.
 */
@RestController
@RequestMapping(value = "/ajax/")
public class ScreenConfigController {

    private ScreenConfigDao screenConfigDao;
    public ScreenConfigDao getScreenConfigDao() {
        return screenConfigDao;
    }
    @Autowired
    public void setScreenConfigDao(ScreenConfigDao screenConfigDao) {
        this.screenConfigDao = screenConfigDao;
    }


    @ResponseBody
    @RequestMapping(value = "screen/add/config")
        public String addScreenConfigController(@RequestParam(value = "userName",required=false) String userName,@RequestParam(value = "screenName",required=false) String screenName,@RequestParam(value = "gridConfig",required=false) String gridConfig,@RequestParam(value = "hescList",required=false) String hescList,@RequestParam(value = "hashUrl",required=false) String hashUrl){
        System.out.println(userName);
        System.out.println(screenName);
        System.out.println(gridConfig);
        System.out.println(hescList);
        System.out.println((hescList+gridConfig).hashCode());
        System.out.println(hashUrl);
        screenConfigDao.addScreenConfig(userName,screenName,gridConfig,hescList,hashUrl);
        return "true";
    }

    @ResponseBody
    @RequestMapping(value = "screen/find/config",method = RequestMethod.GET)
    public Object findScreenConfigController(@RequestParam(value = "userName",required=false) String userName,@RequestParam(value = "screenName",required=false) String screenName){//这里的encode为hash加密后的值
        System.out.println("正在查找用户名和大屏名称");
        return  screenConfigDao.findScreenConfig(userName,screenName);
    }
}
