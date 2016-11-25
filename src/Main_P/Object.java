package Main_P;

import java.awt.Color;

public class Object {
	
	//Genral parametres 
	public int x,y; //Positions
	public int width,height; //Size
	public boolean delete = false; //To Be Deleted
	public boolean Clicked = false; //is Clicked
	public String name;
	public boolean pinned = false;
	
	
	//Animation
	public String Animation = "none"; //Visible
	public int pic_counter = 0;
	public int align ;
	
	//TEXT
	public String text;
	public Color color;
	public boolean textmode = false;
	
	//Some Variables for private uses
	public int p1 = 0;
	public int p2 = 0;
	public int p3 = 0;
	public int p4 = 0;
	
	//Type for P_ENG
	public String control = "none";
	public int xs = 0 ,ys = 0; //Movement forces
	public boolean active = false; //Disable Movement and forces
	public boolean wall = false; //Movement without conditions
	public int gravity = 0; //Y+ force
	public int old = 0; //How many times left from adding the object (Max old = 50 000 )
	public int MaxSpeed = 5 ;
	public int MaxJump = 1 ;
	public int jump = 0;
	public int jumpc = 0;
	public boolean fine = false; //if P_ENG is finished his work for the first time
	public boolean groundtouched = false;
	public boolean hold_when_down = false;
	public boolean friction = false;
	public int friction_ob = -1;
	public boolean friction_enabled = false;
	public boolean ismoving = false;
	
	//Creation of Object
	public Object(String name,int align,String Animation,int x, int y,int width, int height){
		this.x = x ;
		this.y = y ;
		this.width = width ;
		this.height = height ;
		this.name = name;
		this.Animation = Animation;
		this.align = align;
	}
	
	public void kill(){
		this.delete=true;
	}
	
	public void CreateText(String text,Color color){
		this.textmode = true;
		this.text=text;
		this.color=color;
	}
	
	
	/*/Creation of Object Graphics
	public void Graphic(int pic_top ,int pic_GT ,int pic_start ,int pic_finish ,boolean pic_animated,boolean isvisible){
		this.pic_top = pic_top;
		this.pic_GT = pic_GT;
		this.pic_start = pic_start;
		this.pic_finish = pic_finish;
		this.pic_counter = pic_start;
		this.pic_animated = pic_animated;
		this.isvisible = isvisible;
	}*/
	
	//Creation of Object Physics
	public void Physic(boolean active,boolean wall,String control,int gravity,int MaxSpeed , int MaxJump,boolean friction){
		this.control = control;
		/*this.xs = xs;
		this.ys = ys;*/
		this.active = active;
		this.wall = wall;
		this.gravity = gravity;
		this.MaxSpeed = MaxSpeed;
		this.MaxJump = MaxJump;
		this.friction =friction;
	}
	

	
}
