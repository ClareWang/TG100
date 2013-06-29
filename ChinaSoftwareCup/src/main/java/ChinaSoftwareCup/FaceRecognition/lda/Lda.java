package ChinaSoftwareCup.FaceRecognition.lda;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import ChinaSoftwareCup.FaceRecognition.ImageProcessing.base.ImageColor;
import ChinaSoftwareCup.FaceRecognition.log.Log;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import ChinaSoftwareCup.FaceRecognition.ImageProcessing.Image;

public class Lda
{
	private int numberOfPeople; 		
	private int xOfImage; 				
	private int yOfImage; 				
	private int numberOfEachPeople;		
	private static String filePath;
	private CvMat [] classOfPeople;
	
	public Lda(int i,int x,int y,String path)					
	{
		numberOfEachPeople =i;
		xOfImage = x;
		yOfImage = y; 
		filePath = path;
	}
	
	public void setNumberOfPeople(int numberOfPeople)
	{
		this.numberOfPeople=numberOfPeople;
	}
	
	
	public void init()
	{
		CvMat [] p = new CvMat[numberOfPeople * numberOfEachPeople]; 
		for(int num=1;num<=numberOfPeople;num++)
		{
			for(int i=1;i<=numberOfEachPeople;i++)
			{
				p[(num-1)*numberOfEachPeople+(i-1)] =Image.readImage2Mat(getLoadPath(num, i),ImageColor.GRAYSCALE);
			}
		}
		
		classOfPeople= new CvMat[numberOfPeople]; 
		for(int n=0;n<numberOfPeople;n++)
		{
			classOfPeople[n]=creatClassLib(p,n*numberOfEachPeople,n*numberOfEachPeople+numberOfEachPeople-1); //[n,xy]
		}

	}
		
	public int getLDAResult(IplImage inputPeople)
	{
		CvMat unknowPeople=cvCreateMat(inputPeople.height(),inputPeople.width(),CV_64FC1);
		cvConvert(inputPeople,unknowPeople);
		//CvMat unknowPeople=Image.readImage2Mat(filePath+"temp.bmp",ImageColor.GRAYSCALE); //[x,y]
		CvMat unknown = cvMatReshape(unknowPeople); //[1,xy]
		
		double [] result = nullSpaceLDA(unknown,classOfPeople,xOfImage,yOfImage);;
		
		if(result == null)
			return 0;
		
		double test=result[0];
		int resultNum=0;
		Log.info("result[1]:"+result[0]);
		for(int i=1;i<numberOfPeople;i++)
		{
			Log.info("result["+(i+1)+"]:"+result[i]);
			if(result[i]<test)
			{
				resultNum=i;
				test=result[i];
			}
				
		}
		
		cvReleaseMat(unknowPeople);
		cvReleaseMat(unknown);
		if(test>100000)
			return 0;
		else
			return resultNum+1;
	}

	public String getLoadPath(int num,int i) 
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
		
	//calculate the average of a CvMat by rows
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

	//n CvMats(x,y) ---> 1 CvMat(n,x*y)  
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
				cvReleaseMat(temp);
			}
			return output;
		}
	}
	
	//A(x,y)--->B(1,x*y)
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
	
	
	private double [] directLDA(CvMat unknown,CvMat [] classOfPeople,int x,int y)
	{
		int xy=x*y;
		CvMat [] averageOfClass=new CvMat[numberOfPeople];
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
				ob.put(i,j,temp1.get(0,j)*tempResult);
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
				Y.put(i,flag1-j-1,temp2.get(i,0));
			}
		}
		cvReleaseMat(temp1);
		cvReleaseMat(temp2);
		
		CvMat Db=cvCreateMat(flag1,flag1,CV_64FC1);
		temp1=cvCreateMat(numberOfPeople,flag1,CV_64FC1);
		temp2=cvCreateMat(flag1,numberOfPeople,CV_64FC1);
		
		cvGEMM(ob,Y,1,null,0,temp1,0);
		cvTranspose(temp1,temp2);
		cvGEMM(temp2,temp1,1,null,0,Db,0);
		cvReleaseMat(temp1);
		cvReleaseMat(temp2);
		
		for(int i=0;i<flag1;i++)
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
		
		CvMat ow=cvCreateMat(numberOfEachPeople*numberOfPeople,xy,CV_64FC1);
		
		
			
		
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
		cvGEMM(temp2,temp1,1,null,0,Dw,0);
		cvReleaseMat(temp1);
		cvReleaseMat(temp2);

		
		for(int i=0;i<flag2;i++)
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
		cvTranspose(temp1,At);

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
				double r=unknownResult.get(0,j)-newClassOfPeople[i].get(0,j);//��ŷʽ����
				result[i]+=r*r;
			}
			result[i]=Math.sqrt(result[i]);
		}
			
		return result;
	}
	
	private double [] nullSpaceLDA(CvMat unknown,CvMat [] classOfPeople,int x,int y)
	{
		
		int xy=x*y;
		CvMat [] averageOfClass=new CvMat[numberOfPeople];
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
		for(int i=0;i<numberOfPeople;i++)
		{
			for(int j=0;j<xy;j++)
			{
				ob.put(i,j,(averageOfClass[i].get(0,j)-averageOfAll.get(0,j))/(numberOfEachPeople*numberOfPeople));//����ob
			}
		}
		CvMat ob_t=cvCreateMat(xy,numberOfPeople,CV_64FC1);
		cvTranspose(ob,ob_t);
		
		CvMat ow=cvCreateMat(numberOfEachPeople*numberOfPeople,xy,CV_64FC1);
		for(int i=0;i<numberOfPeople;i++)
		{
			for(int n=0;n<numberOfEachPeople;n++)
			{
				for(int j=0;j<xy;j++)
				{
					ow.put(i*numberOfEachPeople+n,j,(classOfPeople[i].get(n,j)-averageOfClass[i].get(0,j))/(numberOfEachPeople*numberOfPeople));
				}
			}
		}
		CvMat ow_t=cvCreateMat(xy,numberOfEachPeople*numberOfPeople,CV_64FC1);
		cvTranspose(ow,ow_t);
		
		CvMat ot=cvCreateMat(numberOfEachPeople*numberOfPeople,xy,CV_64FC1);
		for(int i=0;i<numberOfPeople;i++)
		{
			for(int n=0;n<numberOfEachPeople;n++)
			{
				for(int j=0;j<xy;j++)
				{
					ot.put(i*numberOfEachPeople+n,j,(classOfPeople[i].get(n,j)-averageOfAll.get(0,j))/(numberOfEachPeople*numberOfPeople));
				}
			}
		}
		CvMat ot_t=cvCreateMat(xy,numberOfEachPeople*numberOfPeople,CV_64FC1);
		cvTranspose(ot,ot_t);
		
		
		CvMat temp1=cvCreateMat(numberOfEachPeople*numberOfPeople,numberOfEachPeople*numberOfPeople,CV_64FC1);
		cvGEMM(ot,ot_t,1,null,0,temp1,0);
		
		CvMat V=cvCreateMat(numberOfEachPeople*numberOfPeople,numberOfEachPeople*numberOfPeople,CV_64FC1);
		CvMat D=cvCreateMat(1,numberOfEachPeople*numberOfPeople,CV_64FC1);
		
		cvEigenVV(temp1,V,D,0,-1,-1);
		cvTranspose(V,V);
		
		CvMat U=cvCreateMat(xy,numberOfEachPeople*numberOfPeople-1,CV_64FC1);
		for(int j=0;j<numberOfEachPeople*numberOfPeople-1;j++)
		{
			CvMat Vtemp = cvCreateMat(numberOfEachPeople*numberOfPeople,1,CV_64FC1);
			for(int i=0;i<numberOfEachPeople*numberOfPeople;i++)
			{
				Vtemp.put(i,0,V.get(i,numberOfEachPeople*numberOfPeople-1-j-1));
			}
			
			CvMat temp = cvCreateMat(xy,1,CV_64FC1);
			cvGEMM(ot_t,Vtemp,1,null,0,temp,0);
			
			for(int i=0;i<xy;i++)
			{
				U.put(i,j,temp.get(i,0));
			}
			cvReleaseMat(Vtemp);
			cvReleaseMat(temp);
		}
				
		//Sw1=U'*Sw*U
		//Sw1=U'*(ot'*ot)*U
		//Sw1=(ow*U)'*ow*U;
		CvMat Sw1 = cvCreateMat(numberOfEachPeople*numberOfPeople-1,numberOfEachPeople*numberOfPeople-1,CV_64FC1);
		temp1=cvCreateMat(numberOfEachPeople*numberOfPeople,numberOfEachPeople*numberOfPeople-1,CV_64FC1);
		cvGEMM(ow,U,1,null,0,temp1,0);
		CvMat temp1_t=cvCreateMat(numberOfEachPeople*numberOfPeople-1,numberOfEachPeople*numberOfPeople,CV_64FC1);
		cvTranspose(temp1,temp1_t);
		CvMat temp2=cvCreateMat(numberOfEachPeople*numberOfPeople-1,xy,CV_64FC1);
		cvGEMM(temp1_t,ow,1,null,0,temp2,0);
		cvGEMM(temp2,U,1,null,0,Sw1,0);

		//Sb1=(ob*U)'*ob*U;
		CvMat Sb1 = cvCreateMat(numberOfEachPeople*numberOfPeople-1,numberOfEachPeople*numberOfPeople-1,CV_64FC1);
		temp1=cvCreateMat(numberOfPeople,numberOfEachPeople*numberOfPeople-1,CV_64FC1);
		cvGEMM(ob,U,1,null,0,temp1,0);
		temp1_t=cvCreateMat(numberOfEachPeople*numberOfPeople-1,numberOfPeople,CV_64FC1);
		cvTranspose(temp1,temp1_t);
		temp2=cvCreateMat(numberOfEachPeople*numberOfPeople-1,xy,CV_64FC1);
		cvGEMM(temp1_t,ob,1,null,0,temp2,0);
		cvGEMM(temp2,U,1,null,0,Sb1,0);

		V=cvCreateMat(numberOfEachPeople*numberOfPeople-1,numberOfEachPeople*numberOfPeople-1,CV_64FC1);
		D=cvCreateMat(1,numberOfEachPeople*numberOfPeople-1,CV_64FC1);
		cvEigenVV(Sw1,V,D,0,-1,-1);
		cvTranspose(V,V);
		int flag=0;
		for(int i=0;i<numberOfEachPeople*numberOfPeople-1;i++)
		{
			if(D.get(i)<0.0001)
				flag++;
		}
		if(flag==0)
		{
			Log.error("Can not find V to make V'*Sw1*V=0");
			return null;
		}
		CvMat Q=cvCreateMat(numberOfEachPeople*numberOfPeople-1,flag,CV_64FC1);
		for(int i=0;i<numberOfEachPeople*numberOfPeople-1;i++)
		{
			for(int j=0;j<flag;j++)
			{
				Q.put(i,j,V.get(i,numberOfEachPeople*numberOfPeople-1-flag+j));
			}
		}
		CvMat Q_t=cvCreateMat(flag,numberOfEachPeople*numberOfPeople-1,CV_64FC1);
		//Sb2=Q'*Sb1*Q;
		CvMat Sb2=cvCreateMat(flag,flag,CV_64FC1);
		temp1=cvCreateMat(flag,numberOfEachPeople*numberOfPeople-1,CV_64FC1);
		cvGEMM(Q_t,Sb1,1,null,0,temp1,0);
		cvGEMM(temp1,Q,1,null,0,Sb2,0);
		
		V=cvCreateMat(flag,flag,CV_64FC1);
		D=cvCreateMat(1,flag,CV_64FC1);
		cvEigenVV(Sb2,V,D,0,-1,-1);
		
		//W=U*Q*V
		CvMat W=cvCreateMat(xy,flag,CV_64FC1);
		temp1=cvCreateMat(xy,flag,CV_64FC1);
		cvGEMM(U,Q,1,null,0,temp1,0);
		cvGEMM(temp1,V,1,null,0,W,0);
		
		CvMat [] newClassOfPeopleTemp =new CvMat[numberOfPeople];
		for(int i=0;i<numberOfPeople;i++)
		{
			newClassOfPeopleTemp[i]=cvCreateMat(numberOfEachPeople,flag,CV_64FC1);
			cvGEMM(classOfPeople[i],W,1,null,0,newClassOfPeopleTemp[i],0);
		}
		CvMat [] newClassOfPeople =new CvMat[numberOfPeople];
		for(int i=0;i<numberOfPeople;i++)
		{
			newClassOfPeople[i]=meanByRows(newClassOfPeopleTemp[i]);
		}


		CvMat unknownResult=cvCreateMat(1,flag,CV_64FC1);
		cvGEMM(unknown,W,1,null,0,unknownResult,0);
		
		double [] result=new double[numberOfPeople];
		for(int i=0;i<numberOfPeople;i++)
			result[i]=0;

		for(int i=0;i<numberOfPeople;i++)
		{
			for(int j=0;j<flag;j++)
			{
				double r=unknownResult.get(0,j)-newClassOfPeople[i].get(0,j);
				result[i]+=r*r;
			}
			result[i]=Math.sqrt(result[i]);
		}
		
		
		for(int i=0;i<numberOfPeople;i++)
		{
			cvReleaseMat(averageOfClass[i]);
		}
		cvReleaseMat(averageOfAll);
		cvReleaseMat(ob);
		cvReleaseMat(ob_t);
		cvReleaseMat(ow);
		cvReleaseMat(ow_t);
		cvReleaseMat(ot);
		cvReleaseMat(ot_t);
		cvReleaseMat(V);
		cvReleaseMat(D);
		cvReleaseMat(U);
		cvReleaseMat(temp1);
		cvReleaseMat(temp2);
		cvReleaseMat(temp1_t);
		cvReleaseMat(Q);
		cvReleaseMat(Q_t);
		cvReleaseMat(Sb1);
		cvReleaseMat(Sb2);
		cvReleaseMat(Sw1);
		cvReleaseMat(W);
		for(int i=0;i<numberOfPeople;i++)
		{
			cvReleaseMat(newClassOfPeopleTemp[i]);
		}
		for(int i=0;i<numberOfPeople;i++)
		{
			cvReleaseMat(newClassOfPeople[i]);
		}
		cvReleaseMat(unknownResult);
		
		return result;
	}
	
	public boolean saveImage(IplImage p,int num,int i)
	{
		if(p.height()!=xOfImage||p.width()!=yOfImage)
		{
			Log.error("The size of input image is wrong, should be ["+xOfImage+","+yOfImage+"].");
			return false;
		}
		if(i>numberOfEachPeople)
		{
			Log.error("Each people only has "+numberOfEachPeople+" images.");
			return false;
		}
		if(num<=0||i<=0)
		{
			Log.error("Invalid input.");
			return false;
		}
		String fileName=filePath;
		if(num<=9)
			fileName+="00"+num;
		else if(num<=99)
			fileName+="0"+num;
		else if(num<=999)
			fileName+=""+num;
		else
		{
			Log.error("Input number is too big.");
			return false;
		}
		if(i<=9)
			fileName+="0"+i;
		else if(i<=99)
			fileName+=""+i;
		fileName+=".bmp";			
		Image.writeImage(p, fileName);
		return true;
	}
	
}