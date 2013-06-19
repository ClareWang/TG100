package ChinaSoftwareCup.FaceRecognition.main;

import static com.googlecode.javacv.cpp.opencv_core.*;  
import static com.googlecode.javacv.cpp.opencv_highgui.*;  

import org.apache.log4j.xml.DOMConfigurator;

import com.googlecode.javacv.OpenCVFrameGrabber;

import ChinaSoftwareCup.FaceRecognition.ImageProcessing.FaceDetect;
import ChinaSoftwareCup.FaceRecognition.ImageProcessing.Image;
import ChinaSoftwareCup.FaceRecognition.ImageProcessing.base.ImageColor;
import ChinaSoftwareCup.FaceRecognition.camera.CameraCapture;
import ChinaSoftwareCup.FaceRecognition.log.Log;
import ChinaSoftwareCup.FaceRecognition.vedio.Vedio;


public class JavaCVTest {
	
		
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//DOMConfigurator.configure("log4j.xml");
		Log.info("Start!");
		
		//IplImage image=Image.readImage("D:\\whf.jpg",ImageColor.COLOR);//load the image
		//FaceDetect fd=new FaceDetect(); // detect the face of image
		//IplImage output=fd.doFaceDetect(image);
		IplImage image=null;
		//image=CameraCapture.doCapture();
		//Image.showImage(image);
		//Vedio.test();
		//Image.writeImage(image, "D:\\whf123.jpg");
		//Image.showImage(output);
		Log.info("End!");
	}

}
