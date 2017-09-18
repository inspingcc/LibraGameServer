package com.insping.common.i18;

public class I18nGreeting {

	// 服务器handler异常
	public static final String SERVER_HANDLER_EXECPTION = "server_handler_execption";
	// http请求参数异常
	public static final String HTTP_PARAMS_INVALID = "http_params_invalid";
	// 账号已经存在
	public static final String ACCOUNT_ALREADY_EXIST = "account_already_exist";
	// 账号格式错误
	public static final String ACCOUNT_FORMAT_ERROR = "account_format_error";
	// 密码格式错误
	public static final String PASSWD_FORMAT_ERROR = "passwd_format_error";
	
	// 账号登录,账号密码不匹配
	public static final String ACCOUNT_OR_PASSWD_ERROR = "account_or_passwd_error";
	
	// 账号修改异常,请重试
	public static final String ACCOUNT_MODIFY_EXCEPTION = "account_modify_exception";
	
	// sessionkey已过期
	public static final String ACCOUNT_SESSIONKEY_OVERDUE = "account_sessionkey_overdue";

	// public static String search(String key, Object... params) {
	// Stringcontent vaule =
	// DataManager.getInstance().serach(Stringcontent.class, key);
	// if (vaule == null) {
	// return key;
	// }
	// String result = vaule.getContent();
	// if (params != null) {
	// for (int i = 0; i < params.length; i++) {
	// Object parma = params[i];
	// String regix = "{" + i + "}";
	// build = build.replace(regix, parma.toString());
	// }
	// }
	// return result;
	// }
}
