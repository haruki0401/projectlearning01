import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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


		JLabel bg = new JLabel();

		bg.setOpaque(true);
		bg.setBounds(0,0,1500,1000);
		bg.setBackground(new Color(34,139,34));

		add(bg,0);

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

	public void menuScreen() {//メニュ－画面
		JButton play=new JButton("対局する");
		JButton search=new JButton("player検索");
		JButton signout=new JButton("Sign Out");
		JLabel playerID=new JLabel("ID: "+my.getID()+" さん");
		JLabel playerResult=new JLabel("勝: "+my.getWin()+" 負: "+my.getLose()+" 分: "+my.getDraw()+" 投了: "+my.getGiveUp());


		removeAll();

		background();

		playerID.setHorizontalAlignment(JLabel.CENTER);
		playerID.setBounds(0,50,1500,50);
		playerID.setFont(new Font("MS Gothic",Font.PLAIN,50));
		playerID.setForeground(Color.YELLOW);

		playerResult.setHorizontalAlignment(JLabel.CENTER);
		playerResult.setBounds(0,100,1500,50);
		playerResult.setFont(new Font("MS Gothic",Font.PLAIN,50));
		playerResult.setForeground(Color.YELLOW);

		play.setBounds(550,300,400,100);
		play.setFont(new Font("MS Gothic",Font.PLAIN,50));
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//_click_event
				client.sendMessage("3");//サーバにメッセージ送信

				client.receiveHandler(1);
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


		JLabel menuBg = new JLabel();
		JLabel msg=new JLabel("検索したいplayerIDを入力してください。");
		JTextField searchID=new JTextField(16);
		JButton search=new JButton("検索");
		JLabel errorMsg=new JLabel("");



		removeAll();

		background();
		backToMenu();

		menuBg.setOpaque(true);
		menuBg.setBounds(0,125,1500,750);
		menuBg.setBackground(Color.BLACK);

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
					/*Player[] p=new Player[50];

					for(int i=0;i<50;i++) {
						p[i]=my;

					}

					results(p);*/

					//終わり

					client.sendMessage("2"+input_id);//サーバにメッセージ送信

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

	public void results(Player player,String[] s) {//戦績表示　引数にplayer型の配列
		//スクロールバー(表示する条件も分岐できれば尚良し)の追加が必要

		JLabel menuBg = new JLabel();

		menuBg.setOpaque(true);
		menuBg.setBounds(0,125,1500,750);
		menuBg.setBackground(Color.BLACK);

		JLabel playerID=new JLabel("ID: "+my.getID()+" さん");

		playerID.setHorizontalAlignment(JLabel.CENTER);
		playerID.setBounds(0,50,1500,50);
		playerID.setFont(new Font("MS Gothic",Font.PLAIN,50));
		playerID.setForeground(Color.YELLOW);

		JLabel playerResult=new JLabel("勝: "+player.getWin()
			+" 負: "+player.getLose()+" 分: "+player.getDraw()
			+" 投了: "+player.getGiveUp());

		playerResult.setHorizontalAlignment(JLabel.CENTER);
		playerResult.setBounds(0,100,1500,50);
		playerResult.setFont(new Font("MS Gothic",Font.PLAIN,50));
		playerResult.setForeground(Color.YELLOW);

		JPanel resultArea=new JPanel();
	    resultArea.setLayout(null);

		JLabel historyColumn1;
		JLabel historyColumn2;

		JLabel[] historyID=new JLabel[s.length];
		JLabel[] historyResult=new JLabel[s.length];

		JScrollPane scrollpane = new JScrollPane(resultArea);
		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpane.getVerticalScrollBar().setUnitIncrement(25);//スクローススピード

		scrollpane.setBounds(50,250,1500-105,625);
		scrollpane.setBorder(null);//枠線消す



		removeAll();
		background();
		backToMenu();



		resultArea.setBackground(Color.GRAY);
		resultArea.setPreferredSize(new  Dimension(750,50*(s.length)+1));//Task数に合わせてmainPanelのサイズを変更する



		//resultArea.setPreferredSize(new  Dimension(750,25));


		historyColumn1=new JLabel("対戦相手のID");
		historyColumn1.setHorizontalAlignment(JLabel.CENTER);
		historyColumn1.setFont(new Font("MS Gothic",Font.PLAIN,40));
		historyColumn1.setBounds(50,200,700,50);
		historyColumn1.setForeground(Color.BLACK);
		historyColumn1.setBackground(Color.BLUE);
        historyColumn1.setOpaque(true);

		historyColumn2=new JLabel("勝敗");
		historyColumn2.setHorizontalAlignment(JLabel.CENTER);
		historyColumn2.setFont(new Font("MS Gothic",Font.PLAIN,40));
		historyColumn2.setBounds(750,200,700-5,50);
		historyColumn2.setForeground(Color.BLACK);
		historyColumn2.setBackground(Color.RED);
        historyColumn2.setOpaque(true);





		for(int i=0;i<s.length;i++){
	        System.out.println(i);

	        String name=s[i].split("/")[0];
	        String r=s[i].split("/")[1];
	        String str=null;

	        //win=00,lose=01,draw=10,give up=11
	        if(r.equals("00")) {
	        	str="勝利";
	        }
	        else if(r.equals("01")) {
	        	str="敗北";
	        }
	        else if(r.equals("10")) {
	        	str="引き分け";
	        }
	        else if(r.equals("11")) {
	        	str="投了";
	        }


	        historyID[i]=new JLabel(name);
	        historyID[i].setHorizontalAlignment(JLabel.CENTER);
	        historyID[i].setFont(new Font("MS Gothic",Font.PLAIN,40));
	        historyID[i].setBounds(0,0+(50*(i)),700,50);
	        historyID[i].setForeground(Color.BLACK);

	        if((i%2)==0) {
	        	historyID[i].setBackground(new Color(0,204,255));
	        }else {
	        	historyID[i].setBackground(new Color(204,255,255));
	        }

	        historyID[i].setOpaque(true);


	        historyResult[i]=new JLabel(str);
	        historyResult[i].setHorizontalAlignment(JLabel.CENTER);
	        historyResult[i].setFont(new Font("MS Gothic",Font.PLAIN,40));
	        historyResult[i].setBounds(700,0+(50*(i)),800-105,50);
	        historyResult[i].setForeground(Color.BLACK);

	        if((i%2)==0) {
		        historyResult[i].setBackground(new Color(255,153,204));
	        }else {
		        historyResult[i].setBackground(Color.PINK);
	        }

	        historyResult[i].setOpaque(true);

			resultArea.add(historyID[i]);
			resultArea.add(historyResult[i]);

		}


		add(playerID,0);
		add(playerResult,0);
		add(historyColumn1,0);
		add(historyColumn2,0);
		add(scrollpane,0);

		revalidate();

		repaint();

	}

	public void searchError() {
		removeAll();
		background();
		backToMenu();

		JLabel errorMsg=new JLabel("検索されたIDのplayerは存在しません。");


		errorMsg.setHorizontalAlignment(JLabel.CENTER);
		errorMsg.setBounds(500,200,500,50);
		errorMsg.setFont(new Font("MS Gothic",Font.PLAIN,25));
		errorMsg.setForeground(Color.RED);

		add(errorMsg,0);

		repaint();
	}


	public void playMain(Player[] onlinePlayers) {//オンラインプレイヤーの表示

		removeAll();
		background();
		backToMenu();

		client.receiveHandler(1);//この画面では、常にオファーを受信するため


		JLabel title=new JLabel("現在オンラインのプレイヤー");

		title.setHorizontalAlignment(JLabel.CENTER);
		title.setBounds(0,50,1500,50);
		title.setFont(new Font("MS Gothic",Font.PLAIN,50));
		title.setForeground(Color.BLACK);

		JLabel titleMsg=new JLabel("対戦を申し込みたいplayerIDをクリックしてください。");

		titleMsg.setHorizontalAlignment(JLabel.CENTER);
		titleMsg.setBounds(0,100,1500,50);
		titleMsg.setFont(new Font("MS Gothic",Font.PLAIN,30));
		titleMsg.setForeground(Color.WHITE);

		JButton offer=new JButton();//offer来た時に表示されるオファー承認画面への遷移ボタン

		JPanel playerArea=new JPanel();
		playerArea.setLayout(null);

		JButton[] onlineID=new JButton[onlinePlayers.length];
		JLabel[] onlineResult=new JLabel[onlinePlayers.length];

		JLabel onlineColumn1;
		JLabel onlineColumn2;

		JScrollPane scrollpane = new JScrollPane(playerArea);
		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpane.getVerticalScrollBar().setUnitIncrement(25);//スクローススピード

		scrollpane.setBounds(50,250,1500-105,625);
		scrollpane.setBorder(null);//枠線消す



		playerArea.setBackground(Color.GRAY);
		playerArea.setPreferredSize(new  Dimension(750,50*(onlinePlayers.length)+1));//Task数に合わせてmainPanelのサイズを変更する


		onlineColumn1=new JLabel("対戦相手のID");
		onlineColumn1.setHorizontalAlignment(JLabel.CENTER);
		onlineColumn1.setFont(new Font("MS Gothic",Font.PLAIN,40));
		onlineColumn1.setBounds(50,200,700,50);
		onlineColumn1.setForeground(Color.BLACK);
		onlineColumn1.setBackground(Color.BLUE);
		onlineColumn1.setOpaque(true);

		onlineColumn2=new JLabel("勝敗");
		onlineColumn2.setHorizontalAlignment(JLabel.CENTER);
		onlineColumn2.setFont(new Font("MS Gothic",Font.PLAIN,40));
		onlineColumn2.setBounds(750,200,700-5,50);
		onlineColumn2.setForeground(Color.BLACK);
		onlineColumn2.setBackground(Color.RED);
		onlineColumn2.setOpaque(true);



		for(int i=0;i<onlinePlayers.length;i++){
	        System.out.println(i);

	        onlineID[i]=new JButton(onlinePlayers[i].getID());
	        onlineID[i].setHorizontalAlignment(JLabel.CENTER);
	        onlineID[i].setFont(new Font("MS Gothic",Font.PLAIN,40));
	        onlineID[i].setBounds(0,0+(50*(i)),700,50);
	        onlineID[i].setForeground(Color.BLACK);

	        if((i%2)==0) {
	        	onlineID[i].setBackground(new Color(0,204,255));
	        }else {
	        	onlineID[i].setBackground(new Color(204,255,255));
	        }

	        onlineID[i].setOpaque(true);


	        onlineResult[i]=new JLabel("勝: "+onlinePlayers[i].getWin()+" 負: "+onlinePlayers[i].getLose()+" 分: "+onlinePlayers[i].getDraw()+" 投了: "+onlinePlayers[i].getGiveUp());
	        onlineResult[i].setHorizontalAlignment(JLabel.CENTER);
	        onlineResult[i].setFont(new Font("MS Gothic",Font.PLAIN,40));
	        onlineResult[i].setBounds(700,0+(50*(i)),800-105,50);
	        onlineResult[i].setForeground(Color.BLACK);

	        if((i%2)==0) {
	        	onlineResult[i].setBackground(new Color(255,153,204));
	        }else {
	        	onlineResult[i].setBackground(Color.PINK);
	        }

	        onlineResult[i].setOpaque(true);

	        //playerArea.setPreferredSize(new  Dimension(750,25*(i+2)));//Task数に合わせてmainPanelのサイズを変更する
	        playerArea.add(onlineID[i]);
	        playerArea.add(onlineResult[i]);

		}

		add(title,0);
		add(titleMsg,0);
		add(onlineColumn1,0);
		add(onlineColumn2,0);
		add(scrollpane,0);

		revalidate();

		repaint();

	}




}
