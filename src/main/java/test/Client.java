package test;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("127.0.0.1", 8010);
        System.out.println("client::Socket");

        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream toServer = new ObjectOutputStream(outputStream);
        ObjectInputStream fromServer = new ObjectInputStream(inputStream);

        // sending #1 matrix
        int[][] source = {
                {0, 1, 0},
                {1, 0, 1},
                {1, 0, 1}
        };
        toServer.writeObject("matrix");
        toServer.writeObject(source);

        // sending #3 index for getAdjacentIndices
        toServer.writeObject("AdjacentIndices");
        toServer.writeObject(new Index(1, 1));
        // receiving #1 getAdjacentIndices
        Collection<Index> AdjacentIndices =
                new ArrayList<Index>((Collection<Index>) fromServer.readObject());
        System.out.println("client::getAdjacentIndices:: " + AdjacentIndices);

        // sending #4 index for getReachables
        toServer.writeObject("Reachables");

        toServer.writeObject(new Index(1, 1));
        // receiving #2 getReachables
        Collection<Index> ReachablesIndices =
                new ArrayList<Index>((Collection<Index>) fromServer.readObject());
        System.out.println("client::ReachablesIndices:: " + ReachablesIndices);

        toServer.writeObject("stop");

        System.out.println("client::Close all streams!!!!");
        fromServer.close();
        toServer.close();
        socket.close();
        System.out.println("client::Close socket!!!!");
    }
}
