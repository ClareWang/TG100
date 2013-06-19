package ChinaSoftwareCup.FaceRecognition.camera;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import ChinaSoftwareCup.FaceRecognition.ImageProcessing.Image;
import ChinaSoftwareCup.FaceRecognition.log.Log;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import static com.googlecode.javacv.cpp.opencv_core.cvCopy;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;

public class CameraCapture {

	
	//timer for image capture animation
	static class TimerActionCapture implements ActionListener 
	{
		private CanvasFrame canvasFrame;
		
		private Timer timer;
		public void setTimer(Timer timer)
		{
			this.timer = timer;
		}
		 
		public TimerActionCapture(CanvasFrame canvasFrame)
		{
			this.canvasFrame = canvasFrame;
		}
        public void actionPerformed(ActionEvent e) 
        {

        	timer.stop();
        	try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.error(e1.toString());
			}
        	canvasFrame.dispose();
        	            
        }
    }
	
	public static IplImage doCapture() throws Exception 
	{
		IplImage output;
		OpenCVFrameGrabber grabber=null;
		//open camera source
		grabber = new OpenCVFrameGrabber(0);
		grabber.start();
		//create a frame for real-time image display
		CanvasFrame canvasFrame = new CanvasFrame("Camera");
		IplImage image =grabber.grab();
		
		int width = image.width();  
        int height = image.height();  
        canvasFrame.setCanvasSize(width, height);  
          
        //onscreen buffer for image capture   
        final BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
        Graphics2D bGraphics = bImage.createGraphics();
        //animation timer
		TimerActionCapture timerActionCapture = new TimerActionCapture(canvasFrame);
		final Timer timer=new Timer(100, timerActionCapture);
		timerActionCapture.setTimer(timer);
		//click the frame to capture an image
        canvasFrame.getCanvas().addMouseListener(new MouseAdapter()
        {
        	public void mouseClicked(MouseEvent e)
        	{     
        		timer.start(); //start animation
        		try {  
                    ImageIO.write(bImage, "jpg", new File("d:\\aabbccdd.jpg"));  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }        
            }        	     
        }
        );
        
        int flag=0;
        //real-time image display
        while(canvasFrame.isVisible())
        {
        	image = grabber.grab();
        	if(!timer.isRunning()) 
        	{
        		canvasFrame.showImage(image);
        		bGraphics.drawImage(image.getBufferedImage(),null,0,0);
        		if(1==flag)
        		{
        			break;
        		}
          	}
        	else
        	{
        		flag=1;
        	}
        }
        output=IplImage.createFrom(bImage);
        grabber.stop();
        return output;
	}

}