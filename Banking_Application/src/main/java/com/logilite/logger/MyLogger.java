package com.logilite.logger;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class MyLogger
{
	public static Logger logger = LogManager.getLogger(MyLogger.class);

	static
	{
//		PropertyConfigurator
//				.configure("/home/kalpesh/git/repository/Banking_Application/log4j.properties");
		PropertyConfigurator
		.configure(System.getProperty("user.home") + System.getProperty("file.separator") + "log4j.properties");

	}
	
	//user.home
	//user.dir
}
