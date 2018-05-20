package server;

public class PlayerData {
	// データ類
	protected String name;
	private String password;
	protected int numWin = 0;
	protected int numDraw = 0;
	protected int numLose = 0;
	protected int numEarlyLose = 0;// 投了数（負け数と重複あり）
	// 対戦相手（いない場合は-1）
     // myNumberの値でありreceiver配列の添え字ではない（配列の要素removeでズレてしまうため）
	protected int opposite = -1;

	// すべての文字を使用できるようにするために個別に区切り文字を保存する
	private char divideKey;


	// メモ用：プレイヤー情報として送る数:6;
	private final static int NUM_ELEMENT = 6;


	// ログイン前に通信切れ用
	PlayerData(){}
	// 新規作成用
	PlayerData(String id, String pass){
		name = id; password = pass;
	}
	// ログイン用
	PlayerData(String str){
		StringBuffer strBuf = new StringBuffer(str);
		// 分割文字は一番最初に書く
		this.divideKey = strBuf.charAt(0);
		// 各分割文字列の長さ（最後の1つ先の数字）
		int len;
		String s;
		for(int i = 0;;i++){
			if(i > NUM_ELEMENT - 1)
				break;
			strBuf.delete(0, 1);
			len = strBuf.indexOf("" + divideKey);
			if(len < 0)
				s = strBuf.substring(0);
			else
				s = strBuf.substring(0, len);
			switch(i){
			case 0:
				name = s;
				break;
			case 1:
				password = s;
				break;
			case 2:
				numWin = Integer.parseInt(s);
				break;
			case 3:
				numDraw = Integer.parseInt(s);
				break;
			case 4:
				numLose = Integer.parseInt(s);
				break;
			case 5:
				numEarlyLose = Integer.parseInt(s);// 改行文字が含まれている場合はエラー？
			default:
				System.out.println("区切り記号が多すぎます(" + i + "個)");
				break;
			}
		}
	}
	// name, numWin, numDraw, numLose, opposite, numEarlyLose
	// を送るときに使用する方
	public String sendPlayerData(){
		return new String(divideKey + name + divideKey + numWin + divideKey + numDraw + divideKey + numLose + divideKey + opposite + divideKey + numEarlyLose);
	}

	// 引数の文字列に対して divideKey作成
	public static char makeKey(String str){
		StringBuffer strBuf = new StringBuffer(str);

		// 最初はAから
		char returnChar = 'A';
		while(true){
			if(strBuf.indexOf("" + returnChar) == -1){
				break;
			}
			returnChar += 1;
			if(returnChar == 0)
				System.out.println("関数makeKey()内のキーがオーバーフロー");
		}
		return returnChar;
	}


	// ファイルに書き出す際の文字列を作る関数
	public String fileOutStr(){
		divideKey = makeKey(name + password);
		return new String(divideKey + name + divideKey + password + divideKey + numWin + divideKey + numDraw + divideKey + numLose + divideKey + numEarlyLose);
	}
	protected String serverOutput(){
		return new String("ID:" + name);
	}
}
