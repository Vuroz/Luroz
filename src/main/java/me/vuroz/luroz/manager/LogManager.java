package me.vuroz.luroz.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManager {
	
	public static void log(Class<?> clazz, int level, String message) {
		Logger logger = LoggerFactory.getLogger(clazz);
		/**/ if (level == 0) logger.debug(message);
		else if (level == 1) logger.info(message);
		else if (level == 2) logger.warn(message);
		else if (level == 3) logger.error(message);
		else logger.error("'{}' is not a valid log level", level);
	}
	
	public static void log(Class<?> clazz, int level, String message, Object... arguments) {
		Logger logger = LoggerFactory.getLogger(clazz);
		/**/ if (level == 0) logger.debug(message, arguments);
		else if (level == 1) logger.info(message, arguments);
		else if (level == 2) logger.warn(message, arguments);
		else if (level == 3) logger.error(message, arguments);
		else logger.error("'{}' is not a valid log level", level);
	}

}
