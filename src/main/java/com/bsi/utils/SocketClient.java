package com.bsi.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;  
import java.io.InputStreamReader;  
import java.io.OutputStreamWriter;  
import java.net.Socket;

/**
 * socket客户端
 */
public class SocketClient {
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    private static Logger info_log = LoggerFactory.getLogger("TASK_INFO_LOG");

    public void connect(String host, int port) throws Exception {
        info_log.info("开始连接socket服务器,地址：{}，端口号:{}",host,port);
        socket = new Socket(host, port);
        info_log.info("socket连接状态:{}",socket.isConnected());
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        info_log.info("writer:{}",writer);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        info_log.info("reader:{}",reader);
    }
  
    public void sendMessage(String message) throws Exception {
        writer.write(message);
        writer.newLine();
        writer.flush();
    }
  
    public String receiveMessage() throws Exception {
        StringBuilder response = new StringBuilder();
        info_log.info("reader:{}",reader);
        String line = reader.readLine();
        response.append(line);
        return response.toString();  
    }  
  
    public void disconnect() {
        IOUtils.closeQuietly(writer);
        IOUtils.closeQuietly(reader);
        IOUtils.closeQuietly(socket);
    }
}