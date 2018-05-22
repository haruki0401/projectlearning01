package server;



// InputStreamなどに必要
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
// ServerSocketに必要
import java.net.Socket;
import java.util.ArrayList;

//データ送受信スレッド
public class Receiver extends Thread{
	// クライアントから来たデータがどのタイプか
	private final static int NEW_PLAYER = 0;      // プレイヤーの新規登録
	private final static int LOGIN = 1;           // ログイン
	private final static int SEND_RESULT = 2;     // 戦績表示
	private final static int SEND_NOW_PLAYERS = 3;// 現在ログインしているプレイヤーを表示
	private final static int GAME_FINISH = 4;     // 黒と白を決める
	private final static int GAME_MAIN = 5;       // 相手の駒を伝える
	private final static int WIN_JUDGE = 6;       // 勝敗を把握して戦績の追加
	private final static int SEND_OFFER = 7;      // 対戦相手へのオファー
	private final static int ACCEPT_OFFER = 8;    // オファーを受ける
	private final static int DISCONNECTION = 9;   // 対戦相手が通信切断（サーバから送信のみ）


	PlayerData player = new PlayerData();
	protected int myNumber;

	protected int decidedOppositeNum=-1;//対局が確定した相手のmynumber

	protected int move=-1;//0: 自分の番 1:相手の番


	protected int whereIs=0;//playerが今どの段階にいるか
	//-1:切断 0:接続しただけand新規登録しただけand切断のときにも１を代入 1:ログイン済み 2:オンライン済み 3:オファー送信中 4:対局中

	protected int sendOfferNum=-1;//オファーしている人のmyNumber,-1ならオファーしていない


	protected ArrayList<PlayerData>receiveOfferPlayer=new ArrayList<PlayerData>();//オファーを受けている人のmyNumberの配列 長さ0ならオファー受信してない

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

					//test
//					System.out.println("from"+myNumber+" : "+inputLine);


					// inputLine = inputLine.substring(0, inputLine.length() - 1)
					type = Integer.parseInt("" +inputLine.charAt(0));
					switch(type){
						case NEW_PLAYER:{
							String str1 = inputLine.substring(1);
							while((inputLine = br.readLine()) == null);

//							System.out.println(str1+"/"+inputLine);

							if(server.isNotUsed(str1, inputLine, player,myNumber))
								printWriter.println("01");
							else
								printWriter.println("00");

//							System.out.println(player.sendPlayerData());

							printWriter.flush();
							break;
						}
						case LOGIN:{

							if(inputLine.charAt(1)==('1')) {//log in 要求
								String str1 = inputLine.substring(2);
								while((inputLine = br.readLine()) == null);

//								System.out.println(str1+"/"+inputLine);


								String msg=server.inputText(str1, inputLine, player,myNumber);


								if(msg.equals("0"))
									printWriter.println("10");
								else {
									whereIs=1;//ログインした状態
									printWriter.println("11"+msg);

									server.allPlayerOutput();//player一覧の更新
								}

//								System.out.println(player.sendPlayerData());

								printWriter.flush();
							}
							else if(inputLine.charAt(1)==('0')) {//log out 要求
								whereIs=0;

								server.allPlayerOutput();//player一覧の更新

							}

							break;
						}
						case SEND_RESULT:{
							String str1 = inputLine.substring(1);

							String str2,str3;

							String answer;

							str2=server.getPlayerData(str1);

							if(!str2.equals("")) {
								str3=server.inputResultText(str1);

								answer="2"+str2+str3;
							}else {
								answer="2"+str2;
							}


//							System.out.println(answer);

							printWriter.println(answer);
							printWriter.flush();
							break;
						}
						case SEND_NOW_PLAYERS:{//31: online画面に 32:offline

							//String str=inputLine.substring(1);

							if(inputLine.charAt(1)=='1') {
								whereIs=2;//このタイミングで自分もオンラインにする

								server.allPlayerOutput();//player一覧の更新

								printWriter.println(server.allLoginPlayerData(myNumber));
//								System.out.println(server.allLoginPlayerData(myNumber));
								printWriter.flush();
							}
							else if(inputLine.charAt(1)=='0') {
								whereIs=1;//ログイン画面に戻ったこと

								server.allPlayerOutput();//player一覧の更新

							}



							break;
						}
						case GAME_FINISH:{//ゲーム終了し、menuscreenにもどったことを受信
							move=-1;
							decidedOppositeNum=-1;
							whereIs=1;

							server.allPlayerOutput();//player一覧の更新

							//String msg=server.inputText(str1, inputLine, player,myNumber);

							printWriter.println("9"+player.sendPlayerData());
							printWriter.flush();


							break;
						}
						case GAME_MAIN:{//passの時は"588を送信",それ以外は"5ij"(i,j:任意)


							if(move==0) {//自分の番のときのみ転送

								move=1;

//								System.out.println(decidedOppositeNum);
								server.receiver.get(decidedOppositeNum).setMove(0);

								server.receiver.get(decidedOppositeNum).printWriter.println(inputLine);

//								System.out.println(inputLine);
								server.receiver.get(decidedOppositeNum).printWriter.flush();

							}





							// 相手の添え字
							/*int oppositeNum = server.changeFromMyNum(player.opposite);
							server.receiver.get(oppositeNum).printWriter.println(inputLine);
							server.receiver.get(oppositeNum).printWriter.flush();*/
							break;
						}
						case WIN_JUDGE:{//"6win"普通に勝った,"6earlyWin":相手の投了or切断,"6draw":引き分け
							// 勝ったとき

							if(whereIs==4) {//何回も送らないように

								if(inputLine.equals("6win")) {
									//whereIs=2;

									player.numWin++;
									server.receiver.get(decidedOppositeNum).player.numLose++;
									server.resultWin(myNumber, decidedOppositeNum);
								}
								else if(inputLine.equals("6earlyWin")) {
									//whereIs=2;


									player.numWin++;
									server.receiver.get(decidedOppositeNum).player.numEarlyLose++;
									server.resultEarlyLose(myNumber, decidedOppositeNum);
								}
								else if(inputLine.equals("6draw")) {
									//whereIs=2;

									player.numDraw++;
									server.receiver.get(decidedOppositeNum).player.numDraw++;
									server.resultDraw(myNumber, decidedOppositeNum);
								}

							}


							/*if(inputLine.charAt(1) == '1')
								server.resultWin(server.changeFromMyNum(myNumber), server.changeFromMyNum(player.opposite));
							else if(inputLine.charAt(1) == '2')// 引き分け
								server.resultDraw(server.changeFromMyNum(myNumber), server.changeFromMyNum(player.opposite));
							else if(inputLine.charAt(1) == '3'){// こちらが投了
								int oppositeNum = server.changeFromMyNum(player.opposite);
								server.resultEarlyLose(oppositeNum, server.changeFromMyNum(myNumber));
								server.receiver.get(oppositeNum).printWriter.println(inputLine);
								server.receiver.get(oppositeNum).printWriter.flush();
							}else
								System.out.println("WIN_JUDGEの2つ目が不適です");*/
							break;
						}
						case SEND_OFFER:{//71+id:オファー 70+id:オファーキャンセル
							if(inputLine.charAt(1)=='1'&&sendOfferNum==-1) {
								String oppositeName = inputLine.substring(2);
								int oppositeNum = server.changeFromID(oppositeName);


								if(server.receiver.get(oppositeNum).getSendOfferNum()==myNumber) {//両方がオファー送りあってマッチング成立





									if(server.receiver.get(oppositeNum).getWhereIs()==3) {

										//自分がほかのオファーの人に拒否送信

										cancelOffer(oppositeName);


										//相手がほかのオファーの人に拒否送信

										server.receiver.get(oppositeNum).cancelOffer(player.sendID());

										decidedOppositeNum=oppositeNum;
										server.receiver.get(decidedOppositeNum).setDecidedOppositeNum(myNumber);

										sendOfferNum=-1;
										server.receiver.get(decidedOppositeNum).setSendOfferNum(-1);


//										System.out.println("opposite: "+decidedOppositeNum);
										//add
										receiveOfferPlayer.clear();//オファーされている配列をクリア
										printWriter.println("72");//client側のoffer配列も変更する
										printWriter.flush();

										server.receiver.get(decidedOppositeNum).receiveOfferPlayer.clear();//相手のオファーされている配列もクリア
										server.receiver.get(decidedOppositeNum).printWriter.println("72");//相手のclient側のoffer配列も変更する
										server.receiver.get(decidedOppositeNum).printWriter.flush();

										whereIs=4;
										server.receiver.get(decidedOppositeNum).setWhereIs(4);

										server.allPlayerOutput();//player一覧の更新


										if(myNumber<decidedOppositeNum) {


										//マッチング成功

											//自分に送信(黒)
											setMove(0);

											printWriter.println("811"+server.receiver.get(decidedOppositeNum).player.sendPlayerData());
											printWriter.flush();

											//相手に送信(白)
											server.receiver.get(decidedOppositeNum).setMove(1);

											server.receiver.get(decidedOppositeNum).printWriter.println("812"+player.sendPlayerData());//相手に受理した旨を送信
											server.receiver.get(decidedOppositeNum).printWriter.flush();

										}
										else if(myNumber>decidedOppositeNum){
										//マッチング成功

											//自分に送信(白)
											setMove(1);


											printWriter.println("812"+server.receiver.get(decidedOppositeNum).player.sendPlayerData());
											printWriter.flush();

											//相手に送信(黒)
											server.receiver.get(decidedOppositeNum).setMove(0);

											server.receiver.get(decidedOppositeNum).printWriter.println("811"+player.sendPlayerData());//相手に受理した旨を送信
											server.receiver.get(decidedOppositeNum).printWriter.flush();

										}
									}
									else {//相手がオファー送信やめてるからエラーを送信
										printWriter.println("820"+server.receiver.get(oppositeNum).player.sendPlayerData());
										printWriter.flush();
									}








								}
								else if((server.receiver.get(oppositeNum).getWhereIs()==2)||(server.receiver.get(oppositeNum).getWhereIs()==3)) {
									sendOfferNum=oppositeNum;//オファーしてる人代入
									whereIs=3;

									server.receiver.get(oppositeNum).addOfferPlayer(player);

									server.receiver.get(oppositeNum).printWriter.println("71" + player.sendPlayerData());
									server.receiver.get(oppositeNum).printWriter.flush();
								}else {
									printWriter.println("800"+server.receiver.get(oppositeNum).player.sendPlayerData());
									printWriter.flush();
								}

							}

							else if(inputLine.charAt(1)=='0'&&sendOfferNum!=-1) {
								String oppositeName = inputLine.substring(2);
								int oppositeNum = server.changeFromID(oppositeName);

								sendOfferNum=-1;//オファーしている人がいなくなったので
								whereIs=2;
								server.receiver.get(oppositeNum).removeOfferPlayer(player);

								server.receiver.get(oppositeNum).printWriter.println("70" + player.sendPlayerData());
								server.receiver.get(oppositeNum).printWriter.flush();
							}

							break;
						}
						case ACCEPT_OFFER:{//81+id:オファー受理,80+id:オファー拒否

							String oppositeName = inputLine.substring(2);
							// 添え字
							int oppositeNum = server.changeFromID(oppositeName);
							//int myNum = server.changeFromMyNum(myNumber);

							if(inputLine.charAt(1)=='1') {


								if(server.receiver.get(oppositeNum).getWhereIs()==3) {

									//自分がほかのオファーの人に拒否送信

									cancelOffer(oppositeName);

									/*for(int i=0;i<receiveOfferPlayer.size();i++) {
										if(!(receiveOfferPlayer.get(i).sendID().equals(oppositeName))) {
											int cancelNum = server.changeFromID(receiveOfferPlayer.get(i).sendID());

											server.receiver.get(cancelNum).printWriter.println("800"+player.sendPlayerData());//相手に拒否した旨を送信
											server.receiver.get(cancelNum).printWriter.flush();

											server.receiver.get(cancelNum).setWhereIs(2);
											server.receiver.get(cancelNum).setSendOfferNum(-1);
										}
									}*/


									//相手がほかのオファーの人に拒否送信

									server.receiver.get(oppositeNum).cancelOffer(player.sendID());

									/*for(int i=0;i<server.receiver.get(oppositeNum).receiveOfferPlayer.size();i++) {
										if(!(server.receiver.get(oppositeNum).receiveOfferPlayer.get(i).sendID().equals(player.sendID()))) {
											int cancelNum = server.changeFromID(receiveOfferPlayer.get(i).sendID());

											server.receiver.get(cancelNum).printWriter.println("800"+player.sendPlayerData());//相手に拒否した旨を送信
											server.receiver.get(cancelNum).printWriter.flush();

											server.receiver.get(cancelNum).setWhereIs(2);
											server.receiver.get(cancelNum).setSendOfferNum(-1);
										}
									}*/

									decidedOppositeNum=oppositeNum;
									server.receiver.get(decidedOppositeNum).setDecidedOppositeNum(myNumber);

									sendOfferNum=-1;
									server.receiver.get(decidedOppositeNum).setSendOfferNum(-1);


//									System.out.println("opposite: "+decidedOppositeNum);
									//add
									receiveOfferPlayer.clear();//オファーされている配列をクリア
									printWriter.println("72");//client側のoffer配列も変更する
									printWriter.flush();

									server.receiver.get(decidedOppositeNum).receiveOfferPlayer.clear();//相手のオファーされている配列もクリア
									server.receiver.get(decidedOppositeNum).printWriter.println("72");//相手のclient側のoffer配列も変更する
									server.receiver.get(decidedOppositeNum).printWriter.flush();

									whereIs=4;
									server.receiver.get(decidedOppositeNum).setWhereIs(4);




									server.allPlayerOutput();//player一覧の更新


									if(myNumber<decidedOppositeNum) {


									//マッチング成功

										//自分に送信(黒)
										setMove(0);

										printWriter.println("811"+server.receiver.get(decidedOppositeNum).player.sendPlayerData());
										printWriter.flush();

										//相手に送信(白)
										server.receiver.get(decidedOppositeNum).setMove(1);

										server.receiver.get(decidedOppositeNum).printWriter.println("812"+player.sendPlayerData());//相手に受理した旨を送信
										server.receiver.get(decidedOppositeNum).printWriter.flush();

									}
									else if(myNumber>decidedOppositeNum){
									//マッチング成功

										//自分に送信(白)
										setMove(1);


										printWriter.println("812"+server.receiver.get(decidedOppositeNum).player.sendPlayerData());
										printWriter.flush();

										//相手に送信(黒)
										server.receiver.get(decidedOppositeNum).setMove(0);

										server.receiver.get(decidedOppositeNum).printWriter.println("811"+player.sendPlayerData());//相手に受理した旨を送信
										server.receiver.get(decidedOppositeNum).printWriter.flush();

									}
								}
								else {//相手がオファー送信やめてるからエラーを送信
									printWriter.println("820"+server.receiver.get(oppositeNum).player.sendPlayerData());
									printWriter.flush();
								}

							}

							else if(inputLine.charAt(1)=='0') {
								if(server.receiver.get(oppositeNum).getWhereIs()==3) {
									server.receiver.get(oppositeNum).printWriter.println("800"+player.sendPlayerData());//相手に拒否した旨を送信
									server.receiver.get(oppositeNum).printWriter.flush();

									server.receiver.get(oppositeNum).setWhereIs(2);
									server.receiver.get(oppositeNum).setSendOfferNum(-1);

								}
							}






							// 同時実行制御を入れるために、関数へ
							/*if(server.acceptOffer(n, oppositeNum)){
								printWriter.println("81");
								printWriter.flush();
								server.startJudge(n, oppositeNum);
							}
							else
								printWriter.println("80");
								printWriter.flush();*/





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
			//server.delete(server.changeFromMyNum(myNumber));

			for(int i=0;i<receiveOfferPlayer.size();i++) {//切断したのでオファーを受けていた全員にキャンセル送信
				//if(!(receiveOfferPlayer.get(i).sendID().equals("oppositeName"))) {
					int cancelNum = server.changeFromID(receiveOfferPlayer.get(i).sendID());

					server.receiver.get(i).printWriter.println("800"+player.sendPlayerData());//相手に拒否した旨を送信
					server.receiver.get(i).printWriter.flush();

					server.receiver.get(i).setWhereIs(2);
					server.receiver.get(i).setSendOfferNum(-1);
				//}
			}


			if(decidedOppositeNum!=-1) {

				//相手に切断した情報をおくる
				server.receiver.get(decidedOppositeNum).printWriter.println("4");
			}

			whereIs=-1;//切断状態に移行

			server.allPlayerOutput();//player一覧の更新

		}
	}

	private void cancelOffer(String str) {//str以外にoffercancelされたことを送信
		for(int i=0;i<receiveOfferPlayer.size();i++) {
			if(!(receiveOfferPlayer.get(i).sendID().equals(str))) {
				int cancelNum = server.changeFromID(receiveOfferPlayer.get(i).sendID());

				server.receiver.get(cancelNum).printWriter.println("800"+player.sendPlayerData());//相手に拒否した旨を送信
				server.receiver.get(cancelNum).printWriter.flush();

				server.receiver.get(cancelNum).setWhereIs(2);
				server.receiver.get(cancelNum).setSendOfferNum(-1);
			}
		}
	}
	private void setDecidedOppositeNum(int num) {
		decidedOppositeNum=num;
	}
	private void setMove(int i) {
		move=i;
	}

	private int getSendOfferNum() {
		return sendOfferNum;
	}

	private void setSendOfferNum(int i) {
		sendOfferNum=i;
	}
	public int getWhereIs() {
		return whereIs;
	}

	public void setWhereIs(int i) {
		whereIs=i;
	}

	public void addOfferPlayer(PlayerData player) {
		receiveOfferPlayer.add(player);
	}


	public void removeOfferPlayer(PlayerData player) {
		for(int i=0;i<receiveOfferPlayer.size();i++) {
			if(player==receiveOfferPlayer.get(i)) {
				receiveOfferPlayer.remove(i);
//				System.out.println("オファーキャンセル受付 from "+player.sendPlayerData());
				break;
			}
		}

	}
}