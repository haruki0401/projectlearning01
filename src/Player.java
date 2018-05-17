
public class Player {
	String playerID; //プレイヤID
	String win;
	String lose;
	String draw;


	String order; //先攻後攻情報(ここで使うかわからないのでとりあえず保留)

	Player(String str){
		String[] playerData=str.split(str.substring(0,1));
		playerID=playerData[1];
		win=playerData[2];
		draw=playerData[3];
		lose=playerData[4];

		System.out.println("player 情報を取得しました。");
		System.out.println(playerID+win+draw+lose);


	}

	String getID(){ //プレイヤIDの取得
		return playerID;
	}

	String getWin(){//対戦相手の勝利数取得（string）
		return win;
	}

	String getLose(){
		return lose;
	}

	String getDraw(){
		return draw;
	}

}
