package home_control_panel;



import javax.swing.Icon;
import javax.swing.ImageIcon;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;



public class play extends javax.swing.JFrame implements KeyListener {

    public play() {

    	addKeyListener ( this ) ;
        initComponents();

    }

   
    

  
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        l1 = new javax.swing.JLabel();
        bacause_we_can_label = new javax.swing.JLabel();
        stop = new javax.swing.JLabel();
        forward = new javax.swing.JLabel();
        backward = new javax.swing.JLabel();
        left = new javax.swing.JLabel();
        right = new javax.swing.JLabel();
        image_label = new javax.swing.JLabel();
        
        setTitle("RC CAR HACKED!!");
        
       
        URL uri=this.getClass().getClassLoader().getResource("home_control_panel/rc_car.jpg");
        ImageIcon rc_car= new ImageIcon(uri);
        l1.setFont(new java.awt.Font("Comic Sans MS", Font.ITALIC, 24)); 
        l1.setText("Play"); 
         


        bacause_we_can_label.setFont(new java.awt.Font("Harrington", Font.BOLD, 24)); 
        bacause_we_can_label.setForeground(Color.lightGray); 
        bacause_we_can_label.setText("Because we can"); 
       

        stop.setFont(new java.awt.Font("Tahoma", 1, 11)); 
        stop.setText("2 - Emergency STOP"); 
        

        forward.setFont(new java.awt.Font("Tahoma", 1, 11)); 
        forward.setText("w - Forward"); 
        

        backward.setFont(new java.awt.Font("Tahoma", 1, 11)); 
        backward.setText("s - Backward"); 
       

        left.setFont(new java.awt.Font("Tahoma", 1, 11)); 
        left.setText("a - Left"); 
      

        right.setFont(new java.awt.Font("Tahoma", 1, 11)); 
        right.setText("d - Right"); 
      
        image_label.setIcon(rc_car);
       
        
        



        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(image_label)
                        .addGap(460, 460, 460)
                        .addComponent(bacause_we_can_label))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(left)
                            .addComponent(forward))
                        .addGap(54, 54, 54)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(backward)
                                .addGap(15, 15, 15))
                            .addComponent(right))
                        .addGap(526, 526, 526)
                        .addComponent(stop)))
                .addContainerGap(61, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap(402, Short.MAX_VALUE)
                .addComponent(l1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(277, 277, 277))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(image_label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 284, Short.MAX_VALUE)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(left)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(forward))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(right)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(backward)))
                        .addContainerGap())
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(bacause_we_can_label)
                        .addGap(139, 139, 139)
                        .addComponent(l1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 212, Short.MAX_VALUE)
                        .addComponent(stop)
                        .addGap(20, 20, 20))))
        );
pack();
    }
	Communicator communicator=null;
    private javax.swing.JLabel bacause_we_can_label;
    private javax.swing.JLabel backward;
    private javax.swing.JLabel forward;
    private javax.swing.JLabel image_label;
    private javax.swing.JLabel l1;
    private javax.swing.JLabel left;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel right;
    private javax.swing.JLabel stop;
    
    public void keyTyped ( KeyEvent e ){
    	  if(e.getKeyChar()=='w'){
          	l1.setText ( "Forward" ) ;
    	  }else if(e.getKeyChar()=='s'){
          	l1.setText ( "Backward" ) ;
    	  }else if (e.getKeyChar()=='a'){
          	l1.setText ( "Left" ) ;
    	  }else if (e.getKeyChar()=='d'){
          	l1.setText ( "Right" ) ;
    	  }else if (e.getKeyChar()=='2'){
          	l1.setText("STOP");
    	  }else {
    		  l1.setText("Unknown Command");
    	  }
	
     }
		public void keyPressed ( KeyEvent e){
		
                if(e.getKeyChar()=='w'){
                	l1.setText ( "Forward" ) ;
                   communicator.writeData("w");
                }else if(e.getKeyChar()=='s'){
                	l1.setText ( "Backward" ) ;
                	communicator.writeData("s");
                	
                }else if (e.getKeyChar()=='a'){
                	l1.setText ( "Left" ) ;
                	communicator.writeData("a");
                }else if (e.getKeyChar()=='d'){
                	l1.setText ( "Right" ) ;
                	communicator.writeData("d");
                }else if (e.getKeyChar()=='2'){
                	l1.setText("STOP");
                	communicator.writeData("2");
                }
		}
		public void keyReleased ( KeyEvent e ){
		//l1.setText( "Key Released" ) ;
		if (e.getKeyChar()=='w'){
			communicator.writeData("3");
		}else if(e.getKeyChar()=='s'){
			communicator.writeData("4");
			
		}else if(e.getKeyChar()=='a'){
			communicator.writeData("5");
		}else if(e.getKeyChar()=='d'){
			communicator.writeData("6");
		}
		}
		
  }
  
