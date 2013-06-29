package ChinaSoftwareCup.FaceRecognition.ImageProcessing;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import ChinaSoftwareCup.FaceRecognition.ImageProcessing.base.FilterType;
import ChinaSoftwareCup.FaceRecognition.ImageProcessing.base.ImageColor;
import ChinaSoftwareCup.FaceRecognition.log.Log;

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
		else
		{
			Log.error("The image is null!");
		}
	}
	
	public static IplImage cropImage(IplImage image,int x,int y, int width,int height)
	{
		Log.debug("Crop the image.");
		CvRect r = new CvRect(x, y, width, height);
		cvSetImageROI(image, r);
		IplImage cropped = cvCreateImage(cvSize(width,height), image.depth(), image.nChannels());
		cvCopy(image, cropped);
		cvResetImageROI(image);
		return cropped;
	}
	
	public static IplImage resizeImage(IplImage image, int x,int y)
	{
		Log.debug("Resize the image from ["+image.width()+","+image.height()+"] to ["+x+","+y+"].");
		IplImage output = cvCreateImage(cvSize(x,y), image.depth(), image.nChannels());
		cvResize(image, output, CV_INTER_LINEAR);
		return output;
	}
	
	public static IplImage rgb2Gray(IplImage image)
	{
		Log.debug("Change the image from RGB to gray.");
		IplImage grayImg = IplImage.create(image.width(), image.height(), IPL_DEPTH_8U, 1);
        cvCvtColor(image, grayImg, CV_BGR2GRAY);
        return grayImg;
	}
	
	public static IplImage readImage(String path, ImageColor imageColor)
	{
		IplImage image=null;
		switch (imageColor)
		{
		case COLOR:
			Log.debug("Load image from: "+path+" The ImageColor: "+imageColor.toString());
			image=cvLoadImage(path,1);
			return image;
		case GRAYSCALE:
			Log.debug("Load image from: "+path+" The ImageColor: "+imageColor.toString());
			image=cvLoadImage(path,0);
			return image;
		case DEFAULT:
			Log.debug("Load image from: "+path+" The ImageColor: "+imageColor.toString());
			image=cvLoadImage(path,-1);
			return image;
		default:
			Log.error("The input ImageColor is wrong, return null!");
			return null;
		}
	}
	
	public static CvMat readImage2Mat(String path, ImageColor imageColor)
	{
		CvMat cvMat=null;
		switch (imageColor)
		{
		case COLOR:
			Log.debug("Load image from: "+path+" The ImageColor: "+imageColor.toString());
			cvMat=cvLoadImageM(path,1);
			return cvMat;
		case GRAYSCALE:
			Log.debug("Load image from: "+path+" The ImageColor: "+imageColor.toString());
			cvMat=cvLoadImageM(path,0);
			return cvMat;
		case DEFAULT:
			Log.debug("Load image from: "+path+" The ImageColor: "+imageColor.toString());
			cvMat=cvLoadImageM(path,-1);
			return cvMat;
		default:
			Log.error("The input ImageColor is wrong, return null!");
			return null;
		}
	}
	
	public static boolean writeImage(IplImage image, String path)
	{
		if(path.length()>0 && null != image)
		{
			cvSaveImage(path,image);
			return true;
		}
		else
		{
			Log.error("The path or image is null! Can not save the image!");
			return false;
		}
	}
	
	public static boolean smooth(IplImage image,FilterType filterType,int size)
	{
		if(null != image)
		{
			switch (filterType)
			{
			case GAUSSIAN:
				Log.debug("GAUSSIAN filter.");
				cvSmooth(image,image,CV_GAUSSIAN,size);
				return true;
			case MEDIAN:
				Log.debug("MEDIAN filter.");
				cvSmooth(image,image,CV_MEDIAN,size);
				return true;
			default:
				Log.error("The input FilterType is wrong, return false!");
				return false;
			}
				
		}
		else
		{
			Log.error("The image is null!");
			return false;
		}
	}
	
	public static void histeq(IplImage image)
	{
		Log.debug("Equalize the hist of the image.");
		cvEqualizeHist(image,image);
	}
}