package com.insping;import com.insping.libra.account.AccountManager;import com.insping.libra.dao.db.DBManager;import com.insping.libra.sock.net.handler.HandlerManager;public interface Instances {    HandlerManager handlerMgr = HandlerManager.getInstance();    DBManager dbMgr = DBManager.getInstance();    AccountManager accountMgr = AccountManager.getInstance();}