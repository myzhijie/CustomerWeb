package com.jingsky.customer.util.util;

import java.util.LinkedList;
import java.util.List;

/**
 * 异常综合类。
 * @author 朱志杰 QQ：862990787
 * Sep 18, 2013 6:21:54 AM
 */
public class ExceptionUtil {
	
	/**
	 * 功能：得到异常的堆栈信息。包括异常信息简述，以及详细堆栈信息。
	 * @author 朱志杰 QQ：862990787
	 * Sep 18, 2013 6:26:04 AM
	 * @param e 异常
	 * @return List<String>
	 */
	public static List<String> getStackTrace(Exception e){
		List<String> errList=new LinkedList<String>();
		errList.add(e.toString());
		for(StackTraceElement line : e.getStackTrace()){
			errList.add(line.toString());
		}
		return errList;
	}

	/**
	 * 获取堆栈信息的第一行，即类最后执行到的地方，不是异常的message。
	 * @param e 异常
	 * @return String 如果异常信息不足两行，则返回null。
	 */
	public static String getFirstStackLine(Exception e){
		List<String> stackList=getStackTrace(e);
		return stackList.size()>=2 ? stackList.get(1) : null;
	}
}
