import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private static String inputServer;

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
            Socket finalSocket = socket;
            ServerSocket finalServer = server;
            new Thread(() -> {
                while (true) {
                    try {
                        Scanner serverScanner = new Scanner(System.in);
                        inputServer = serverScanner.nextLine();
                        if (inputServer.equals("/exit")) {
                            fromServerStream.writeUTF("Сервер завершил работу");
                            System.out.println("Завершение программы");
                            finalSocket.close();
                            finalServer.close();
                            System.exit(0);
                        }else{
                            fromServerStream.writeUTF(inputServer);
                        }
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

                        if (clientMessage.equalsIgnoreCase("Клиент отключился")){
                            System.out.println("Клиент отключился. Сокеты закрыты. Сервер остановлен");
                            finalSocket.close();
                            finalServer.close();
                            System.exit(0);
                          } else {
                            System.out.println("Клиент: " + clientMessage);
                        }

                    } catch ( Exception e) {
                        e.printStackTrace();
                        System.out.println("Клиент отключился. Сеанс связи завершен"); //к сожалению, не придумал как более корректно обработать ситуацию, когда клиент просто вышел
                        System.exit(0);
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
