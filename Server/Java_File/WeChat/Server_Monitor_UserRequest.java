package WeChat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedList;


public class Server_Monitor_UserRequest implements Runnable {
    /**
     * 单个用户的主线程（用于监听用户发来的各种请求）
     */
    public Socket image_clientSocket;
    public Socket object_clientSocket;
    public Socket request_clientSocket;
    public Socket info_clientSocket;
    public String user_account;
    public boolean isInit = false;
    public boolean isApply_list = false;
    public void run() {
        System.out.println(Thread.currentThread().getId());
        while (true){
            try {
                if (isApply_list && isInit) {
                    if (!Server.apply_info_list.isEmpty()) {
                        LinkedList<User_message> user_message_temp_list =
                                new LinkedList<>();
                        for (User_message user_message : Server.apply_info_list) {
                            if (user_message.friend_account.equals(user_account)) {
                                user_message_temp_list.add(user_message);
                                try {
                                    if (request_clientSocket != null) {
                                        DataOutputStream request_dataOut = new DataOutputStream(
                                                request_clientSocket.getOutputStream());
                                        request_dataOut.writeUTF("对方发来好友申请");
                                        if (object_clientSocket != null) {
                                            Connection connection = GetConnection.connection(
                                                    "friend", "010905");
                                            Statement sql = connection.createStatement();
                                            //此时me_account为添加好友一方的账号
                                            ResultSet resultSet = sql.executeQuery(Mysql_CreateTable
                                                    .SELECT_USERINFO_AND_FRIEND_APPLY_FOR_YOU(user_message.me_nickName));
                                            User_message user_messages = new User_message();
                                            while (resultSet.next()) {
                                                user_messages.hello_text =
                                                        resultSet.getString("招呼语");
                                                user_messages.friend_name_for_add =
                                                        resultSet.getString("为对方设置的备注");
                                            }
                                            resultSet = sql.executeQuery("select * from user_info where `账号` = '" +
                                                    user_message.me_account + "'");
                                            while (resultSet.next()) {
                                                user_messages.friend_account =
                                                        resultSet.getString("账号");
                                                user_messages.friend_nickName =
                                                        resultSet.getString("昵称");
                                                user_messages.friend_signature =
                                                        resultSet.getString("个性签名");
                                                user_messages.friend_region =
                                                        resultSet.getString("区域");
                                                user_messages.friend_city =
                                                        resultSet.getString("地区");
                                            }
                                            File file = new File(Server.server_path + "/images/" +
                                                    user_messages.friend_nickName + ".png");
                                            BufferedImage friend_bufferedImage = ImageIO.read(file);
                                            ByteArrayOutputStream friend_avatar_out = new ByteArrayOutputStream();
                                            ImageIO.write(friend_bufferedImage, "png", friend_avatar_out);
                                            user_messages.friend_avatar = friend_avatar_out.toByteArray();
                                            resultSet = sql.executeQuery("select * from " + user_message.friend_nickName +
                                                    "_apply where `账号` = '" + user_messages.friend_account + "'");
                                            if (! resultSet.next()) {
                                                sql.executeUpdate(
                                                        "insert into " + user_message.friend_nickName +
                                                                "_apply " + "(`账号`, `招呼语`, `同意与否`) " +
                                                                "values('" + user_messages.friend_account +
                                                                "', '" + user_messages.hello_text +
                                                                "', 0)");
                                            } else {
                                                sql.executeUpdate(
                                                        "update " + user_message.friend_nickName +
                                                                "_apply set `招呼语` = '" + user_messages.hello_text +
                                                                "', `同意与否` = 0)");
                                            }
                                            ObjectOutputStream object_out = new ObjectOutputStream(
                                                    object_clientSocket.getOutputStream());
                                            object_out.writeObject(user_messages);
                                            //friend_nickName为添加一方
                                            //将添加一方的头像传输至被添加一方
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        for (User_message user_message : user_message_temp_list) {
                            Server.apply_info_list.remove(user_message);
                        }
                    }
                    if (!Server.agree_apply_info_list.isEmpty()) {
                        LinkedList<User_message> user_message_temp_list =
                                new LinkedList<>();
                        for (User_message user_message : Server.agree_apply_info_list) {
                            if (user_message.friend_account.equals(user_account)) {
                                user_message_temp_list.add(user_message);
                                try {
                                    if (request_clientSocket != null) {
                                        DataOutputStream request_dataOut = new DataOutputStream(
                                                request_clientSocket.getOutputStream());
                                        request_dataOut.writeUTF("对方同意添加申请");
                                        if (object_clientSocket != null) {
                                            Connection connection = GetConnection.connection(
                                                    "friend", "010905");
                                            Statement sql = connection.createStatement();
                                            //对方同意了好友申请me_account即是对方的账号
                                            //通过连接数据获取对方的详细信息
                                            ResultSet resultSet = sql.executeQuery(
                                                    "select * from user_info where `账号` = '" +
                                                            user_message.me_account + "'");
                                            User_message user_messages = new User_message();
                                            //当初添加好友时为朋友设置的备注
                                            user_messages.friend_name =
                                                    user_message.friend_name_for_add;
                                            user_messages.me_nickName =
                                                    user_message.friend_nickName;
                                            user_messages.chat_text = user_message.hello_text;
                                            user_messages.last_chat_time = user_message.last_chat_time;
                                            while (resultSet.next()) {
                                                user_messages.friend_account =
                                                        resultSet.getString("账号");
                                                user_messages.friend_nickName =
                                                        resultSet.getString("昵称");
                                                user_messages.friend_signature =
                                                        resultSet.getString("个性签名");
                                                user_messages.friend_region =
                                                        resultSet.getString("区域");
                                                user_messages.friend_city =
                                                        resultSet.getString("地区");
                                            }
                                            File file = new File(Server.server_path + "/images/" +
                                                    user_messages.friend_nickName + ".png");
                                            BufferedImage bufferedImage = ImageIO.read(file);
                                            ByteArrayOutputStream image_binary_out =
                                                    new ByteArrayOutputStream();
                                            ImageIO.write(bufferedImage, "png", image_binary_out);
                                            user_messages.friend_avatar = image_binary_out.toByteArray();
                                            User_message user_message_clone =
                                                    (User_message) cloneObject(user_messages);
                                            sql.executeUpdate(
                                                    Mysql_CreateTable.INSERT_USER_CONTACTS_TABLE(user_message_clone));
                                            ObjectOutputStream object_out = new ObjectOutputStream(
                                                    object_clientSocket.getOutputStream());
                                            object_out.writeObject(user_messages);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                        for (User_message user_message : user_message_temp_list) {
                            Server.agree_apply_info_list.remove(user_message);
                        }
                    }
                    if (!Server.send_information_info_list.isEmpty()) {
                        LinkedList<User_message> user_message_temp_list =
                                new LinkedList<>();
                        for (User_message user_message : Server.send_information_info_list) {
                            if (user_message.friend_account.equals(user_account)) {
                                user_message_temp_list.add(user_message);
                                try {
                                    if (request_clientSocket != null) {
                                        DataOutputStream request_dataOut = new DataOutputStream(
                                                request_clientSocket.getOutputStream());
                                        request_dataOut.writeUTF("对方发来信息");
                                        if (object_clientSocket != null) {
                                            Connection connection = GetConnection.connection(
                                                    "friend", "010905");
                                            Statement sql = connection.createStatement();
                                            ResultSet resultSet;
                                            User_message user_messages = new User_message();
                                            user_messages.me_account = user_message.friend_account;
                                            user_messages.friend_account = user_message.me_account;
                                            //获取发送方信息
                                            resultSet = sql.executeQuery("select * from user_info where `账号` = '" +
                                                    user_messages.friend_account + "'");
                                            while (resultSet.next()) {
                                                user_messages.friend_nickName =
                                                        resultSet.getString("昵称");
                                                user_messages.friend_signature =
                                                        resultSet.getString("个性签名");
                                                user_messages.friend_region =
                                                        resultSet.getString("区域");
                                                user_messages.friend_city =
                                                        resultSet.getString("地区");
                                            }
                                            File file = new File(Server.server_path + "/images/" +
                                                    user_messages.friend_nickName + ".png");
                                            BufferedImage bufferedImage = ImageIO.read(file);
                                            ByteArrayOutputStream image_binary_out =
                                                    new ByteArrayOutputStream();
                                            ImageIO.write(bufferedImage, "png", image_binary_out);
                                            user_messages.friend_avatar = image_binary_out.toByteArray();
                                            //获取接收方昵称
                                            resultSet = sql.executeQuery("select * from user_info where `账号` = '" +
                                                    user_messages.me_account + "'");
                                            while (resultSet.next()) {
                                                user_messages.me_nickName =
                                                        resultSet.getString("昵称");
                                            }
                                            resultSet = sql.executeQuery(
                                                    "select * from " +
                                                            user_messages.friend_nickName +
                                                            "_contacts where `账号` = '" +
                                                            user_messages.me_account + "'");
                                            while (resultSet.next()) {
                                                user_messages.chat_text =
                                                        resultSet.getString("聊天内容");
                                                user_messages.last_chat_time =
                                                        resultSet.getTimestamp("聊天时间").toLocalDateTime();
                                            }
                                            resultSet = sql.executeQuery("select * from " +
                                                    user_messages.me_nickName +
                                                    "_contacts where `账号` = '" + user_messages.friend_account + "'");
                                            while (resultSet.next()) {
                                                user_messages.friend_name =
                                                        resultSet.getString("备注");
                                            }
                                            User_message user_message_clone =
                                                    (User_message) cloneObject(user_messages);
                                            sql.executeUpdate(
                                                    Mysql_CreateTable.UPDATE_CHAT_USER_CONTACTS_TABLE(user_message_clone));
                                            ObjectOutputStream object_out =
                                                    new ObjectOutputStream(
                                                            object_clientSocket.getOutputStream());
                                            object_out.writeObject(user_messages);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                        for (User_message user_message : user_message_temp_list) {
                            Server.send_information_info_list.remove(user_message);
                        }
                    }
                    if (!Server.delete_friend_info_list.isEmpty()) {
                        LinkedList<User_message> user_message_temp_list =
                                new LinkedList<>();
                        for (User_message user_message : Server.delete_friend_info_list) {
                            if (user_message.friend_account.equals(user_account)) {
                                user_message_temp_list.add(user_message);
                                try {
                                    if (request_clientSocket != null) {
                                        DataOutputStream request_dataOut = new DataOutputStream(
                                                request_clientSocket.getOutputStream());
                                        request_dataOut.writeUTF("对方将您删除");
                                        if (object_clientSocket != null) {
                                            Connection connection = GetConnection.connection(
                                                    "friend", "010905");
                                            Statement sql = connection.createStatement();
                                            sql.executeUpdate(
                                                    "delete from " + user_message.friend_nickName +
                                                            "_contacts where `账号` = '" +
                                                            user_message.me_account + "' ");
                                            ObjectOutputStream object_out =
                                                    new ObjectOutputStream(
                                                            object_clientSocket.getOutputStream());
                                            object_out.writeObject(user_message.me_account);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                        for (User_message user_message : user_message_temp_list) {
                            Server.delete_friend_info_list.remove(user_message);
                        }
                    }
                    isApply_list = false;
                    isInit = false;
                }
                if (request_clientSocket != null) {
                    DataInputStream request_dataIn = new DataInputStream(
                            request_clientSocket.getInputStream());
                    String request = request_dataIn.readUTF();
                    switch (request) {
                        case "初始化" -> {
                            if (object_clientSocket != null) {
                                ObjectInputStream object_in = new ObjectInputStream(
                                        object_clientSocket.getInputStream());
                                User user = (User) object_in.readObject();
                                Connection connection = GetConnection.connection("friend", "010905");
                                Statement sql = connection.createStatement();
                                ResultSet resultSet = sql.executeQuery(Mysql_CreateTable
                                        .SELECT_USERINFO_AND_FRIEND_CONTACTS(user.nickName));
                                LinkedList<User_message> user_message_list =
                                        new LinkedList<>();
                                while (resultSet.next()) {
                                    User_message user_message = new User_message();
                                    user_message.friend_account =
                                            resultSet.getString("账号");
                                    user_message.friend_nickName =
                                            resultSet.getString("昵称");
                                    user_message.friend_name =
                                            resultSet.getString("备注");
                                    user_message.friend_signature =
                                            resultSet.getString("个性签名");
                                    user_message.friend_region =
                                            resultSet.getString("区域");
                                    user_message.friend_city =
                                            resultSet.getString("地区");
                                    user_message.chat_text =
                                            resultSet.getString("聊天内容");
                                    user_message.last_chat_time =
                                            resultSet.getTimestamp("聊天时间")
                                                    .toLocalDateTime();
                                    user_message.me_account = user.account;
                                    user_message.me_nickName = user.nickName;
                                    File file = new File(Server.server_path + "/images/" +
                                            user_message.friend_nickName + ".png");
                                    BufferedImage friend_bufferedImage = ImageIO.read(file);
                                    ByteArrayOutputStream image_byte_out = new ByteArrayOutputStream();
                                    ImageIO.write(friend_bufferedImage, "png", image_byte_out);
                                    user_message.friend_avatar = image_byte_out.toByteArray();
                                    user_message_list.add(user_message);
                                }
                                ObjectOutputStream object_out = new ObjectOutputStream(
                                        object_clientSocket.getOutputStream());
                                object_out.writeObject(user_message_list);
                                isInit = true;
                            }
                        }
                        case "申请列表" -> {
                            if (object_clientSocket != null) {
                                ObjectInputStream object_in = new ObjectInputStream(
                                        object_clientSocket.getInputStream());
                                User user = (User) object_in.readObject();
                                Connection connection = GetConnection.connection(
                                        "friend", "010905");
                                Statement sql = connection.createStatement();
                                ResultSet resultSet = sql.executeQuery(Mysql_CreateTable
                                        .SELECT_USERINFO_AND_FRIEND_APPLY(user.nickName));
                                LinkedList<User_message> user_message_apply_list =
                                        new LinkedList<>();
                                while (resultSet.next()) {
                                    User_message user_message = new User_message();
                                    user_message.friend_account =
                                            resultSet.getString("账号");
                                    user_message.friend_nickName =
                                            resultSet.getString("昵称");
                                    user_message.friend_signature =
                                            resultSet.getString("个性签名");
                                    user_message.friend_region =
                                            resultSet.getString("区域");
                                    user_message.friend_city =
                                            resultSet.getString("地区");
                                    user_message.hello_text =
                                            resultSet.getString("招呼语");
                                    user_message.accept =
                                            resultSet.getBoolean("同意与否");
                                    File file = new File(Server.server_path + "/images/" +
                                            user_message.friend_nickName + ".png");
                                    BufferedImage friend_bufferedImage = ImageIO.read(file);
                                    ByteArrayOutputStream image_byte_out = new ByteArrayOutputStream();
                                    ImageIO.write(friend_bufferedImage, "png", image_byte_out);
                                    user_message.friend_avatar = image_byte_out.toByteArray();
                                    user_message_apply_list.add(user_message);
                                }
                                if (object_clientSocket != null) {
                                    ObjectOutputStream object_out = new ObjectOutputStream(
                                            object_clientSocket.getOutputStream());
                                    object_out.writeObject(user_message_apply_list);
                                }
                                isApply_list = true;
                            }
                        }
                        case "搜索用户" -> {
                            if (info_clientSocket != null) {
                                DataInputStream info_dataIn = new DataInputStream(
                                        info_clientSocket.getInputStream());
                                String user_account = info_dataIn.readUTF();
                                Connection connection = GetConnection.connection(
                                        "friend", "010905");
                                Statement sql = connection.createStatement();
                                ResultSet resultSet = sql.executeQuery(
                                        "select * from user where `账号`='" +
                                                user_account + "'");
                                if (resultSet.next()) {
                                    if (info_clientSocket != null) {
                                        DataOutputStream info_dataOut =
                                                new DataOutputStream(
                                                        info_clientSocket.getOutputStream());
                                        info_dataOut.writeBoolean(true);
                                        if (image_clientSocket != null) {
                                            String nickName =
                                                    resultSet.getString("昵称");
                                            String account =
                                                    resultSet.getString("账号");
                                            User_message user_message = new User_message();
                                            user_message.friend_account = account;
                                            user_message.friend_nickName = nickName;
                                            File file = new File(Server.server_path + "/images/" +
                                                    user_message.friend_nickName + ".png");
                                            BufferedImage friend_bufferedImage = ImageIO.read(file);
                                            ByteArrayOutputStream image_byte_out = new ByteArrayOutputStream();
                                            ImageIO.write(friend_bufferedImage, "png", image_byte_out);
                                            user_message.friend_avatar = image_byte_out.toByteArray();
                                            if (object_clientSocket != null) {
                                                ObjectOutputStream object_out =
                                                        new ObjectOutputStream(
                                                                object_clientSocket.getOutputStream());
                                                object_out.writeObject(user_message);
                                            }
                                        }
                                    }
                                } else {
                                    if (info_clientSocket != null) {
                                        DataOutputStream info_dataOut =
                                                new DataOutputStream(
                                                        info_clientSocket.getOutputStream());
                                        info_dataOut.writeBoolean(false);
                                    }
                                }
                            }
                        }
                        case "更新用户个人信息" -> {
                            if (object_clientSocket != null) {
                                ObjectInputStream object_in = new ObjectInputStream(
                                        object_clientSocket.getInputStream());
                                User_message user_message =
                                        (User_message) object_in.readObject();
                                Connection connection = GetConnection.connection(
                                        "friend", "010905");
                                Statement sql = connection.createStatement();
                                ResultSet resultSet = sql.executeQuery("select * from user_info where `账号` = '" +
                                        user_message.me_account + "'");
                                String oldNickName = null;
                                if (resultSet.next()) {
                                    oldNickName = resultSet.getString("昵称");
                                }

                                if (oldNickName != null && !oldNickName.equals(user_message.me_nickName)) {
                                    File file = new File(Server.server_path + "/images/" +
                                            oldNickName + ".png");
                                    BufferedImage bufferedImage = ImageIO.read(file);
                                    boolean boo = file.delete();
                                    file = new File(Server.server_path + "/images/" +
                                            user_message.me_nickName + ".png");
                                    ImageIO.write(bufferedImage, "png", file);
                                    sql.executeUpdate("rename table " +
                                            oldNickName + "_apply to " +
                                            user_message.me_nickName + "_apply ");
                                    sql.executeUpdate("rename table " +
                                            oldNickName + "_apply_friend to " +
                                            user_message.me_nickName + "_apply_friend ");
                                    sql.executeUpdate("rename table " +
                                            oldNickName + "_contacts to " +
                                            user_message.me_nickName + "_contacts ");
                                }
                                sql.executeUpdate(
                                        "update user_info set `昵称` = '" +
                                                user_message.me_nickName + "', `个性签名` = '" +
                                                user_message.me_signature + "', `区域` = '" +
                                                user_message.me_region + "', `地区` = '" +
                                                user_message.me_city + "' where `账号` = '" +
                                                user_message.me_account + "'");
                                sql.executeUpdate("update user set `昵称` = '" +
                                        user_message.me_nickName + "' where `账号` = '" +
                                        user_message.me_account + "'");
                                ObjectInputStream image_byte_in = new ObjectInputStream(
                                        image_clientSocket.getInputStream());
                                byte[] image_binary_byte = (byte[])image_byte_in.readObject();
                                ByteArrayInputStream image_binary_in =
                                        new ByteArrayInputStream(image_binary_byte);
                                BufferedImage bufferedImage = ImageIO.read(image_binary_in);
                                File file_old = new File(Server.server_path + "/images/" +
                                        user_message.me_nickName + ".png");
                                if (file_old.delete()) {
                                    File file_new = new File(Server.server_path + "/images/" +
                                            user_message.me_nickName + ".png");
                                    bufferedImage = SetImageIcon.getBufferedImage(bufferedImage);
                                    ImageIO.write(bufferedImage, "png", file_new);
                                }
                            }
                        }
                        case "保存" -> {
                            //在用户退出程序时保存联系人信息数据
                            if (object_clientSocket != null) {
                                ObjectInputStream object_in = new ObjectInputStream(
                                        object_clientSocket.getInputStream());
                                LinkedList<User_message> user_message_list =
                                        CastList.castList(object_in.readObject(),
                                                User_message.class);
                                Connection connection = GetConnection.connection("friend", "010905");
                                Statement sql = connection.createStatement();
                                for (User_message user_message : user_message_list) {
                                    sql.executeUpdate(
                                            "update " + user_message.me_nickName +
                                                    "_contacts " + "set" +
                                                    "`备注` = '" + user_message.friend_name + "', " +
                                                    "`聊天时间` = '" + Timestamp.valueOf(user_message.last_chat_time) + "', " +
                                                    "`聊天内容` = '" + user_message.chat_text + "' " +
                                                    "where `账号` = '" + user_message.friend_account + "'");
                                }
                            }
                        }
                        case "添加好友" -> {
                            if (object_clientSocket != null) {
                                ObjectInputStream object_in = new ObjectInputStream(
                                        object_clientSocket.getInputStream());
                                User_message user_message =
                                        (User_message) object_in.readObject();
                                User_message user_message_clone =
                                        (User_message)cloneObject(user_message);
                                Connection connection = GetConnection.connection("friend", "010905");
                                Statement sql = connection.createStatement();
                                ResultSet resultSet = sql.executeQuery("select * from " + user_message_clone.me_nickName +
                                        "_apply_friend where `账号` = '" + user_message_clone.friend_account + "'");
                                if (! resultSet.next()) {
                                    sql.executeUpdate(Mysql_CreateTable.INSERT_USER_APPLY_FOR_YOU_TABLE(user_message_clone));
                                } else {
                                    sql.executeUpdate("update " + user_message_clone.me_nickName +
                                            "_apply_friend set `招呼语` = '" + user_message_clone.hello_text +
                                            "', `为对方设置的备注` = '" + user_message_clone.friend_name_for_add + "'");
                                }
                                boolean other_oline = false; //判断对方是否在线
                                for (Server_Monitor_UserRequest monitor_user : Server.monitor_userRequest_list) {
                                    if (monitor_user.user_account.equals(user_message.friend_account)) {
                                        other_oline = true;
                                        break;
                                    }
                                }
                                if (other_oline) {
                                    friend_apply(user_message);
                                } else {
                                    Server.apply_info_list.add(user_message);
                                }
                            }
                        }
                        case "同意申请" -> {
                            if (object_clientSocket != null) {
                                ObjectInputStream object_in = new ObjectInputStream(
                                        object_clientSocket.getInputStream());
                                User_message user_message =
                                        (User_message) object_in.readObject();
                                Connection connection = GetConnection.connection(
                                        "friend", "010905");
                                Statement sql = connection.createStatement();
                                ResultSet resultSet;
                                sql.executeUpdate(
                                        "update " + user_message.me_nickName +
                                                "_apply set `同意与否` = 1 where `账号` = '" +
                                                user_message.friend_account + "'");
                                resultSet = sql.executeQuery("select * from " + user_message.friend_nickName +
                                        "_apply_friend where `账号` = '" + user_message.me_account + "'");
                                while (resultSet.next()) {
                                    user_message.friend_name_for_add =
                                            resultSet.getString("为对方设置的备注");
                                }
                                User_message user_message_clone =
                                        (User_message) cloneObject(user_message);
                                sql.executeUpdate(
                                        Mysql_CreateTable.INSERT_USER_CONTACTS_TABLE(user_message_clone));
                                boolean other_oline = false; //判断对方是否在线
                                for (Server_Monitor_UserRequest monitor_user : Server.monitor_userRequest_list) {
                                    if (monitor_user.user_account.equals(user_message.friend_account)) {
                                        other_oline = true;
                                        break;
                                    }
                                }
                                if (other_oline) {
                                    agree_friend_apply(user_message);
                                } else {
                                    Server.agree_apply_info_list.add(user_message);
                                }
                            }
                        }
                        case "删除好友" -> {
                            if (object_clientSocket != null) {
                                ObjectInputStream object_in = new ObjectInputStream(
                                        object_clientSocket.getInputStream());
                                User_message user_message =
                                        (User_message) object_in.readObject();
                                Connection connection = GetConnection.connection(
                                        "friend", "010905");
                                Statement sql = connection.createStatement();
                                //此时me_account为删除一方,friend_account为被删除一方
                                sql.executeUpdate(
                                        "delete from " + user_message.me_nickName +
                                                "_contacts where `账号` = '" +
                                                user_message.friend_account + "' ");
                                User_message user_message_clone =
                                        (User_message) cloneObject(user_message);
                                boolean other_oline = false; //判断对方是否在线
                                for (Server_Monitor_UserRequest monitor_user : Server.monitor_userRequest_list) {
                                    if (monitor_user.user_account.equals(user_message.friend_account)) {
                                        other_oline = true;
                                        break;
                                    }
                                }
                                if (other_oline) {
                                    delete_friend(user_message_clone);
                                } else {
                                    Server.delete_friend_info_list.add(user_message_clone);
                                }
                            }
                        }
                        case "发送信息" -> {
                            if (object_clientSocket != null) {
                                ObjectInputStream object_in = new ObjectInputStream(
                                        object_clientSocket.getInputStream());
                                User_message user_message =
                                        (User_message) object_in.readObject();
                                User_message user_message_clone =
                                        (User_message) cloneObject(user_message);
                                Connection connection = GetConnection.connection(
                                        "friend", "010905");
                                Statement sql = connection.createStatement();
                                sql.executeUpdate(
                                        Mysql_CreateTable.UPDATE_CHAT_USER_CONTACTS_TABLE(user_message_clone));
                                boolean other_oline = false; //判断对方是否在线
                                for (Server_Monitor_UserRequest monitor_user : Server.monitor_userRequest_list) {
                                    if (monitor_user.user_account.equals(user_message.friend_account)) {
                                        other_oline = true;
                                        break;
                                    }
                                }
                                if (other_oline) {
                                    send_chat(user_message);
                                } else {
                                    Server.send_information_info_list.add(user_message);
                                }
                            }
                        }
                        case "获取个人信息" -> {
                            if (info_clientSocket != null) {
                                DataInputStream info_dataIn = new DataInputStream(
                                        info_clientSocket.getInputStream());
                                String user_account = info_dataIn.readUTF();
                                Connection connection = GetConnection.connection("friend", "010905");
                                Statement sql = connection.createStatement();
                                ResultSet resultSet = sql.executeQuery(
                                        "select * from user_info where `账号` = '" + user_account + "'");
                                User_message user_message = new User_message();
                                while (resultSet.next()) {
                                    user_message.me_account = user_account;
                                    user_message.me_nickName = resultSet.getString("昵称");
                                    user_message.me_signature = resultSet.getString("个性签名");
                                    user_message.me_region = resultSet.getString("区域");
                                    user_message.me_city = resultSet.getString("地区");
                                }
                                if (object_clientSocket != null) {
                                    ObjectOutputStream object_out = new ObjectOutputStream(
                                            object_clientSocket.getOutputStream());
                                    object_out.writeObject(user_message);
                                }
                                if (image_clientSocket != null) {
                                    File file = new File(Server.server_path + "/images/" +
                                            user_message.me_nickName + ".png");
                                    BufferedImage bufferedImage = ImageIO.read(file);
                                    bufferedImage = SetImageIcon.getBufferedImage(bufferedImage);
                                    ByteArrayOutputStream image_binary_out = new ByteArrayOutputStream();
                                    ImageIO.write(bufferedImage, "png", image_binary_out);
                                    ObjectOutputStream image_byte_out = new ObjectOutputStream(
                                            image_clientSocket.getOutputStream());
                                    image_byte_out.writeObject(image_binary_out.toByteArray());
                                }
                            }
                        }
                        case "登录" -> {
                            if (object_clientSocket != null) {
                                ObjectInputStream object_in = new ObjectInputStream(
                                        object_clientSocket.getInputStream());
                                User user = (User) object_in.readObject();
                                Connection connection = GetConnection.connection(
                                        "friend", "010905");
                                Statement sql = connection.createStatement();
                                ResultSet resultSet = sql.executeQuery(
                                        "select * from user where 账号='" + user.account +
                                                "' and 密码='" + user.password + "'");
                                if (resultSet.next()) {
                                    if (info_clientSocket != null) {
                                        user_account = user.account;
                                        if (!Server.oline_user_account_list.contains(user_account)) {
                                            Server.monitor_userRequest_list.add(this);
                                            Server.oline_user_account_list.add(user.account);
                                        } else {
                                            Server_Monitor_UserRequest user_monitors = null;
                                            for (Server_Monitor_UserRequest user_monitor :
                                                    Server.monitor_userRequest_list) {
                                                if (user_monitor.user_account.equals(user.account)) {
                                                    user_monitors = user_monitor;
                                                    if (user_monitor.request_clientSocket != null) {
                                                        DataOutputStream request_dataOut = new DataOutputStream(
                                                                user_monitor.request_clientSocket.getOutputStream());
                                                        request_dataOut.writeUTF("二次登录");
                                                    }
                                                }
                                            }
                                            Server.monitor_userRequest_list.remove(user_monitors);
                                            Server.monitor_userRequest_list.add(this);
                                        }
                                        DataOutputStream info_dataOut =
                                                new DataOutputStream(
                                                        info_clientSocket.getOutputStream());
                                        info_dataOut.writeBoolean(true);
                                        User_message user_message = new User_message();
                                        resultSet = sql.executeQuery(
                                                "select * from user_info where `账号` = '" +
                                                        user.account + "'");
                                        while (resultSet.next()) {
                                            user_message.me_account =
                                                    resultSet.getString("账号");
                                            user_message.me_nickName =
                                                    resultSet.getString("昵称");
                                            user_message.me_signature =
                                                    resultSet.getString("个性签名");
                                            user_message.me_region =
                                                    resultSet.getString("区域");
                                            user_message.me_city =
                                                    resultSet.getString("地区");
                                        }
                                        ObjectOutputStream object_out =
                                                new ObjectOutputStream(
                                                        object_clientSocket.getOutputStream());
                                        object_out.writeObject(user_message);
                                    }
                                } else {
                                    if (info_clientSocket != null) {
                                        DataOutputStream info_dataOut =
                                                new DataOutputStream(
                                                        info_clientSocket.getOutputStream());
                                        info_dataOut.writeBoolean(false);
                                    }
                                }
                            }
                        }
                        case "注册" -> {
                            LinkedList<User_message> user_message_list =
                                    new LinkedList<>();
                            Connection connection = GetConnection.connection(
                                    "friend", "010905");
                            Statement sql = connection.createStatement();
                            ResultSet resultSet = sql.executeQuery(
                                    "select `账号`, `昵称` from user");
                            while (resultSet.next()) {
                                User_message user_message = new User_message();
                                user_message.user_account = resultSet.getString("账号");
                                user_message.user_nickName = resultSet.getString("昵称");
                                user_message_list.add(user_message);
                            }
                            if (object_clientSocket != null) {
                                ObjectOutputStream object_out =
                                        new ObjectOutputStream(
                                                object_clientSocket.getOutputStream());
                                object_out.writeObject(user_message_list);
                            }
                        }
                        case "注册成功" -> {
                            if (object_clientSocket != null) {
                                ObjectInputStream object_in = new ObjectInputStream(
                                        object_clientSocket.getInputStream());
                                User user = (User) object_in.readObject();
                                Connection connection = GetConnection.connection(
                                        "friend", "010905");
                                Statement sql = connection.createStatement();
                                sql.executeUpdate(
                                        "insert into user (`账号`, `昵称`, `密码`) values('" +
                                                user.account + "', '" + user.nickName + "', '" +
                                                user.password + "')");
                                sql.executeUpdate(
                                        "insert into user_info (`账号`, `昵称`, `个性签名`, `区域`, `地区`) values('" +
                                                user.account + "', '" + user.nickName + "', '', '', '')");
                                sql.executeUpdate(Mysql_CreateTable
                                        .CREATE_FRIEND_APPLY_TABLE(user.nickName));
                                sql.executeUpdate(Mysql_CreateTable
                                        .CREATE_FRIEND_CONTACTS_TABLE(user.nickName));
                                sql.executeUpdate(Mysql_CreateTable
                                        .CREATE_FRIEND_APPLY_FOR_YOU_TABLE(user.nickName));
                                if (image_clientSocket != null) {
                                    ObjectInputStream image_byte_in = new ObjectInputStream(
                                            image_clientSocket.getInputStream());
                                    byte[] image_binary_byte = (byte[])image_byte_in.readObject();
                                    ByteArrayInputStream image_binary_in =
                                            new ByteArrayInputStream(image_binary_byte);
                                    BufferedImage bufferedImage = ImageIO.read(image_binary_in);
                                    File file = new File(Server.server_path + "/images",
                                            user.nickName + ".png");
                                    ImageIO.write(bufferedImage, "png", file);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("用户离开");
                System.out.println(e.getMessage());
                e.printStackTrace();
                Server.monitor_userRequest_list.removeIf(user_monitor ->
                        user_monitor.user_account.equals(user_account));
                return;
            }
        }
    }
    public void friend_apply(User_message user_message) {
        for (Server_Monitor_UserRequest monitor_user :
                Server.monitor_userRequest_list) {
            if (monitor_user.user_account
                    .equals(user_message.friend_account)) {
                try {
                    DataOutputStream other_request_dataOut =
                            new DataOutputStream(
                                    monitor_user
                                            .request_clientSocket.getOutputStream());
                    other_request_dataOut.writeUTF("对方发来好友申请");
                    if (monitor_user.object_clientSocket != null) {
                        Connection connection = GetConnection.connection(
                                "friend", "010905");
                        Statement sql = connection.createStatement();
                        //此时me_account为添加好友一方的账号
                        ResultSet resultSet = sql.executeQuery(Mysql_CreateTable
                                .SELECT_USERINFO_AND_FRIEND_APPLY_FOR_YOU(user_message.me_nickName));
                        User_message user_messages = new User_message();
                        while (resultSet.next()) {
                            user_messages.hello_text =
                                    resultSet.getString("招呼语");
                            user_messages.friend_name_for_add =
                                    resultSet.getString("为对方设置的备注");
                        }
                        resultSet = sql.executeQuery("select * from user_info where `账号` = '" +
                                user_message.me_account + "'");
                        while (resultSet.next()) {
                            user_messages.friend_account =
                                    resultSet.getString("账号");
                            user_messages.friend_nickName =
                                    resultSet.getString("昵称");
                            user_messages.friend_signature =
                                    resultSet.getString("个性签名");
                            user_messages.friend_region =
                                    resultSet.getString("区域");
                            user_messages.friend_city =
                                    resultSet.getString("地区");
                        }
                        File file = new File(Server.server_path + "/images/" +
                                user_messages.friend_nickName + ".png");
                        BufferedImage friend_bufferedImage = ImageIO.read(file);
                        ByteArrayOutputStream friend_avatar_out = new ByteArrayOutputStream();
                        ImageIO.write(friend_bufferedImage, "png", friend_avatar_out);
                        user_messages.friend_avatar = friend_avatar_out.toByteArray();
                        resultSet = sql.executeQuery("select * from " + user_message.friend_nickName +
                                "_apply where `账号` = '" + user_messages.friend_account + "'");
                        if (! resultSet.next()) {
                            sql.executeUpdate(
                                    "insert into " + user_message.friend_nickName +
                                            "_apply " + "(`账号`, `招呼语`, `同意与否`) " +
                                            "values('" + user_messages.friend_account +
                                            "', '" + user_messages.hello_text +
                                            "', 0)");
                        } else {
                            sql.executeUpdate(
                                    "update " + user_message.friend_nickName +
                                            "_apply set `招呼语` = '" + user_messages.hello_text +
                                            "', `同意与否` = 0");
                        }
                        ObjectOutputStream object_out = new ObjectOutputStream(
                                monitor_user.object_clientSocket.getOutputStream());
                        object_out.writeObject(user_messages);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void agree_friend_apply(User_message user_message) {
        for (Server_Monitor_UserRequest monitor_user :
                Server.monitor_userRequest_list) {
            if (monitor_user.user_account
                    .equals(user_message.friend_account)) {
                try {
                    DataOutputStream other_request_dataOut =
                            new DataOutputStream(
                                    monitor_user
                                            .request_clientSocket.getOutputStream());
                    other_request_dataOut.writeUTF("对方同意添加申请");
                    if (monitor_user.object_clientSocket != null) {
                        Connection connection = GetConnection.connection(
                                "friend", "010905");
                        Statement sql = connection.createStatement();
                        //对方同意了好友申请me_account即是对方的账号
                        //通过连接数据获取对方的详细信息
                        ResultSet resultSet = sql.executeQuery(
                                "select * from user_info where `账号` = '" +
                                        user_message.me_account + "'");
                        User_message user_messages = new User_message();
                        //当初添加好友时为朋友设置的备注
                        user_messages.friend_name =
                                user_message.friend_name_for_add;
                        user_messages.me_nickName =
                                user_message.friend_nickName;
                        user_messages.chat_text = user_message.hello_text;
                        user_messages.last_chat_time = user_message.last_chat_time;
                        while (resultSet.next()) {
                            user_messages.friend_account =
                                    resultSet.getString("账号");
                            user_messages.friend_nickName =
                                    resultSet.getString("昵称");
                            user_messages.friend_signature =
                                    resultSet.getString("个性签名");
                            user_messages.friend_region =
                                    resultSet.getString("区域");
                            user_messages.friend_city =
                                    resultSet.getString("地区");
                        }
                        File file = new File(Server.server_path + "/images/" +
                                user_messages.friend_nickName + ".png");
                        BufferedImage bufferedImage = ImageIO.read(file);
                        ByteArrayOutputStream image_binary_out =
                                new ByteArrayOutputStream();
                        ImageIO.write(bufferedImage, "png", image_binary_out);
                        user_messages.friend_avatar = image_binary_out.toByteArray();
                        User_message user_message_clone =
                                (User_message) cloneObject(user_messages);
                        sql.executeUpdate(
                                Mysql_CreateTable.INSERT_USER_CONTACTS_TABLE(user_message_clone));
                        ObjectOutputStream object_out = new ObjectOutputStream(
                                monitor_user.object_clientSocket.getOutputStream());
                        object_out.writeObject(user_messages);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void delete_friend(User_message user_message) {
        for (Server_Monitor_UserRequest monitor_user :
                Server.monitor_userRequest_list) {
            if (monitor_user.user_account
                    .equals(user_message.friend_account)) {
                try {
                    DataOutputStream other_request_dataOut =
                            new DataOutputStream(
                                    monitor_user
                                            .request_clientSocket.getOutputStream());
                    other_request_dataOut.writeUTF("对方将您删除");
                    if (monitor_user.object_clientSocket != null) {
                        Connection connection = GetConnection.connection(
                                "friend", "010905");
                        Statement sql = connection.createStatement();
                        sql.executeUpdate(
                                "delete from " + user_message.friend_nickName +
                                        "_contacts where `账号` = '" +
                                        user_message.me_account + "' ");
                        ObjectOutputStream object_out =
                                new ObjectOutputStream(
                                        monitor_user.object_clientSocket.getOutputStream());
                        object_out.writeObject(user_message.me_account);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void send_chat(User_message user_message) {
        for (Server_Monitor_UserRequest monitor_user :
                Server.monitor_userRequest_list) {
            if (monitor_user.user_account
                    .equals(user_message.friend_account)) {
                try {
                    DataOutputStream other_request_dataOut =
                            new DataOutputStream(
                                    monitor_user
                                            .request_clientSocket.getOutputStream());
                    other_request_dataOut.writeUTF("对方发来信息");
                    if (monitor_user.object_clientSocket != null) {
                        Connection connection = GetConnection.connection(
                                "friend", "010905");
                        Statement sql = connection.createStatement();
                        ResultSet resultSet;
                        User_message user_messages = new User_message();
                        user_messages.me_account = user_message.friend_account;
                        user_messages.friend_account = user_message.me_account;
                        //获取发送方信息
                        resultSet = sql.executeQuery("select * from user_info where `账号` = '" +
                                user_messages.friend_account + "'");
                        while (resultSet.next()) {
                            user_messages.friend_nickName =
                                    resultSet.getString("昵称");
                            user_messages.friend_signature =
                                    resultSet.getString("个性签名");
                            user_messages.friend_region =
                                    resultSet.getString("区域");
                            user_messages.friend_city =
                                    resultSet.getString("地区");
                        }
                        File file = new File(Server.server_path + "/images/" +
                                user_messages.friend_nickName + ".png");
                        BufferedImage bufferedImage = ImageIO.read(file);
                        ByteArrayOutputStream image_binary_out =
                                new ByteArrayOutputStream();
                        ImageIO.write(bufferedImage, "png", image_binary_out);
                        user_messages.friend_avatar = image_binary_out.toByteArray();
                        //获取接收方昵称
                        resultSet = sql.executeQuery("select * from user_info where `账号` = '" +
                                user_messages.me_account + "'");
                        while (resultSet.next()) {
                            user_messages.me_nickName =
                                    resultSet.getString("昵称");
                        }
                        resultSet = sql.executeQuery(
                                "select * from " +
                                        user_messages.friend_nickName +
                                        "_contacts where `账号` = '" +
                                        user_messages.me_account + "'");
                        while (resultSet.next()) {
                            user_messages.chat_text =
                                    resultSet.getString("聊天内容");
                            user_messages.last_chat_time =
                                    resultSet.getTimestamp("聊天时间").toLocalDateTime();
                        }
                        resultSet = sql.executeQuery("select * from " +
                                user_messages.me_nickName +
                                "_contacts where `账号` = '" + user_messages.friend_account + "'");
                        while (resultSet.next()) {
                            user_messages.friend_name =
                                    resultSet.getString("备注");
                        }
                        User_message user_message_clone =
                                (User_message) cloneObject(user_messages);
                        sql.executeUpdate(
                                Mysql_CreateTable.UPDATE_CHAT_USER_CONTACTS_TABLE(user_message_clone));
                        ObjectOutputStream object_out =
                                new ObjectOutputStream(
                                        monitor_user.object_clientSocket.getOutputStream());
                        object_out.writeObject(user_messages);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public Object cloneObject(Object object) {
        Object object_clone = new Object();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream objectOut =
                    new ObjectOutputStream(out);
            objectOut.writeObject(object);
            ByteArrayInputStream in =
                    new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream objectIn =
                    new ObjectInputStream(in);
            object_clone = objectIn.readObject();
        } catch (Exception ignored) {}
        return object_clone;
    }
}