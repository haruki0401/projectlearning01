package server;



// Fileなどに必要
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
// ServerSocketに必要
import java.net.ServerSocket;
import java.net.Socket;
// ArrayListに必要
import java.util.ArrayList;

public class Server {
	protected static int PORT; // サーバの待ち受けポート

	// プレイヤーデータのあるファイル
	private final static String FILE_NAME = "./player.txt";
	// 全体の戦績データのあるファイル
	private final static String RESULT_FILE_NAME = "./result.txt";


	private static FileReader fileRead;
	private static BufferedReader bufFileRead;
	private static FileWriter fileWrite;
	private static BufferedWriter bufFileWrite;

	// Receiverの配列
	protected ArrayList<Receiver> receiver = new ArrayList<Receiver>();


	// サーバ起動からの接続者の総数
	private int numPlayer = 0;
	// を変えてないか
	private static boolean semaphore = false;

	// テキストの読み込みや書き込みをしてないか
	private static boolean textSemaphore = false;
	private static boolean resultSemaphore = false;

	public static void main(String[] args) {
		PORT = Integer.parseInt(args[0]);
		Server s = new Server();
		s.mainLoop();
	}
	private void mainLoop(){
		try{
			System.out.println("サーバを起動しました");
			// ポートを取得
	        int port = Server.PORT;

			// サーバーソケットを作成
	        ServerSocket ss = new ServerSocket(port);
	        while (true) {
	        	// 新規接続を受け付ける
	        	Socket socket = ss.accept();

	        	System.out.println("クライアントが新たに接続されました");
			while(semaphore);
			setSemaphore(true);
			receiver.add(new Receiver(socket, numPlayer, this));
         	receiver.get(numPlayer).start();
			numPlayer++;
	        setSemaphore(false);
		   }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 同期用関数
	protected synchronized static void setSemaphore(boolean s){
		if(s)
			while(semaphore); // 2つ連続でここまで飛んできた時は終了まで待つ
		semaphore = s;
	}
	protected synchronized static void setTextSemaphore(boolean s){
		if(s)
			while(textSemaphore); // 2つ連続でここまで飛んできた時は終了まで待つ
		textSemaphore = s;
	}
	protected synchronized static void setResultSemaphore(boolean s){
		if(s)
			while(resultSemaphore); // 2つ連続でここまで飛んできた時は終了まで待つ
		resultSemaphore = s;
	}

	/*protected synchronized void delete(int n){
		while(semaphore);
		setSemaphore(true);
		if(receiver.get(n).player.opposite != -1){
			int opposite = changeFromMyNum(receiver.get(n).player.opposite);
			// 対戦相手も切れた場合は考えてない
			receiver.get(opposite).printWriter.println("9");
			receiver.get(opposite).printWriter.flush();
			resultEarlyLose(opposite, n);
		}
		receiver.remove(n);
		numPlayer--;
		setSemaphore(false);
		allPlayerOutput();
	}*/


	// ログインに成功すれば現在のプレイヤーデータに追加
	// 引数　id, password
	protected String inputText(String id, String pass, PlayerData pla,int myNumber){
		//System.out.println(textSemaphore);
		while(textSemaphore);
		setTextSemaphore(true);
		//StringBuffer strBuf = new StringBuffer(id + pass);
		// 2つ目,3つ目の区切り文字の位置用
		int secondKeyAt;
		int thirdKeyAt;

		String answer="0";

		int alreadyLogin=0;

		try{

			for(int i=0;i<receiver.size();i++) {//同じIDで既にログインしていないかチェック
				if(receiver.get(i).player.sendID().equals(id)) {
					if(receiver.get(i).getWhereIs()!=0&&receiver.get(i).getWhereIs()!=-1) {
						alreadyLogin=1;
						break;
					}
				}
			}

			if(alreadyLogin==0) {

				fileRead = new FileReader(FILE_NAME);
				bufFileRead = new BufferedReader(fileRead);
				while(bufFileRead.ready()){
					String readedLine = bufFileRead.readLine();

					String[] str=readedLine.split("/");





					if(str[0].equals(id)&&str[1].equals(pass)) {

						receiver.get(myNumber).player=new PlayerData(readedLine);


						//System.out.println(pla.sendPlayerData());

						//allPlayerOutput();
						 answer=str[0]+'/'+str[2]+'/'+str[3]+'/'+str[4]+'/'+str[5];
						 break;

					}


						// まずid + passに区切り文字がないことを確認
						/*if(strBuf.indexOf(lineBuf.charAt(0) + "") == -1){
							// まずは2つ目、3つ目の区切り文字の場所を把握
							secondKeyAt = readedLine.indexOf(readedLine.charAt(0), 1);
							thirdKeyAt = readedLine.indexOf(readedLine.charAt(0),secondKeyAt + 1);
							if(secondKeyAt < 0 || thirdKeyAt < 0)
								System.out.println("関数inputtext()内のsecondKeyAtもしくはthirdKeyAtの値が不適です");

							// ID,パスワードがあってたなら
							if(lineBuf.substring(1, secondKeyAt).equals(id) && lineBuf.substring(secondKeyAt + 1, thirdKeyAt).equals(pass)){
								pla = new PlayerData(readedLine);
								allPlayerOutput();
								break;
							}
						}*/
					/*}else{
						setTextSemaphore(false);
						return false;*/


				}
				bufFileRead.close();
				fileRead.close();
			}
		}
		catch(FileNotFoundException e){
			System.out.println("fileError");
		}
		// ファイルがない以外の問題の時
		catch(Exception e){
			e.printStackTrace();
		}
		setTextSemaphore(false);
		return answer;
	}
	// 新規登録の際に使えるidかどうか（ついでに登録も）
	protected synchronized boolean isNotUsed(String id, String pass, PlayerData pla,int myNumber){

		while(textSemaphore);
		setTextSemaphore(true);
		StringBuffer strBuf = new StringBuffer(id);
		// 2つ目の区切り文字の位置用
		int secondKeyAt;

		boolean a=true;
	//	System.out.println(id);
		try{

			fileRead = new FileReader(FILE_NAME);
			bufFileRead = new BufferedReader(fileRead);
			while(bufFileRead.ready()){
				//if(bufFileRead.ready()){
					String readedLine = bufFileRead.readLine();

					String[] str=readedLine.split("/");

					if(str[0].equals(id)) {
						 a=false;
						 break;
					}


					/*StringBuffer lineBuf = new StringBuffer(readedLine);


					// まずidに区切り文字がないことを確認
					if(strBuf.indexOf(lineBuf.charAt(0) + "") == -1){
						// 2つ目の区切り文字の場所を把握
						secondKeyAt = readedLine.indexOf(readedLine.charAt(0), 1);
						if(secondKeyAt < 0)
							System.out.println("関数isNotUsed()内のsecondKeyAtの値が不適です");

						// IDが一致してたなら
						if(lineBuf.substring(1, secondKeyAt).equals(id)){
							setTextSemaphore(false);
							return false;
						}
					}*/
			}

					bufFileRead.close();
					fileRead.close();
			//	System.out.println("通貨1");
				setTextSemaphore(false);

				if(a) {
					receiver.get(myNumber).player=new PlayerData(id,pass);

					//pla = new PlayerData(id, pass);
				//	System.out.println(textSemaphore);
					outputText(receiver.get(myNumber).player.fileOutStr(), FILE_NAME);
				}
					//return true;

			//while
		}
		catch(FileNotFoundException e){
			System.out.println("no file");
		}
		// ファイルがない以外の問題の時
		catch(Exception e){
			e.printStackTrace();
		}
	//	System.out.println("通貨3");
		setTextSemaphore(false);
		return a;
	}

	// 引数:追加で書き込む文字列, ファイル名
	// IDパスワード：player.fileOutStr(), FILE_NAME
	//      戦績:, RESULT_FILE_NAME
	protected static synchronized void outputText(String str, String name){
		if(name.equals(FILE_NAME)){
			while(textSemaphore);
			setTextSemaphore(true);
		}else if(name.equals(RESULT_FILE_NAME)){
			while(resultSemaphore);
			setResultSemaphore(true);
		}
		try{
			fileWrite = new FileWriter(name, true);
			bufFileWrite = new BufferedWriter(fileWrite);
			bufFileWrite.write(str+"\n");
			bufFileWrite.close();
			fileWrite.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		if(name.equals(FILE_NAME))
			setTextSemaphore(false);
		else if(name.equals(RESULT_FILE_NAME))
			setResultSemaphore(false);
	}

	// 個人の戦績を更新する関数(2人同時)
	// 引数：添え字
	protected synchronized void renewText(int num1, int num2){
		while(textSemaphore);
		setTextSemaphore(true);
		try{
			// 読み込み部
			ArrayList<String> inoutStr = new ArrayList<String>();//一時的にこの配列にplayer.txtのデータ読み込む

			fileRead = new FileReader(FILE_NAME);
			bufFileRead = new BufferedReader(fileRead);

			while(bufFileRead.ready())
				inoutStr.add(bufFileRead.readLine());

			bufFileRead.close();
			fileRead.close();

			// 書き込み部
			fileWrite = new FileWriter(FILE_NAME, false);
			bufFileWrite = new BufferedWriter(fileWrite);
			String id, line;
			//int secondKey;

			String name1,name2;

			name1=receiver.get(num1).player.sendID();
			name2=receiver.get(num2).player.sendID();


			for(int i = 0; i < inoutStr.size(); i++){
				line=inoutStr.get(i);

				id = inoutStr.get(i).split("/")[0];
				//secondKey = line.indexOf(line.charAt(0), 1);
				//id = line.substring(1, secondKey);

				if(id.equals(name1))
					line = receiver.get(num1).player.fileOutStr();
				else if(id.equals(name2))
					line = receiver.get(num2).player.fileOutStr();
				bufFileWrite.write(line+"\n");
			}

			bufFileWrite.close();
			fileWrite.close();
			//-----------------------------------------------
		}
		catch(Exception e){
			e.printStackTrace();
		}
		setTextSemaphore(false);
	}

	// 戦績データファイルがあるなら読み込む
	//  引数：検索する文字列, 表示する最大数(-1なら全て)
	// 戻り値：見つかった行を全て改行込みで繋いだ文字列（区切り文字込み）
	protected String inputResultText(String str){
		while(resultSemaphore);
		setResultSemaphore(true);

		String returnStr = "";
		String firstPlayer, secondPlayer,judge;
		int secondKeyAt, thirdKeyAt;
		try{
			// ヒット数
			int hit = 0;
			StringBuffer searchKey = new StringBuffer(str);

			fileRead = new FileReader(RESULT_FILE_NAME);
			bufFileRead = new BufferedReader(fileRead);
			while(bufFileRead.ready()){
				String readedLine = bufFileRead.readLine();
				StringBuffer strBuf = new StringBuffer(readedLine);
				// divideKeyが検索文字列に含まれるなら違う
				//char divideKey = strBuf.charAt(0);

				firstPlayer=readedLine.split("/")[0];
				secondPlayer=readedLine.split("/")[1];
				judge=readedLine.split("/")[2];

				if(firstPlayer.equals(str)) {
					if(judge.equals("lose")) {
						returnStr+="\n"+secondPlayer+"/"+"01";
					}
					else if(judge.equals("earlyLose")) {
						returnStr+="\n"+secondPlayer+"/"+"11";
					}
					else if(judge.equals("draw")) {
						returnStr+="\n"+secondPlayer+"/"+"10";
					}

					hit++;
				}
				else if(secondPlayer.equals(str)) {
					if(judge.equals("lose")) {
						returnStr+="\n"+firstPlayer+"/"+"00";
					}
					else if(judge.equals("earlyLose")) {
						returnStr+="\n"+firstPlayer+"/"+"00";
					}
					else if(judge.equals("draw")) {
						returnStr+="\n"+firstPlayer+"/"+"10";
					}

					hit++;
				}

				if(hit>=30) {//ヒット数30以下
					break;
				}


				/*if(searchKey.indexOf(divideKey + "") == -1)
					continue;
				// そうでないなら普通に検索しても変わらない
				secondKeyAt = strBuf.indexOf(divideKey + "", 1);
				thirdKeyAt = strBuf.indexOf(divideKey + "", secondKeyAt + 1);
				firstPlayer = readedLine.substring(1, secondKeyAt);
				secondPlayer = readedLine.substring(secondKeyAt + 1, thirdKeyAt);
				if(firstPlayer.equals(str) || secondPlayer.equals(str)){
					returnStr += changeResultText(readedLine, str);
					hit++;
				}
				if(maxNum != -1 && hit >= maxNum)
					break;*/
			}
			bufFileRead.close();
			fileRead.close();
		}
		catch(FileNotFoundException e){
		}
		// ファイルがない以外の問題の時
		catch(Exception e){
			e.printStackTrace();
		}

		setResultSemaphore(false);
		return returnStr;
	}
	// inputResultTextの出力分の形を整える
	private String changeResultText(String str, String myName){
		String retStr = "";
		char divideKey = str.charAt(0);
		int secondKeyAt, thirdKeyAt;
		String firstName, secondName;
		secondKeyAt = str.indexOf(divideKey + "", 1);
		thirdKeyAt = str.indexOf(divideKey + "", secondKeyAt + 1);


		// 引き分けまたは投了の時
		if(thirdKeyAt != -1){
			firstName = str.substring(1, secondKeyAt);
			secondName = str.substring(secondKeyAt + 1, thirdKeyAt);
			// 投了の時
			if(str.indexOf(divideKey + "", thirdKeyAt + 1) != -1){
				if(firstName.equals(myName)){
					retStr += divideKey + secondName + divideKey + "0\n";
				}else if(secondName.equals(myName)){
					retStr += divideKey + firstName + divideKey + "3\n";
				}else
					System.out.println("changeResultText()内で名前の不一致が起きています");
			}else{
				if(firstName.equals(myName)){
					retStr += divideKey + secondName + divideKey + "1\n";
				}else if(secondName.equals(myName)){
					retStr += divideKey + firstName + divideKey + "1\n";
				}else
					System.out.println("changeResultText()内で名前の不一致が起きています");
			}
		}else{
			firstName = str.substring(1, secondKeyAt);
			secondName = str.substring(secondKeyAt + 1);
			if(firstName.equals(myName)){
				retStr += divideKey + secondName + divideKey + "0\n";
			}else if(secondName.equals(myName)){
				retStr += divideKey + firstName + divideKey + "2\n";
			}else
				System.out.println("changeResultText()内で名前の不一致が起きています");
		}

		return retStr;
	}


	// myNumberから添え字にする
	protected int changeFromMyNum(int myNum){
		for(int i = 0;;i++){
			if(receiver.get(i).myNumber == myNum)
				return i;
		}
	}
	// IDから添え字にする
	protected int changeFromID(String id){
		for(int i = 0;;i++){
			if(receiver.get(i).player.id.equals(id)&&receiver.get(i).getWhereIs()!=-1&&receiver.get(i).getWhereIs()!=0)
				return i;
		}
	}

	// 現在ログインしている全ての人
	protected String allLoginPlayerData(int myNumber){
		// できれば入力途中は避けたい
		int max = receiver.size();
		//System.out.println(max);
		String retStr = "3";

		int playerFound=0;//onlineplayerが見つかったかどうかの変数

		try{//receiver配列バグった時のために一応例外処理
			for(int i = 0;i < max;i++){

				if(i==myNumber) {//自分を見つけた
					//do nothing
				}else if(receiver.get(i).getWhereIs()==2||receiver.get(i).getWhereIs()==3){
					retStr += receiver.get(i).player.sendPlayerData()+"\n";
					playerFound=1;
				}
			}

			if(playerFound==1) {//最後の改行消す
				retStr=retStr.substring(0,retStr.length()-1);
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return retStr;
	}
	// １人のplayerdata表示
	protected String getPlayerData(String str){
		while(textSemaphore);
		setTextSemaphore(true);

		String retStr = "";

		StringBuffer strBuf;
		char divideKey;
		int secondKeyAt, thirdKeyAt;
		try{
			fileRead = new FileReader(FILE_NAME);
			bufFileRead = new BufferedReader(fileRead);

			while(bufFileRead.ready()){
				// readLineには改行が含まれないはず
				String readedLine = bufFileRead.readLine();

				String s[]=readedLine.split("/");

				String id=s[0];


				if(id.equals(str)) {
					retStr+=s[0]+"/"+s[2]+"/"+s[3]+"/"+s[4]+"/"+s[5];

					break;
				}

				/*strBuf = new StringBuffer(bufFileRead.readLine() + "\n");
				//divideKey = strBuf.charAt(0);
				secondKeyAt = strBuf.indexOf("" + divideKey, 1);
				thirdKeyAt = strBuf.indexOf("" + divideKey, secondKeyAt + 1);
				strBuf.delete(secondKeyAt, thirdKeyAt);

				retStr += strBuf + "\n";*/
			}
			bufFileRead.close();
			fileRead.close();
		}
		catch(Exception e){
			e.printStackTrace();
			//retStr =  allPlayerData();
		}
		setTextSemaphore(false);
		return retStr;
	}

	// オファー受理
	// 引数： 受理した方の添え字、オファーを出したほうの添え字
	protected synchronized boolean acceptOffer(int rAccept, int rOffer){
		if(receiver.get(rOffer).player.opposite != -1)
			return false;
		// 念のためこっちも
		if(receiver.get(rAccept).player.opposite != -1)
			return false;
		receiver.get(rOffer).player.opposite = receiver.get(rAccept).myNumber;
		receiver.get(rOffer).printWriter.println("8" + receiver.get(rAccept).player.sendPlayerData());
		receiver.get(rOffer).printWriter.flush();
		receiver.get(rAccept).player.opposite = receiver.get(rOffer).myNumber;
		return true;
	}

	protected void startJudge(int rAccept, int rOffer){
		// rOfferの方が先に来たとき
		//（myNumberはServerが動き始めてから、上がり続ける）
		if(receiver.get(rAccept).myNumber > receiver.get(rOffer).myNumber){
			receiver.get(rOffer).printWriter.println("40");
			receiver.get(rAccept).printWriter.println("41");
		}else{
			receiver.get(rOffer).printWriter.println("41");
			receiver.get(rAccept).printWriter.println("40");
		}
		receiver.get(rOffer).printWriter.flush();
		receiver.get(rAccept).printWriter.flush();
	}
	// 引数は添え字
	protected void resultWin(int winnerNum,int loserNum){

		renewText(winnerNum, loserNum);

		//char divideKey = PlayerData.makeKey(receiver.get(winnerNum).player.id + receiver.get(loserNum).player.id);
		outputText(receiver.get(loserNum).player.sendID() + "/" + receiver.get(winnerNum).player.sendID()+"/lose/win", RESULT_FILE_NAME);
	}
	protected void resultDraw(int num1,int num2){

		renewText(num1, num2);

		//char divideKey = PlayerData.makeKey(receiver.get(num1).player.id + receiver.get(num2).player.id);
		// 引き分けは最後に区切り文字を入れる
		outputText(receiver.get(num1).player.id +"/" + receiver.get(num2).player + "/draw", RESULT_FILE_NAME);
	}

	protected void resultEarlyLose(int winnerNum,int loserNum){

		/*
		receiver.get(winnerNum, loserNum);//エラー吐くのでコメントアウトした。（なにかは謎）
		*/

		renewText(winnerNum, loserNum);
		// 投了は最後区切り文字2つ
		//char divideKey = PlayerData.makeKey(receiver.get(winnerNum).player.id + receiver.get(loserNum).player.id);
		outputText(receiver.get(loserNum).player.id +"/" + receiver.get(winnerNum).player.id + "/earlyLose/win", RESULT_FILE_NAME);
	}
	public void allPlayerOutput(){
		String state="";
		int where;

		System.out.println("現在ログインしているプレイヤー");
		for(int i = 0; i < receiver.size();i++) {
			where=receiver.get(i).getWhereIs();

			if(where!=-1&&where!=0) {
				if(where==1) {
					state="ログイン中";
				}
				else if(where==2||where==3) {
					state="オンライン中";
				}
				else if(where==4) {
					state="対局中";
				}

				System.out.println("number "+i+".ID: "+receiver.get(i).player.sendID()+", "+state);
			}
		}
	}
}
