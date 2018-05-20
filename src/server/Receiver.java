package server;


// InputStreamなどに必要
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
// ServerSocketに必要
import java.net.Socket;

//データ送受信スレッド
public class Receiver extends Thread{
	// クライアントから来たデータがどのタイプか
	private final static int NEW_PLAYER = 0;      // プレイヤーの新規登録
	private final static int LOGIN = 1;           // ログイン
	private final static int SEND_RESULT = 2;     // 戦績表示
	private final static int SEND_NOW_PLAYERS = 3;// 現在ログインしているプレイヤーを表示
	private final static int START_JUDGE = 4;     // 黒と白を決める
	private final static int GAME_MAIN = 5;       // 相手の駒を伝える
	private final static int WIN_JUDGE = 6;       // 勝敗を把握して戦績の追加
	private final static int SEND_OFFER = 7;      // 対戦相手へのオファー
	private final static int ACCEPT_OFFER = 8;    // オファーを受ける
	private final static int DISCONNECTION = 9;   // 対戦相手が通信切断（サーバから送信のみ）


	PlayerData player = new PlayerData();
	protected int myNumber;


	private InputStreamReader sisr; //受信データ用文字ストリーム
	private BufferedReader br; //文字ストリーム用のバッファ
	protected PrintWriter printWriter; //データ送信用オブジェクト
	private Server server;

	Receiver (Socket socket, int num, Server server){
		this.server = server;
		try{
			sisr = new InputStreamReader(socket.getInputStream());
			br = new BufferedReader(sisr);
			// このソケットに送るときのための物で、myNumberの違いに注意
			printWriter = new PrintWriter(socket.getOutputStream(), true);
			myNumber = num;
		} catch (IOException e) {
			System.err.println("データ受信時にエラーが発生しました: " + e);
		}
	}
	public void run(){
		int type;
		try{
			while(true) {// データを受信し続ける
				String inputLine = br.readLine();//データを一行分読み込む
				if (inputLine != null){ //データを受信したら
					// inputLine = inputLine.substring(0, inputLine.length() - 1)
					type = Integer.parseInt("" +inputLine.charAt(0));
					switch(type){
						case NEW_PLAYER:{
							String str1 = inputLine.substring(1);
							while((inputLine = br.readLine()) == null);

							System.out.println(str1+"/"+inputLine);

							if(server.isNotUsed(str1, inputLine, player,myNumber))
								printWriter.println("01");
							else
								printWriter.println("00");

							System.out.println(player.sendPlayerData());

							printWriter.flush();
							break;
						}
						case LOGIN:{
							String str1 = inputLine.substring(1);
							while((inputLine = br.readLine()) == null);

							System.out.println(str1+"/"+inputLine);


							String msg=server.inputText(str1, inputLine, player,myNumber);


							if(msg.equals("0"))
								printWriter.println("10");
							else {

								printWriter.println("11"+msg);
							}

							System.out.println(player.sendPlayerData());

							printWriter.flush();
							break;
						}
						case SEND_RESULT:{
							String str1 = inputLine.substring(1);
							printWriter.println(server.inputResultText(str1, -1));
							printWriter.flush();
							break;
						}
						case SEND_NOW_PLAYERS:{
							//char c = inputLine.charAt(1);
							//if(c == '0')
							printWriter.println(server.allLoginPlayerData());

							System.out.println(server.allLoginPlayerData());

							printWriter.flush();
							break;
						}
						//case START_JUDGE:
							//break;
						case GAME_MAIN:{
							// 相手の添え字
							int oppositeNum = server.changeFromMyNum(player.opposite);
							server.receiver.get(oppositeNum).printWriter.println(inputLine);
							server.receiver.get(oppositeNum).printWriter.flush();
							break;
						}
						case WIN_JUDGE:{
							// 勝ったとき
							if(inputLine.charAt(1) == '1')
								server.resultWin(server.changeFromMyNum(myNumber), server.changeFromMyNum(player.opposite));
							else if(inputLine.charAt(1) == '2')// 引き分け
								server.resultDraw(server.changeFromMyNum(myNumber), server.changeFromMyNum(player.opposite));
							else if(inputLine.charAt(1) == '3'){// こちらが投了
								int oppositeNum = server.changeFromMyNum(player.opposite);
								server.resultEarlyLose(oppositeNum, server.changeFromMyNum(myNumber));
								server.receiver.get(oppositeNum).printWriter.println(inputLine);
								server.receiver.get(oppositeNum).printWriter.flush();
							}else
								System.out.println("WIN_JUDGEの2つ目が不適です");
							break;
						}
						case SEND_OFFER:{
							String oppositeName = inputLine.substring(1);
							int oppositeNum = server.changeFromID(oppositeName);
							server.receiver.get(oppositeNum).printWriter.println("7" + player.sendPlayerData());
							server.receiver.get(oppositeNum).printWriter.flush();
							break;
						}
						case ACCEPT_OFFER:{
							String oppositeName = inputLine.substring(1);
							// 添え字
							int n = server.changeFromMyNum(myNumber);
							int oppositeNum = server.changeFromID(oppositeName);
							// 同時実行制御を入れるために、関数へ
							if(server.acceptOffer(n, oppositeNum)){
								printWriter.println("81");
								printWriter.flush();
								server.startJudge(n, oppositeNum);
							}
							else
								printWriter.println("80");
							printWriter.flush();
							break;
						}
						//case DISCONNECTION:
							//break;
						default:
							System.out.println("クライアント側からの送信のタイプが不適です");
							break;
					}
				}
			}
		} catch (IOException e){ // 接続が切れたとき
			//System.err.println("クライアントとの接続が切れました．");
			server.delete(server.changeFromMyNum(myNumber));
		}
	}
}