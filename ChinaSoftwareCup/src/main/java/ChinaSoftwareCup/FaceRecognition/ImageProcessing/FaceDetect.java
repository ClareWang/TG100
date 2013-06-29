package ChinaSoftwareCup.FaceRecognition.ImageProcessing;

import ChinaSoftwareCup.FaceRecognition.log.Log;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;


public class FaceDetect
{
	private static final int SCALE = 2;     
    private static final String CASCADE_FILE = "C:\\opencv\\data\\haarcascades\\haarcascade_frontalface_alt.xml";
    
    private CvMemStorage storage;
    private CvHaarClassifierCascade cascade;
    
    public FaceDetect()
    {
    	storage = CvMemStorage.create();
    	cascade = new CvHaarClassifierCascade(cvLoad(CASCADE_FILE));
    }
    
    
    public IplImage getMaxSizeFace(IplImage origImg)
    {
    	IplImage grayImg = Image.rgb2Gray(origImg);
    	CvSeq faces=doFaceDetect(grayImg);
    	int total = faces.total();
  	    Log.info("Found " + total + " face(s)");
  	    
  	    int num=-1;
 	    int max=0;
 	    for (int i = 0; i < total; i++) 
 	    {
 	    	CvRect r = new CvRect(cvGetSeqElem(faces, i));
 	    	if(r.width()*r.height()>max)
 	    	{
 	    		max=r.width()*r.height();
 	    		num=i;
 	    	}
 	    }
 	    if(num>-1)
 	    {
 	    	CvRect r = new CvRect(cvGetSeqElem(faces, num));
 	    	double ratio=0.8;
 	    	int x=(int)(r.x()*SCALE+r.width()*SCALE*(1-ratio)/2);
 	    	int y=r.y()*SCALE;
 	    	int width=(int)(r.width()*SCALE*ratio);
 	    	int height=r.height()*SCALE;
 	    	IplImage output=Image.cropImage(grayImg, x, y, width, height);
 	    	cvReleaseImage(grayImg);
 	    	return output;
 	    }
 	    else
 	    {
 	    	Log.error("No faces found.");
 	    	cvReleaseImage(grayImg);
 	    	return null;
 	    }
    }
    
    public IplImage [] getAllFaces(IplImage origImg)
    {
    	IplImage grayImg = Image.rgb2Gray(origImg);
    	CvSeq faces=doFaceDetect(grayImg);
    	int total = faces.total();
  	    Log.info("Found " + total + " face(s)");
  	    
  	    if(total==0)
  	    	return null;
  	    
  	    IplImage [] output=new IplImage[total];
  	    
 	    for (int i = 0; i < total; i++) 
 	    {
 	    	CvRect r = new CvRect(cvGetSeqElem(faces, i));
 	    	// assume that (the width of face)/(the height of face)=ratio=0.8
 	    	double ratio=0.8;
 	    	int x=(int)(r.x()*SCALE+r.width()*SCALE*(1-ratio)/2);
 	    	int y=r.y()*SCALE;
 	    	int width=(int)(r.width()*SCALE*ratio);
 	    	int height=r.height()*SCALE;
 	    	output[i]=Image.cropImage(grayImg, x, y, width, height);
 	    }
 	    cvReleaseImage(grayImg);
 	    return output;
 	  
    }
    
    private CvSeq doFaceDetect(IplImage inputImg)
    {
    	
        // scale the grayscale (to speed up face detection)
        IplImage smallImg = IplImage.create(inputImg.width()/SCALE, inputImg.height()/SCALE, IPL_DEPTH_8U, 1);
               
        cvResize(inputImg, smallImg, CV_INTER_LINEAR);
    	
    	Log.info("Detecting faces...");
    	CvSeq faces = cvHaarDetectObjects(smallImg, this.cascade, this.storage, 1.1, 2, 0);
    	
	    cvReleaseImage(smallImg);
    	
    	return faces;
    	//cvClearMemStorage(storage);
    }
}