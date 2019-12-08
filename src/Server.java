import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {

        ServerSocket server = null;
        Socket socket = null;

        DataInputStream clientStream;
        DataOutputStream fromServerStream;


        try {
            server = new ServerSocket(1984);
            System.out.println("Server is running");
            socket = server.accept();
            System.out.println("Client connected");

            clientStream = new DataInputStream(socket.getInputStream());
            fromServerStream = new DataOutputStream(socket.getOutputStream());

            //пишем от имени сервера
            new Thread(() -> {
                while (true) {

                    try {
                        Scanner serverScanner = new Scanner(System.in);
                        String inputServer = serverScanner.nextLine();
                        if (inputServer.equals("/exit")) {
                            System.out.println("Exit");
                            System.exit(0);
                        }
                        fromServerStream.writeUTF(inputServer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

            //принимаем то, что написал клиент
            new Thread(() -> {

                while (true) {

                    try {
                        String clientMessage = clientStream.readUTF();
                        System.out.println("Клиент: " + clientMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
