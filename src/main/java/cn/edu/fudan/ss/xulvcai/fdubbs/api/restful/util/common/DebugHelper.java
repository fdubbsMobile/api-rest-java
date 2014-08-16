package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.common;

import java.util.Calendar;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugHelper {

	private static final boolean debugSupported = true;
	private static Logger logger = LoggerFactory.getLogger(DebugHelper.class);

	public static boolean shouldGenerateDebugData() {
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		int shanghaiHour = calendar.get(Calendar.HOUR_OF_DAY);

		boolean debug = debugSupported && (shanghaiHour < 9);

		logger.info("shouldGenerateDebugData : " + debug);
		return debug;
	}
}
