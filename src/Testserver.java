import java.net.ServerSocket;
import java.net.Socket;

class Testserver {
        public static void main(String[] args) {
                try {
                        // ポートを取得
                        int port = Integer.parseInt(args[0]);

                        // サーバーソケットを作成
                        ServerSocket ss = new ServerSocket(port);

                        // 無限ループ
                        while (true) {
                                // クライアントからの要求を受け取る
                                Socket s = ss.accept();

                                // 結果をクライアントに書き込む
                                /*OutputStream os = s.getOutputStream();
                                DataOutputStream dos = new DataOutputStream(os);
                                dos.writeInt(random.nextInt());*/

                                // ソケットをクローズ
                                s.close();
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
}
