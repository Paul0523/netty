package com.paul0523;



import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

public class TcpClient {

    public static void main(String[] args) throws Exception{
        new TcpClient().start();
    }

    public void start() throws Exception{
        Socket socket = new Socket("127.0.0.1", 8080);
        new MyThread(socket).start();
        while (true) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String line = bufferedReader.readLine();
            OutputStream outputStream = socket.getOutputStream();
            if ("bye".equals(line)) {
                outputStream.write(line.getBytes("utf-8"));
                break;
            }
            outputStream.write(line.getBytes("utf-8"));
        }
    }


    class MyThread extends Thread {

        private Socket socket;

        public MyThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            try {
                byte[] bytes = new byte[1024];
                InputStream inputStream = socket.getInputStream();
                int count = 0;
                while ((count = inputStream.read(bytes)) != -1) {
                    System.out.println(new String(bytes, 0, count, Charset.forName("utf-8")));
                }
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
