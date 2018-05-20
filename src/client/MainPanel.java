package client;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MainPanel extends JPanel{

	Client client;

	Font f=new Font("Arial",Font.PLAIN,50);

	//JLabel errorMsg=new JLabel("");//入力エラー時のメッセージ


	MainPanel(Client cl,String name){
		client=cl;
		this.setName(name);

		//mainScreen();
	}

	public void background() {//簡単のために背景表示のみメソッドを分割
		BufferedImage bgImage=null;

		try {
			bgImage=ImageIO.read(new File("./bg.png"));
		}catch(Exception e) {
			e.printStackTrace();
			bgImage=null;
		}


		JLabel bg = new JLabel(new ImageIcon(bgImage));

		bg.setBounds(0,0,1500,1000);
		add(bg,0);

	}

	public void backToMain() {
		JButton backToMain=new JButton("Back to Main");

		backToMain.setBounds(50,900,400,50);
		backToMain.setFont(f);
		backToMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//login2_button_click_event
				mainScreen();
				repaint();

				client.receiveHandler(0);//データ要求キャンセル

			}
		});

		add(backToMain,0);
	}

	public void mainScreen() {//起動時画面

		JButton createID=new JButton("新規作成");
		JButton login=new JButton("LOGIN");
		JLabel title=new JLabel("OTHELLO GAME");

		removeAll();

		background();

		createID.setBounds(550,400,400,100);
		createID.setFont(new Font("MS Gothic",Font.PLAIN,50));
		createID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//createID_button_click_event
				loginScreen(0);
				repaint();
			}
		});

		login.setBounds(550,600,400,100);
		login.setFont(f);
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//login_button_click_event
				loginScreen(1);
				repaint();
			}
		});




		title.setHorizontalAlignment(JLabel.CENTER);
	    //title.setPreferredSize(new Dimension(550,200));
		title.setBounds(550,200,400,50);
		title.setFont(f);
		title.setForeground(Color.RED);
		title.setFont(f);


		add(createID,0);
		add(login,0);
		add(title,0);

		repaint();
	}

	public void loginScreen(int i){//ログイン画面に遷移,引数0:新規作成,1:ログイン

		removeAll();

		background();
		backToMain();

		JLabel loginBg = new JLabel();
		JLabel loginMsg=new JLabel("プレイヤー情報を入力してください。");
		JLabel idMsg=new JLabel("id");
		JLabel passMsg=new JLabel("pass");
		JTextField id=new JTextField(16);
		JPasswordField pass=new JPasswordField(16);
		JButton login2=new JButton();//このボタンだけ、ログインのときと新規作成のときで分岐
		JLabel errorMsg=new JLabel("");//入力エラー時のメッセージ



		loginBg.setOpaque(true);
		loginBg.setBounds(375,125,750,750);
		loginBg.setBackground(Color.BLACK);


		loginMsg.setHorizontalAlignment(JLabel.CENTER);
		loginMsg.setBounds(500,200,500,50);
		loginMsg.setFont(new Font("MS Gothic",Font.PLAIN,25));
		loginMsg.setForeground(Color.WHITE);

		idMsg.setHorizontalAlignment(JLabel.CENTER);
		idMsg.setBounds(550,300,400,50);
		idMsg.setFont(f);
		idMsg.setForeground(Color.WHITE);

		id.setBounds(550,400,400,50);
		id.setFont(f);

		passMsg.setHorizontalAlignment(JLabel.CENTER);
		passMsg.setBounds(550,500,400,50);
		passMsg.setFont(f);
		passMsg.setForeground(Color.WHITE);

		pass.setBounds(550,600,400,50);
		pass.setFont(f);

		errorMsg.setHorizontalAlignment(JLabel.CENTER);
		errorMsg.setBounds(500,700,500,50);
		errorMsg.setFont(new Font("MS Gothic",Font.PLAIN,25));
		errorMsg.setForeground(Color.RED);

		if(i==0) {
			login2.setText("新規作成");
			login2.setFont(new Font("MS Gothic",Font.PLAIN,25));
		}else {
			login2.setText("LOGIN");
			login2.setFont(f);
		}
		login2.setBounds(550,750,400,50);
		login2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//login2_button_click_event
				String input_id;
				String input_pass;

				String temppass;//passはchar[]型のためストリング型に変換
				temppass=new String(pass.getPassword());

				if(id.getText().equals("")&&temppass.equals("")) {
					errorMsg.setText("idとpassを入力してください。");
				}else if(id.getText().equals("")) {
					errorMsg.setText("idを入力してください。");
				}else if(temppass.equals("")) {
					errorMsg.setText("passを入力してください。");
				}else{
					errorMsg.setText("");
					input_id=id.getText();
					input_pass=temppass;

					//test
					System.out.println(input_id);
					System.out.println(input_pass);

					if(i==0) {
						//client.getTempPlayerID(input_id);
						client.sendMessage("0"+input_id+"\n"+input_pass);
					}else if(i==1) {
						//client.getTempPlayerID(input_id);
						//client.sendMessage("1"+input_id+"\n"+input_pass);
						client.sendMessage("1"+input_id+"\n"+input_pass);//test用

					}
					//client.sendMsg(input_id);
					errorMsg.setText("サーバと通信中・・・");

					client.receiveHandler(1);//データ要求

					//サーバに接続するメソッドに（clientクラスに戻したほうがいいかも）　確認中などのメッセージ表示させたい
					remove(login2);
					//メインに戻るを消すかどうするか
					repaint();
				}
			}
		});




		add(loginBg,0);
		add(loginMsg,0);
		add(idMsg,0);
		add(id,0);
		add(passMsg,0);
		add(pass,0);
		add(errorMsg,0);
		add(login2,0);

	}


	public void connectError() {
		JLabel msg1=new JLabel("サーバ接続時にエラーが発生しました。");
		JLabel msg2=new JLabel("ゲームクライアントを終了してください。");

		removeAll();

		background();

		msg1.setHorizontalAlignment(JLabel.CENTER);
		msg1.setBounds(500,200,500,50);
		msg1.setFont(new Font("MS Gothic",Font.PLAIN,25));
		msg1.setForeground(Color.RED);

		msg2.setHorizontalAlignment(JLabel.CENTER);
		msg2.setBounds(500,300,500,50);
		msg2.setFont(new Font("MS Gothic",Font.PLAIN,25));
		msg2.setForeground(Color.RED);

		add(msg1,0);
		add(msg2,0);

		repaint();
	}

	public void authenticationMsg(int i){//0:登録失敗1:登録成功2:login失敗
		JLabel msg1=new JLabel("");
		JLabel msg2=new JLabel("");

		removeAll();
		background();
		backToMain();


		if(i==0) {
			msg1.setText("入力されたIDはすでに存在します。");
		}else if(i==1){
			msg1.setText("アカウントの新規作成に成功しました。");
			msg2.setText("メイン画面に戻ってログインしてください。");
		}else if(i==2){
			msg1.setText("ログインできませんでした");
		}

		msg1.setHorizontalAlignment(JLabel.CENTER);
		msg1.setBounds(500,500,500,50);
		msg1.setFont(new Font("MS Gothic",Font.PLAIN,25));
		msg1.setForeground(Color.RED);

		msg2.setHorizontalAlignment(JLabel.CENTER);
		msg2.setBounds(500,600,500,50);
		msg2.setFont(new Font("MS Gothic",Font.PLAIN,25));
		msg2.setForeground(Color.RED);

		add(msg1,0);
		add(msg2,0);


		repaint();

	}

}
