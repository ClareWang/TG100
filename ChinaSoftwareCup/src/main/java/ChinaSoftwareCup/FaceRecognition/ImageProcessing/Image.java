package ChinaSoftwareCup.FaceRecognition.ImageProcessing;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class Image
{
	public static void showImage(IplImage image)
	{
		if(null!=image)
		{
			cvNamedWindow("Example",CV_WINDOW_AUTOSIZE); //create a window to deploy the image
		    cvShowImage("Example",image);//deploy the image
		    cvWaitKey(0);//if press any button, destroy the window and image
		    //cvReleaseImage(image);
		}
	}
	
	public static IplImage cropImage(IplImage image,int x,int y, int width,int height)
	{
		CvRect r = new CvRect(x, y, width, height);
		IplImage cropped = cvCreateImage(cvGetSize(image), image.depth(), image.nChannels());
		cvCopy(image, cropped);
		cvSetImageROI(cropped, r);
		return cropped;
	}
	
	public static IplImage rgb2Gray(IplImage image)
	{
		IplImage grayImg = IplImage.create(image.width(), image.height(), IPL_DEPTH_8U, 1);
        cvCvtColor(image, grayImg, CV_BGR2GRAY);
        return grayImg;
	}
	
}