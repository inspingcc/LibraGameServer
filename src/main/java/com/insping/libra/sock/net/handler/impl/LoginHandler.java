package com.insping.libra.sock.net.handler.impl;

import java.util.Map;

import com.insping.common.i18.I18nGreeting;
import com.insping.common.utils.JsonUtil;
import com.insping.common.utils.StringUtils;
import com.insping.libra.account.AccountType;
import com.insping.libra.account.User;
import com.insping.libra.sock.net.handler.ServerHandler;
import com.insping.libra.sock.net.response.ReturnObject;
import com.insping.log.LibraLog;

public class LoginHandler extends ServerHandler {

    @Override
    public void doLogic(Object param, ReturnObject resp) throws Exception {
        String account = "";
        String passwd = "";

        // 检测账号合法性
        if (StringUtils.isNull(account) || StringUtils.isNull(passwd)) {
            resp.fail(I18nGreeting.HTTP_PARAMS_INVALID);
            return;
        }
        AccountType type = accountMgr.matchType(account);
        if (type == null) {
            resp.fail(I18nGreeting.ACCOUNT_OR_PASSWD_ERROR);
            return;
        }
        // 账号是否已经存在
        if (!accountMgr.accountIsExist(type, account)) {
            resp.fail(I18nGreeting.ACCOUNT_OR_PASSWD_ERROR);
            return;
        }
        User user = accountMgr.accountLogin(type, account, passwd);
        if (user == null) {
            resp.fail(I18nGreeting.ACCOUNT_OR_PASSWD_ERROR);
            return;
        }
        LibraLog.info("LoginHandler-doLogic : login success ,uid:" + user.getUid() + ",name:" + user.getName());
        resp.success(JsonUtil.ObjectToJsonString(user));
    }
}
