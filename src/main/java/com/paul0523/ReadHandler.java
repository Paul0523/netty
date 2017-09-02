package com.paul0523;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ReadHandler implements Runnable{

    private SelectionKey selectionKey;

    public ReadHandler(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
            byteBuffer.clear();
            int readbytes = socketChannel.read(byteBuffer);
            byteBuffer.flip();
            if (readbytes > 0) {
                byte[] bytes = new byte[readbytes];
                byteBuffer.get(bytes, 0, readbytes);
                System.out.println(new String(bytes, 0, readbytes, Charset.forName("utf-8")));
            } else if (readbytes == -1) {
                socketChannel.close();
                return;
            }
            byteBuffer.flip();
            selectionKey.attach(byteBuffer);
            selectionKey.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
            selectionKey.selector().wakeup();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
