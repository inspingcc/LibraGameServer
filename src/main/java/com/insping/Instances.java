package com.insping;import com.insping.libra.account.AccountManager;import com.insping.libra.dao.db.DBManager;import com.insping.libra.sock.connection.ConnectionManager;import com.insping.libra.sock.net.handler.HandlerManager;public interface Instances {    public static final HandlerManager handlerMgr = HandlerManager.getInstance();    public static final ConnectionManager connectionMgr = ConnectionManager.getInstance();    public static final DBManager dbMgr = DBManager.getInstance();    public static final AccountManager accountMgr = AccountManager.getInstance();}