import java.io.IOException;
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
			//受信用object


			socket.close();//これはいるのか？

		}catch(UnknownHostException e) {
			System.out.println("ホストのＩＰアドレスが判定できません: "+e);
			mainPanel.errorOutput();//アプリケーション画面にも表示(アプリケーションメッセージでは２つのエラーを区別しない。)
		}catch(IOException e) {
			System.out.println("サーバー接続時にエラーが発生しました: "+e);
			mainPanel.errorOutput();
		}
	}

	//データ送信用クラス

	//データ受信用クラス

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
