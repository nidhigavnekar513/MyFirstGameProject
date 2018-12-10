import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class MY_GAME extends Applet implements Runnable,ActionListener,KeyListener {
	
	Button b,bE;
	Label l;
	static int setI=1;
	int bx,by,lx,ly,x;
	int life =3;
	int pin, score = 0, pChoice =0;
	static int lastScore;
	int newScore;
	String Username;
	
	String url = "jdbc:oracle:thin:@localhost:1521:XE";
	Connection con;
	Statement st;
	
	Scanner sc = new Scanner(System.in);
	MY_GAME mg;
	Thread th ;
	
	public void init(){
		
		
		 	mg = new MY_GAME();
		 	th = new Thread(this);	
			setLayout(null);
			bE = new Button("RAVAN");
			b = new Button("JIVAN");
			l = new Label("");
	
	
			b.setBounds(400,800,100,50);
			b.setBackground(Color.YELLOW);
			b.addKeyListener(this);
	
			setBackground(Color.BLACK);
			bE.setBackground(Color.GREEN);
			bE.setBounds(10, 10, 180, 30);
	
			l.setBounds(440, 780, 20, 20);
			l.setBackground(Color.RED);
	
			add(b);
			add(bE);
			add(l);

			b.addActionListener(this);

	     mg.connect_Database();
	     System.out.println("Instructions to play a game.");
	     System.out.println("You have 3 life.\nIf you miss the chance to hit the RaaVon then life will get reduced");
	     System.out.println("Click the button(JIVAN) to hit the enemy");
	     System.out.println("For New-Player.PRESS 1\nAlready a Player.PRESS 0");
	     pChoice = sc.nextInt();
	     
	     if(pChoice == 1){
	    	 mg.write_Database(Username, pin ,score);
	    	 
	    	 System.out.println("Please Login First");
	    	 
	    	 System.out.println("Enter Username");
	    	 Username = sc.next();
	    	 System.out.println("Enter your Pin");
	    	 pin = sc.nextInt();
	    	mg.login(Username,pin,score);
	     }
	     else{
	    	 
	    	 System.out.println("Enter Username");
	    	 Username = sc.next();
	    	 System.out.println("Enter your Pin");
	    	 pin = sc.nextInt();
	    	 mg.login(Username,pin,score);
	    	
	     }
	     
	     mg.read_Database(Username, pin, score);
	     
	     if(setI == 0){

				th.start();
	     }
	     else{
    	  	System.out.println("You have only one more chance to login");
    	  	
    	  	System.out.println("Enter Username");
 	 		Username = sc.next();
 	 		System.out.println("Enter your Pin");
 	 		pin = sc.nextInt();
 	  		mg.login(Username,pin,score);
 	  		
 	 			if(setI==0){

 	 				th.start();
 	 			}	
 	 			else{		
 	 					System.out.println("Sorryyyy.......");
 	 			}	
 	 		}
		}

	public void connect_Database(){
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("Driver Found");
			con = DriverManager.getConnection(url,"system","nidh513");
			System.out.println("Connection Done");
			st = con.createStatement();
			
			}
			catch(Exception e){
			e.printStackTrace();
			}

	}

	public void write_Database(String wName, int wPin, int wScore){
		
		try{
		
			System.out.println("Enter New Username");
			wName = sc.next();
			
			System.out.println("Enter New Pin");
			wPin = sc.nextInt();
			
			wScore =0;
			String write_Query = "Insert into my_game values('"+wName+"' , '"+wPin+"','"+wScore+"')";
			st.execute(write_Query);
			System.out.println("Data Inserted Successfully");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void login(String lName, int lPin, int lScore) {
		
		try{
			
			String query = "Select * from my_game where name = '"+lName+"' and pin ='"+lPin+"'";
			ResultSet rs = st.executeQuery(query);
			
			if(rs.next()){
				
				System.out.println("Login Successful");
				setI=0;
		    }
			else{
		    	System.out.println("Login Unsuccessful");
		    	setI=1;
		    }
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public void read_Database(String rName, int rPin, int rScore){
		
		try{
			String readQuery = "Select * from my_game where name = '"+rName+"'";
			ResultSet rs = st.executeQuery(readQuery);
			
			if(rs.next()){
				do{
					System.out.println("YOUR HIGHSCORE IS :"+rs.getInt(3));
					lastScore = rs.getInt(3);
				}while(rs.next());
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void update(String uName, int uScore){
		
		try{
			
		
		String update_Query = "update my_game set score = '"+uScore+"' where name = '"+uName+"'";
		int result = st.executeUpdate(update_Query);
			
			if(result == 0){
				System.out.println("Data not found");
			}
			else{
				System.out.println("Data Updated Successfully");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	@Override
	
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == e.VK_UP){
			
			System.out.println("UP Key Is Pressed");
			ly = l.getY();
			ly-=8;
			l.setLocation(l.getX(), ly);
			
			by = b.getY();
			by-=8;
			b.setLocation(b.getX(), by);
		}
		
		if(e.getKeyCode() == e.VK_DOWN){
			System.out.println("DOWN Key Is Pressed");
			ly = l.getY();
			ly+=8;
			l.setLocation(l.getX(), ly);
			
			by = b.getY();
			by+=8;
			b.setLocation(b.getX(), by);
		}
		
		if(e.getKeyCode() == e.VK_RIGHT){
			System.out.println("RIGHT Key Is Pressed");
			lx = l.getX();
			lx+=8;
			l.setLocation(lx, l.getY());
			
			bx = b.getX();
			bx+=8;
			b.setLocation(bx, b.getY());
		}
		
		if(e.getKeyCode() == e.VK_LEFT){
			System.out.println("LEFT Key Is Pressed");
			bx = b.getX();
			bx-=8;
			b.setLocation(bx, b.getY());
			
			lx = l.getX();
			lx-=8;
			l.setLocation(lx, l.getY());
		}
		
		if(e.getKeyCode() == e.VK_ENTER){
			System.out.println("ENTER Key Is Pressed");
			for(int y = l.getY() ; y>10 ; y-=3){
					l.setLocation(l.getX(), y);
					delay(5);
			}
			hitEnemy();
			l.setLocation(b.getX()+40,b.getY()-20);
			bE.setBackground(Color.GREEN);
		}
	}	
	
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == b){
			
			for(int y = l.getY() ; y>10 ; y= y-3){
				l.setLocation(l.getX(), y);
				delay(5);
			}
			hitEnemy();
			l.setLocation(b.getX()+40,b.getY()-20);
			bE.setBackground(Color.GREEN);
		}
	}
	
	public void run() {
		
		for(;;){
			enemyMove();
		} 
	}
		
    public void delay(int n){
		
		try{
			Thread.sleep(n);;
		}
		catch(InterruptedException e){
				e.printStackTrace();
		}
	}

	public void enemyMove(){
			
		System.out.println("RaaVan is moving in right direction");	
		
		for(x = 0;x<50;x++){
	    	bE.setLocation(bE.getX()+x, bE.getY());
	    	delay(100);
	    }	
	    
		System.out.println("RaaVan is moving in left direction");
        for(x = 0;x<50;x++){
        	bE.setLocation(bE.getX()-x, bE.getY());
	    	delay(100);
	    }	
	}

	
	public void hitEnemy(){
		
		if(l.getY()<60){
			
			if(l.getX()>bE.getX()&&l.getX()<bE.getX()+180){
				score++;
				newScore = score;
				System.out.println("Your Score is:"+score);
				bE.setBackground(Color.RED);
				delay(200);
				repaint();
				
			}
			else{
				life--;
				System.out.println("You Lost One LIfe");	
				
				if(life>0){
					repaint();
				}
				else{
					th.stop();
					b.disable();
					bE.disable();
					System.out.println("GAME OVER");
					repaint();
					if(newScore > lastScore){
						System.out.println("Congratulations.\nYour HighScore is:"+newScore);
						update(Username,score);
					}
					else{
						System.out.println("Your Score Is Not Updated");
					}
				}
			}
		}
	}
	public void paint(Graphics g){
		
		g.setFont(new Font("TimesNewRoman",Font.PLAIN,50));
		g.setColor(Color.BLUE);
		g.drawString("Score:"+score, 1400, 300);
		
		g.setFont(new Font("TimesNewRoman",Font.PLAIN,50));
		g.setColor(Color.BLUE);
		g.drawString("Life left:"+life, 1400, 400);		
		
		g.setFont(new Font("TimesNewRoman",Font.PLAIN,50));
		g.setColor(Color.BLUE);
		g.drawString("Highscore:"+lastScore, 1400, 500);		
		
		if(life == 0){
			g.setFont(new Font("REGULAR",Font.PLAIN,200));
			g.setColor(Color.RED);
			g.drawString("GAME OVER", 200, 600);		
			
		}
	}
}


	

