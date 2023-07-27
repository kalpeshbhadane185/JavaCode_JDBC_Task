package com.logilite.logger;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class MyLogger
{
	public static Logger logger = LogManager.getLogger(MyLogger.class);

	static
	{
		PropertyConfigurator
				.configure(System.getProperty("user.dir") + System.getProperty("file.separator") + "log4j.properties");
	}
}
