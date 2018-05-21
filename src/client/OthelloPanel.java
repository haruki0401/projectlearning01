package client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OthelloPanel extends JPanel implements MouseListener,MouseMotionListener,ActionListener,Runnable {
	public static final String TIME_LIMIT ="1";
    private static JButton buttonArray[][];//ボタン用の配列
    private static JButton theButton;
    private static JButton giveUp;
    private static JButton pass;
    private static JButton whichTurn;
    JTextField jtext;
    JButton jstart;
    JButton jend;
    Thread th=null;
    static int turn;//手番
    static int rivalI; //相手のi
    static int rivalJ; //相手のj

    private static JLabel win;
    private static JLabel blackData01;
    private static JLabel blackData02;
    private static JLabel blackData03;
    private static JLabel blackData04;
    private static JLabel blackData05;
    private static JLabel whiteData01;
    private static JLabel whiteData02;
    private static JLabel whiteData03;
    private static JLabel whiteData04;
    private static JLabel whiteData05;

    private static ImageIcon blackIcon;
	private static ImageIcon whiteIcon;
	private static ImageIcon choiceIcon;
	private static ImageIcon boardIcon;
//	private static ImageIcon o01 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\001.png");
//	private static ImageIcon o02 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\002.png");
//	private static ImageIcon o03 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\003.png");
//	private static ImageIcon o04 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\004.png");
//	private static ImageIcon o05 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\005.png");
//	private static ImageIcon o06 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\006.png");
//	private static ImageIcon o07 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\007.png");
//	private static ImageIcon o08 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\008.png");
//	private static ImageIcon o09 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\009.png");
//	private static ImageIcon o10 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\010.png");
//	private static ImageIcon o11 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\011.png");
//	private static ImageIcon o12 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\012.png");
//	private static ImageIcon o13= new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\013.png");
//	private static ImageIcon o14 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\014.png");
//	private static ImageIcon o15 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\015.png");
//	private static ImageIcon o16 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\016.png");
//	private static ImageIcon o17 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\017.png");

	 Client client;
    static Othello oB;

    public  OthelloPanel(int a, Client cl) {
        cl.setSize(1500,1000);        //ウィンドウのサイズを設定する
       this.client=cl;
       //アイコンの設定
        whiteIcon = new ImageIcon("./White.jpg");
        blackIcon = new ImageIcon("./Black.jpg");
        boardIcon = new ImageIcon("./GreenFrame.jpg");
        choiceIcon = new ImageIcon("./Choice.jpg");
        //ボタンの生成
        buttonArray = new JButton[8][8];
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
            	if(oB.othello[i][j]==1) {
            buttonArray[i][j] = new JButton(blackIcon);//ボタンにアイコンを設定する
            	}else if(oB.othello[i][j]==2) {
                    buttonArray[i][j] = new JButton(whiteIcon);
            	}else {
                    buttonArray[i][j] = new JButton(boardIcon);
            	}
            buttonArray[i][j].setBounds(550+i*50,50+j*50,50,50);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
            buttonArray[i][j].addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
            buttonArray[i][j].addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
            buttonArray[i][j].setActionCommand(Integer.toString(i)+Integer.toString(j));//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）
            add(buttonArray[i][j]);//ペインに貼り付ける
        }
        }

        blackData01=new JLabel("黒");
        blackData01.setFont(new Font("Arial",Font.PLAIN,16));
        blackData01.setBounds(50,50,100,50);
        add(blackData01);

        blackData02=new JLabel("プレイヤ名");
        blackData02.setFont(new Font("Arial",Font.PLAIN,8));
        blackData02.setBounds(50,75,100,50);
        add(blackData02);

        blackData03=new JLabel("宇佐美");
        blackData03.setFont(new Font("Arial",Font.PLAIN,8));
        blackData03.setBounds(50,100,100,50);
        add(blackData03);

        blackData04=new JLabel("戦績");
        blackData04.setFont(new Font("Arial",Font.PLAIN,8));
        blackData04.setBounds(50,175,100,50);
        add(blackData04);

        blackData05=new JLabel("100戦 100勝 0敗 0分");
        blackData05.setFont(new Font("Arial",Font.PLAIN,8));
        blackData05.setBounds(50,200,100,50);
        add(blackData05);



        whiteData01=new JLabel("白");
        whiteData01.setFont(new Font("Arial",Font.PLAIN,16));
        whiteData01.setBounds(1450,50,100,50);
        add(whiteData01);

        whiteData02=new JLabel("プレイヤ名");
        whiteData02.setFont(new Font("Arial",Font.PLAIN,8));
        whiteData02.setBounds(1450,75,100,50);
        add(whiteData02);

        whiteData03=new JLabel("俊介");
        whiteData03.setFont(new Font("Arial",Font.PLAIN,8));
        whiteData03.setBounds(1450,100,100,50);
        add(whiteData03);

        whiteData04=new JLabel("戦績");
        whiteData04.setFont(new Font("Arial",Font.PLAIN,8));
        whiteData04.setBounds(1450,175,100,50);
        add(whiteData04);

        whiteData05=new JLabel("100戦 100勝 0敗 0分");
        whiteData05.setFont(new Font("Arial",Font.PLAIN,8));
        whiteData05.setBounds(1450,200,100,50);
        add(whiteData05);




        giveUp=new JButton("投了");
        giveUp.setBounds(500,550,100,50);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
        giveUp.addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
        giveUp.addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
        giveUp.setActionCommand("0");//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）
        add(giveUp);

        pass=new JButton("パス");
        pass.setBounds(900,550,100,50);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
        pass.addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
        pass.addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
        pass.setActionCommand("1");//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）
        add(pass);


        jstart=new JButton("はじめ");
        jstart.setBounds(50,200,100,50);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
        jstart.addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
        jstart.addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
        jstart.setActionCommand("3");
        add(jstart);

        jend=new JButton("終了");
        jend.setBounds(50,600,100,50);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
        jend.addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
        jend.addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
        jend.setEnabled(false);//対局終了時にtrueに変更
        jend.setActionCommand("4");
        add(jend);



        jtext=new JTextField(TIME_LIMIT);
        jtext.setBounds(50,450,100,50);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
        add(jtext);

//    }
//
//    public static void main(String[] args) {
    	turn=1;
    	oB =new Othello(a,cl);

        if(oB.bw==1) {
        whichTurn=new JButton("あなたの手番です");
        }else {
            whichTurn=new JButton("相手の手番です");
        }
        whichTurn.setBounds(600,650,200,50);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
        add(whichTurn);


    	oB.print();
        apply();

        if(oB.bw==1) {
		System.out.println("a");//タイマースタート
		if(th==null) {
			th=new Thread(this);
			th.start();
		}
        }
		System.out.println(oB.bw);

    	System.out.println("オセロ呼び出し");
    	//if(oB.bw==turn) {
    		if(oB.bw==1) {
    			oB.choiceB();
    		}
    //	}
		apply();

    }

//    public OthelloPanel(int a, Client cl) {
//		// TODO 自動生成されたコンストラクター・スタブ
//	}

	public void mouseClicked(MouseEvent e) {//ボタンをクリックしたときの処理
    	if(turn==oB.bw) {//自分のターンならば、
    		int theArrayIndexOfI;
    		int theArrayIndexOfJ;
    		System.out.println("クリックされた");
    		//client.sendMessage("9");//turn,i,j
    		theButton = (JButton)e.getComponent();//クリックしたオブジェクトを得る．型が違うのでキャストする
    		String theArrayIndex = theButton.getActionCommand();//ボタンの配列の番号を取り出す
    		System.out.println(theArrayIndex);
    		char[] ArrayIndChar=theArrayIndex.toCharArray();


        if(theArrayIndex.length()>1 && oB.bw==1) {//長さが2以上すなわち、盤面のボタンがクリック
        	theArrayIndexOfI=Character.getNumericValue(ArrayIndChar[0]);//縦番地
        	theArrayIndexOfJ=Character.getNumericValue(ArrayIndChar[1]);//横番地
        	System.out.println(theArrayIndexOfI);
        	System.out.println(theArrayIndexOfJ);
            System.out.println("black");
        	if(oB.othello[theArrayIndexOfI][theArrayIndexOfJ]==4) { //黒が手を打つ
    			//oB.bw=2;//一人プレイ用
        			oB.playB(theArrayIndexOfI,theArrayIndexOfJ);
        			client.sendMessage("5"+theArrayIndexOfI+theArrayIndexOfJ);//識別子i,j
    				client.receiveHandler(1);
    				oB.randJudge=0;//未定
        			oB.endJudge=0;//passの回reset
        	        System.out.println("afterplayendJudge="+oB.endJudge);
        			clear();//choiceの消去
        			oB.print();
        			apply(); //ボタンのアイコン更新
        			oB.judge();//勝敗判定



        			whichTurn.setText("相手の手番です");

        			repaint();//画面のオブジェクトを描画し直す

            		System.out.println("n");
        			jtext.setText(TIME_LIMIT);
            		if(th!=null) {
            				th=null;
            		}

        	}//if
        }//if
        else if(theArrayIndex.length()>1 && oB.bw==2) {//白が手を打つ
            theArrayIndexOfI=Character.getNumericValue(ArrayIndChar[0]);
            theArrayIndexOfJ=Character.getNumericValue(ArrayIndChar[1]);
            System.out.println(theArrayIndexOfI);
            System.out.println(theArrayIndexOfJ);
            System.out.println("white");
            if(oB.othello[theArrayIndexOfI][theArrayIndexOfJ]==4) {
            	oB.playW(theArrayIndexOfI,theArrayIndexOfJ);
    			client.sendMessage("5"+theArrayIndexOfI+theArrayIndexOfJ);
    			client.receiveHandler(1);
   				oB.randJudge=0;//未定
            	oB.endJudge=0;
            	clear();
    	        System.out.println("afterplayendJudge="+oB.endJudge);
            	oB.print();
            	apply(); //ボタンのアイコン更新
            	oB.judge();
            	System.out.println("通過");
            	//oB.bw=1;//一人プレイ用
            	//oB.sendInf(turn,theArrayIndexOfI,theArrayIndexOfJ);

    			whichTurn.setText("相手の手番です");

            	repaint();//画面のオブジェクトを描画し直す
        		System.out.println("n");
    			jtext.setText(TIME_LIMIT);
        		if(th!=null) {
        				th=null;
        		}

            }//if
        }//if
        else{
            int giveOrPass=Character.getNumericValue(ArrayIndChar[0]); //giveUpならintの0、passならintの1
            if(giveOrPass==0) {
                jend.setEnabled(true);//対局終了時にtrueに変更
            	//giveのそうさ
            	if(oB.bw==1) {
            			//oB.giveUp(1);
            		oB.endJudge=2;
            			client.sendMessage("599");
            			client.receiveHandler(1);
            			win=new JLabel("白の勝利");
            			add(win);
            			win.setBounds(0,0,100,50);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
            			repaint();
                   		System.out.println("n");
            			jtext.setText(TIME_LIMIT);
                		if(th!=null) {
                				th=null;
                		}
            	}else {
        			//oB.giveUp(2);
            		oB.endJudge=2;
        			client.sendMessage("599");
        			client.receiveHandler(1);
           			win=new JLabel("黒の勝利");
        			add(win);
        			win.setBounds(0,0,100,50);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
        			repaint();
               		System.out.println("n");
        			jtext.setText(TIME_LIMIT);
            		if(th!=null) {
            				th=null;
            		}
            	}
            }else if(giveOrPass==1) {
            	//passのそうさ
            	//if(oB.bw==1) {
                    oB.endJudge++;

                    oB.judge();
        			client.sendMessage("588");
        			client.receiveHandler(1);
        	        System.out.println("afterPasssendJudge="+oB.endJudge);

                    //oB.choiceW();
        			//client.sendMessage("9othello1"+theArrayIndexOfI+theArrayIndexOfJ);
                    apply();

        			whichTurn.setText("相手の手番です");

                    repaint();//画面のオブジェクトを描画し直す
                    //oB.bw=2;
               		System.out.println("n");
        			jtext.setText(TIME_LIMIT);
            		if(th!=null) {
            				th=null;
            		}

//            	}else {
//                    oB.endJudge++;
//                    oB.judge();
//        			client.sendMessage("588");
//        			client.receiveHandler(1);
//                   // oB.choiceB();
//                    apply();
//                    repaint();//画面のオブジェクトを描画し直す
//                    //oB.bw=1;
//               		System.out.println("n");
//        			jtext.setText(TIME_LIMIT);
//            		if(th!=null) {
//            				th=null;
//            		}
//
//            	}
            }else if(giveOrPass==3){
//        		System.out.println("a");
//        		if(th==null) {
//        			th=new Thread(this);
//        			th.start();
//        		}

            }else{
//        		System.out.println("n");
//    			jtext.setText("30");
//        		if(th!=null) {
//        				th=null;
//        		}

            }
        }
    	}//大きなif
    }


    public static void clear() {//4の消去
        for(int j=0;j<8;j++){
            for(int i=0;i<8;i++){
            	if(oB.othello[i][j]==4) {
            		oB.othello[i][j]=0;            	}
        }
        }
    }

    public static void apply() {//アイコンの適用
        for(int j=0;j<8;j++){
            for(int i=0;i<8;i++){
            	if(oB.othello[i][j]==1) {
            		 buttonArray[i][j].setIcon(blackIcon);//blackIconに設定する
            	}else if(oB.othello[i][j]==2) {
            		 buttonArray[i][j].setIcon(whiteIcon);//whiteIconに設定する
            	}else if(oB.othello[i][j]==4){
           		 	buttonArray[i][j].setIcon(choiceIcon);//whiteIconに設定する
            	}else {
            		 buttonArray[i][j].setIcon(boardIcon);//boardIconに設定する
            	}
            }
        }
    }

    public void ChangetoW() {//白への手番変更
        try{
           	Thread.sleep(300); //3000ミリ秒Sleepする

           }catch(InterruptedException e1){}

    	apply();
    }


    public void waiting() {
    	System.out.println("waitingに到達");
		whichTurn.setText("あなたの手番です");
    	System.out.println(turn);
    	System.out.println(oB.bw);
    	if(turn==oB.bw) {//相手からの情報が来たとき
    	        if(oB.bw==1) {
    		        if(rivalI<8 && rivalJ<8 ) {
    	        		oB.othello[rivalI][rivalJ]=4;//受け取ったijの番地を4にする
    	        		oB.playW(rivalI,rivalJ);//playW(i,j)を実行
    		        }else if(rivalI==9 && rivalJ==9 ) {
	    	            jend.setEnabled(true);//対局終了時にtrueに変更
	               		System.out.println("n");
	                    jend.setEnabled(true);//対局終了時にtrueに変更
	        			jtext.setText(TIME_LIMIT);
	            		if(th!=null) {
	            				th=null;
	            		}
	            		oB.endJudge=2;
    		        	oB.giveUp(2);//白のギブアップ);
    		        }
    	        		oB.choiceB();
    	        		apply();
    	        		repaint();
    	            }else if(oB.bw==2){
    	    	        if(rivalI<8 && rivalJ<8 ) {
    	            	oB.othello[rivalI][rivalJ]=4;//受け取ったijの番地を4にする
    	            	oB.playB(rivalI,rivalJ);//playB(i,j)を実行
    	    	        }else if(rivalI==9 && rivalJ==9 ) {
    	    	            jend.setEnabled(true);//対局終了時にtrueに変更
    	               		System.out.println("n");
    	                    jend.setEnabled(true);//対局終了時にtrueに変更
    	        			jtext.setText(TIME_LIMIT);
    	            		if(th!=null) {
    	            				th=null;
    	            		}
    	            		oB.endJudge=2;
        		        	oB.giveUp(1);//黒のギブアップ);
        		        }
//    	    	        else if(rivalI==8 && oB.endJudge==2) {
//                       		System.out.println("n");
//                			jtext.setText(TIME_LIMIT);
//                    		if(th!=null) {
//                    				th=null;
//                    		}
//        		        }

//    	    	        if(oB.endJudge==2) {
//                   		System.out.println("n");
//            			jtext.setText(TIME_LIMIT);
//                		if(th!=null) {
//                				th=null;
//                		}
//    		        }

    	            	oB.choiceW();
    	            	apply();
    	            	repaint();
    	        }
    	        System.out.println("endJudge="+oB.endJudge);

    	        oB.judge();
         		System.out.println("emptyy="+oB.emp_num);
    	        if(oB.emp_num==0) {

        			jtext.setText(TIME_LIMIT);
            		if(th!=null) {
            				th=null;
            		}
                    jend.setEnabled(true);//対局終了時にtrueに変更
    	        }

    	        if(oB.endJudge!=2) {
    			System.out.println("a");//タイマースタート
    			if(th==null) {
    				th=new Thread(this);
    				th.start();
    			}
    	        }
    	        if(oB.endJudge==2){
               		System.out.println("n");
                    jend.setEnabled(true);//対局終了時にtrueに変更
        			jtext.setText(TIME_LIMIT);
            		if(th!=null) {
            				th=null;
            		}
                    jend.setEnabled(true);//対局終了時にtrueに変更

    	        }

    	}

    }

    public void setNum(int tur,int ii,int jj) {
    	turn=tur;
    	if(oB.bw==1) {
    		oB.playW(ii,jj);
    		repaint();
    	}else {
    		oB.playB(ii,jj);
    		repaint();
    	}
    }




    class ThreadX extends Thread {
        String str;
        long msec;
       // static String greet="Hello";

        ThreadX(String str, long msec) {
            this.str = str;
            this.msec = msec;
        }

        public void run() {
            try {
                while(true) {
                    Thread.sleep(msec);  // Threadを継承しているので sleep(msec); でも可　InterruptedExceptionを投げる
                    System.out.println(str+":");
                }
            }
            catch(InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }


	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	void startAction(ActionEvent e) {
		System.out.println("a");
		if(th==null) {
			th=new Thread(this);
			th.start();
		}
	}
	void endAction(ActionEvent e) {
		System.out.println("n");
		if(th!=null) {
				th=null;
		}
	}
	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		int i;
		while(th!=null) {
			i=Integer.parseInt(jtext.getText());
			try {
				th.sleep(1000);
				if(th==null) break;
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				break;
			}
			if(i-1>=0) {
				jtext.setText(Integer.toString(i-1));
			}else {
				jtext.setText(TIME_LIMIT);
				th=null;
				if(oB.bw==1) {
					oB.choiceB();
					System.out.println(oB.randJudge);
					if(oB.randJudge>0){
					oB.random(1);
					}
					System.out.println("おけない");
				}else {
					oB.choiceW();
					System.out.println(oB.randJudge);
					if(oB.randJudge>0){
					oB.random(2);
					}
					System.out.println("おけない");
				}
				System.out.println("rand実行");
				if(oB.randJudge>0) {
    			client.sendMessage("5"+oB.r1+oB.r2);//識別子i,j
    			oB.endJudge=0;//passの回reset
				}else {
	    			client.sendMessage("588");//識別子i,j
	    			oB.endJudge++;//passの回inc
				}
				client.receiveHandler(1);
				oB.randJudge=0;//未定
//    			oB.endJudge=0;//passの回reset
    			clear();//choiceの消去
    			oB.print();
    			apply(); //ボタンのアイコン更新
    			oB.judge();//勝敗判定
            	//oB.sendInf(turn,theArrayIndexOfI,theArrayIndexOfJ);
    			whichTurn.setText("相手の手番です");
    			repaint();//画面のオブジェクトを描画し直す

			}
		}

	}


	public void endOthello() {
		if(oB.sendedMessage==1) {
    		System.out.println("n");
			jtext.setText(TIME_LIMIT);
    		if(th!=null) {
    				th=null;
    		}
    		//6受け取ったらスレッド停止、終了ボタンをtrueに
		}
	}
}

