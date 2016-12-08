/**
 * Created by Dartyx on 12/5/2016.
 */
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Client implements Runnable {
    Socket requestSocket[];           //socket connect to the server
    ObjectOutputStream out[];         //stream write to the socket
    ObjectInputStream in[];          //stream read from the socket
    BufferedReader bufferedReader[];
    private int number;
    String message;                //message send to the server
    String MESSAGE;                //capitalized message read from the server
    private PeerProcess peer;
    private int peerID;
    private Thread t;
    public Client(PeerProcess peer) {
        number = peer.getNumber();
        this.peerID=peer.getPeerID();
        requestSocket = new Socket[number];
        out = new ObjectOutputStream[number];
        in = new ObjectInputStream[number];
        //bufferedReader = new BufferedReader[number];
        this.peer=peer;
        this.number=number-1;
        //System.out.println("number is "+number);
    }
    public void start(){
        //System.out.println("starting client "+peer.getPeerID());
        if(t==null){
            t = new Thread(this, "server");
            t.start();
        }
    }
    public void run()
    {
        try{
            //create a socket to connect to the server
            for(int i=0;i<number;++i) {
                new Handler(peer,8000+i).start();
            }
        }
        catch (Exception e) {
            System.err.println("Client messed up with handler");
        }

    }
    private static class Handler extends Thread {
        private List<Integer> interested = new ArrayList<Integer>();
        private Socket socket;
        private ObjectInputStream in;    //stream read from the socket
        private ObjectOutputStream out;    //stream write to the socket
        private int portToTalkTo;        //The index number of the client
        private PeerProcess peer;
        private int peerID;
        private boolean bitfield[];
        private boolean isChoked;
        public Handler(PeerProcess peer,int portToTalkTo) {
            isChoked=true;
            this.peer = peer;
            this.peerID = peer.getPeerID();
            this.portToTalkTo=portToTalkTo;
            this.bitfield=peer.getBitfield();
        }
        public void run() {
            try{

                socket = new Socket("localhost", portToTalkTo);
                System.out.println("Connected to localhost in port "+ portToTalkTo);
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());
                sendHandshake();
                sendBitfield();
                while(!peer.isAllDone()){
                    while(!interested.isEmpty()){
                        //loop to resend
                        interestedLoop();//send int, if get back choke, keep sending int, if get have, takeOffMarket and go to next
                        //sendRequest();//request the piece and get it and fill it in
                            //send request
                            //take interested
                            //if choke, nothing
                            //if have, send request
                            //accept piece
                    }
                //start the loop of sending for interested, and either requesting or sending agian
                //send interested from array
                //if receive choke, then resend
                //if recieve unchoke,
                //send
                }
                System.out.println("no more interested pieces in port "+portToTalkTo);
            }
            catch (ConnectException e) {
                System.err.println("Connection refused. You need to initiate a server first.");
            }
            catch(UnknownHostException unknownHost){
                System.err.println("You are trying to connect to an unknown host!");
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
            finally{
                //Close connections
                try{

                        in.close();
                        out.close();
                        socket.close();

                }
                catch(IOException ioException){
                    ioException.printStackTrace();
                }
            }
        }
        public void recheckInterested(){
            boolean check[] = peer.getBitfield();
            for(int i = interested.size();i>=0;--i){
                if(check[interested.get(i)]==true)interested.remove(i);
            }

        }
        public void interestedLoop(){
            int index = interested.get(0);
            int count=0;
            do {
                if(count==2){
                    count=0;
                    recheckInterested();
                    index = interested.get(0);
                }
                sendInterested(index);


                count++;
            }while(isChoked);
        }
        public void sendInterested(int index){
            try{
                Message interest = new InterestedMessage(index);
                sendMessage(interest);
                Message response = (Message)in.readObject();
                if(response.getType()==0){
                    isChoked=true;
                    System.out.println("is choked from port "+portToTalkTo);
                }
                else if(response.getType()==1){
                    isChoked=false;
                    System.out.println("is unchoked from port "+portToTalkTo);
                    if(interested.get(0)==index) {
                        sendMessage(interest);
                        response = (Message) in.readObject();
                        if(response.getType()==4){
                            takeResponsibility(index);
                            sendRequest(index);
                            Message piece = (Message) in.readObject();
                            if(piece.getType()==7){
                                getPiece(((PieceMessage)piece));
                            }
                        }
                    }
                }
                else if(response.getType()==4){
                    if(interested.get(0)==index){
                        takeResponsibility(index);
                        sendRequest(index);
                        Message piece = (Message) in.readObject();
                        if(piece.getType()==7){
                            getPiece(((PieceMessage)piece));
                        }
                    }
                }
            }
            catch ( ClassNotFoundException e ) {
                System.err.println("Class not found");
            }
            catch ( Exception e ) {
                System.err.println("Instream thingy");
            }

        }
        public void getPiece(PieceMessage message){
            peer.setPiece(message.getIndex(),message.getPiece());
        }
        public void takeResponsibility(int index){
            peer.setBitfieldByIndex(index);
        }
        public void sendRequest(int index){
            try{
                Message handshake = new HandShakeMessage(peerID);
                sendMessage(handshake);
                Message response = (HandShakeMessage)in.readObject();
            }
            catch ( ClassNotFoundException e ) {
                System.err.println("Class not found");
            }
            catch ( Exception e ) {
                System.err.println("Instream thingy");
            }



        }
        public void sendHandshake(){
            try{

                //read a sentence from the standard input
                //message = "Client "+peer.getPortID() + "connecting to "+(1000+i);//bufferedReader.readLine();
                Message handshake = new HandShakeMessage(peerID);
                //System.out.print(handshake);
                //Send the sentence to the server
                //System.out.println("sending handshake from peerID "+peerID+" to port "+(8000+i));
                sendMessage(handshake);
                //Receive the upperCase sentence from the server
                Message response = (HandShakeMessage)in.readObject();
                //show the message to the user
                //System.out.println("Received handshake from peerID "+((HandShakeMessage)response).getPeerID()+" from port "+(8000+((HandShakeMessage)response).getPeerID()%1000-1) );

            }
            catch ( ClassNotFoundException e ) {
                System.err.println("Class not found");
            }
            catch ( Exception e ) {
                System.err.println("Instream thingy");
            }

        }
        public void sendBitfield(){
            try{

                //read a sentence from the standard input
                //message = "Client "+peer.getPortID() + "connecting to "+(1000+i);//bufferedReader.readLine();
                Message handshake = new BitFieldMessage(peer.getBitfield());
                //System.out.print(handshake);
                //Send the sentence to the server
                //System.out.println("sending handshake from peerID "+peerID+" to port "+(8000+i));
                sendMessage(handshake);
                //Receive the upperCase sentence from the server
                BitFieldMessage response = (BitFieldMessage)in.readObject();
                updateBitfield(response.getBitfield());
                //show the message to the user
                //System.out.println("Received handshake from peerID "+((HandShakeMessage)response).getPeerID()+" from port "+(8000+((HandShakeMessage)response).getPeerID()%1000-1) );

            }
            catch ( ClassNotFoundException e ) {
                System.err.println("Class not found");
            }
            catch ( Exception e ) {
                System.err.println("Instream thingy");
            }

        }
        public void updateBitfield(boolean other[]){
            boolean ours[] = peer.getBitfield();
            System.out.println("updating bitfield");
            for(int i=0;i<other.length;++i){
                if(ours[i]==false && other[i]==true)interested.add(i);
            }
        }
        public void updateInterested(){
            boolean other[] = peer.getBitfield();
            for(int i = interested.size();i>=0;--i){
                if(other[interested.get(i)]==true)interested.remove(i);
            }
        }
        public void sendMessage(Message msg)
        {
            try{
                //stream write the message
                out.writeObject(msg);
                out.flush();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
}

