package ChinaSoftwareCup.FaceRecognition.lda;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import ChinaSoftwareCup.FaceRecognition.log.Log;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class LdaImpl implements Lda
{
	private int numberOfPeople; 		//样本人数
	private int xOfImage = 120; 				//图像宽度
	private int yOfImage = 120; 				//图像高度
	private int numberOfEachPeople =3;		//每个人所含有的样本数量
	private static String filePath = "D:\\PICtest\\";
	
	public LdaImpl(int num)					//构造函数，传入参数为库中样本人数
	{
		numberOfPeople=num;
		xOfImage = 120;
		yOfImage = 120; 
		numberOfEachPeople =3;
		filePath = "D:\\PICtest\\";
	}
	
	
	public int getLDAResult()
	{
		
		long t1=System.currentTimeMillis();
		int xy=xOfImage*yOfImage;
		
		CvMat unknowPeople=cvLoadImageM("D:\\PICtest\\00101.bmp",0); //temp为未知图像
		
		CvMat [] p = new CvMat[numberOfPeople * numberOfEachPeople]; //将库中所有样本图像读入至p中 
		for(int num=1;num<=numberOfPeople;num++)
		{
			for(int i=1;i<=numberOfEachPeople;i++)
			{
				p[(num-1)*numberOfEachPeople+(i-1)] =cvLoadImageM(getLoadPath(num, i),0);
			}
		}
		
		

		CvMat unknown = cvMatReshape(unknowPeople); //将未知图像转化为（1*14400）矩阵unknown 
		
		long t2=System.currentTimeMillis();	
		System.out.println("t2-t1: "+(t2-t1));
		
		CvMat [] classOfPeople= new CvMat[numberOfPeople]; //已知样本类
		for(int n=0;n<numberOfPeople;n++)
		{
			classOfPeople[n]=creatClassLib(p,n*numberOfEachPeople,n*numberOfEachPeople+numberOfEachPeople-1);
		}
		long t3=System.currentTimeMillis();	
		System.out.println("t3-t2: "+(t3-t2));
		CvMat [] averageOfClass=new CvMat[numberOfPeople]; //样本均值
		for(int i=0;i<numberOfPeople;i++)
		{
			averageOfClass[i]=meanByRows(classOfPeople[i]);
		}
		
		CvMat averageOfAll=cvCreateMat(1, xy,CV_64FC1);
		double tmp;
		for(int j=0;j<xy;j++)
		{
			tmp=0;
			for(int i=0;i<numberOfPeople;i++)
			{
				tmp+=averageOfClass[i].get(0,j)/numberOfPeople;
			}
			averageOfAll.put(0,j,tmp);
		}
		
		
		
		CvMat ob=cvCreateMat(numberOfPeople,xy,CV_64FC1);
		CvMat temp1=cvCreateMat(1,xy,CV_64FC1);
		
		double tempResult=Math.sqrt((double)numberOfEachPeople);
		for(int i=0;i<numberOfPeople;i++)
		{
			for(int j=0;j<xy;j++)
			{
				temp1.put(0,j,averageOfClass[i].get(0,j)-averageOfAll.get(0,j)); //temp1=averageOfClass[i]-averageOfAll
			}
			for(int j=0;j<xy;j++)
			{
				ob.put(i,j,temp1.get(0,j)*tempResult);//计算ob
			}
		}
		cvReleaseMat(temp1);
		CvMat ob_t=cvCreateMat(xy,numberOfPeople,CV_64FC1);
		cvTranspose(ob,ob_t);
		temp1=cvCreateMat(numberOfPeople,numberOfPeople,CV_64FC1);
		cvGEMM(ob,ob_t,1,null,0,temp1,0);
		
		CvMat V=cvCreateMat(numberOfPeople,numberOfPeople,CV_64FC1);
		CvMat D=cvCreateMat(1,numberOfPeople,CV_64FC1);
		
		cvEigenVV(temp1,V,D,0,-1,-1);
		cvTranspose(V,V);
		
		cvReleaseMat(temp1);
		
		int flag1=numberOfPeople;
		
		for(int j=0;j<flag1;j++)
		{
			if(D.get(0,j)>0.00001)
				continue;
			else
			{
				flag1=j;
				break;
			}
		}
		
		long t4=System.currentTimeMillis();	
		System.out.println("t4-t3: "+(t4-t3));
		
		CvMat Y=cvCreateMat(xy,flag1,CV_64FC1);
		temp1=cvCreateMat(numberOfPeople,1,CV_64FC1);
		CvMat temp2=cvCreateMat(xy,1,CV_64FC1);
		for(int j=0;j<flag1;j++)
		{
			for(int i=0;i<numberOfPeople;i++)
			{
				temp1.put(i,0,V.get(i,j));
			}
			cvGEMM(ob_t,temp1,1,null,0,temp2,0);
			for(int i=0;i<xy;i++)
			{
				Y.put(i,flag1-j-1,temp2.get(i,0));//求矩阵Y
			}
		}
		cvReleaseMat(temp1);
		cvReleaseMat(temp2);
		
		CvMat Db=cvCreateMat(flag1,flag1,CV_64FC1);
		temp1=cvCreateMat(numberOfPeople,flag1,CV_64FC1);
		temp2=cvCreateMat(flag1,numberOfPeople,CV_64FC1);
		
		cvGEMM(ob,Y,1,null,0,temp1,0);
		cvTranspose(temp1,temp2);
		cvGEMM(temp2,temp1,1,null,0,Db,0);//求矩阵Db
		cvReleaseMat(temp1);
		cvReleaseMat(temp2);
		
		for(int i=0;i<flag1;i++)//矫正Db为对角矩阵
		{
			for(int j=0;j<flag1;j++)
			{
				if(i==j)
					continue;
				else
				{
					Db.put(i,j,0.0);
				}
			}
		}
		
		long t5=System.currentTimeMillis();	
		System.out.println("t5-t4: "+(t5-t4));
		
		CvMat Z=cvCreateMat(xy,flag1,CV_64FC1);
		CvMat Z_t=cvCreateMat(flag1,xy,CV_64FC1);
		temp1=cvCreateMat(flag1,flag1,CV_64FC1);
		temp2=cvCreateMat(flag1,flag1,CV_64FC1);
		cvPow(Db,temp1,0.5);
		cvPow(temp1,temp2,-1);
		cvGEMM(Y,temp2,1,null,0,Z,0);
		cvReleaseMat(temp1);
		cvReleaseMat(temp2);
		cvTranspose(Z,Z_t);
		
		CvMat ow=cvCreateMat(numberOfEachPeople*numberOfPeople,xy,CV_64FC1);;//Sw=ow_t*ow 类内离散度矩阵
		
		
			
		
		temp1=cvCreateMat(1,xy,CV_64FC1);
		temp2=cvCreateMat(1,xy,CV_64FC1);
		for(int i=0;i<numberOfPeople;i++)
		{
			for(int n=0;n<numberOfEachPeople;n++)
			{
				for(int j=0;j<xy;j++)
				{
					temp2.put(0,j,classOfPeople[i].get(n,j));
				}
				for(int j=0;j<xy;j++)
				{
					temp1.put(0,j,temp2.get(0,j)-averageOfClass[i].get(0,j)); //temp1=averageOfClass[i]-averageOfAll
				}
				for(int j=0;j<xy;j++)
				{
					ow.put(i*numberOfEachPeople+n,j,temp1.get(0,j));
				}
			}
		}
		cvReleaseMat(temp1);
		cvReleaseMat(temp2);
		
		long t6=System.currentTimeMillis();
		System.out.println("t6-t5: "+(t6-t5));
		
		CvMat owt=cvCreateMat(numberOfPeople*numberOfEachPeople,flag1,CV_64FC1);
		cvGEMM(ow,Z,1,null,0,owt,0);

		CvMat owt_t=cvCreateMat(flag1,numberOfPeople*numberOfEachPeople,CV_64FC1);
		cvTranspose(owt,owt_t); 
		
		CvMat Vt=cvCreateMat(numberOfPeople*numberOfEachPeople,numberOfPeople*numberOfEachPeople,CV_64FC1);
		CvMat Dt=cvCreateMat(1,numberOfPeople*numberOfEachPeople,CV_64FC1);
		temp1=cvCreateMat(numberOfPeople*numberOfEachPeople,numberOfPeople*numberOfEachPeople,CV_64FC1);
		cvGEMM(owt,owt_t,1,null,0,temp1,0);
		cvEigenVV(temp1, Vt, Dt, 0, -1, -1);
		cvTranspose(Vt,Vt); 
		
		cvReleaseMat(temp1);
		
		int flag2=numberOfPeople*numberOfEachPeople;
		for(int j=0;j<flag2;j++)
		{
			if(Dt.get(0,j)>0.0001)
				continue;
			else
			{
				flag2=j;
				break;
			}
		}
		CvMat U=cvCreateMat(flag1,flag2,CV_64FC1);
		CvMat U_t=cvCreateMat(flag2,flag1,CV_64FC1);
		temp1=cvCreateMat(numberOfPeople*numberOfEachPeople,1,CV_64FC1);
		temp2=cvCreateMat(flag1,1,CV_64FC1);
		for(int j=0;j<flag2;j++)
		{
			for(int i=0;i<numberOfPeople*numberOfEachPeople;i++)
			{
				temp1.put(i,0,Vt.get(i,j));
			}
			cvGEMM(owt_t,temp1,1,null,0,temp2,0);
			for(int i=0;i<flag1;i++)
			{
				U.put(i,flag2-j-1,temp2.get(i,0));
			}
		}
		cvReleaseMat(temp1);
		cvReleaseMat(temp2);
		cvTranspose(U,U_t);
		
		CvMat Dw=cvCreateMat(flag2,flag2,CV_64FC1);
		
		temp1=cvCreateMat(numberOfPeople*numberOfEachPeople,flag2,CV_64FC1);
		temp2=cvCreateMat(flag2,numberOfPeople*numberOfEachPeople,CV_64FC1);

		cvGEMM(owt,U,1,null,0,temp1,0);
		cvTranspose(temp1,temp2);
		cvGEMM(temp2,temp1,1,null,0,Dw,0);//求矩阵Dw
		cvReleaseMat(temp1);
		cvReleaseMat(temp2);

		
		for(int i=0;i<flag2;i++)//矫正Db为对角矩阵
		{
			for(int j=0;j<flag2;j++)
			{
				if(i==j)
					continue;
				else
				{
					Dw.put(i,j,0.0);
				}
			}
		}

		
		CvMat A=cvCreateMat(flag2,xy,CV_64FC1);
		cvGEMM(U_t,Z_t,1,null,0,A,0);


		temp1=cvCreateMat(flag2,xy,CV_64FC1);
		CvMat At=cvCreateMat(xy,flag2,CV_64FC1);


		CvMat M1=cvCreateMat(flag2,flag2,CV_64FC1);
		CvMat M2=cvCreateMat(flag2,flag2,CV_64FC1);
		cvPow(Dw,M1,0.5);
		cvPow(M1,M2,-1);
		cvGEMM(M2,A,1,null,0,temp1,0);
		cvTranspose(temp1,At);//At为所求得的降维矩阵

		cvReleaseMat(temp1);
		cvReleaseMat(M1);
		cvReleaseMat(M2);
		
		
		CvMat [] newClassOfPeopleTemp =new CvMat[numberOfPeople];
		for(int i=0;i<numberOfPeople;i++)
		{
			newClassOfPeopleTemp[i]=cvCreateMat(numberOfEachPeople,flag2,CV_64FC1);
			cvGEMM(classOfPeople[i],At,1,null,0,newClassOfPeopleTemp[i],0);
		}
		CvMat [] newClassOfPeople =new CvMat[numberOfPeople];
		for(int i=0;i<numberOfPeople;i++)
		{
			newClassOfPeople[i]=meanByRows(newClassOfPeopleTemp[i]);
		}


		CvMat unknownResult=cvCreateMat(1,flag2,CV_64FC1);
		cvGEMM(unknown,At,1,null,0,unknownResult,0);
		
		double [] result=new double[numberOfPeople];
		for(int i=0;i<numberOfPeople;i++)
			result[i]=0;

		for(int i=0;i<numberOfPeople;i++)
		{
			for(int j=0;j<flag2;j++)
			{
				double r=unknownResult.get(0,j)-newClassOfPeople[i].get(0,j);//求欧式距离
				result[i]+=r*r;
			}
			result[i]=Math.sqrt(result[i]);
		}
		
		
		double test=result[0];
		int resultNum=0;
		for(int i=1;i<numberOfPeople;i++)
		{
			if(result[i]<test)
			{
				resultNum=i;//获得匹配后的结果
				test=result[i];
			}
				
		}
		
		long t7=System.currentTimeMillis();	
		System.out.println("t7-t6: "+(t7-t6));
		return resultNum+1;
	}
	
	public static void main(String[] args)
	{
		
		long startMili=System.currentTimeMillis();// 当前时间对应的毫秒数	
			
		LdaImpl lda=new LdaImpl(10);
		System.out.println("LDA result： "+lda.getLDAResult());		
		
		long endMili=System.currentTimeMillis();	
		System.out.println("总耗时为："+(endMili-startMili)+"毫秒");
	}
	
	public String getLoadPath(int num,int i) //num：第几个人, i第几张图像
	{
		if(num <= 0)
		{
			Log.error("i<=0");
			return null;
		}
		else if (i <= 0)
		{
			Log.error("num<=0");
			return null;
		}
		else
		{
			String loadPath=filePath;
			if(num <= 9)
			{
				loadPath+="00";
			}
			else if(num <= 99)
			{
				loadPath+="0";
			}
			loadPath+=num;
			if(i<=9)
			{
				loadPath+="0";
			}
			loadPath+=i;
			loadPath+=".bmp";
			
			
			return loadPath;
		}
	}
	
	public void showImage(IplImage image)
	{
		cvNamedWindow("Example",CV_WINDOW_AUTOSIZE); //create a window to deploy the image
	    cvShowImage("Example",image);//deploy the image
	    cvWaitKey(0);//if press any button, destroy the window and image
	    cvReleaseImage(image); 
	}
	
	//按行求input的均值，返回output。若input为x*y,则out为1*y
	private CvMat meanByRows(CvMat input)
	{
		if(null == input)
		{
			Log.error("Input CvMat is null");
			return null;
		}
		else
		{
			int x=input.rows();
			int y=input.cols();
			CvMat output =cvCreateMat(1, y, CV_64FC1);
			double temp;
			for(int j=0;j<y;j++)
			{
				temp=0;
				for(int i=0;i<x;i++)
				{
					temp+=input.get(i,j)/x;
				}
				output.put(0,j,temp);
			}
			return output;
		}
	}
	
	//构建样本类，每一个人有N张图像
	private CvMat creatClassLib(CvMat [] p,int start,int end)
	{
		if(null == p)
		{
			Log.error("The input CvMat is null!");
			return null;
		}
		else
		{
			int x=p[start].rows();
			int y=p[start].cols();
			CvMat output=cvCreateMat(end-start+1,x*y, CV_64FC1);
			for(int k=start;k<=end;k++)
			{
				CvMat temp=cvMatReshape(p[k]);						
				for(int i=0;i<x*y;i++)
				{
					output.put(k-start,i,temp.get(0,i));
				}
			}
			return output;
		}
	}
	
	// 将某个x*y的矩阵转化为1*(x*y)的矩阵
	private CvMat cvMatReshape(CvMat input)
	{
		if(null == input)
		{
			Log.error("The input CvMat is null!");
			return null;
		}
		else
		{
			int rows=input.rows();
			int cols=input.cols();
			CvMat output=cvCreateMat(1,rows*cols,CV_64FC1);
			for(int i=0;i<rows;i++)
			{
				for(int j=0;j<cols;j++)
				{
					output.put(0, i*cols+j, input.get(i,j));
				}
			}
			return output;
		}
	}
	
}