import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;




class Testserver {
	PrintWriter out;

	public void accept(String ip) {
		try {
            // ポートを取得
            int port = Integer.parseInt(ip);

            // サーバーソケットを作成
            ServerSocket ss = new ServerSocket(port);

            // 無限ループ
            while (true) {
                    // クライアントからの要求を受け取る
                    Socket s = ss.accept();

        			out =new PrintWriter(s.getOutputStream(), true); //データ送信用オブジェクトの用意


                    // 結果をクライアントに書き込む
                    /*OutputStream os = s.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(os);
                    dos.writeInt(random.nextInt());*/
        			out.println("aiueo");//送信データをバッファに書き出す
        			out.flush();//送信データを送る
        			System.out.println("サーバにメッセージ " +"aiueo"+ " を送信しました");
                    // ソケットをクローズ
                    s.close();
            }


    } catch (Exception e) {
            e.printStackTrace();
    }
	}



	public static void main(String[] args) {
		Testserver server = new Testserver();//待ち受けポート10000番でサーバオブジェクトを準備
		String ipAddress=args[0];
		server.accept(ipAddress);
    }



}



