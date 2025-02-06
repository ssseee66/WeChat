package WeChat;

import java.sql.Timestamp;

public class Mysql_CreateTable {
    public static String CREATE_FRIEND_APPLY_TABLE(String nickName) {
        return  "CREATE TABLE `" + nickName+ "_apply`  (" +
                "`账号` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL, " +
                "`招呼语` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL, " +
                "`同意与否` tinyint NOT NULL, " +
                "PRIMARY KEY (`账号`) USING BTREE " +
                ") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic";
    }
    public static String SELECT_USERINFO_AND_FRIEND_APPLY(String nickName) {
        return "select a.`账号`, a.`招呼语`, a.`同意与否`, b.`昵称`, b.`个性签名`, b.`区域`, b.`地区` from " +
                nickName + "_apply a, user_info b where a.`账号` = b.`账号`";
    }
    public static String SELECT_USERINFO_AND_FRIEND_CONTACTS(String nickName) {
        return "select a.`账号`, a.`备注`, a.`聊天时间`, a.`聊天内容`, b.`昵称`, b.`个性签名`, b.`区域`, b.`地区` from " +
                nickName + "_contacts a, user_info b where a.`账号` = b.`账号`";
    }
    public static String SELECT_USERINFO_AND_FRIEND_APPLY_FOR_YOU(String nickName) {
        return "select a.`账号`, a.`招呼语`, a.`为对方设置的备注`, b.`昵称`, b.`个性签名`, b.`区域`, b.`地区` from " +
                nickName + "_apply_friend a, user_info b where a.`账号` = b.`账号`";
    }
    public static String CREATE_FRIEND_APPLY_FOR_YOU_TABLE(String nickName) {
        return  "CREATE TABLE `" + nickName+ "_apply_friend`  (" +
                "`账号` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL, " +
                "`招呼语` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL, " +
                "`为对方设置的备注` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL, " +
                "PRIMARY KEY (`账号`) USING BTREE " +
                ") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic";
    }
    public static String CREATE_FRIEND_CONTACTS_TABLE(String nickName) {
        return "CREATE TABLE `" + nickName + "_contacts`  (" +
                "`账号` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL, " +
                "`备注` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL, " +
                "`聊天时间` timestamp NOT NULL, " +
                "`聊天内容` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL, " +
                "PRIMARY KEY (`账号`) USING BTREE " +
                ") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic";
    }
    public static String INSERT_USER_APPLY_FOR_YOU_TABLE(User_message user_message) {
        return "insert into " + user_message.me_nickName +
                "_apply_friend (`账号`, `招呼语`, `为对方设置的备注`) " +
                "values('" + user_message.friend_account +
                "', '" + user_message.hello_text +
                "', '" + user_message.friend_name_for_add + "')";
    }
    public static String INSERT_USER_CONTACTS_TABLE(User_message user_message) {
        return "insert into " + user_message.me_nickName +
        "_contacts (`账号`, `备注`, `聊天时间`, `聊天内容`) " +
        "values('" + user_message.friend_account +
        "', '" + user_message.friend_name +
        "', '" + Timestamp.valueOf(user_message.last_chat_time) +
        "', '" + user_message.chat_text + "')";
    }
    public static String UPDATE_CHAT_USER_CONTACTS_TABLE(User_message user_message) {
        return "update " + user_message.me_nickName +
                "_contacts set `聊天时间` = '" + Timestamp.valueOf(user_message.last_chat_time) +
                "', `聊天内容` = '" + user_message.chat_text + "' " +
                "where `账号` = '" + user_message.friend_account + "'";
    }
}
