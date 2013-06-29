package ChinaSoftwareCup.FaceRecognition.vedio;

import ChinaSoftwareCup.FaceRecognition.ImageProcessing.FaceDetect;
import ChinaSoftwareCup.FaceRecognition.ImageProcessing.Image;
import ChinaSoftwareCup.FaceRecognition.lda.Lda;

import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class Vedio
{
	public static void doFaceRecognition(String filePath,Lda lda) throws Exception 
	{
		IplImage pFrame=null;
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(filePath);
		grabber.start();
		pFrame = grabber.grab();
		FaceDetect fd=new FaceDetect();
		lda.init();
        while( pFrame!=null )
        {
        	IplImage [] p=fd.getAllFaces(pFrame);       	
        	if(p!=null)
        	{
        		for(int i=0;i<p.length;i++)
        		{
	        		IplImage people=Image.resizeImage(p[i], 120, 120);
	        		if(lda.getLDAResult(people)>0)
	        		{
	        			Image.showImage(pFrame);
	        		}
        		}
        	}
        	pFrame = grabber.grab();
        }
        grabber.stop();
	}
}