package ChinaSoftwareCup.FaceRecognition.main;

import static com.googlecode.javacv.cpp.opencv_core.*;  
import static com.googlecode.javacv.cpp.opencv_highgui.*;  

import org.apache.log4j.xml.DOMConfigurator;

import ChinaSoftwareCup.FaceRecognition.ImageProcessing.FaceDetect;
import ChinaSoftwareCup.FaceRecognition.ImageProcessing.Image;
import ChinaSoftwareCup.FaceRecognition.log.Log;


public class JavaCVTest {
	
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//DOMConfigurator.configure("log4j.xml");
		Log.info("Start!");
		IplImage image=cvLoadImage("D:\\whf.jpg");//load the image
		
		FaceDetect fd=new FaceDetect(); // detect the face of image
		IplImage output=fd.doFaceDetect(image);
		Image.showImage(image);
		Image.showImage(output);
		
		Log.info("End!");
	}

}
