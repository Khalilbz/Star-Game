package Main_P;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

//import dev.code.gfx.ImageLoader;

public class Game {

	//Private Variables
	public int score =0;
	
	//Mode
	boolean creator_xsys = false;
	boolean testanimation = false;
	boolean creator = false; 
				int p = 10;//Camera Speed in Creator mode
	
	// Form
	Display f;
	String title;
	int width;
	int height;
	
	//Frames
		//Frame1
		int Mf1[] = new int[100]; // Menu Table in the frame1
		int Mf1s = 0;   // Menu mf1 selector
		int Mf1l = 0;   // Menu mf1 length

	// Graphic
	boolean Header = false;
	BufferStrategy bs;
	Graphics g;
	int GT = 1; //G_ENG Timer
	BufferedImage world_image ;
	
	//Camera
	int Camera_x = 0;
	int Camera_y = -200;
	int Camera_xs = 0;
	int Camera_ys = 0;
	boolean Camera_xr = true;
	boolean Camera_yr = true;
	String Camera_focus = "none";
	boolean Camera_min_x_b = true;
	boolean Camera_max_x_b = false;
	int Camera_min_x = 0;
	int Camera_max_x = 0;
	
	//Physic
	int PT = 1; //P_ENG Timer
	boolean gdt = false;
	

	//WFrames
	public Object World[] = new Object[1500];
	public ArrayList<Object> objectA = new ArrayList<>();
	public ArrayList<Object> objectB = new ArrayList<>();
	public Animation Animations[] = new Animation[500];
	int obn = 0;
	int ann = 0;
	
	// Input
	KeyManager key;

	// Game Work
	int cuF; //Current Frame
	boolean running;
	int itime;

	private void slp() {
		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}


	public static Image makeColorTransparent(final BufferedImage im, final Color color)
	   {
	      final RGBImageFilter filter = new RGBImageFilter()
	      {
	         // the color we are looking for (white)... Alpha bits are set to opaque
	         public int markerRGB = color.getRGB() | 0xFFFFFFFF;

	         public final int filterRGB(final int x, final int y, final int rgb)
	         {
	            if ((rgb | 0xFF000000) == markerRGB)
	            {
	               // Mark the alpha bits as zero - transparent
	               return 0x00FFFFFF & rgb;
	            }
	            else
	            {
	               // nothing to do
	               return rgb;
	            }
	         }
	      };

	      final ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
	      return Toolkit.getDefaultToolkit().createImage(ip);
	   }
	
	private static BufferedImage imageToBufferedImage(final Image image)
	   {
	      final BufferedImage bufferedImage =
	         new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	      final Graphics2D g2 = bufferedImage.createGraphics();
	      g2.drawImage(image, 0, 0, null);
	      g2.dispose();
	      return bufferedImage;
	    }
	
	public Animation getan(String name){
		Animation test = null;
		boolean found = false;
		for(int i=0;i<ann;i++){
			if(Animations[i].name == name){
				test = Animations[i] ;
				found = true;
			}
		}
		if(found == false){
			System.out.println("ERROR Animation = "+name);
			test = Animations[0];
		}
		return test;
	}
	
	public Object getob(String name){
		Object test = null;
		boolean found = false;
		for(int i=0;i<obn;i++){
			if(World[i].name == name){
				test = World[i] ;
				found = true;
			}
		}
		if(found == false){
			System.out.println("ERROR Object = "+name);
			test = World[0];
		}
		return test;
	}
	
	public int Getwidth(Object obja){
		if(obja.width==0){
			return getan(obja.Animation).width;
			}else{
				return obja.width;
				}}
	
	public int Getheight(Object obja){
		if(obja.height==0){
			return getan(obja.Animation).height;
			}else{
				return obja.height;
				}}
	public boolean Downcoll(Object obja,Object objb){
		return objb.y+Getheight(objb) < obja.y && objb.x-(Getwidth(objb)/4) < obja.x && objb.x+Getwidth(objb) > obja.x;
	}
	
	public void create_physic(String ObjectName,boolean active,boolean wall,String control,int gravity,int MaxSpeed , int MaxJump,boolean friction){
		for(int i=0;i<obn;i++){
			if(World[i].name == ObjectName){
				World[i].control = control;
				World[i].active = active;
				World[i].wall = wall;
				World[i].gravity = gravity;
				World[i].MaxSpeed = MaxSpeed;
				World[i].MaxJump = MaxJump;
				World[i].friction = friction;
			}
		}
	}
	
	public void create_wall(String ObjectName){
		for(int i=0;i<obn;i++){
			if(World[i].name == ObjectName){
				World[i].wall=true;
			}
		}
	}
	
	public boolean collession(String name1, String name2) {
		boolean bcol = false;
		objectA.clear();
		objectB.clear();
		for(int i=0;i<obn;i++)
		  {
			if(World[i].delete==false){
			for(int j=0;j<obn;j++){
				if(World[j].delete==false){
			  if(World[i].name == name1 && World[j].name == name2){
				  int wi1,wi2,hi1,hi2;
				  wi1=World[i].width;
				  wi2=World[j].width;
				  if(wi1==0){
					  wi1=getan(World[i].Animation).width;
					  hi1=getan(World[i].Animation).height;
				  }else{
					  hi1=World[i].height;
				  }
				  if(wi2==0){
					  wi2=getan(World[j].Animation).width;
					  hi2=getan(World[j].Animation).height;
				  }else{
					  hi2=World[j].height;
				  }
				  //System.out.println(World[i].x+","+World[i].y+"["+wi1+","+hi1+"]   "+World[j].x+","+World[j].y+"  ["+wi2+","+hi2+"]");
			 if( (new Rectangle(World[i].x-5,World[i].y-5,wi1+10,hi1+10)).intersects(new Rectangle(World[j].x,World[j].y,wi2,hi2)) )	  
		     {
				 objectA.add(World[i]);
				 objectB.add(World[j]);
				 bcol = true;
		     }
		     
		     
		  }}}}}
		//System.out.println(bcol);
		  return bcol;
	}
	
	void LoadAnimations(){
		//Animations
		//box
				Animations[ann] = new Animation();
				Animations[ann].place("box",0 , 59, 59);
				Animations[ann].conf(0, 0, 5, false, false);ann++;

				//left-sole
				Animations[ann] = new Animation();
				Animations[ann].place("left-sole",59 , 97, 96);
				Animations[ann].conf(0, 0, 5, false, false);ann++;

				//middle-sole
				Animations[ann] = new Animation();
				Animations[ann].place("middle-sole",155 , 97, 97);
				Animations[ann].conf(0, 0, 5, false, false);ann++;

				//panel
				Animations[ann] = new Animation();
				Animations[ann].place("panel",252 , 48, 50);
				Animations[ann].conf(0, 0, 5, false, false);ann++;

				//player
				Animations[ann] = new Animation();
				Animations[ann].place("player",302 , 73, 75);
				Animations[ann].conf(0, 0, 5, false, false);ann++;

				//right-sole
				Animations[ann] = new Animation();
				Animations[ann].place("right-sole",377 , 97, 96);
				Animations[ann].conf(0, 0, 5, false, false);ann++;

				//water
				Animations[ann] = new Animation();
				Animations[ann].place("water",473 , 96, 87);
				Animations[ann].conf(0, 0, 5, false, false);ann++;
		
			//other
		System.out.println("Animations is Loaded !");
	}
	
	//TODO World initialization
	void Init(int arg0) {
		if(arg0 == -2){
			obn = 0;
			World[obn] = new Object("Test Animations",1,"none",0,0,0,0);
			obn++;
			
		}
			if(arg0 == -1){
			// keyManager
			f.GetFrame().addKeyListener(key);
			LoadAnimations();
			}
			
			/////////////////////////////////////////////////////////////////////////
			//All the Frames
			/////////////////////////////////////////////////////////////////////////

			if(arg0 == 1){
				int i;
				obn = 0;
				
			//Add Objects	
				
				Camera_focus = "player";
				
				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",0,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",59,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",118,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",177,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",236,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",295,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",354,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",413,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",472,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",531,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",590,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",649,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",708,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",354,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",413,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",472,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",531,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",590,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",649,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",708,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",708,-236,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",708,-177,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",708,-118,59,59);
				obn++;

				//panel
				World[obn] = new Object("panel",5,"panel",413,-354,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-177,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-236,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-354,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-413,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-472,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-531,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-590,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",118,-177,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",118,-354,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",118,-531,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",767,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",885,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",885,-177,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1003,-177,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1062,-177,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1121,-177,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1239,-177,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,-177,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1357,-177,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1357,-236,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1357,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1239,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",826,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",826,-236,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",885,-236,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",767,-236,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",826,-177,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",767,-177,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",944,-177,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1180,-177,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1239,-236,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,-236,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1357,-118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,-118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1239,-118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1180,-118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1121,-118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1062,-118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1003,-118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",944,-118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",885,-118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",826,-118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",767,-118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",767,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",826,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",885,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",944,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1003,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1062,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1121,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1180,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1239,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1357,-59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",0,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",59,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",118,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",177,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",236,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",295,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",354,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",413,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",472,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",531,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",590,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",649,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",708,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",767,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",826,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",885,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",944,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1003,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1062,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1121,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1180,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1239,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1357,0,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1357,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1239,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1180,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1121,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1003,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",944,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",885,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",826,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",767,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",649,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",590,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",531,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",472,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",413,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",354,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",295,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",236,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",177,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",118,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",59,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",0,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",0,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",708,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1062,59,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1357,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1239,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1180,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1121,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1062,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1003,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",944,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",885,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",826,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",767,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",708,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",649,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",590,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",531,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",472,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",413,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",413,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",354,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",295,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",236,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",177,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",118,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",59,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",59,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",0,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,118,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",590,-354,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",649,-354,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",649,-413,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",708,-354,59,59);
				obn++;

				//water
				World[obn] = new Object("water",5,"water",944,-295,59,59);
				obn++;

				//water
				World[obn] = new Object("water",5,"water",1003,-295,59,59);
				obn++;

				//water
				World[obn] = new Object("water",5,"water",1062,-295,59,59);
				obn++;

				//water
				World[obn] = new Object("water",5,"water",1121,-295,59,59);
				obn++;

				//water
				World[obn] = new Object("water",5,"water",1180,-295,59,59);
				obn++;

				//water
				World[obn] = new Object("water",5,"water",1180,-236,59,59);
				obn++;

				//water
				World[obn] = new Object("water",5,"water",1121,-236,59,59);
				obn++;

				//water
				World[obn] = new Object("water",5,"water",1062,-236,59,59);
				obn++;

				//water
				World[obn] = new Object("water",5,"water",1003,-236,59,59);
				obn++;

				//water
				World[obn] = new Object("water",5,"water",944,-236,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1416,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1475,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1534,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1593,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1652,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1711,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1770,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1829,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1888,-295,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1888,-354,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1888,-413,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1829,-354,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1770,-531,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1711,-531,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1652,-531,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1593,-531,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1534,-531,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1475,-531,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1416,-531,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1357,-649,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1298,-649,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1239,-649,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1180,-649,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1121,-649,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1062,-649,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1003,-649,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",944,-649,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",885,-767,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",826,-767,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",767,-767,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",708,-767,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",649,-767,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",590,-767,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",531,-767,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",472,-767,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-649,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-708,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-767,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-826,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-885,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-944,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-1003,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",-59,-1062,59,59);
				obn++;

				//panel
				World[obn] = new Object("panel",5,"panel",1180,-708,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",1298,-708,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",1593,-590,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",1947,-413,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",2006,-413,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",2065,-413,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",2124,-413,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",2183,-413,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",2183,-472,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",2183,-531,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",2183,-590,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",2183,-649,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",2183,-708,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",2183,-767,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",2183,-826,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",2183,-885,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",2183,-944,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",2183,-1003,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",2065,-472,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",649,-826,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",649,-885,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",649,-944,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",649,-1003,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",649,-1062,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",649,-1121,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",649,-1180,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",649,-1239,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",649,-1298,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",649,-1357,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",649,-1416,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",649,-1475,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",649,-1534,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",649,-1593,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",649,-1652,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",649,-1711,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",708,-826,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",767,-826,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",177,-885,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",118,-885,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",59,-885,59,59);
				obn++;

				//middle-sole
				World[obn] = new Object("middle-sole",5,"middle-sole",0,-885,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",59,-944,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",59,-1003,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",413,-236,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",472,-236,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",531,-236,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",590,-236,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",590,-177,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",531,-118,59,59);
				obn++;

				//box
				World[obn] = new Object("box",5,"box",649,-118,59,59);
				obn++;

				//player
				World[obn] = new Object("player",5,"player",27,-240,30,30);
				obn++;

				create_physic("player",true, true, "udrl", 0, 7, 16,false);
				create_physic("box",true, true, "none", 0, 7, 6,false);
				//create_wall("water");
				create_wall("right-sole");
				create_wall("left-sole");
				create_wall("middle-sole");
				create_wall("right-sole");
			}
			
			if(arg0 == 2){
				obn = 0;
				
			//Add Objects
				//ob 0
			}
			
			/*
			//ob X
			World[obn] = new Object(50,120,180,50,183,50);
			World[obn].Graphic(545, 10, 0, 0, false, true);
			Mf1[Mf1l] = X ;Mf1l++; // if its a Menu
			obn++;	
			*/

		}
		
	//Frames
 /*Loading ...*/	void Frame0(){
		itime = 0;Init(0);if(cuF == 0){System.out.print("Game Loading Pictures ...");}
		while (cuF == 0) {
			
			
		int i = 0;
		itime = 1;
		for(i=1;i<=50;i++){
		//itime++;
		bs = f.GetCanvas().getBufferStrategy();
		if (bs == null) {
			f.GetCanvas().createBufferStrategy(3);
			bs = f.GetCanvas().getBufferStrategy();
		}
		g = bs.getDrawGraphics();

		// ///////////Start Drawing
		//System.out.print(((GT/10)+1)+"\n");
		
		g.clearRect(0, 0, width, height);
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.white);
	/*	g.drawRect((width/2)-70, (height/2)-20, 100, 30);
		g.drawRect((width/2)-80, (height/2)-30, 120, 50);
		g.drawRect((width/2)-90, (height/2)-40, 140, 70);
	*/	g.drawString("Loading ...",(width/* /2 */)-100, (height /* /2 */)-50 );
		
		// //////////END of Drawing

		bs.show();
		g.dispose();
		}
			//Images[i] = null;
			try {
				world_image = ImageIO.read(Game.class.getResource("/images/world.png"));
				world_image = imageToBufferedImage(makeColorTransparent(world_image,Color.white));
				System.out.print("\nWorld Picture is Loaded !");
			} catch (IOException e) {
				System.out.print("\nWorld Picture is not Loaded X");
				e.printStackTrace();
			}

		//Conditions
		if(testanimation){cuF = -2;}else{
		cuF = 1;}
		itime++;
		}
	}
	
	void Frame1(){
		itime = 0;Init(1);
		while (cuF == 1) {
			itime++;
			///////////////
			/*if(getob("player").p1 == 0){
			if(getob("player").xs == 0){getob("player").Animation="mario-small";}
			if(getob("player").xs > 0){getob("player").Animation="mario-small-walk-to-right";}
			if(getob("player").xs < 0){getob("player").Animation="mario-small-walk-to-left";}
			if(getob("player").ys != 0){getob("player").Animation="mario-small-fly";}
			}
			if(getob("player").p1 == 1){
			if(getob("player").xs == 0){getob("player").Animation="mario-big";}
			if(getob("player").xs > 0){getob("player").Animation="mario-big-walk-to-right";}
			if(getob("player").xs < 0){getob("player").Animation="mario-big-walk-to-left";}
			if(getob("player").ys != 0){getob("player").Animation="mario-big-fly";}
			}
			if(collession("player","gift")){
				for(int o=0;o<objectA.size();o++){
					Object obja=objectA.get(o);//Getwidth() --> to get the width of an object (read the function declaration to understand)
					Object objb=objectB.get(o);//Getheight() --> to get the height
					
					if(Downcoll(obja,objb))
						{score++;objb.Animation="end-gift";objb.name="end-gift";}// else System.out.println("false!");
				}
			getob("score").text="00000"+score;
			}
			if(collession("player","champ")){
				for(int o=0;o<objectA.size();o++){
					Object obja=objectA.get(o);//Getwidth() --> to get the width of an object (read the function declaration to understand)
					Object objb=objectB.get(o);//Getheight() --> to get the height
					obja.p1=1;
					objb.delete=true;
			}}
			if(collession("player","s-gift")){
				for(int o=0;o<objectA.size();o++){
					Object obja=objectA.get(o);//Getwidth() --> to get the width of an object (read the function declaration to understand)
					Object objb=objectB.get(o);//Getheight() --> to get the height
				if(Downcoll(obja,objb)){
					//mario
					World[obn] = new Object("champ",5,"champ",objb.x,objb.y-20,0,0);
					World[obn].Physic(true, true, "none", 5, 7, 6);
					World[obn].xs=50;
					obn++;
					objb.Animation="end-gift";
					objb.name="end-gift";
				}}
			}*/
			///////////////
		G_Eng();
		P_Eng();
		slp();
	}}



	void Frame2(){
		itime = 0;Init(2);
		while (cuF == 2) {
		itime++;
		
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
		
		
		G_Eng();
		P_Eng();
		slp();
	}}
	
	void Framen(){
		itime = 1;Init(-2);
		
		while (cuF == -2) {
			System.out.println(itime+" : "+World[0].Animation+" - "+(ann-1));
			World[0].Animation = Animations[itime].name;
			System.out.println(Animations[itime].name);
			if(key.keys[KeyEvent.VK_ENTER]){ 
				if(itime < ann-1){
				itime++;
				}
				key.keys[KeyEvent.VK_ENTER]	= false;
			}
			G_Eng();
		
		/* Menu Frame
		 * if(key.keys[KeyEvent.VK_UP]){
				if(Mf1s>0){World[Mf1[Mf1s]].pic_start=0;World[Mf1[Mf1s]].pic_counter=0; Mf1s--;}key.keys[KeyEvent.VK_UP] = false;}
			if(key.keys[KeyEvent.VK_DOWN]){
				if(Mf1s<Mf1l-1){World[Mf1[Mf1s]].pic_start=0;World[Mf1[Mf1s]].pic_counter=0; Mf1s++;}key.keys[KeyEvent.VK_DOWN] = false;
			}World[Mf1[Mf1s]].pic_start=1;World[Mf1[Mf1s]].pic_counter=1;
			if(key.keys[KeyEvent.VK_ENTER]){ 
				if(Mf1s == 0){}
				if(Mf1s == 1){}
				if(Mf1s == 2){}
			}
			*/
		
		//Your code
		
		
		G_Eng();
		P_Eng();
		slp();
	}}
		
	public Game(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		f = new Display(title, width, height);
		key = new KeyManager();
		cuF = 0;
		running = true;
	}

	public void Run() {
		Init(-1);
		while (running) {

			Frame0();

			Frame1();

			Frame2();
			
			Framen();
		}
	}


	void G_Eng() {
		bs = f.GetCanvas().getBufferStrategy();
		if (bs == null) {
			f.GetCanvas().createBufferStrategy(3);
			bs = f.GetCanvas().getBufferStrategy();
		}
		g = bs.getDrawGraphics();

		// ///////////Start Drawing

		//G_ENG Timer
		GT++;
		if(GT>101){GT=1;}
		//System.out.print(GT);
		
		// Cleaning Screen
		g.clearRect(0, 0, width, height);
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);

		//Showing Objects on the screen
		if(obn!=0){
		for(int a=1;a<=10;a++){
			for(int i=0;i<obn;i++){
				if(World[i].delete==false){
				if(World[i].align == a){
					if(World[i].Animation != "none"){
						if(World[i].pic_counter>getan(World[i].Animation).finish){
								World[i].pic_counter = getan(World[i].Animation).start;
							}
						if(getan(World[i].Animation).animated){
							
							if((GT % getan(World[i].Animation).interval) == 0) {
								if( (World[i].pic_counter >= getan(World[i].Animation).finish+getan(World[i].Animation).start) || (World[i].pic_counter < getan(World[i].Animation).start) ){
									if(getan(World[i].Animation).oneway){
										World[i].Animation = "none";
									}else{
										World[i].pic_counter = getan(World[i].Animation).start;
									}
								}
								else{
									World[i].pic_counter++; //System.out.print(World[i].pic_counter+"    "); }
									}
							}
						}
						int w = World[i].width;
						int h = World[i].height;
						if(w == 0){w = getan(World[i].Animation).width;}
						if(h == 0){h = getan(World[i].Animation).height;}
						//System.out.println("top = "+getan(World[i].Animation).top+" width = "+getan(World[i].Animation).width);
						int camx = Camera_x;
						int camy = Camera_y;
						if(World[i].pinned){camx=0;camy=0;}
						//System.out.println(World[i].name+"["+i+"]"+" ==> "+(getan(World[i].Animation).width*World[i].pic_counter)+" - "+getan(World[i].Animation).top+" - "+getan(World[i].Animation).width+" - "+getan(World[i].Animation).height);
						g.drawImage(world_image.getSubimage((getan(World[i].Animation).width*World[i].pic_counter), getan(World[i].Animation).top, getan(World[i].Animation).width, getan(World[i].Animation).height), World[i].x-camx, World[i].y-camy,w,h, null);
						if(creator_xsys){
						g.setColor(Color.black);
						g.drawString("xs="+World[i].xs, World[i].x-camx, World[i].y-camy+10);
						g.drawString("ys="+World[i].ys, World[i].x-camx, World[i].y-camy+20);
						}
						}else{//if Animation is "none"
						if(World[i].textmode==true){
							int camx = Camera_x;
							int camy = Camera_y;
							if(World[i].pinned){camx=0;camy=0;}
							g.setColor(World[i].color);
							g.drawString(World[i].text, World[i].x-camx, World[i].y-camy);
							//System.out.println("Drawing Text = "+World[i].text);
						}
					}
				}
			}
			}
		}
		}
		//////////END of Drawing

		bs.show();
		g.dispose();
		//f.GetFrame().getMousePosition().getX()
		//System.out.print(f.GetFrame().getMousePosition().getX()+" x  "+f.GetFrame().getMousePosition().getY()+" y \n");
	}

	void P_Eng() {
		if(!creator){
		//P_ENG Timer
				PT++;
				if(PT>30){PT=1;}
		
		for(int i=0;i<obn;i++){
			if(World[i].delete==false){
				int oldx = World[i].x;
				int oldy = World[i].y;
			//Camera Init
			if(World[i].name == Camera_focus){Camera_xs = World[i].x-width/2; Camera_ys = World[i].y-height/2;}
			if(PT % 2 == 1){
			if(Camera_xs > Camera_x){ Camera_x++; }
			if(Camera_ys > Camera_y){ Camera_y++; }
			if(Camera_xs < Camera_x){ Camera_x--; }
			if(Camera_ys < Camera_y){ Camera_y--; }
			}
			if(Camera_xr == false){ Camera_x = 0; }
			if(Camera_yr == false){ Camera_y = 0; }
			if(Camera_x < Camera_min_x){Camera_x = Camera_min_x;}
				
				//if(World[i].active==false){ World[i].xs=0; World[i].ys=0; }
				if(World[i].active == true){
				//Movement
					if(PT % 2 == 1){}
					if(World[i].groundtouched == false){World[i].ys += World[i].gravity;}
			//Keyboard Control
					int step = 1;
					if(World[i].control == "udrl"){
						if(key.keys[KeyEvent.VK_UP]){if(World[i].ys>-World[i].MaxSpeed){World[i].ys-=step;}}
						if(key.keys[KeyEvent.VK_DOWN]){if(World[i].ys<World[i].MaxSpeed){World[i].ys+=step;}}
						if(key.keys[KeyEvent.VK_RIGHT]){if(World[i].xs<World[i].MaxSpeed){World[i].xs+=step;}}
						if(key.keys[KeyEvent.VK_LEFT]){if(World[i].xs>-World[i].MaxSpeed){World[i].xs-=step;}}
					}
					if(World[i].control == "zsdq"){
						if(key.keys[KeyEvent.VK_Z]){if(World[i].ys>-World[i].MaxSpeed){World[i].ys-=step;}}
						if(key.keys[KeyEvent.VK_S]){if(World[i].ys<World[i].MaxSpeed){World[i].ys+=step;}}
						if(key.keys[KeyEvent.VK_D]){if(World[i].xs<World[i].MaxSpeed){World[i].xs+=step;}}
						if(key.keys[KeyEvent.VK_Q]){if(World[i].xs>-World[i].MaxSpeed){World[i].xs-=step;}}
					}
					if(World[i].control == "url"){
						//System.out.println(World[i].groundtouched);
						if((key.keys[KeyEvent.VK_UP]) && World[i].groundtouched){World[i].ys = -World[i].MaxJump*World[i].MaxSpeed*2;}
						if(PT % 2 == 1){
						if((key.keys[KeyEvent.VK_UP]) && (World[i].groundtouched == false) && World[i].ys >= 0 && World[i].hold_when_down){World[i].ys = -2;}
						 }
						if(key.keys[KeyEvent.VK_RIGHT]){if(World[i].xs<World[i].MaxSpeed){World[i].xs+=step;}}
						if(key.keys[KeyEvent.VK_LEFT]){if(World[i].xs>-World[i].MaxSpeed){World[i].xs-=step;}}
					}
					if(World[i].control == "zdq"){
						//System.out.println(World[i].groundtouched);
						if((key.keys[KeyEvent.VK_Z]) && World[i].groundtouched){World[i].ys = -World[i].MaxJump*World[i].MaxSpeed*2;}
						if(PT % 2 == 1){
						if((key.keys[KeyEvent.VK_Z]) && (World[i].groundtouched == false) && World[i].ys >= 0 && World[i].hold_when_down){World[i].ys = -2;}
						 }
						if(key.keys[KeyEvent.VK_D]){if(World[i].xs<World[i].MaxSpeed){World[i].xs+=step;}}
						if(key.keys[KeyEvent.VK_Q]){if(World[i].xs>-World[i].MaxSpeed){World[i].xs-=step;}}
					}

					
				}
			//END-Keyboard_control
				
			if((World[i].old < 50000) && (PT == 1) ){World[i].old++;}
			gdt = false;
			for(int j=0;j<obn;j++){
				if(World[j].delete==false){
				if(i!=j){
					//Collession xs ys with walls
						if(World[j].wall && World[i].active && World[i].wall){
							int wj = World[j].width;
							int hj = World[j].height;
							int wi = World[i].width;
							int hi = World[i].height;
							if(wj == 0){wj = getan(World[j].Animation).width;}
							if(hj == 0){hj = getan(World[j].Animation).height;}
							if(wi == 0){wi = getan(World[i].Animation).width;}
							if(hi == 0){hi = getan(World[i].Animation).height;}
							
							//-----------------------------------------------------------------------------------------
							
							
							//down
							if(World[i].ys>0){
								if((new Rectangle(World[i].x,World[i].y+hi-World[i].MaxSpeed,wi,World[i].MaxSpeed*2)).intersects(new Rectangle(World[j].x,World[j].y,wj,hj))){
									if(!(new Rectangle(World[i].x,World[i].y+hi-World[i].MaxSpeed,wi,World[i].MaxSpeed+1)).intersects(new Rectangle(World[j].x,World[j].y,wj,hj))){
										World[i].y++;
									}
									if(World[j].active == true){
									World[j].ys += World[i].ys-1;
									if(World[i].ys == 1){World[j].ys++;}
									}
									World[i].ys=0;
									if(gdt == false){gdt = true;}
								}
							}else
							//up
								if(World[i].ys<0){
									if((new Rectangle(World[i].x,World[i].y-World[i].MaxSpeed,wi,World[i].MaxSpeed*2)).intersects(new Rectangle(World[j].x,World[j].y,wj,hj))){
										if(!(new Rectangle(World[i].x,World[i].y-1,wi,World[i].MaxSpeed+1)).intersects(new Rectangle(World[j].x,World[j].y,wj,hj))){
											World[i].y--;
										}
										if(World[j].active == true){
										World[j].ys -= World[i].ys+1;
										if(World[i].ys == -1){World[j].ys--;}
										}
										World[i].ys=0;
										//if(Math.abs(World[j].xs)<Math.abs(World[i].xs))
										//	World[j].xs=World[i].xs;
										//if(World[i].name.equals("player"))System.out.println(Math.abs(World[j].xs)+"<"+Math.abs(World[i].xs));
									}
								}
							//right
							if(World[i].xs>0){
								if((new Rectangle(World[i].x+wi-World[i].MaxSpeed,World[i].y,World[i].MaxSpeed*2,hi)).intersects(new Rectangle(World[j].x,World[j].y,wj,hj))){
									if(!(new Rectangle(World[i].x+wi-World[i].MaxSpeed,World[i].y,World[i].MaxSpeed+1,hi)).intersects(new Rectangle(World[j].x,World[j].y,wj,hj))){
										World[i].x++;
									}
									if(World[j].active == true){
									World[j].xs += World[i].xs-1;
									if(World[i].xs == 1){World[j].xs++;}
									}
									World[i].xs=0;
									//World[i].friction_enabled=true;
								}

							}else
							//left
								if(World[i].xs<0){
									if((new Rectangle(World[i].x-World[i].MaxSpeed,World[i].y,World[i].MaxSpeed*2,hi)).intersects(new Rectangle(World[j].x,World[j].y,wj,hj))){
										if(!(new Rectangle(World[i].x-1,World[i].y,World[i].MaxSpeed+1,hi)).intersects(new Rectangle(World[j].x,World[j].y,wj,hj))){
											World[i].x--;
										}
										if(World[j].active == true){
										World[j].xs -= World[i].xs+1;
										if(World[i].xs == -1){World[j].xs--;}
										}
										World[i].xs=0;
										//World[i].friction_enabled=true;
									}
								}


							
							//Adding Friction Option
							/*if(World[i].friction && (new Rectangle(World[i].x,World[i].y-World[i].MaxSpeed,wi,World[i].MaxSpeed+World[i].MaxSpeed)).intersects(new Rectangle(World[j].x,World[j].y,wj,hj))){
								if(World[i].xs>0)
									if(!(new Rectangle(World[i].x+wi-World[i].MaxSpeed,World[i].y,World[i].MaxSpeed*2,hi)).intersects(new Rectangle(World[j].x,World[j].y,wj,hj)))
									if(Math.abs(World[j].xs)<Math.abs(World[i].xs))
										World[j].friction_ob = i;
								}
							
							//Resloving illogic problems 
							//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
						/*	for(int o=1;o<=1;o++){
							if((new Rectangle(World[i].x,World[i].y+hi,wi,o)).intersects(new Rectangle(World[j].x,World[j].y,wj,hj))){World[i].y-=o;}
							if((new Rectangle(World[i].x,World[i].y-o,wi,o)).intersects(new Rectangle(World[j].x,World[j].y,wj,hj))){World[i].y+=o;}
							}
						*/	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
							
							//Last Conditions of logic
							//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
						/*	if(World[i].ys>0){
								if((new Rectangle(World[i].x,World[i].y+1,wi,hi)).intersects(new Rectangle(World[j].x,World[j].y,wj,hj))){
									World[i].ys=0;}}
							if(World[i].ys<0){
								if((new Rectangle(World[i].x,World[i].y-1,wi,hi)).intersects(new Rectangle(World[j].x,World[j].y,wj,hj))){
									World[i].ys=0;}}
						*/	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
										
					
							//-----------------------------------------------------------------------------------------
							
						}
						
				}
			}}
			for(int j =0;j<obn;j++){
				

				//Friction movement
				if(World[i].friction)
				if((new Rectangle(World[i].x,World[i].y-World[i].MaxSpeed,Getwidth(World[i]),World[i].MaxSpeed*2)).intersects(new Rectangle(World[j].x,World[j].y,Getwidth(World[j]),Getheight(World[j])))){
					
						//if(World[i].ismoving)
						if(World[j].active)
						if(Math.abs(World[j].xs)<Math.abs(World[i].xs))
							World[j].xs=World[i].xs;
						//if(World[i].ismoving==false)World[j].xs-=1;
				}
			}
							World[i].groundtouched = gdt;
							if(PT % 2 == 1 && World[i].active){
								//Movement with xs>x 
								if((Math.abs(World[i].xs) < World[i].MaxSpeed) && (World[i].xs != 0)){
									World[i].x += World[i].xs ;
									if(World[i].xs > 0){ World[i].xs--; }
									if(World[i].xs < 0){ World[i].xs++; }
								}
								else{
									if(World[i].xs > 0){ World[i].xs--; World[i].x += World[i].MaxSpeed;}
									if(World[i].xs < 0){ World[i].xs++; World[i].x -= World[i].MaxSpeed;}
								}
								//Movement with ys>y
								if((Math.abs(World[i].ys) < World[i].MaxSpeed) && (World[i].ys != 0)){
									World[i].y += World[i].ys ;
									if(World[i].ys > 0){ World[i].ys--; }
									if(World[i].ys < 0){ World[i].ys++; }
								}
								else{
									if(World[i].ys > 0){ World[i].ys--; World[i].y += World[i].MaxSpeed;}
									if(World[i].ys < 0){  World[i].ys++; World[i].y -= World[i].MaxSpeed;}
								}
							}
		if(oldx==World[i].x && oldy==World[i].y)World[i].ismoving=false; else World[i].ismoving=true;
			}}
		
		/////////////////////////////////////////////////////////////Updating all objects to add friction XS
		/*for(int i=0;i<obn;i++)if(World[i].friction_ob!=-1)if(World[World[i].friction_ob].friction_enabled){
			World[i].xs=World[World[i].friction_ob].xs;
			World[World[i].friction_ob].friction_enabled=false;
			World[i].friction_ob=-1;
		}
		for(int i=0;i<obn;i++)if(World[i].friction_ob!=-1)if(World[World[i].friction_ob].friction_enabled){
			World[World[i].friction_ob].friction_enabled=false;
			World[i].friction_ob=-1;
		}*/
	

	}else{
		if(Camera_xs > Camera_x){ Camera_x+=p; }
		if(Camera_ys > Camera_y){ Camera_y+=p; }
		if(Camera_xs < Camera_x){ Camera_x-=p; }
		if(Camera_ys < Camera_y){ Camera_y-=p; }
		if(key.keys[KeyEvent.VK_UP]){Camera_ys-=p;}
		if(key.keys[KeyEvent.VK_DOWN]){Camera_ys+=p;}
		if(key.keys[KeyEvent.VK_RIGHT]){Camera_xs+=p;}
		if(key.keys[KeyEvent.VK_LEFT]){Camera_xs-=p;}
	}
	}

	
	
	//End of Class
	
}
