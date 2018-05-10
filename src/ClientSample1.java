//パッケージのインポート
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ClientSample1 extends JFrame implements MouseListener {
	private JButton buttonArray[];//オセロ盤用のボタン配列
	private JButton stop, pass; //停止、スキップ用ボタン
	private JLabel colorLabel; // 色表示用ラベル
	private JLabel turnLabel; // 手番表示用ラベル
	private Container c; // コンテナ
	private ImageIcon blackIcon, whiteIcon, boardIcon; //アイコン

	// コンストラクタ
	public ClientSample1() {
		//テスト用に局面情報を初期化
		String [] grids =
			{"board","board","board","board","board","board","board","board",
			"board","board","board","board","board","board","board","board",
			"board","board","board","board","board","board","board","board",
			"board","board","board","black","white","board","board","board",
			"board","board","board","white","black","board","board","board",
			"board","board","board","board","board","board","board","board",
			"board","board","board","board","board","board","board","board",
			"board","board","board","board","board","board","board","board"};
		int row = 8; //オセロ盤の縦横マスの数
		//ウィンドウ設定
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じる場合の処理
		setTitle("ネットワーク対戦型オセロゲーム");//ウィンドウのタイトル
		setSize(row * 45 + 10, row * 45 + 200);//ウィンドウのサイズを設定
		c = getContentPane();//フレームのペインを取得
		//アイコン設定(画像ファイルをアイコンとして使う)
		whiteIcon = new ImageIcon("White.jpg");
		blackIcon = new ImageIcon("Black.jpg");
		boardIcon = new ImageIcon("GreenFrame.jpg");
		c.setLayout(null);//
		//オセロ盤の生成
		buttonArray = new JButton[row * row];//ボタンの配列を作成
		for(int i = 0 ; i < row * row ; i++){
			if(grids[i].equals("black")){ buttonArray[i] = new JButton(blackIcon);}//盤面状態に応じたアイコンを設定
			if(grids[i].equals("white")){ buttonArray[i] = new JButton(whiteIcon);}//盤面状態に応じたアイコンを設定
			if(grids[i].equals("board")){ buttonArray[i] = new JButton(boardIcon);}//盤面状態に応じたアイコンを設定
			c.add(buttonArray[i]);//ボタンの配列をペインに貼り付け
			// ボタンを配置する
			int x = (i % row) * 45;
			int y = (int) (i / row) * 45;
			buttonArray[i].setBounds(x, y, 45, 45);//ボタンの大きさと位置を設定する．
			buttonArray[i].addMouseListener(this);//マウス操作を認識できるようにする
			buttonArray[i].setActionCommand(Integer.toString(i));//ボタンを識別するための名前(番号)を付加する
		}
		//終了ボタン
		stop = new JButton("終了");//終了ボタンを作成
		c.add(stop); //終了ボタンをペインに貼り付け
		stop.setBounds(0, row * 45 + 30, (row * 45 + 10) / 2, 30);//終了ボタンの境界を設定
		stop.addMouseListener(this);//マウス操作を認識できるようにする
		stop.setActionCommand("stop");//ボタンを識別するための名前を付加する
		//パスボタン
		pass = new JButton("パス");//パスボタンを作成
		c.add(pass); //パスボタンをペインに貼り付け
		pass.setBounds((row * 45 + 10) / 2, row * 45 + 30, (row * 45 + 10 ) / 2, 30);//パスボタンの境界を設定
		pass.addMouseListener(this);//マウス操作を認識できるようにする
		pass.setActionCommand("pass");//ボタンを識別するための名前を付加する
		//色表示用ラベル
		colorLabel = new JLabel("色は未定です");//色情報を表示するためのラベルを作成
		colorLabel.setBounds(10, row * 45 + 60 , row * 45 + 10, 30);//境界を設定
		c.add(colorLabel);//色表示用ラベルをペインに貼り付け
		//手番表示用ラベル
		turnLabel = new JLabel("手番は未定です");//手番情報を表示するためのラベルを作成
		turnLabel.setBounds(10, row * 45 + 120, row * 45 + 10, 30);//境界を設定
		c.add(turnLabel);//手番情報ラベルをペインに貼り付け
	}

	// メソッド
	public void connectServer(String ipAddress, int port){	// サーバに接続
	}
	public void sendMessage(String msg){	// サーバに操作情報を送信
	}
	public void receiveMessage(String msg){	// メッセージの受信
	}
	public void updateDisp(){	// 画面を更新する
	}
	public void acceptOperation(String command){	// プレイヤの操作を受付
	}
  	//マウスクリック時の処理
	public void mouseClicked(MouseEvent e) {
		JButton theButton = (JButton)e.getComponent();//クリックしたオブジェクトを得る．キャストを忘れずに
		String command = theButton.getActionCommand();//ボタンの名前を取り出す
		System.out.println("マウスがクリックされました。押されたボタンは " + command + "です。");//テスト用に標準出力
	}
	public void mouseEntered(MouseEvent e) {}//マウスがオブジェクトに入ったときの処理
	public void mouseExited(MouseEvent e) {}//マウスがオブジェクトから出たときの処理
	public void mousePressed(MouseEvent e) {}//マウスでオブジェクトを押したときの処理
	public void mouseReleased(MouseEvent e) {}//マウスで押していたオブジェクトを離したときの処理
	//テスト用のmain
	public static void main(String args[]){
		ClientSample1 oclient = new ClientSample1();
		oclient.setVisible(true);
	}
}