import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;




/*class NewCanvas extends Canvas{
	public void paint(Graphics g) {

	}
}*/

public class Client extends JFrame{

	public static final int x=1500;
	public static final int y=1000;

	private static int receiveHandler=0;//1:データ受信要求中

	private PrintWriter out;
	private Receiver receiver;

	//panel作成
	public String[] PanelNames= {"main","Othello"};

	MainPanel mainPanel=new MainPanel(this,PanelNames[0]);

	public Client() {
		this.add(mainPanel);
		mainPanel.setLayout(null);
		mainPanel.setVisible(true);

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
			//socket.close();//これはいるのか？]





		}catch(UnknownHostException e) {
			System.out.println("ホストのＩＰアドレスが判定できません: "+e);
			mainPanel.errorOutput();//アプリケーション画面にも表示(アプリケーションメッセージでは２つのエラーを区別しない。)
		}catch(IOException e) {
			System.out.println("サーバー接続時にエラーが発生しました: "+e);
			mainPanel.errorOutput();
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
					if(inputLine!=null) {
						receiveMsg(inputLine);
					}
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void receiveMsg(String msg) {
		System.out.println("サーバからメッセージ " + msg + " を受信しました"); //テスト用標準出力
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
