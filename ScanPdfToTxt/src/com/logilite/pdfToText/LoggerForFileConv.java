package com.logilite.pdfToText;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class LoggerForFileConv
{

	public static Logger logger = Logger.getLogger(com.logilite.pdfToText.LoggerForFileConv.class.getName());

	static
	{
		try
		{
			Layout layout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L -%m%n");

			ConsoleAppender consoleHandler = new ConsoleAppender(layout);

			Appender appender = new FileAppender(layout,
					System.getProperty("user.dir") + System.getProperty("file.separator") + "logFile.log");

			logger.addAppender(appender);

			logger.addAppender(consoleHandler);

			BasicConfigurator.configure(appender);

		}
		catch (Exception e)
		{
			logger.log(Level.WARN, e);
		}
	}
}