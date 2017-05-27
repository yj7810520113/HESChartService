/**
 * Created by maomao on 2017/4/26.
 * yinjun104@qq.com
 */
package org.maomao.controller.ajax;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import org.hamcrest.Description;
import org.maomao.dao.HomeConfigDao;
import org.maomao.dao.ScreenConfigDao;
import org.maomao.jwt.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

/**
 * Created by maomao on 2017/4/26.
 */
@RestController
@RequestMapping(value = "/ajax/")
public class HomeConfigController {
    @Autowired
    private HomeConfigDao homeConfigDao;


    @ResponseBody
    @RequestMapping(value = "home/my")
    public Object addScreenConfigController(HttpServletRequest request){
        return homeConfigDao.HomeMyDao(getUIDController.getUID(request));
    }

    @ResponseBody
    @RequestMapping(value = "home/all/share",method = RequestMethod.GET)
    public Object findScreenConfigController(){
        return  homeConfigDao.HomeAllShareDao();
    }
    //根据用户判断大屏名称是否存在，若不存在则添加大屏，若存在返回大屏已经存在
    @ResponseBody
    @RequestMapping(value = "home/my/find",method = RequestMethod.POST)
    public boolean findScreenConfigController(HttpServletRequest request,@RequestParam(value = "screenName",required=false) String screenName,@RequestParam(value = "description",required=false) String screenDescription){
        System.out.println("正在查找该大屏是否存在,若存在该大屏，则插入该大屏");
        System.out.println(screenName);
        System.out.println(screenDescription);
        return  homeConfigDao.findAndAddScreenDao(getUIDController.getUID(request),screenName, screenDescription);
    }
    //复制大屏
    @ResponseBody
    @RequestMapping(value = "home/my/copy",method = RequestMethod.POST)
    public boolean copyScreenConfigController(HttpServletRequest request,@RequestParam(value = "id",required=false) String id,@RequestParam(value = "screenName",required=false) String screenName,@RequestParam(value = "description",required=false) String screenDescription){
        System.out.println("正在复制大屏");
        return  homeConfigDao.copyScreenDao(id,getUIDController.getUID(request),screenName, screenDescription);
    }
}
