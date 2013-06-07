package ChinaSoftwareCup.FaceRecognition.main;

import static com.googlecode.javacv.cpp.opencv_core.*;  
import static com.googlecode.javacv.cpp.opencv_highgui.*;  

import org.apache.log4j.xml.DOMConfigurator;

import ChinaSoftwareCup.FaceRecognition.log.Log;


public class JavaCVTest {
	
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DOMConfigurator.configure("log4j.xml");
		Log.info("Start!");
		IplImage image=cvLoadImage("D:\\bitmap.bmp");//load the image 
		if(null!=image)
		{
			cvNamedWindow("Example",CV_WINDOW_AUTOSIZE); //create a window to deploy the image
		    cvShowImage("Example",image);//deploy the image
		    cvWaitKey(0);//if press any button, destroy the window and image
		    cvReleaseImage(image); 
		}
		Log.info("End!");

	}

}
