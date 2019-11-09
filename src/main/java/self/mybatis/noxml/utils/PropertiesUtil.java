/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package self.mybatis.noxml.utils;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * 全局配置类
 */

public class PropertiesUtil {

	/**
	 * 当前对象实例
	 */
	private static PropertiesUtil global = new PropertiesUtil();
	
	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = Maps.newHashMap();

	private static Environment environment;

	/**
	 * 获取当前对象实例
	 */
	public static PropertiesUtil getInstance() {
		return global;
	}

	public static void setEnvironment(Environment e){
			environment = e;
	}
	
	/**
	 * 获取配置
	 * @see {fns:getConfig('adminPath')}
	 */
	public static String getProperty(String key) {
		String value = map.get(key);
		if (value == null ){
			if(environment!=null)
				value = environment.getProperty(key);
			map.put(key, value != null ? value : StringUtils.EMPTY);
		}
		return value;
	}

	
}
