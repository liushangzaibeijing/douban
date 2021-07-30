package com.xiu.crawling.douban.nio;

import org.apache.http.client.utils.DateUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Set;

/**
 * @author xieqx
 * @className NioServer 服务端
 * @desc java 多路复用IO
 * @date 2021/7/28 17:38
 **/
public class NioServer {

    private ServerSocketChannel serverChannel;

    private Selector selector;

    /**
     * 创建一个NIO服务端
     * @param port
     */
    public void initNioServer(Integer port){
        try {
            //打开一个通道Channel
            serverChannel = ServerSocketChannel.open();
            //绑定端口
            serverChannel.socket().bind(new InetSocketAddress(8999));
            //设置非阻塞
            serverChannel.configureBlocking(false);
            //创建一个多路复用选择器
            selector = Selector.open();
            //通道注册到选择器上
            //第一个参数是选择器，第二个参数是需要选择器处理的事件
            //有三种类型的事件 分别是OP_READ读事件，OP_WRITE写事件，OP_ACCEPT建立连接事件  OP_CONNECT连接准备就绪 多个事件用 | 运算符连接诶
            serverChannel.register(selector, SelectionKey.OP_ACCEPT );

            this.listen(selector);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen(Selector selector){
        try {
            while(true){
                selector.select();
                //获取注册在选择器上的事件
                Set<SelectionKey> keys = selector.selectedKeys();
                for(SelectionKey key:keys){
                    //处理连接准备
                    if(key.isConnectable()){
                        handleConnect(key);
                    }
                    //连接建立事件
                    if(key.isAcceptable()){
                        handleAccept(key);
                    }
                    //读请求
                    if(key.isReadable()){
                        handleRead(key);
                    }
                    //处理写就绪
                    if(key.isWritable()){
                        handleWrite(key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleWrite(SelectionKey key) {
        System.out.println("接受到写请求");
    }

    private void handleRead(SelectionKey key) throws IOException {
        //读数据
        //从key中获取channel
        SocketChannel socketChannel = (SocketChannel)key.channel();
        //创建用于读写的缓冲对象
        ByteBuffer buff = ByteBuffer.allocate(1024);
        int read = socketChannel.read(buff);
        if(read != -1){
            //获取到数据并进行响应
            String msg = new String(buff.array()).trim();
            System.out.println("NIO server received message =  " + msg);
            socketChannel.write(ByteBuffer.wrap("hi client ".getBytes()));
        }
    }

    private void handleAccept(SelectionKey key) {
        try {
            System.out.println("客户端连接建立");
            //获取客户端
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            SocketChannel accept = channel.accept();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //读请求数据
            int read = accept.read(buffer);
            if(read!=-1){
                //设置EOF 准备重缓冲区读数据
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                String request = new String(bytes, "UTF-8");
                System.out.println("server received client request : " + request);
//                String request = new String(buffer.array()).trim();
//                System.out.println("server received client request："+new String(buffer.array()).trim());
                //写数据 设置当前时间
                //清空buffer 准备写
                buffer.clear();
                String response = "bad request";
                if(request.equals("tell me now time")){
                    response = "hi client now time is "+ DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
                }
                buffer.put(response.getBytes());
                buffer.flip();
                accept.write(buffer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConnect(SelectionKey key) {
        System.out.println("连接状态就绪");
    }


    public static void main(String[] args) throws IOException {
        NioServer server = new NioServer();
        server.initNioServer(8999);
    }

}
