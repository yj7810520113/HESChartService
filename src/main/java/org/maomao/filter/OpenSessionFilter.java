package org.maomao.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.Session; 
import org.hibernate.SessionFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

//��ȡspring�Ĺ�����hibernate��sessionfactory
//��Ҫ�ڱ�ʾ���ȡspring�Ĺ��������λ��hibernate��sessionfactory
//���ϣ����dao���ȡ��ʾ�����ݣ�Ӧ�ý�Ҫ��ȡ����ݴ洢��ThreadLocal��
public class OpenSessionFilter implements Filter{
	//spring �Ĺ�����init�л�ȡ
	private static WebApplicationContext wac;
	private static SessionFactory sessionFactory;

	private static ThreadLocal<Session> sessionHolder=new ThreadLocal<Session>();
	private static void setSession(Session session){
		sessionHolder.set(session);
	}
	public  static Session getSession(){
		return sessionHolder.get();
	}
	@SuppressWarnings("unused")
	private static void removeSession(){
		sessionHolder.remove();
		if(!sessionFactory.isClosed()){
			sessionFactory.close();
		}
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		//����session 
		/*
		 * ��Ҫ��ȡspring�Ĺ���
		 * ���ʹ��classpathxmlapplicationcontext��ȡ�Ļ���
		 * ����һ���¹�������servlet��ʼ���Ĺ�����2������
		 * ����ͨ��������ȡ
		 * ��spring��ͨ��
		 */
		//System.out.println(request.toString()+"   "+request);
		try{
			System.out.println("openSession");
		setSession(sessionFactory.openSession());
		chain.doFilter(request, response);
		}
		finally{ 
			//System.out.println("removeSession");
//			removeSession();
		}
		//�ر�session
		
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		///��ȡweb�е�spring������
		wac=WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
		sessionFactory=(SessionFactory) wac.getBean("sessionFactory");
	}

}
