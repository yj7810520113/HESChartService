/**
 * Created by maomao on 2017/4/26.
 * yinjun104@qq.com
 */
package org.maomao.controller.ajax;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.Soundbank;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by maomao on 2017/4/26.
 */
@RestController
@RequestMapping(value = "/ajax/")
public class WebAPIProxyController {
    @ResponseBody
    @RequestMapping(value = "proxy",method = RequestMethod.GET)
    public String proxy(@RequestParam(value = "url",required=false) String url){
        System.out.println(url);
        return getWebCon(url);
    }
    public  String getWebCon(String domain) {
        // System.out.println("开始读取内容...("+domain+")");
        StringBuffer sb = new StringBuffer();
        try {
            java.net.URL url = new java.net.URL(domain);
            BufferedReader in = new BufferedReader(new InputStreamReader(url
                    .openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
        } catch (Exception e) { // Report any errors that arise
            return "error";
        }
        return sb.toString();
    }

}
