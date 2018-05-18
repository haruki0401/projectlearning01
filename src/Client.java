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

	//panel作成
	public String[] PanelNames= {"main","menu","Othello"};

	MainPanel mainPanel=new MainPanel(this,PanelNames[0]);
	MenuPanel menuPanel;


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

	/*public void getTempPlayerID(String p) {
		tempPlayerID=p;
	}*/

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

				menuPanel=new MenuPanel(this,my,PanelNames[1]);

				/*this.add(menuPanel);
				menuPanel.setLayout(null);
				menuPanel.setVisible(true);*/

				changePanel(0,1);

				menuPanel.menuScreen();



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
					results.add(str);
					System.out.println("!");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			String[]  s= results.toArray(new String[results.size()]);

			//System.out.println(p.length);

			menuPanel.results(player,s);


			break;
		}

		}
	}

	public void changePanel(int i,int j) {//引数: from,to
		if(i==0) {
			this.remove(mainPanel);
		}else if(i==1) {
			this.remove(menuPanel);
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
