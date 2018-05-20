package client;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OthelloPanel extends JPanel implements MouseListener,MouseMotionListener {
    private static JButton buttonArray[][];//ボタン用の配列
    private static JButton theButton;
    private static JButton giveUp;
    private static JButton pass;
    //private Container c;
    private static int turn;//手番
    private static JLabel win;
    private static ImageIcon blackIcon;
	private static ImageIcon whiteIcon;
	private static ImageIcon choiceIcon;
	private static ImageIcon boardIcon;
	private static ImageIcon o01 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\001.png");
	private static ImageIcon o02 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\002.png");
	private static ImageIcon o03 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\003.png");
	private static ImageIcon o04 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\004.png");
	private static ImageIcon o05 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\005.png");
	private static ImageIcon o06 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\006.png");
	private static ImageIcon o07 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\007.png");
	private static ImageIcon o08 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\008.png");
	private static ImageIcon o09 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\009.png");
	private static ImageIcon o10 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\010.png");
	private static ImageIcon o11 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\011.png");
	private static ImageIcon o12 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\012.png");
	private static ImageIcon o13= new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\013.png");
	private static ImageIcon o14 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\014.png");
	private static ImageIcon o15 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\015.png");
	private static ImageIcon o16 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\016.png");
	private static ImageIcon o17 = new ImageIcon("C:\\\\Users\\\\Loxalliott\\\\Desktop\\\\pleiades-4.7.1-java-win-64bit-jre_20171019\\\\pleiades\\\\workspace\\\\OThello\\\\src\\\\017.png");

	private static  Client client;
    static Othello oB;

    public OthelloPanel(int a, Client cl) {
    	//removeAll();
        //ウィンドウを作成する
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じるときに，正しく閉じるように設定する
        //setTitle("Othello");        //ウィンドウのタイトルを設定する
        cl.setSize(1500,700);        //ウィンドウのサイズを設定する
        //c = getContentPane();    //フレームのペインを取得する
        //c.setLayout(null);        //自動レイアウトの設定を行わない
        client=cl;

        client.sendMessage("!");

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


            buttonArray[i][j].setBounds(500+i*50,j*50,50,50);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
            buttonArray[i][j].addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
            buttonArray[i][j].addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
            buttonArray[i][j].setActionCommand(Integer.toString(i)+Integer.toString(j));//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）
            add(buttonArray[i][j]);//ペインに貼り付ける

            }
        }

        giveUp=new JButton("投了");
        giveUp.setBounds(400,600,100,50);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
        giveUp.addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
        giveUp.addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
        giveUp.setActionCommand("0");//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）

        add(giveUp);


        pass=new JButton("パス");
        pass.setBounds(1000,600,100,50);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
        pass.addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
        pass.addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
        pass.setActionCommand("1");//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）

        add(pass);

//    }
//
//    public static void main(String[] args) {
    	turn=1;
    	oB =new Othello(a);
    	oB.print();
    	//Othello oW =new Othello(2);
//        OthelloPanel gui = new OthelloPanel();
       // gui.setVisible(true);
        apply();


    	System.out.println("risky!");

//        	while(turn!=3) {
//        		if(turn!=oB.bw) {
//        	        if(oB.bw==1) {
//        	        		//受け取ったijの番地を4にする
//        	        		//playW(i,j)を実行
//        	        		oB.choiceB();
//        	        		apply();
//        	            }else {
//        	        		//受け取ったijの番地を4にする
//        	        		//playB(i,j)を実行
//
//        	            	oB.choiceW();
//        	            	apply();
//
//        	            }
//        	}
//
//        }
        	System.out.println("risky!");

        	repaint();
    }

//    public OthelloPanel(int a, Client cl) {
//		// TODO 自動生成されたコンストラクター・スタブ
//	}

	public void mouseClicked(MouseEvent e) {//ボタンをクリックしたときの処理
    	if(turn==oB.bw) {
    	 int theArrayIndexOfI;
    	 int theArrayIndexOfJ;
        System.out.println("クリック");
        theButton = (JButton)e.getComponent();//クリックしたオブジェクトを得る．型が違うのでキャストする
        String theArrayIndex = theButton.getActionCommand();//ボタンの配列の番号を取り出す
        System.out.println(theArrayIndex);
        char[] ArrayIndChar=theArrayIndex.toCharArray();


        if(theArrayIndex.length()>1 && oB.bw==1) {
        	theArrayIndexOfI=Character.getNumericValue(ArrayIndChar[0]);
        	theArrayIndexOfJ=Character.getNumericValue(ArrayIndChar[1]);
        	System.out.println(theArrayIndexOfI);
        	System.out.println(theArrayIndexOfJ);

        	if(oB.othello[theArrayIndexOfI][theArrayIndexOfJ]==4) { //黒が手を打つ
            	System.out.println(theArrayIndexOfI);
            	System.out.println(theArrayIndexOfJ);
        			oB.playB(theArrayIndexOfI,theArrayIndexOfJ);
        			client.sendMessage("9");//turn,i,j
        			oB.endJudge=0;
        			oB.print();
        			apply(); //ボタンのアイコン更新
        			oB.judge();
        			System.out.println(oB.bw);
        			turn=2;//一人プレイ用
                	oB.sendInf(turn,theArrayIndexOfI,theArrayIndexOfJ);
        			repaint();//画面のオブジェクトを描画し直す
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
    			client.sendMessage("9thello1"+theArrayIndexOfI+theArrayIndexOfJ);

            	oB.endJudge=0;
            	oB.print();
            	oB.judge();
            	apply(); //ボタンのアイコン更新
            	System.out.println(oB.bw);
            	turn=1;//一人プレイ用
            	oB.sendInf(turn,theArrayIndexOfI,theArrayIndexOfJ);
            	repaint();//画面のオブジェクトを描画し直す
            }//if
        }//if
        else{
            int giveOrPass=Character.getNumericValue(ArrayIndChar[0]); //giveUpならintの0、passならintの1
            if(giveOrPass==0) {
            	//giveのそうさ
            	if(oB.bw==1) {
            			oB.giveUp(1);

            			win=new JLabel("白の勝利");
            			add(win);
            			win.setBounds(0,0,100,50);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
            			repaint();
            	}else {
        			oB.giveUp(2);
           			win=new JLabel("黒の勝利");
        			add(win);
        			win.setBounds(0,0,100,50);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
        			repaint();
            	}
            }else {
            	//passのそうさ
            	if(oB.bw==1) {
                    oB.endJudge++;
                    oB.judge();
                    oB.choiceW();
                    apply();
                    repaint();//画面のオブジェクトを描画し直す
                    oB.bw=2;

            	}else {
                    oB.endJudge++;
                    oB.judge();
                    oB.choiceB();
                    apply();
                    repaint();//画面のオブジェクトを描画し直す
                    oB.bw=1;

            	}
            }
        }
    	}//大きなif
    }

    public void mouseEntered(MouseEvent e) {//マウスがオブジェクトに入ったときの処理
    }

    public void mouseExited(MouseEvent e) {//マウスがオブジェクトから出たときの処理
    }

    public void mousePressed(MouseEvent e) {//マウスでオブジェクトを押したときの処理（クリックとの違いに注意）
    }

    public void mouseReleased(MouseEvent e) {//マウスで押していたオブジェクトを離したときの処理
    }

    public void mouseDragged(MouseEvent e) {//マウスでオブジェクトとをドラッグしているときの処理
    }

    public void mouseMoved(MouseEvent e) {//マウスがオブジェクト上で移動したときの処理
    }

    public static void apply() {
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

    public void changeColor() {
        for(int j=0;j<8;j++){
            for(int i=0;i<8;i++){
    	if(oB.othello[i][j]==5) {
   		 buttonArray[i][j].setIcon(o01);
   		 repaint();
   		 buttonArray[i][j].setIcon(o02);
   		 repaint();
    	}
    	}
            }
    }

    public void waiting() {
    	int stop=0;
    	while(turn!=3) {
    		if(turn==oB.bw) {
    	        if(oB.bw==1 && stop==0) {
    	        		//受け取ったijの番地を4にする
    	        		//playW(i,j)を実行
    	        		oB.choiceB();
    	        		apply();
    	        		stop=1;
    	            }else if(oB.bw==2 && stop==0){
    	        		//受け取ったijの番地を4にする
    	        		//playB(i,j)を実行

    	            	oB.choiceW();
    	            	apply();
    	            	stop=1;

    	            }
    	}else {
    		stop=0;
    	}

    }
    }

}