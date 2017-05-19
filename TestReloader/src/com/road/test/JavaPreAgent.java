package com.road.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.road.ClassReloader;
import com.road.testfinal.BallMgr;

public class JavaPreAgent {
	 public static final Logger logger = LoggerFactory.getLogger(JavaAgent.class);
	 
	public static void main(String[] args) {
		 try
	        {
			 BallMgr ballMgr = new BallMgr();
	            while (true)
	            {
	                String className = "com.road.testfinal.BallMgr";
	                String classPath = "./bin/" + className.replace(".", "/")
                    + ".class";
	                
	                byte[] buffer = FileUtils.file2byte(classPath);
	                
	                try {
	                    Class<?> reloadClass = Class.forName(className.trim());
		                ClassReloader.load(reloadClass, buffer);
					} catch (Exception e) {
						e.printStackTrace();
					}

	                System.err.println(ballMgr.add());

	                Thread.sleep(1000);
	            }
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	        finally
	        {
	        }
	}
}
