package WeChat;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;

public class WeChat {
    public static Socket request_socket = null;
    public static Socket info_socket = null;
    public static Socket object_socket = null;
    public static Socket image_socket = null;
    public static boolean isNotRecord = false;
    public static Pattern pattern_ip_add =
            Pattern.compile(
                    "^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])" +
                            "(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$");
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InetAddress server_ipAddress = null;
        boolean isArrive = false;
        while (!isArrive) {
            System.out.print("请输入服务端的IP地址:");
            String ipAddress = scanner.nextLine();
            boolean isTrueAddress = false;
            while (!isTrueAddress) {
                try {
                    if (pattern_ip_add.matcher(ipAddress).find()) {
                        server_ipAddress = InetAddress.getByName(ipAddress);
                        isTrueAddress = true;
                    } else {
                        System.out.print("IP地址输入错误!请重新输入:");
                        ipAddress = scanner.nextLine();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (server_ipAddress != null) {
                try {
                    info_socket = new Socket(server_ipAddress, 1395);
                    object_socket = new Socket(server_ipAddress, 5295);
                    image_socket = new Socket(server_ipAddress, 1495);
                    request_socket = new Socket(server_ipAddress, 1095);
                } catch (ConnectException e) {
                    System.out.println(e.getMessage());
                    if (e.getMessage().equals("拒绝连接")) {
                        System.out.println("主机拒绝连接,请确保服务端开启了服务");
                    }
		    continue;
                } catch (NoRouteToHostException e) {
                    if (e.getMessage().equals("没有到主机的路由")) {
                        System.out.println("没有到主机的路由,请确认主机IP是否正确");
                    }
		    continue;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isArrive = true;
                Login login = new Login();
                login.setBounds(200, 100, 400, 400);
                login.setTitle("登录");
            }
        }
        System.out.println("客户端启动成功!");
    }
}
