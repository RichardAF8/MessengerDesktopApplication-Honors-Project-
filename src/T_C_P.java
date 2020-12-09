import javafx.scene.control.TextArea;
import net.didion.jwnl.data.Exc;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

//Abstract class for TCP client
public abstract class T_C_P {
    private Socket socket;
    private String content;
    private ReentrantLock lock = new ReentrantLock();
    private BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);


    // connects to the listening socket of the server
    public T_C_P(String serverAddress, int serverPort) throws Exception {
        try {
            this.socket = new Socket(serverAddress, serverPort);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // sends message
    public void sendMessage(String message, TextArea view) throws IOException {
        try {
            // appends TextField

            updateTextField("Me:\n"+message, view);

            // Sends message to the server
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(message);
            out.flush();

        } catch (Exception e) {
            System.out.println(e);
            socket.close();
            System.out.println("Server Closed");
            return;
        }
    }

    public void updateTextField(String message, TextArea view) {

        // prevents concurrent string modification while updating TextField on the G.U.I
        lock.lock();
        try{
        content = view.getText() + "\n" + message;
        view.setText(content);}
        finally {
            lock.unlock();
        }

    }

    public void addToQueue(String item) {
        try {
            queue.put(item);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String removeFromQueue() {
        try {
            return queue.take();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Message cannot be loaded";
    }

    public InputStream getInputStream() throws IOException {
        return this.socket.getInputStream();
    }

    public void closeSocket() throws IOException {
        socket.close();
    }

    public InetAddress getInetAddress() {
        return socket.getInetAddress();
    }

    // waits for message from the server
    public abstract void receiveMessage(TextArea view) throws IOException;
}
