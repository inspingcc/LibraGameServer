package com.insping.libra.account;

import com.insping.common.utils.StringUtils;
import com.insping.libra.dao.db.DaoData;
import com.insping.libra.dao.db.SqlData;
import com.insping.log.LibraLog;
import org.apache.commons.codec.digest.DigestUtils;

import com.insping.Instances;
import com.insping.common.utils.TimeUtils;

public class User implements DaoData, Instances {
    private long uid;
    private String account;
    private String phoneNumber;
    private String email;
    private String passwd;
    private String salt;
    private String sessionKey;
    private long registerTime;

    private String name;
    private String icon;
    private byte gender;// 0:男 . 1:女 . 2:保密
    private String address;
    private String introduction;

    public User() {

    }

    /**
     * @param type
     * @param account
     * @param passwd  采用常见的加密方式 sha1(sha1(passwd)+salt)
     */
    public User(AccountType type, String account, String passwd) {
        this.uid = accountMgr.idMake.incrementAndGet();
        switch (type) {
            case COMMON:
                this.account = account;
                break;
            case EMAIL:
                this.email = account;
                break;
            case PHONENUMBER:
                this.phoneNumber = account;
                break;
            default:
                LibraLog.error("new User is error by cauce is type is default");
        }
        this.salt = StringUtils.randomSalt();
        this.passwd = DigestUtils.sha1Hex(passwd + salt);
        this.registerTime = TimeUtils.nowLong();
        this.name = "libra_" + uid;
        this.icon = "blue_001";
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public User clone() {
        User user = new User();
        user.setUid(uid);
        user.setAccount(account);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setSessionKey(sessionKey);
        user.setRegisterTime(registerTime);

        user.setName(name);
        user.setIcon(icon);
        user.setGender(gender);
        user.setAddress(address);
        user.setIntroduction(introduction);
        return user;
    }

    @Override
    public String table() {
        return DaoData.TABLE_NAME_USER;
    }

    @Override
    public String[] wheres() {
        return new String[]{DaoData.USER_ID};
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public void insertData(SqlData data) {

    }

    /**
     * 保存user数据
     */
    public void save() {
        //// TODO db
        //// 暂时
        //accountMgr.users.put(account, this);
        //accountMgr.accounts.put(account, "");
        dbMgr.getAppDao().saveDaoData(this);
    }

    @Override
    public void loadFromData(SqlData data) {
        uid = data.getLong(DaoData.USER_ID);
        account = data.getString(DaoData.USER_ACCOUNT);
        phoneNumber = data.getString(DaoData.USER_PHONENUMBER);
        email = data.getString(DaoData.USER_EMAIL);
        passwd = data.getString(DaoData.USER_PASSWD);
        salt = data.getString(DaoData.USER_SALT);
        sessionKey = data.getString(DaoData.USER_SESSIONKEY);
        registerTime = data.getLong(DaoData.USER_REGISTERTIME);
        name = data.getString(DaoData.USER_NAME);
        icon = data.getString(DaoData.USER_ICON);
        gender = data.getByte(DaoData.USER_GENDER);
        address = data.getString(DaoData.USER_ADDRESS);
        introduction = data.getString(DaoData.USER_INTRODUCTION);
    }

    @Override
    public void saveToData(SqlData data) {
        data.put(DaoData.USER_ID, uid);
        data.put(DaoData.USER_ACCOUNT, account);
        data.put(DaoData.USER_PHONENUMBER, phoneNumber);
        data.put(DaoData.USER_EMAIL, email);
        data.put(DaoData.USER_PASSWD, passwd);
        data.put(DaoData.USER_SALT, salt);
        data.put(DaoData.USER_SESSIONKEY, sessionKey);
        data.put(DaoData.USER_REGISTERTIME, registerTime);
        data.put(DaoData.USER_NAME, name);
        data.put(DaoData.USER_ICON, icon);
        data.put(DaoData.USER_GENDER, gender);
        data.put(DaoData.USER_ADDRESS, address);
        data.put(DaoData.USER_INTRODUCTION, introduction);
    }

    @Override
    public void over() {

    }

    @Override
    public boolean saving() {
        return false;
    }
}
