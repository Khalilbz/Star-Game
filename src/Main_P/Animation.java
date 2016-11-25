package Main_P;

public class Animation {

	public String name ;
	public int top = 0;
	public int width,height;
	public int start = 0;
	public int finish = 0;
	public int interval = 0;
	public boolean animated = false;
	public boolean oneway = false;
	
	public Animation(){
	}
	
	public void place(String name,int top ,int width ,int height ){
		this.name = name;
		this.top = top;
		this.width = width;
		this.height = height;
		
	}
	
	public void conf(int start,int finish,int interval,boolean animated,boolean oneway){
		this.start = start;
		this.finish = finish;
		this.interval = interval;
		this.animated = animated;
		this.oneway = oneway;
		
	}
}
