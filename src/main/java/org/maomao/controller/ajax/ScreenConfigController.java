/**
 * Created by maomao on 2017/4/26.
 * yinjun104@qq.com
 */
package org.maomao.controller.ajax;

import com.alibaba.fastjson.JSONObject;
import javafx.application.Application;
import org.hashids.Hashids;
import org.maomao.dao.ScreenConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Date;
import sun.misc.BASE64Decoder;

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
    public String addScreenConfigController(@RequestParam(value = "id",required=false) String id,@RequestParam(value = "gridConfig",required=false) String gridConfig,@RequestParam(value = "hescList",required=false) String hescList,@RequestParam(value = "hashUrl",required=false) String hashUrl){
        System.out.println(id);
        System.out.println(gridConfig);
        System.out.println(hescList);
        System.out.println((hescList+gridConfig).hashCode());
        System.out.println(hashUrl);
        screenConfigDao.addScreenConfig(id,gridConfig,hescList,hashUrl);
        return "true";
    }

    @ResponseBody
    @RequestMapping(value = "screen/update/config")
    public boolean updateScreenConfigController(@RequestParam(value = "id",required=false) String id,@RequestParam(value = "gridConfig",required=false) String gridConfig,@RequestParam(value = "hescList",required=false) String hescList,@RequestParam(value = "hashUrl",required=false) String hashUrl){
        System.out.println(id);
        System.out.println(gridConfig);
        System.out.println(hescList);
        System.out.println((hescList+gridConfig).hashCode());
        System.out.println(hashUrl);
        return screenConfigDao.updateScreenConfig(id,gridConfig,hescList,hashUrl);
    }

    @ResponseBody
    @RequestMapping(value = "screen/find/config",method = RequestMethod.GET)
    public Object findScreenConfigController(HttpServletRequest request,@RequestParam(value = "id",required=false) String id){//这里的encode为hash加密后的值
        System.out.println("正在查找用户名和大屏名称");
        return  screenConfigDao.findScreenConfig(id,getUIDController.getUID(request));
    }
    @ResponseBody
    @RequestMapping(value = "screen/find/config/preview",method = RequestMethod.GET)
    public Object findScreenPreviewConfigController(HttpServletRequest request,@RequestParam(value = "id",required=false) String id){//这里的encode为hash加密后的值
        System.out.println("正在查找用户名和大屏名称");
        return  screenConfigDao.findScreenPreviewConfig(id,getUIDController.getUID(request));
    }

    @ResponseBody
    @RequestMapping(value = "screen/delete/config")
    public boolean deleteScreenConfigController(HttpServletRequest request,@RequestParam(value = "id",required=false) String id){
        return screenConfigDao.deleteScreenConfig(getUIDController.getUID(request),id);
    }

    @RequestMapping("screen/update/img")
    public String  updateScreenImgController(@RequestParam(value = "id",required=false) String id,@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request) throws IOException {
        long  startTime=System.currentTimeMillis();
        System.out.println("fileName："+file.getOriginalFilename());
        //保存img至服务器
        String filePrefixName=new Date().getTime()+file.getOriginalFilename();
        String path=request.getSession().getServletContext().getRealPath("")+"/img/"+filePrefixName;

        File newFile=new File(path);
        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(newFile);
        long  endTime=System.currentTimeMillis();
        System.out.println("保存图片耗时："+String.valueOf(endTime-startTime)+"ms");
        screenConfigDao.updateScreenImgConfigDao(id,"/img/"+filePrefixName);
        return "success";
    }
    @RequestMapping("screen/update/imgBase64")
    public String  updateScreenImgBaseController(@RequestParam(value = "id",required=false) String id,@RequestParam("file") String file, HttpServletRequest request) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        String filePrefixName=id+"-"+new Date().getTime();
        try
        {
            //Base64解码
            byte[] b = decoder.decodeBuffer(file.split(",")[1]);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            String imgFilePath = request.getSession().getServletContext().getRealPath("")+"/img/"+filePrefixName+".png";//新生成的图片
            System.out.println(imgFilePath);
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
        }
        catch (Exception e)
        {
        }
        screenConfigDao.updateScreenImgConfigDao(id,"/img/"+filePrefixName+".png");
        return "success";
    }
}
