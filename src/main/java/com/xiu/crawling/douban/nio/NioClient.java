package com.xiu.crawling.douban.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author xieqx
 * @className NioClient 客户端
 * @desc java 多路复用IO
 * @date 2021/7/28 17:38
 **/
public class NioClient {

    private SocketChannel clientChannel;


    /**
     * 创建一个NIO服务端
     * @param port
     */
    public void initNioClient(Integer port){
        try {
            //打开一个通道Channel
            clientChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",port));
            //绑定端口
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAndRecv(String words) throws IOException
    {
        byte[] msg = words.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(msg);
        buffer.flip();
        System.out.println("client request: " + words);
        clientChannel.write(buffer);
        buffer.clear();
        clientChannel.read(buffer);
        System.out.println("Client received: " + new String(buffer.array()).trim());
        clientChannel.close();
    }

    public static void main(String[] args) throws IOException {
        NioClient client = new NioClient();
        client.initNioClient(8999);
        client.sendAndRecv("tell me now time");
    }
}
