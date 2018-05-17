import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class MenuPanel extends JPanel{
	Client client;
	Player my;

	Font f=new Font("Arial",Font.PLAIN,50);


	MenuPanel(Client cl,Player my,String name){
		client=cl;
		this.setName(name);
		this.my=my;

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

	public void menuScreen() {//メニュ－画面
		JButton play=new JButton("対局する");
		JButton search=new JButton("player検索");
		JButton signout=new JButton("Sign Out");
		JLabel playerID=new JLabel("ID: "+my.getID()+" さん");
		JLabel playerResult=new JLabel("勝: "+my.getWin()+" 負: "+my.getLose()+" 分: "+my.getDraw());


		removeAll();

		background();

		playerID.setHorizontalAlignment(JLabel.CENTER);
		playerID.setBounds(500,50,500,50);
		playerID.setFont(new Font("MS Gothic",Font.PLAIN,50));
		playerID.setForeground(Color.YELLOW);

		playerResult.setHorizontalAlignment(JLabel.CENTER);
		playerResult.setBounds(500,100,500,50);
		playerResult.setFont(new Font("MS Gothic",Font.PLAIN,50));
		playerResult.setForeground(Color.YELLOW);

		play.setBounds(550,300,400,100);
		play.setFont(new Font("MS Gothic",Font.PLAIN,50));
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//_click_event
				//
			}
		});

		search.setBounds(550,500,400,100);
		search.setFont(new Font("MS Gothic",Font.PLAIN,50));
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//_click_event
				search();
			}
		});

		signout.setBounds(50,900,400,50);
		signout.setFont(new Font("MS Gothic",Font.PLAIN,50));
		signout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//_click_event
				client.changePanel(1,0);
			}
		});

		add(playerID,0);
		add(playerResult,0);
		add(play,0);
		add(search,0);
		add(signout,0);


		repaint();
	}

	public void search() {//探したいプレイヤーの入力画面
		BufferedImage menuBgImage=null;

		try {
			menuBgImage=ImageIO.read(new File("./menubg.png"));
		}catch(Exception e) {
			e.printStackTrace();
			menuBgImage=null;
		}

		JLabel menuBg = new JLabel(new ImageIcon(menuBgImage));
		JLabel msg=new JLabel("検索したいplayerIDを入力してください。");
		JTextField searchID=new JTextField(16);
		JButton search=new JButton("検索");
		JLabel errorMsg=new JLabel("");



		removeAll();

		background();
		backToMenu();

		menuBg.setBounds(0,125,1500,750);

		msg.setHorizontalAlignment(JLabel.CENTER);
		msg.setBounds(500,200,500,50);
		msg.setFont(new Font("MS Gothic",Font.PLAIN,25));
		msg.setForeground(Color.WHITE);

		searchID.setBounds(550,300,400,50);
		searchID.setFont(f);

		search.setBounds(550,400,400,50);
		search.setFont(new Font("MS Gothic",Font.PLAIN,50));
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//_click_event
				String input_id;

				if(searchID.getText().equals("")) {
					errorMsg.setText("idを入力してください。");
				}else{
					errorMsg.setText("");
					input_id=searchID.getText();
					//test
					System.out.println(input_id);


					//debug用
					Player[] p=new Player[10];

					for(int i=0;i<10;i++) {
						p[i]=my;

					}

					results(p);

					//終わり

					client.sendMessage("3"+input_id);

					//client.sendMsg(input_id);
					errorMsg.setText("サーバと通信中・・・");

					client.receiveHandler(1);//データ要求

					//サーバに接続するメソッドに（clientクラスに戻したほうがいいかも）　確認中などのメッセージ表示させたい
					remove(search);
					//メインに戻るを消すかどうするか
					repaint();
				}
			}
		});

		errorMsg.setHorizontalAlignment(JLabel.CENTER);
		errorMsg.setBounds(500,700,500,50);
		errorMsg.setFont(new Font("MS Gothic",Font.PLAIN,25));
		errorMsg.setForeground(Color.RED);

		add(menuBg,0);
		add(msg,0);
		add(searchID,0);
		add(search,0);
		add(errorMsg,0);

		repaint();
	}

	public void results(Player[] players) {//戦績表示　引数にplayer型の配列
		//スクロールバー(表示する条件も分岐できれば尚良し)の追加が必要
		BufferedImage menuBgImage=null;

		try {
			menuBgImage=ImageIO.read(new File("./menubg.png"));
		}catch(Exception e) {
			e.printStackTrace();
			menuBgImage=null;
		}

		JLabel menuBg = new JLabel(new ImageIcon(menuBgImage));

		JPanel resultArea=new JPanel();
		//SpringLayout layout1 = new SpringLayout();
	    //resultArea.setLayout(null);

		JLabel[] result=new JLabel[players.length];
		JScrollPane scrollpane = new JScrollPane(resultArea);
		resultArea.setPreferredSize(new Dimension(100, 100));




		removeAll();
		background();
		backToMenu();


		menuBg.setBounds(0,125,1500,750);

		/*resultArea.setBounds(375,125,750,750);
		resultArea.setBackground(Color.BLUE);*/



		/*result[0]=new JLabel("対戦相手: "+"aiueo"+" | "+"WIN");
		result[1]=new JLabel("対戦相手: "+"aiueo"+" | "+"LOSE");*/

		/*result[0].setHorizontalAlignment(JLabel.CENTER);
		result[0].setPreferredSize(new Dimension(500,30));
		//result[0].setBounds(0,200,500,50);
		result[0].setFont(new Font("MS Gothic",Font.PLAIN,25));
		result[0].setForeground(Color.WHITE);

		result[1].setHorizontalAlignment(JLabel.CENTER);
		result[1].setPreferredSize(new Dimension(500,30));
		//result[1].setBounds(0,300,500,50);
		result[1].setFont(new Font("MS Gothic",Font.PLAIN,25));
		result[1].setForeground(Color.WHITE);*/


		for(int i=0;i<10;i++){
			//if(i>0) {
	        System.out.println(i);
	        result[i]=new JLabel("対戦相手: "+"aiueo"+" | "+"WIN");
	        result[i].setHorizontalAlignment(JLabel.CENTER);
			//result[0].setPreferredSize(new Dimension(500,30));
			//result[0].setBounds(0,200,500,50);
			result[i].setFont(new Font("MS Gothic",Font.PLAIN,25));
			result[i].setForeground(Color.WHITE);
	        result[i].setBounds(50,300+(100*i),500,50);
			//}

			//resultArea.setPreferredSize(new Dimension(720,61*(i+1)));//Task数に合わせてmainPanelのサイズを変更する
			resultArea.add(result[i]);
		}

		resultArea.add(scrollpane);

		/*resultArea.add(result[0]);
		resultArea.add(result[1]);*/

		//add(menuBg,0);

		add(resultArea,0);

		repaint();

	}

	public void backToMenu() {
		JButton backToMain=new JButton("Back to Menu");

		backToMain.setBounds(50,900,400,50);
		backToMain.setFont(f);
		backToMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//login2_button_click_event
				menuScreen();
				repaint();

				client.receiveHandler(0);//データ要求キャンセル

			}
		});

		add(backToMain,0);
	}


}
