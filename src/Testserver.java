import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;




class Testserver {
	PrintWriter out;

	private Receiver receiver;

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

        			out.println("connect ok");//送信データをバッファに書き出す
        			out.flush();//送信データを送る
        			System.out.println("サーバにメッセージを送信しました");


        			receiver=new Receiver(s);
        			receiver.start();


                    // ソケットをクローズ
                    //s.close();
            }


    } catch (Exception e) {
            e.printStackTrace();
    }
	}

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
						System.out.println("!");
						System.out.println(inputLine);
					}
					if(inputLine.equals("11_2")) {
						out.println("11sharukis10s20s30");
						out.flush();
	        			System.out.println("サーバにメッセージを送信しました");
					}
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}



	public static void main(String[] args) {
		Testserver server = new Testserver();
		String ipAddress=args[0];
		server.accept(ipAddress);
    }



}



