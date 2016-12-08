/**
 * Created by Dartyx on 12/5/2016.
 */
import java.net.*;
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class PeerProcess {

    public boolean isAllDone() {
        return AllDone;
    }

    private boolean AllDone;
    private int peerID;
    private int portID;
    private int number;
    private boolean[] bitfield;
    private List<Integer> requested = new ArrayList<Integer>();
    private byte[][] pieces;

    public byte[][] getPieces() {
        return pieces;
    }

    public void setPieces(byte[][] pieces) {
        this.pieces = pieces;
    }

    public byte[] getPiece(int index){
        return pieces[index];
    }
    public void setPiece(int index, byte[] piece){
        pieces[index]=piece;
    }
    public void setBitfieldByIndex(int index){
        requested.add(index);
        bitfield[index]=true;
    }
    public void removeFromRequested(int index){
        int temp = requested.indexOf(index);
        if(temp!=-1)requested.remove(temp);
    }

    public boolean[] getBitfield() {
        return bitfield;
    }

    public void setBitfield(boolean[] bitfield) {
        this.bitfield = bitfield;
    }

    public int getPeerID() {
        return peerID;
    }

    public void setPeerID(int peerID) {
        this.peerID = peerID;
    }

    public int getPortID() {
        return portID;
    }

    public void setPortID(int portID) {
        this.portID = portID;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getHasFile() {
        return hasFile;
    }

    public void setHasFile(int hasFile) {
        this.hasFile = hasFile;
    }

    public Socket getRequestSocket() {
        return requestSocket;
    }

    public void setRequestSocket(Socket requestSocket) {
        this.requestSocket = requestSocket;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String[] getInfo() {
        return Info;
    }

    public void setInfo(String[] info) {
        Info = info;
    }

    public int getNumPrefNeighbors() {
        return numPrefNeighbors;
    }

    public void setNumPrefNeighbors(int numPrefNeighbors) {
        this.numPrefNeighbors = numPrefNeighbors;
    }

    public int getUnchokeInt() {
        return unchokeInt;
    }

    public void setUnchokeInt(int unchokeInt) {
        this.unchokeInt = unchokeInt;
    }

    public int getOptimisticUnchokeInt() {
        return optimisticUnchokeInt;
    }

    public void setOptimisticUnchokeInt(int optimisticUnchokeInt) {
        this.optimisticUnchokeInt = optimisticUnchokeInt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getPieceSize() {
        return pieceSize;
    }

    public void setPieceSize(int pieceSize) {
        this.pieceSize = pieceSize;
    }

    public int getArraySize() {
        return arraySize;
    }

    public void setArraySize(int arraySize) {
        this.arraySize = arraySize;
    }

    public void setAllDone(boolean allDone) {
        AllDone = allDone;
    }



    private int hasFile;
    private Socket requestSocket;           //socket connect to the server
    private ObjectOutputStream out;         //stream write to the socket
    private ObjectInputStream in;          //stream read from the socket
    private String message;                //message send to the server
    private String MESSAGE;                //capitalized message read from the server
    private String Info[];
    private int numPrefNeighbors;
    private int unchokeInt;
    private int optimisticUnchokeInt;
    private String fileName;
    private int fileSize;
    private int pieceSize;
    private int arraySize;
    private Client client;
    private Server server;
    public PeerProcess(int peerID,int portID,int hasFile,int numPrefNeighbors,int unchokeInt, int optimisticUnchokeInt, int fileSize, int pieceSize, int arraySize, String fileName, String Info[]) throws FileNotFoundException{
        this.peerID=peerID;
        this.number=peerID%1000;
        this.portID=portID;
        this.hasFile=hasFile;
        this.numPrefNeighbors=numPrefNeighbors;
        this.unchokeInt=unchokeInt;
        this.optimisticUnchokeInt=optimisticUnchokeInt;
        this.fileSize=fileSize;
        this.pieceSize=pieceSize;
        this.arraySize=arraySize;
        this.fileName=fileName;
        this.Info=Info;
        this.bitfield=new boolean[arraySize];
        this.AllDone=false;
        //this.pieces;
        pieces = new byte[arraySize][];
        //here you need to load the file in
    }
    void run()
    {
        try{
            server = new Server(this);
            server.start();
            client = new Client(this);
            client.start();
            while(!AllDone){
                //figure out unchoke
                //figure out 
            }
        }
        catch (Exception e) {
            System.err.println("IDK what happened");
        }
    }


    public static void main(String args[]) throws Exception
    {
        int num=0,unch=0,opt=0,fSize=0,pSize=0,aSize=0,hasFile=0;
        String temp[] = new String[4];
        String Info[] = new String[4];
        String file="";
        if(args.length==0){
            System.out.println("you messed up");
            return;
        }
        int peerID = Integer.parseInt(args[0]);
        int portID = peerID%1000+8000-1;
        try{
            Scanner common = new Scanner(new File ("Common.cfg"));
            temp = common.nextLine().split(" ");
            num = Integer.parseInt(temp[1]);
            temp = common.nextLine().split(" ");
            unch = Integer.parseInt(temp[1]);
            temp = common.nextLine().split(" ");
            opt = Integer.parseInt(temp[1]);
            temp = common.nextLine().split(" ");
            file = temp[1];
            temp = common.nextLine().split(" ");
            fSize = Integer.parseInt(temp[1]);
            temp = common.nextLine().split(" ");
            pSize = Integer.parseInt(temp[1]);
            aSize=fSize/pSize+1;

        }
        catch (FileNotFoundException e){
            System.out.println("File1NotFound");
        }
        try{

        String info = "";
        for(int i = 0; i<peerID%1000;i++){
            Scanner peerInfo = new Scanner(new File ("PeerInfo.cfg"));
            info = peerInfo.nextLine();
            Info = info.split(" ");
        }
        hasFile = Integer.parseInt(Info[3]);
        System.out.println(Info[3]);
    }
    catch(FileNotFoundException e){
        System.out.println("File2NotFound");
    }

        int clientNum = 1;
        PeerProcess peer = new PeerProcess(peerID,portID,hasFile,num,unch,opt,fSize,pSize,aSize,file,Info);
        peer.run();

    }
}
