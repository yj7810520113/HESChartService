package org.maomao.filter;

import net.minidev.json.JSONObject;
import org.maomao.jwt.Jwt;
import org.maomao.jwt.TokenState;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/15.
 */
public class CheckTokenFilter  extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if(request.getRequestURI().endsWith("/admin/login")||request.getRequestURI().contains("preview")||request.getRequestURI().endsWith(".png")){
            //登陆接口不校验token，直接放行
            chain.doFilter(request, response);
            return;
        }
        //其他API接口一律校验token
        System.out.println("开始校验token");
        //从请求头中获取token
        String token=request.getHeader("token");
        Map<String, Object> resultMap= Jwt.validToken(token);
        TokenState state=TokenState.getTokenState((String)resultMap.get("state"));
        switch (state) {
            case VALID:
                //取出payload中数据,放入到request作用域中
                request.setAttribute("data", resultMap.get("data"));
                //放行
                chain.doFilter(request, response);
                break;
            case EXPIRED:
            case INVALID:
                System.out.println("无效token");
                //token过期或者无效，则输出错误信息返回给ajax
                JSONObject outputMSg=new JSONObject();
                outputMSg.put("success", false);
                outputMSg.put("msg", "您的token不合法或者过期了，请重新登陆");
//                output(outputMSg.toJSONString(), response);
                MyResponseRequestWrapper responseWrapper = new MyResponseRequestWrapper(response);
                responseWrapper.getWriter().write(outputMSg.toJSONString());
                break;
        }

    }
    public void output(String jsonStr,HttpServletResponse response) throws IOException{
        response.setContentType("text/html;charset=UTF-8;");
        PrintWriter out = response.getWriter();
//		out.println();
        out.write(jsonStr);
        out.flush();
        out.close();

    }

}
