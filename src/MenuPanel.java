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

		scrollpane.setBounds(50,200,1500-105,675);
		scrollpane.setBorder(null);//枠線消す



		removeAll();
		background();
		backToMenu();



		resultArea.setBackground(Color.GRAY);


		resultArea.setPreferredSize(new  Dimension(750,25));//Task数に合わせてmainPanelのサイズを変更する


		historyColumn1=new JLabel("playerID");
		historyColumn1.setHorizontalAlignment(JLabel.CENTER);
		historyColumn1.setFont(new Font("MS Gothic",Font.PLAIN,40));
		historyColumn1.setBounds(0,0,700,50);
		historyColumn1.setForeground(Color.WHITE);
		historyColumn1.setBackground(Color.BLACK);
        historyColumn1.setOpaque(true);

		historyColumn2=new JLabel("勝敗");
		historyColumn2.setHorizontalAlignment(JLabel.CENTER);
		historyColumn2.setFont(new Font("MS Gothic",Font.PLAIN,40));
		historyColumn2.setBounds(700,0,800-105,50);
		historyColumn2.setForeground(Color.WHITE);
		historyColumn2.setBackground(Color.RED);
        historyColumn2.setOpaque(true);

		resultArea.add(historyColumn1);
		resultArea.add(historyColumn2);



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
	        historyID[i].setBounds(0,0+(50*(i+1)),700,50);
	        historyID[i].setForeground(Color.WHITE);
	        historyID[i].setBackground(Color.GRAY);
	        historyID[i].setOpaque(true);


	        historyResult[i]=new JLabel(str);
	        historyResult[i].setHorizontalAlignment(JLabel.CENTER);
	        historyResult[i].setFont(new Font("MS Gothic",Font.PLAIN,40));
	        historyResult[i].setBounds(700,0+(50*(i+1)),800-105,50);
	        historyResult[i].setForeground(Color.WHITE);
	        historyResult[i].setBackground(Color.PINK);
	        historyResult[i].setOpaque(true);

			resultArea.setPreferredSize(new  Dimension(750,25*(i+2)));//Task数に合わせてmainPanelのサイズを変更する
			resultArea.add(historyID[i]);
			resultArea.add(historyResult[i]);

		}


		add(playerID,0);
		add(playerResult,0);
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
