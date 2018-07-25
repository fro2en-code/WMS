package com.ymt.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ExceptionUtils implements HandlerExceptionResolver {
	private static final String returnMessage = "{\"retcode\":\"-1\",\"retmsg\":\"[exceptionMessage]\",\"suc\":false}";
	private static final Logger logger = Logger.getLogger(ExceptionUtils.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		logger.error("", ex);
		try {
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter pw = response.getWriter();
			pw.print(returnMessage.replace("[exceptionMessage]", (null != ex.getMessage()) ? ex.getMessage() : "内部错误"));
			pw.flush();
			pw.close();
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}

}
