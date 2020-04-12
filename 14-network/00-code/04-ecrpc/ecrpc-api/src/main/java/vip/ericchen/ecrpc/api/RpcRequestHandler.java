package vip.ericchen.ecrpc.api;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>
 * description
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/04/12 22:00
 */
public class RpcRequestHandler {

    private String host;
    private int port;
    private RpcRequest rpcRequest;

    public RpcRequestHandler(String host, int port, RpcRequest rpcRequest) {
        this.host = host;
        this.port = port;
        this.rpcRequest = rpcRequest;
    }

    public Object send() throws IOException {
        Object answer = null;
        Socket socket = new Socket(host, port);
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            outputStream.writeObject(rpcRequest);
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
            Object obj = null;
            while ((obj = inputStream.readObject()) != null) {
                return obj;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return answer;
    }
}
