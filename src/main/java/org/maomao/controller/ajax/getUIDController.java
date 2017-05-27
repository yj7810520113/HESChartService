package org.maomao.controller.ajax;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * Created by Administrator on 2017/5/17.
 */
public class getUIDController {
    public static String getUID(HttpServletRequest request){
        String token=request.getHeader("token");
        String uid="";
        JWSObject jwsObject = null;
        try {
            jwsObject = JWSObject.parse(token);
            Payload payload = jwsObject.getPayload();
            uid= (String)payload.toJSONObject().get("uid");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return uid;
    }
}
