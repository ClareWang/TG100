package ChinaSoftwareCup.FaceRecognition.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log
{
	static public void info(String infoMessage)
	{
		Logger logger = LoggerFactory.getLogger("EventLog");
		logger.info(infoMessage);
	}
	static public void error(String errorMessage)
	{
		Logger logger = LoggerFactory.getLogger("EventLog");
		logger.error(errorMessage);
	}
}