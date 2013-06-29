package ChinaSoftwareCup.FaceRecognition.main;

import static com.googlecode.javacv.cpp.opencv_core.*;  
import static com.googlecode.javacv.cpp.opencv_highgui.*;  

import org.apache.log4j.xml.DOMConfigurator;

import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import ChinaSoftwareCup.FaceRecognition.ImageProcessing.FaceDetect;
import ChinaSoftwareCup.FaceRecognition.ImageProcessing.Image;
import ChinaSoftwareCup.FaceRecognition.ImageProcessing.base.ImageColor;
import ChinaSoftwareCup.FaceRecognition.UI.UI_01;
import ChinaSoftwareCup.FaceRecognition.camera.CameraCapture;
import ChinaSoftwareCup.FaceRecognition.lda.Lda;
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
		
		//Lda lda=new Lda(3,120,120,"D:\\PICtest\\");
	
		//Log.info("Result: "+lda.getLDAResult(6));
		
		
		//IplImage image=Image.readImage("D:\\whf.jpg",ImageColor.GRAYSCALE);//load the image
		
		//UI_01 ui=new UI_01();
		
		Lda lda=new Lda(1,120,120,"D:\\VedioFR\\");
		FaceDetect fd =new FaceDetect();
		
		IplImage people1=Image.readImage("D:\\people1.jpg",ImageColor.COLOR);
		Image.showImage(people1);
		IplImage p1=Image.resizeImage(fd.getMaxSizeFace(people1),120,120);
		Image.showImage(p1);
		lda.saveImage(p1, 1, 1);
		
		IplImage people2=Image.readImage("D:\\people2.jpg",ImageColor.COLOR);
		Image.showImage(people2);
		IplImage p2=Image.resizeImage(fd.getMaxSizeFace(people2),120,120);
		Image.showImage(p2);
		lda.saveImage(p2, 2, 1);
		
		lda.setNumberOfPeople(2);
		
		//Vedio.doFaceRecognition("D:\\sample1.wmv",lda);
		
		lda.init();
		for(int i=0;i<5000;i++)
		{
			lda.getLDAResult(p1);
		}
		
		//lda.saveImage(temp, 13, 2);
		
		//IplImage image=null;
		//image=CameraCapture.doCapture();
		//FaceDetect fd=new FaceDetect(); // detect the face of image
		//IplImage output=fd.doFaceDetect(image);
		
		//IplImage finalImage=Image.resizeImage(image, 200, 160);
		//Image.histeq(finalImage);
		//Image.showImage(finalImage);

		//Image.showImage(grayImage);
		
		
		//Vedio.test();
		//Image.writeImage(image, "D:\\whf123.jpg");
		
		//Log.info("End!");
	}

}
