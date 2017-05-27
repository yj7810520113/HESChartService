package org.maomao.controller.ajax;

import com.alibaba.fastjson.JSONObject;
import org.maomao.dao.UserConfigDao;
import org.maomao.jwt.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/12.
 */
@RestController
@RequestMapping(value = "/admin/")
public class UserConfigController {
    @Autowired
    private UserConfigDao userConfigDao;

    @ResponseBody
    @RequestMapping(value = "login")
    public JSONObject addScreenConfigController(@RequestParam(value = "userName",required=false) String userName, @RequestParam(value = "passWord",required=false) String passWord, HttpServletRequest request, HttpServletResponse response){
        if(userConfigDao.userLoginDao(userName,passWord)){
            //生成token
            Map<String , Object> payload=new HashMap<String, Object>();
            Date date=new Date();
            payload.put("uid", userName);//用户ID
            payload.put("iat", date.getTime());//生成时间
            payload.put("ext",date.getTime()+1000*60*60*24*60);//过期时间2个月
            String token= Jwt.createToken(payload);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("success",true);
            jsonObject.put("token",token);
            return jsonObject;
        }
        else{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("success",false);
            return jsonObject;
        }
    }
}
