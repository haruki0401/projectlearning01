package client;
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
						System.out.println("受信メッセージ: "+inputLine);
					}
					if(inputLine.equals("11_2")) {
						out.println("11haruki/10/20/30/40");
						out.flush();
	        			System.out.println("サーバにメッセージを送信しました");
					}
					if(inputLine.equals("212")) {
						//out.println("2");

						String str;
						str="2yamada/10/20/30/40";

						for(int i=0;i<20;i++) {
							str+="\nharuki"+i+"/00";
						}
						//out.println("2yamada/10/20/30/40\nharuki/00\numemoto/01");//win=000,lose=001,draw=010,give up(相手が)=011,(自分が)=100
						out.println(str);
						out.flush();
	        			System.out.println("サーバにメッセージを送信しました");
					}
					if(inputLine.equals("3")) {
						//out.println("2");


						String str;
						str="3yamada/10/20/30/40/5";

						for(int i=0;i<20;i++) {
							str+="\nyamada"+i+"/10/20/30/40/5";
						}
						//out.println("3yamada/10/20/30/40/5\nyamada1/10/20/30/40/5\nyamada2/10/20/30/40/5\nyamada2/10/20/30/40/5\nyamada2/10/20/30/40/5\nyamada2/10/20/30/40/5\nyamada2/10/20/30/40/5\nyamada2/10/20/30/40/5\nyamada2/10/20/30/40/5\nyamada2/10/20/30/40/5\nyamada2/10/20/30/40/5\nyamada2/10/20/30/40/5\nyamada2/10/20/30/40/5\nyamada2/10/20/30/40/5\nyamada2/10/20/30/40/5\nyamada2/10/20/30/40/5\nyamada2/10/20/30/40/5");//win=000,lose=001,draw=010,give up(相手が)=011,(自分が)=100

						//str="3";
						out.println(str);
						out.flush();
	        			System.out.println("サーバにメッセージを送信しました");


					}
					if(inputLine.equals("7yamada")) {
						out.println("80");
						out.flush();
	        			System.out.println("サーバにメッセージを送信しました");

	        			out.println("70yamada/10/20/30/40/5");
						out.flush();
	        			System.out.println("オファー送信");
					}

				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}



	public static void main(String[] args) {

		/*ArrayList<Player> getOfferPlayer = new ArrayList<Player>();//offer人数
		Player[]  s= getOfferPlayer.toArray(new Player[getOfferPlayer.size()]);


		System.out.println(s.length);*/


		Testserver server = new Testserver();
		String ipAddress=args[0];
		server.accept(ipAddress);



    }



}



