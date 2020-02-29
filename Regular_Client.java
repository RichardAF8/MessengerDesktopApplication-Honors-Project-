import java.io.*;
import java.util.Scanner;

public class Regular_Client extends T_C_P {

    public Regular_Client(String serverAddress, int serverPort) throws Exception {
        super(serverAddress, serverPort);
    }



    //implements abstract method recieveMessage
    public void recieveMessage() throws IOException {
        while (true) {
            try {
                //InputStream, represents an ordered stream of bytes.
                //In other words, you can read data from a Java InputStream as an ordered sequence of bytes.
                InputStream rec = this.socket.getInputStream();
                // thereby turning the byte based InputStream into a character based Reader. In other words,
                //the Java InputStreamReader interprets the bytes of an InputStream as text instead of numerical data.
                InputStreamReader reader = new InputStreamReader(rec);
                int character;
//The String object is immutable. Every time you use one of the methods in the System.String class,
                //you create a new string object in memory,
                //The System.Text.StringBuilder class can be used when you want to modify a string without creating a new object.
                StringBuilder data = new StringBuilder();
                // this was -1 before...no idea
                //read return char as int which contains the char value of the char read.
                while ((character = reader.read()) != 0) {
                    //converts the char array message sent by server to a string
                    //cast character as char..converting the int byte equvialent to a readable char
                    data.append((char) character);
                }

                System.out.print(data);

            } catch (Exception e) {
                System.out.println(e);
                socket.close();
                System.out.println("Server Closed");
                return;
            }
        }

    }
}