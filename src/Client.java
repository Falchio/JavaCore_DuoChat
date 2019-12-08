import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void  main(String[] args){
            final String IP_ADDRESS = "localhost";
            final int PORT = 1984;
        DataInputStream in;
        DataOutputStream out;
        Socket socket;

        try {
            socket = new Socket(IP_ADDRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());



            new Thread(() -> {
                try {
                    while (true) {
                        Scanner clientScanner = new Scanner(System.in);
//                        if (clientScanner.nextLine().equals("/exit")){
//                            System.out.println("Выход из программы");
//                            socket.close();
//                            System.exit(0);
//                        }
                        String  clientMessage = clientScanner.nextLine();
                        out.writeUTF(clientMessage);
                     }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    while (true) {
                       String serverMessage = in.readUTF();
                       System.out.println("Сервер: " + serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
