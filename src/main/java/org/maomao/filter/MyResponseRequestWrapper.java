package org.maomao.filter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Created by Administrator on 2017/5/15.
 */
public class MyResponseRequestWrapper extends HttpServletResponseWrapper {
    public MyResponseRequestWrapper(HttpServletResponse response) {
        super(response);
    }
}
