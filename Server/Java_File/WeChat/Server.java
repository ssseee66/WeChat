package WeChat;

import java.io.*;
import java.util.LinkedList;


public class Server {
    public static File server_path = new File(System.getProperty("user.dir"));
    public static LinkedList<Server_Monitor_UserRequest>
            monitor_userRequest_list;
    //对方发来申请信息链表,当中的信息指定的账号用户登录时发送给该用户,发出"好友申请"信息
    public static LinkedList<User_message> apply_info_list;
    //同意好友申请信息链表,当中的信息指定账号用户登录是发送给给用户,发出"添加好友通过"信息
    public static LinkedList<User_message> agree_apply_info_list;
    //对方删除好友,将删除一方账号发给被删除一方,提示被对方删除
    public static LinkedList<User_message> delete_friend_info_list;
    //对方发送信息内容链表,当接收信息方登录后,将信息发送给接收方
    public static LinkedList<User_message> send_information_info_list;
    //在线用户的账号链表
    public static LinkedList<String> oline_user_account_list;
    //
    public static LinkedList<Thread> thread_list;
    public static void main(String[] args) {
        monitor_userRequest_list = new LinkedList<>();
        apply_info_list = new LinkedList<>();
        agree_apply_info_list = new LinkedList<>();
        delete_friend_info_list = new LinkedList<>();
        send_information_info_list = new LinkedList<>();
        oline_user_account_list = new LinkedList<>();
        read_information();
        thread_list = new LinkedList<>();
        Server_Monitor_User server_monitor_user =
                new Server_Monitor_User();
        Thread server_main_thread = new Thread(server_monitor_user);
        server_main_thread.setName("牛逼");
        server_main_thread.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                super.run();
                try {
                    File file = new File(server_path + "/file/apply_info_list.txt");
                    FileOutputStream file_out = new FileOutputStream(file, false);
                    ObjectOutputStream object_out = new ObjectOutputStream(file_out);
                    if (!apply_info_list.isEmpty())
                        object_out.writeObject(apply_info_list);
                    object_out.close();
                    file_out.close();
                    file = new File(server_path + "/file/agree_apply_info_list.txt");
                    file_out = new FileOutputStream(file, false);
                    object_out = new ObjectOutputStream(file_out);
                    if (! agree_apply_info_list.isEmpty())
                        object_out.writeObject(agree_apply_info_list);
                    object_out.close();
                    file_out.close();
                    file = new File(server_path + "/file/delete_friend_info_list.txt");
                    file_out = new FileOutputStream(file, false);
                    object_out = new ObjectOutputStream(file_out);
                    if (! delete_friend_info_list.isEmpty())
                        object_out.writeObject(delete_friend_info_list);
                    object_out.close();
                    file_out.close();
                    file = new File(server_path + "/file/send_information_info_list.txt");
                    file_out = new FileOutputStream(file, false);
                    object_out = new ObjectOutputStream(file_out);
                    if (! send_information_info_list.isEmpty())
                        object_out.writeObject(send_information_info_list);
                    object_out.close();
                    file_out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public static void read_information() {
        File file = new File(server_path + "/file/apply_info_list.txt");
        try {
            FileInputStream file_in = new FileInputStream(file);
            ObjectInputStream object_in = new ObjectInputStream(file_in);
            while (file_in.available() > 0)
                apply_info_list = CastList.castList(object_in.readObject(), User_message.class);
            object_in.close();
            file_in.close();
            file = new File(server_path + "/file/agree_apply_info_list.txt");
            file_in = new FileInputStream(file);
            object_in = new ObjectInputStream(file_in);
            while (file_in.available() > 0)
                agree_apply_info_list = CastList.castList(object_in.readObject(), User_message.class);
            object_in.close();
            file_in.close();
            file = new File(server_path + "/file/delete_friend_info_list.txt");
            file_in = new FileInputStream(file);
            object_in = new ObjectInputStream(file_in);
            while (file_in.available() > 0)
                delete_friend_info_list = CastList.castList(object_in.readObject(), User_message.class);
            object_in.close();
            file_in.close();
            file = new File(server_path + "/file/send_information_info_list.txt");
            file_in = new FileInputStream(file);
            object_in = new ObjectInputStream(file_in);
            while (file_in.available() > 0)
                send_information_info_list = CastList.castList(object_in.readObject(), User_message.class);
            object_in.close();
            file_in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
