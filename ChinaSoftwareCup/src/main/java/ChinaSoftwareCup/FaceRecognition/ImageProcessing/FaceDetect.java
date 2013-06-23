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
    
    public IplImage doFaceDetect(IplImage origImg)
    {
    	IplImage grayImg = Image.rgb2Gray(origImg);
        // scale the grayscale (to speed up face detection)
        IplImage smallImg = IplImage.create(grayImg.width()/SCALE, grayImg.height()/SCALE, IPL_DEPTH_8U, 1);
        cvResize(grayImg, smallImg, CV_INTER_LINEAR);
    	CvMemStorage storage = CvMemStorage.create();
    	CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad(CASCADE_FILE));
    	Log.info("Detecting faces...");
    	CvSeq faces = cvHaarDetectObjects(smallImg, cascade, storage, 1.1, 2, 0);
    	cvClearMemStorage(storage);
  	    // iterate over the faces and draw yellow rectangles around them
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
   	    	IplImage output=Image.cropImage(grayImg, r.x()*SCALE, r.y()*SCALE, r.width()*SCALE, r.height()*SCALE);
   	    	return output;
   	    }
   	    else
   	    {
   	    	Log.error("No faces found.");
   	    	return null;
   	    }
   	    
   	    
    }
}