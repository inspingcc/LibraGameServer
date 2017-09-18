package com.insping.libra.world;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.Properties;

import com.insping.Const;
import com.insping.log.LibraLog;

public class LibraConfig {
    public static long SYSTEM_TRANFOEM_ID = 1;// 服务器列表传输数据的用户编号
    public static int SERVER_ID = 20002;// 服务器列表实例号
    public static String SERVER_NAME = "服务器名字";// 服务器名称
    public static String SERVER_IP = "0.0.0.0";// 服务器IP地址

    public static int GATEWAY_SERVER_ID = 10002;// 网关服务器的ServerID
    public static String GATEWAY_IP = "192.168.0.104";// 对应网关服务器的IP
    public static int GATEWAY_PORT = 9051;// 网关服务器的端口
    public static long SERVER_HEARTBEAT_PERIOD = 3000*1000;// 向网关服务器发送的单位时间


    public static String COMMON_ACCOUNT_REGEX = "^[A-Za-z0-9]{6,16}";
    public static String EMAIL_ACCOUNT_REGEX = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
    public static String PHONE_ACCOUNT_REGEX = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}";

    public static void load() {
        try {
            // 加载部分本地数据
            SERVER_IP = InetAddress.getLocalHost().getHostAddress();
            // 加载配置文件
            Properties properties = new Properties();
            properties.load(new InputStreamReader(new BufferedInputStream(new FileInputStream(Const.CONF_PATH + "config.properties")), "UTF-8"));
            Field[] fields = LibraConfig.class.getDeclaredFields();
            for (Field field : fields) {
                String str = properties.getProperty(field.getName());
                if (str == null) {
                    continue;
                }
                loadOneProperty(field, str, null);
            }
            LibraLog.info("LibraConfig-load : Config Properties loaded!");
        } catch (Exception e) {
            LibraLog.error("LibraConfig-load : Exception is :" + e.getMessage());
        }

    }

    public static void loadOneProperty(Field f, String val, Object obj) throws Exception {
        if (f.getType() == byte.class) {
            f.set(obj, Byte.parseByte(val));
        } else if (f.getType() == int.class) {
            f.set(obj, Integer.parseInt(val));
        } else if (f.getType() == short.class) {
            f.set(obj, Short.parseShort(val));
        } else if (f.getType() == long.class) {
            f.set(obj, Long.parseLong(val));
        } else if (f.getType() == float.class) {
            f.set(obj, Float.parseFloat(val));
        } else if (f.getType() == boolean.class) {
            f.set(obj, Boolean.parseBoolean(val));
        } else if (f.getType() == String.class) {
            f.set(obj, val);
        } else if (f.getType() == Timestamp.class) {
            f.set(obj, Timestamp.valueOf(val));
        }
    }

}
