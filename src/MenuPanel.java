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

public class MenuPanel extends JPanel{
	Client client;
	//Player my;

	MenuPanel(Client cl,String name){
		client=cl;
		this.setName(name);
		//this.my=my;
		//menu(this.my);

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
		add(bg);

	}

	public void menu(Player my) {//メニュ－画面
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
		//msg1.setForeground(Color.RED);

		playerResult.setHorizontalAlignment(JLabel.CENTER);
		playerResult.setBounds(500,100,500,50);
		playerResult.setFont(new Font("MS Gothic",Font.PLAIN,50));
		//msg1.setForeground(Color.RED);

		play.setBounds(550,200,400,100);
		play.setFont(new Font("MS Gothic",Font.PLAIN,50));
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//_click_event
				//
			}
		});

		search.setBounds(550,400,400,100);
		search.setFont(new Font("MS Gothic",Font.PLAIN,50));
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//_click_event
				//
			}
		});

		signout.setBounds(550,600,400,100);
		signout.setFont(new Font("MS Gothic",Font.PLAIN,50));
		signout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//_click_event
				//
			}
		});

		add(playerID);
		add(playerResult);
		add(play,0);
		add(search,0);
		add(signout,0);


		repaint();

		System.out.println("!");
	}

	public void results(Player[] players) {//戦績表示　引数にplayer型の配列
		//スクロールバー(表示する条件も分岐できれば尚良し)の追加が必要
	}


}
