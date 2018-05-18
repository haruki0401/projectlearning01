
public class Player {
	String playerID; //プレイヤID
	String win;
	String lose;
	String draw;
	String giveUp;


	String order; //先攻後攻情報(ここで使うかわからないのでとりあえず保留)

	Player(String str){
		String[] playerData=str.split("/");
		playerID=playerData[0];
		win=playerData[1];
		draw=playerData[2];
		lose=playerData[3];
		giveUp=playerData[4];

		System.out.println("player 情報を取得しました。");
		System.out.println(playerID+win+draw+lose+giveUp);


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

	String getGiveUp(){
		return giveUp;
	}

}
