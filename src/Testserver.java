import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;




class Testserver {
	PrintWriter out;
	Message msg;

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
        			msg=new Message("aiueo",0);
        			out.println(msg);//送信データをバッファに書き出す
        			out.flush();//送信データを送る
        			System.out.println("サーバにメッセージ " + msg.getMsg() + " を送信しました");
                    // ソケットをクローズ
                    s.close();
            }


    } catch (Exception e) {
            e.printStackTrace();
    }
	}

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


	public static void main(String[] args) {
		Testserver server = new Testserver();//待ち受けポート10000番でサーバオブジェクトを準備
		String ipAddress=args[0];
		server.accept(ipAddress);
    }



}



