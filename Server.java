/**
 * Created by Dartyx on 12/5/2016.
 */
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Server implements Runnable{

    //private static final int sPort = 8001;   //The server will be listening on this port number

//    public static void main(String[] args) throws Exception {
//        System.out.println("The server is running.");
//        ServerSocket listener = new ServerSocket(sPort);
//        int clientNum = 1;
//        try {
//            while(true) {
//                new Handler(listener.accept(),clientNum).start();
//                System.out.println("Client "  + clientNum + " is connected!");
//                clientNum++;
//            }
//        } finally {
//            listener.close();
//        }
//
//    }
    private PeerProcess peer;
    private int peerID;
    private int sPort;
    private Thread t;
    public Server(PeerProcess peer){
        this.peer = peer;
        this.peerID = peer.getPeerID();
        sPort=peer.getPortID();
    }
    public void start(){
        System.out.println("starting server "+sPort);
        if(t==null){
            t = new Thread(this, "server");
            t.start();
        }
    }
    public void run(){
        try {
            System.out.println("The server is running on port "+sPort);
            ServerSocket listener = new ServerSocket(sPort);
            int clientNum = 1;
            try {
                while (true) {
                    new Handler(peer,listener.accept(), clientNum).start();
                    System.out.println("Client " + clientNum + " is connected!");
                    clientNum++;
                }
            } finally {
                listener.close();
            }
        }
        catch (Exception e){
            System.out.println("Server is messing up");
        }




    }
    /**
     * A handler thread class.  Handlers are spawned from the listening
     * loop and are responsible for dealing with a single client's requests.
     */
    private static class Handler extends Thread {
        private String message;    //message received from the client
        private String MESSAGE;    //uppercase message send to the client
        //private int
        private Socket connection;
        private ObjectInputStream in;	//stream read from the socket
        private ObjectOutputStream out;    //stream write to the socket
        private int no;		//The index number of the client
        private PeerProcess peer;
        private int peerID;
        public Handler(PeerProcess peer ,Socket connection, int no) {
            this.connection = connection;
            this.no = no;
            this.peer = peer;
            this.peerID=peer.getPeerID();
        }

        public void run() {
            try{
                //initialize Input and Output streams
                out = new ObjectOutputStream(connection.getOutputStream());
                out.flush();
                in = new ObjectInputStream(connection.getInputStream());
                try{
                    while(true)
                    {
                        //receive the message sent from the client
                        Message message = (Message)in.readObject();
                        //show the message to the user
                        System.out.println("got to message");
                        switch(message.getType()){
                            case -1:
                                handshake((HandShakeMessage)message);
                                break;
                            case 0:
                                System.out.println("somehow got a choke in the server");
                                break;
                            case 1:
                                System.out.println("somehow got a choke in the server");
                                break;
                            case 2:
                                interested((InterestedMessage)message);
                                break;
                            case 3:
                                System.out.println("somehow got a not interested in the server");
                                break;
                            case 4:
                                System.out.println("somehow got a have in the server");
                                break;
                            case 5:
                                bitfield((BitFieldMessage)message);
                                break;
                            case 6:
                                request((RequestMessage)message);
                                break;
                            case 7:
                                System.out.println("somehow got a piece in the server");
                                break;
                        }
                        //System.out.println("Receive message: " + message + " from client " + no);
                        //Capitalize all letters in the message
                        //MESSAGE  = message.toUpperCase();
                        //send MESSAGE back to the client
                        //sendMessage(MESSAGE);
                    }
                }
                catch(ClassNotFoundException classnot){
                    System.err.println("Data received in unknown format");
                }
            }
            catch(IOException ioException){
                System.out.println("Disconnect with Client " + no);
            }
            finally{
                //Close connections
                try{
                    in.close();
                    out.close();
                    connection.close();
                }
                catch(IOException ioException){
                    System.out.println("Disconnect with Client " + no);
                }
            }
        }
        public void handshake(HandShakeMessage handshake){
            //System.out.println("recieved handshake from "+handshake.getPeerID());
            Message response = new HandShakeMessage(peerID);
            try{
                out.writeObject(response);
                out.flush();
                //System.out.println("Send message: " + msg + " to Client " + no);
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }

        }
        public void choke(){}
        public void unchoke(){}
        public void interested(InterestedMessage message){



        }
        public void notInterested(){}
        public void have(){}
        public void bitfield(BitFieldMessage message){
            Message response = new BitFieldMessage(peer.getBitfield());
            try{
                out.writeObject(response);
                out.flush();
                //System.out.println("Send message: " + msg + " to Client " + no);
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
        public void request(RequestMessage message){}
        public void piece(){}
        //send a message to the output stream
        public void sendMessage(String msg)
        {
            try{
                out.writeObject(msg);
                out.flush();
                System.out.println("Send message: " + msg + " to Client " + no);
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }

    }

}

