package ChinaSoftwareCup.FaceRecognition.vedio;

import ChinaSoftwareCup.FaceRecognition.ImageProcessing.Image;

import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class Vedio
{
	public static void test(String filePath) throws Exception 
	{
		IplImage pFrame=null;
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(filePath);
		grabber.start();
		pFrame = grabber.grab();
        while( pFrame!=null )
        {
        	Image.showImage(pFrame);
        	pFrame = grabber.grab();
        }
        grabber.stop();
	}
}