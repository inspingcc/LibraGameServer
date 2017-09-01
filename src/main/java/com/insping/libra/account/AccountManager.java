package com.insping.libra.account;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import com.insping.Instances;
import com.insping.common.utils.StringUtils;
import com.insping.libra.dao.db.ConditionUnit;
import com.insping.libra.dao.db.DaoData;
import com.insping.libra.dao.db.Operator;
import com.insping.libra.dao.db.SqlData;
import com.insping.libra.world.LibraConfig;
import org.apache.commons.codec.digest.DigestUtils;

import com.insping.log.LibraLog;

public class AccountManager implements Instances {
    private AccountManager() {
    }

    private static AccountManager instance = new AccountManager();

    public static AccountManager getInstance() {
        return instance;
    }

    // uid 生成器
    AtomicLong idMake = null;

    // 所有用户
    Map<String, User> users = new ConcurrentHashMap<String, User>();

    // 所有的用户账号
    Map<String, String> accounts = new ConcurrentHashMap<String, String>();

    public void init() {
        String sql = "select IFNULL(MAX(id),9999) from user";
        long id = dbMgr.getAppDao().getPrimaryKeyData(sql);
        idMake = new AtomicLong(id);
    }

    /**
     * 账号注册
     *
     * @param account
     * @param password
     * @return
     */
    public User accountRegister(AccountType type, String account, String password) {
        User user = new User(type, account, password);
        // 保存到数据库
        user.save();
        return user.clone();
    }

    /**
     * 账号登录
     *
     * @param type
     * @param account
     * @param password
     * @return
     */
    public User accountLogin(AccountType type, String account, String password) {
        User user = searchUser(type, account);
        if (user == null) {
            LibraLog.info("AccountManager-accountLogin : exception error ");
            return null;
        }
        if (user.getPasswd().equals(DigestUtils.sha1Hex(password + user.getSalt()))) {
            // 验证密码正确
            // 生成对应的sessionKey
            user.setSessionKey(UUID.randomUUID().toString());
            return user.clone();
        } else {
            return null;
        }
    }

    public User accountModifyPasswd(Map<String, String> params) {

        return null;
    }

    public User accountModifyInfo(Map<String, String> params) {

        return null;
    }

    public boolean accountVerifyKey(Map<String, String> params) {

        return true;
    }

    /**
     * 账号是邮件
     *
     * @param account
     * @return
     */
    public boolean accountIsEmail(String account) {
        return true;
    }

    /**
     * 账号是手机号码
     *
     * @param account
     * @return
     */
    public boolean accountIsPhoneNo(String account) {
        return true;
    }

    /**
     * 账号是否已经存在
     *
     * @param account
     * @return
     */
    public boolean accountIsExist(AccountType type, String account) {
        ConditionUnit condition = null;
        switch (type) {
            case COMMON:
                condition = new ConditionUnit(DaoData.USER_ACCOUNT, Operator.EQ, account);
                break;
            case EMAIL:
                condition = new ConditionUnit(DaoData.USER_EMAIL, Operator.EQ, account);
                break;
            case PHONENUMBER:
                condition = new ConditionUnit(DaoData.USER_PHONENUMBER, Operator.EQ, account);
                break;
            default:
                LibraLog.error("accountIsExist: switch is default error!");
                return true;
        }
        return dbMgr.getAppDao().getData(DaoData.TABLE_NAME_USER, condition) != null;
    }

    /**
     * 账号格式合法
     *
     * @param account
     * @return
     */
    public boolean accountIsValid(AccountType type, String account) {
        String regex = "";
        switch (type) {
            case COMMON:
                regex = LibraConfig.COMMON_ACCOUNT_REGEX;
                break;
            case EMAIL:
                regex = LibraConfig.EMAIL_ACCOUNT_REGEX;
                break;
            case PHONENUMBER:
                regex = LibraConfig.PHONE_ACCOUNT_REGEX;
                break;
            default:
                return false;
        }
        return Pattern.matches(regex, account);
    }

    /**
     * 获取用户
     *
     * @param type
     * @param account
     * @return
     */
    public User searchUser(AccountType type, String account) {
        String key = "";
        switch (type) {
            case COMMON:
                key = DaoData.USER_ACCOUNT;
                break;
            case EMAIL:
                key = DaoData.USER_EMAIL;
                break;
            case PHONENUMBER:
                key = DaoData.USER_PHONENUMBER;
                break;
        }
        if (StringUtils.isNull(key)) {
            LibraLog.info("AccountManager-searchUser: key is null!");
            return null;
        }
        SqlData data = dbMgr.getAppDao().getData(DaoData.TABLE_NAME_USER, new ConditionUnit(key, Operator.EQ, account));
        if (data == null) {
            LibraLog.info("AccountManager-searchUser: data is null!");
            return null;
        }
        User user = new User();
        user.loadFromData(data);
        return user;
    }

    /**
     * 匹配当前账号的类型
     *
     * @param account
     * @return
     */
    public AccountType matchType(String account) {
        if (Pattern.matches(LibraConfig.COMMON_ACCOUNT_REGEX, account)) {
            return AccountType.COMMON;
        }
        if (Pattern.matches(LibraConfig.EMAIL_ACCOUNT_REGEX, account)) {
            return AccountType.EMAIL;
        }
        if (Pattern.matches(LibraConfig.PHONE_ACCOUNT_REGEX, account)) {
            return AccountType.PHONENUMBER;
        }
        return null;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(StringUtils.randomSalt());
        }
    }
}
