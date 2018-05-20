package server;

public class PlayerData {
	// データ類
	protected String id;
	private String pass;
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
		this.id = id; this.pass = pass;
	}
	// ログイン用
	PlayerData(String str){
		//StringBuffer strBuf = new StringBuffer(str);
		// 分割文字は一番最初に書く
		//this.divideKey = strBuf.charAt(0);
		// 各分割文字列の長さ（最後の1つ先の数字）

		String[] data=str.split("/");

		int len;
		String s;
		/*for(int i = 0;;i++){
			if(i > NUM_ELEMENT - 1)
				break;
			strBuf.delete(0, 1);
			len = strBuf.indexOf("" + '/');
			if(len < 0)
				s = strBuf.substring(0);
			else
				s = strBuf.substring(0, len);
			switch(i){
			case 0:
				id = s;
				break;
			case 1:
				pass = s;
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
		}*/

		this.id=data[0];
		this.pass=data[1];
		this.numWin=Integer.parseInt(data[2]);
		this.numDraw=Integer.parseInt(data[3]);
		this.numLose=Integer.parseInt(data[4]);
		this.numEarlyLose=Integer.parseInt(data[5]);


		System.out.println("id"+id);



	}
	// name, numWin, numDraw, numLose, opposite, numEarlyLose
	// を送るときに使用する方
	public String sendPlayerData(){
		return new String(id + '/' + numWin + '/' + numDraw + '/' + numLose +  '/' + numEarlyLose);
	}

	// 引数の文字列に対して '/'作成
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
		//'/' = '/';
		return new String(id + '/' + pass + '/' + numWin + '/' + numDraw + '/' + numLose + '/' + numEarlyLose);
	}
	protected String serverOutput(){
		return new String("ID:" + id);
	}
}
