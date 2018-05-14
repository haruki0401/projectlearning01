import java.io.IOException;
import java.io.ObjectOutputStream;
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

	ObjectOutputStream oos;
	Receiver receive;
	private PrintWriter out;//データ送信用オブジェクト


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
			//oos=new ObjectOutputStream(socket.getOutputStream());
			//受信用object
			receive=new Receiver();

			//socket.close();//これはいるのか？]



			out = new PrintWriter(socket.getOutputStream(), true); //データ送信用オブジェクトの用意


		}catch(UnknownHostException e) {
			System.out.println("ホストのＩＰアドレスが判定できません: "+e);
			mainPanel.errorOutput();//アプリケーション画面にも表示(アプリケーションメッセージでは２つのエラーを区別しない。)
		}catch(IOException e) {
			System.out.println("サーバー接続時にエラーが発生しました: "+e);
			mainPanel.errorOutput();
			//mainPanel.mainScreen();//とりあえず今はサーバに接続できないのでエラーで表示するように（）
		}
	}

	public void sendMessage(Message msg){	// サーバに操作情報を送信
		out.println(msg);//送信データをバッファに書き出す
		out.flush();//送信データを送る
		System.out.println("サーバにメッセージ " + msg.getMsg() + " を送信しました"); //テスト標準出力
	}

	//データ送信用メソッド
	/*public void sendMsg(String msg) {//サーバに操作情報を送信
		try {
			oos.writeObject(msg);
			oos.flush();
			System.out.println("サーバにメッセージ " + msg + " を送信しました"); //テスト標準出力
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("!");//test
		}
	}*/

	class Message{//送信データのオブジェクト
		String msg;//送信データ内容
		int type;//送信データ種類

		Message(String msg,int type){
			this.msg=msg;
			this.type=type;
		}

		public String getMsg() {
			return this.msg;
		}

		public int getType() {
			return this.type;
		}
	}

	//データ受信用クラス
	class Receiver{

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
