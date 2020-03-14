import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public abstract class T_C_P {
    public Socket socket;
    public Scanner scanner;
    public String input;

    //connects to the listening socket of the server
    public T_C_P(String serverAddress, int serverPort) throws Exception {
        this.socket = new Socket(serverAddress, serverPort);

    }

    //sends message
    public void sendMessage() throws IOException {import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public abstract class T_C_P {
    public Socket socket;
    private Scanner scanner;
    private String input;
    public String content;

    //connects to the listening socket of the server
    public T_C_P(String serverAddress, int serverPort) throws Exception {
        try{
        this.socket = new Socket(serverAddress, serverPort);}
        catch (Exception e){System.out.println(e);}

    }

    //sends message
    public void sendMessage(String message, TextArea view) throws IOException {
        try {
            content=view.getText()+"\n"+"Me:\n"+message;

            view.setText(content);

                input = message;
                PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
                out.println(input);
                out.flush();

        } catch (Exception e) {
            System.out.println(e);
            socket.close();
            System.out.println("Server Closed");
            return;
        }
    }

    //waits for message from the server
    public abstract void receiveMessage(TextArea view) throws IOException;
}

        try {
            this.scanner = new Scanner(System.in);
            while (true) {
                input = scanner.nextLine();
                PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
                out.println(input);
                out.flush();
            }
        } catch (Exception e) {
            System.out.println(e);
            socket.close();
            System.out.println("Server Closed");
            return;
        }
    }

    //waits for message from the server
    public abstract void recieveMessage() throws IOException;
}
