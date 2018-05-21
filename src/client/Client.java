package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;



public class Client extends JFrame{

	public static final int x=1500;
	public static final int y=1000;

	private static int receiveHandler=0;//1:データ受信要求中

	private PrintWriter out;
	private Receiver receiver;

	//private String tempPlayerID;//一時的なIDの保存

	private Player my;
	private ArrayList<Player> getOfferPlayer = new ArrayList<Player>();//offer人数

	private String nowOfferPlayer="";

	//panel作成
	//public String[] PanelNames= {"main","menu","Othello"};

	MainPanel mainPanel=new MainPanel(this);
	MenuPanel menuPanel;

	OthelloPanel othelloPanel;


	public Client() {//コンストラクタ
		this.add(mainPanel);
		mainPanel.setLayout(null);
		mainPanel.setVisible(true);

		/*this.add(menuPanel);
		menuPanel.setLayout(null);
		menuPanel.setVisible(false);*/

		this.setSize(x,y);
	}


	public void connectServer(String ipAddress,int port) {
		Socket socket=null;
		try {
			socket=new Socket(ipAddress,port);

			mainPanel.mainScreen();

			//送信用object
			out = new PrintWriter(socket.getOutputStream(), true); //データ送信用オブジェクトの用意

			//受信用object
			receiver=new Receiver(socket);
			receiver.start();
			//socket.close();//これはいるのか？





		}catch(UnknownHostException e) {
			System.out.println("ホストのＩＰアドレスが判定できません: "+e);
			mainPanel.connectError();//アプリケーション画面にも表示(アプリケーションメッセージでは２つのエラーを区別しない。)
		}catch(IOException e) {
			System.out.println("サーバー接続時にエラーが発生しました: "+e);
			mainPanel.connectError();
			//mainPanel.mainScreen();//とりあえず今はサーバに接続できないのでエラーで表示するように（）
		}
	}

	//データ送信用メソッド

	public void sendMessage(String msg){	// サーバに操作情報を送信
		out.println(msg);//送信データをバッファに書き出す
		out.flush();//送信データを送る
		System.out.println("サーバにメッセージ " + msg+ " を送信しました"); //テスト標準出力
	}

	public void receiveHandler(int i){//receiveHandler:データ受信をクライアントが欲している状態であるかどうかを判断する変数
		receiveHandler=i;
	}

	//データ受信用クラス
	class Receiver extends Thread{
		private InputStreamReader sisr;
		private BufferedReader br;

		Receiver(Socket socket){
			try {
				sisr=new InputStreamReader(socket.getInputStream());
				br=new BufferedReader(sisr);
			}catch(IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				while(true) {
					String inputLine=br.readLine();
					//if(inputLine!=null) {//試験用
					if(inputLine!=null&&receiveHandler==1) {
						classifyMsg(inputLine,br);
					}
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}


	public void classifyMsg(String msg,BufferedReader br) {//ここで受信データの種類判別
		System.out.println("サーバからメッセージ " + msg + " を受信しました"); //テスト用標準出力



		int type;

		type = Integer.parseInt("" +msg.charAt(0));

		switch(type){

		case 0:{

			receiveHandler=0;

			if(msg.equals("00")) {//新規作成失敗
				mainPanel.authenticationMsg(0);
			}else if(msg.equals("01")) {//成功
				mainPanel.authenticationMsg(1);
			}

			receiveHandler=0;

			break;
		}

		case 1:{
			receiveHandler=0;

			if(msg.equals("10")) {//login失敗
				mainPanel.authenticationMsg(2);
			}else if((msg.substring(0,2)).equals("11")) {//成功
				my=new Player(msg.substring(2));

				//this.remove(mainPanel);

				//repaint();

				menuPanel=new MenuPanel(this,my);

				//test

				//othelloPanel=new OthelloPanel(1,this);



				/*this.add(menuPanel);
				menuPanel.setLayout(null);
				menuPanel.setVisible(true);*/

				changePanel(0,1);



				menuPanel.ruleScreen();



			}


			break;
		}

		case 2:{
			receiveHandler=0;

			String str=msg.substring(1);

			//Player[] players;
			ArrayList<String> results = new ArrayList<String>();
			Player player=null;

			if(!(str.equals(""))) {
				player=new Player(str);
				System.out.println("!");
			}

			try {
				while(br.ready()) {
					str=br.readLine();

					System.out.println("str:"+str);
					results.add(str);
					System.out.println("!");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}



			String[]  s= results.toArray(new String[results.size()]);

			if(player==null) {
				menuPanel.searchError();
			}else {
				menuPanel.results(player,s);
			}


			break;
		}

		case 3:{
			receiveHandler=0;

			String str=msg.substring(1);

			ArrayList<Player> onlinePlayers = new ArrayList<Player>();

			Player player=null;


			if(!(str.equals(""))) {
				player=new Player(str);
				onlinePlayers.add(player);
				System.out.println("!");
			}

			try {
				while(br.ready()) {
					str=br.readLine();
					player=new Player(str);
					onlinePlayers.add(player);
					System.out.println("!");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			Player[]  onlines= onlinePlayers.toArray(new Player[onlinePlayers.size()]);

			Player[]  offers= getOfferPlayer.toArray(new Player[getOfferPlayer.size()]);


			menuPanel.playMain(onlines,offers);


			break;
		}



		case 4:{
			if(othelloPanel.oB.bw==1) {
				othelloPanel.oB.giveUp(2);
			}else {
				othelloPanel.oB.giveUp(1);
			}
			break;
		}
		case 5:{
			//receiveHandler=0;
	        if(othelloPanel.turn==1 && othelloPanel.oB.bw==2) {
	        	othelloPanel.turn=2;
	        }else {
	        	//othelloPanel.turn=1;
	        }


	        if(othelloPanel.turn==1) {
				System.out.println("白が"+msg+"万を受信");
	        }else {
				System.out.println("黒が"+msg+"万を受信");
	        }

	        char[] rivalInfo=msg.toCharArray();
//	        System.out.println(rivalInfo[0]+"万");//turn
//	        System.out.println(rivalInfo[1]+"万");//i
//	        System.out.println(rivalInfo[2]+"万");//j
	        int[] InfoNum=new int[3];
	     //   InfoNum[1]=Character.getNumericValue(rivalInfo[1]);
	        InfoNum[1]=Character.getNumericValue(rivalInfo[1]);
	        InfoNum[2]=Character.getNumericValue(rivalInfo[2]);
	   //     System.out.println(InfoNum[1]);//turn
//	        System.out.println(InfoNum[1]);//i
//	        System.out.println(InfoNum[2]);//j
//	        System.out.println(othelloPanel.oB.bw+"万");
	 //       othelloPanel.turn=InfoNum[1];

	        othelloPanel.rivalI=InfoNum[1];
	        othelloPanel.rivalJ=InfoNum[2];


//	        	if(othelloPanel.turn==othelloPanel.oB.bw) {//相手からの情報が来たとき
	        			othelloPanel.waiting();
//	        	}

			break;
		}

		case 7:{

			String str=msg.substring(2);

			Player player=null;


			if(msg.charAt(1)=='1') {//オファー受付
				player=new Player(str);
				getOfferPlayer.add(player);
				System.out.println("オファー受付 from "+player.getID());
			}
			else if(msg.charAt(1)=='0') {//オファーキャンセル受付
				player=new Player(str);

				for(int i=0;i<getOfferPlayer.size();i++) {
					if(player.getID().equals(getOfferPlayer.get(i).getID())) {
						getOfferPlayer.remove(i);
						System.out.println("オファーキャンセル受付 from "+player.getID());
						break;
					}
				}

				/*if(nowOfferPlayer.equals(player.getID())) {//今自分がオファー送信してた人からのキャンセルの場合

					nowOfferPlayer="";

					menuPanel.opponentCancel(player.getID());

				}*/
			}




			//Player[]  s= getOfferPlayer.toArray(new Player[getOfferPlayer.size()]);

			if(menuPanel.getScreenIsPlayMain()==1) {
				//System.out.println("reload");

				sendMessage("31");//更新ついでにnow playerも更新
			}


			break;
		}

		case 8:{//800+playerキャンセル,811or812+player:マッチング成立 820+player:受理したが相手が切断


			String str=msg.substring(3);

			Player player=new Player(str);

			if(msg.substring(0,3).equals("800")) {
				//オファーキャンセルなどのエラー、onlinePlayerに戻る
				menuPanel.opponentCancel(0,player.getID());
			}
			else if(msg.substring(0,3).equals("811")) {//811:黒
				//receiveHandler=0;

				//マッチング成立、オセロパネルに遷移
				//menuPanel.setScreenIsPlayMain(0);
				othelloPanel=new OthelloPanel(1,this);

				//othelloPanel=new OthelloPanel(1,this,my,player);


				changePanel(1,2);
			}
			else if(msg.substring(0,3).equals("812")) {//812:白
				//receiveHandler=0;

				//マッチング成立、オセロパネルに遷移
				//menuPanel.setScreenIsPlayMain(0);
				othelloPanel=new OthelloPanel(2,this);

				//othelloPanel=new OthelloPanel(2,this,my,player);


				changePanel(1,2);
			}
			else if(msg.substring(0,3).equals("820")) {
				menuPanel.opponentCancel(1,player.getID());
			}

			break;
		}

		case 9:{
			receiveHandler=0;

			my=new Player(msg.substring(1));

			menuPanel=new MenuPanel(this,my);

			changePanel(2,1);



			menuPanel.menuScreen();
		}

		}
	}

	public void changePanel(int i,int j) {//引数: from,to
		if(i==0) {
			this.remove(mainPanel);
		}else if(i==1) {
			this.remove(menuPanel);
		}else if(i==2) {
			this.remove(othelloPanel);
		}


		if(j==0) {
			this.add(mainPanel,0);
			mainPanel.setLayout(null);
			mainPanel.setVisible(true);
			mainPanel.mainScreen();
			this.revalidate();
		}else if(j==1) {
			this.add(menuPanel,0);
			menuPanel.setLayout(null);
			menuPanel.setVisible(true);
			this.revalidate();

		}else if(j==2) {
			this.add(othelloPanel,0);
			othelloPanel.setVisible(true);
			othelloPanel.setLayout(null);
			this.revalidate();
		}


	}


	public void setNowOfferPlayer(String id) {
		nowOfferPlayer=id;
	}

	public void removeOfferPlayer(String id) {
		for(int i=0;i<getOfferPlayer.size();i++) {
			if(getOfferPlayer.get(i).getID().equals(id)) {
				getOfferPlayer.remove(i);

				System.out.println("remove");
				break;
			}
		}
	}



	public static void main(String[] args) {
		Client client=new Client();
		client.setDefaultCloseOperation(EXIT_ON_CLOSE);
		client.setVisible(true);
		client.setResizable(false);



		String ipAddress=args[0];
		int port=Integer.parseInt(args[1]);

		client.connectServer(ipAddress,port);


	}

}
