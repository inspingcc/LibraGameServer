package com.insping.libra.dao.db;

public interface DaoData {


    /**
     * 表名
     *
     * @return
     */
    String table();

    /**
     * 主键id
     *
     * @return
     */
    String[] wheres();

    /**
     * 是否删除 默认false. 当返回为true的条件下,saveDaoData()会被删除
     *
     * @return
     */
    boolean delete();

    /**
     * 插入数据,已废弃,推荐使用saveToData()
     *
     * @param data
     */
    void insertData(SqlData data);

    /**
     * 保存
     */
    void save();

    /**
     * 从data中加载数据到DaoData
     *
     * @param data
     */
    void loadFromData(SqlData data);

    /**
     * 将DaoData序列化成data
     *
     * @param data
     */
    void saveToData(SqlData data);

    /**
     * 在saveToData后执行的方法
     */
    void over();

    /**
     * 是否正在保存
     *
     * @return
     */
    boolean saving();


    // 用户表
    String TABLE_NAME_USER = "user";
    String USER_ID = "id";
    String USER_ACCOUNT = "account";
    String USER_EMAIL = "email";
    String USER_PHONENUMBER = "phoneNumber";
    String USER_PASSWD = "passwd";
    String USER_SALT = "salt";
    String USER_SESSIONKEY = "sessionKey";
    String USER_REGISTERTIME = "registerTime";

    String USER_NAME = "name";
    String USER_ICON = "icon";
    String USER_GENDER = "gender";
    String USER_ADDRESS = "address";
    String USER_INTRODUCTION = "introduction";

}
