package WeChat;

import java.net.ServerSocket;
import java.net.Socket;


public class Server_Monitor_User implements Runnable {
    /**
     * 第一个开启的线程并且是一直运行
     * 用于服务端监控用户登录和注册，当用户发来登录请求则为其开启相应线程为其
     * 传输聊天内容、好友申请等信息数据
     * 1095端口监听请求和发送回响
     * 1395端口监听发送布尔值之类的信息
     * 1495端口用于传输图像
     * 5295端口用于传输携带关键信息的对象
     */
    public static ServerSocket request_serverSocket = null;
    public static ServerSocket info_serverSocket = null;
    public static ServerSocket object_serverSocket = null;
    public static ServerSocket image_serverSocket = null;

    public void run() {
        Monitor();
    }

    public void Monitor() {
        if (Thread.currentThread().getName().equals("牛逼")) {
            System.out.println("主线程!");
            while (true) {
                Socket request_clientSocket = null;
                Socket info_clientSocket = null;
                Socket object_clientSocket = null;
                Socket image_clientSocket = null;
                try {
                    request_serverSocket = new ServerSocket(1095);
                    object_serverSocket = new ServerSocket(5295);
                    info_serverSocket = new ServerSocket(1395);
                    image_serverSocket = new ServerSocket(1495);
                } catch (Exception e) {
                    System.out.println("正在监听用户登录和注册........");
                }
                try {
                    if (request_serverSocket != null)
                        request_clientSocket = request_serverSocket.accept();
                    if (info_serverSocket != null)
                        info_clientSocket = info_serverSocket.accept();
                    if (object_serverSocket != null)
                        object_clientSocket = object_serverSocket.accept();
                    if (image_serverSocket != null)
                        image_clientSocket = image_serverSocket.accept();
                    if (image_clientSocket != null) {
                        Server_Monitor_UserRequest user_monitor =
                                new Server_Monitor_UserRequest();
                        user_monitor.request_clientSocket = request_clientSocket;
                        user_monitor.info_clientSocket = info_clientSocket;
                        user_monitor.object_clientSocket = object_clientSocket;
                        user_monitor.image_clientSocket = image_clientSocket;
                        Thread user_thread = new Thread(user_monitor);
                        user_thread.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
