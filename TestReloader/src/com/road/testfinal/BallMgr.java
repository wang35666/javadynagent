package com.road.testfinal;

public final class BallMgr
{
    private int i = 0;
    
    private int j = 1;
    
    public class Inner{
    	private int a;
    	private int b;
		public int getA() {
			return a;
		}
		public void setA(int a) {
			this.a = a;
		}
		public int getB() {
			return b;
		}
		public void setB(int b) {
			this.b = b;
		}
    	
    }
    
    public BallMgr()
    {
        Inner inner = new Inner();
        inner.setA(1);
    }
    
    public int add()
    {
    	  Inner inner = new Inner();
          inner.setA(1);
          
    	int a = i + j;
    	float b = a/ 3 + inner.getA();
    	
        return (int) (b * 5 + 1);
    }
}
