package com.insping;

import com.insping.libra.sock.SockServer;
import com.insping.libra.world.LibraConfig;
import com.insping.log.LibraLog;

public class ServerApp implements Instances {
	public static void main(String[] args) {
		// 日志初始化
		LibraLog.init();
		// 加载config
		LibraConfig.load();
		// 加载handler
		handlerMgr.register();
		// 加载数据库
		dbMgr.init();
		// 初始化id生成
		accountMgr.init();
		// 启动服务
		SockServer.getInstance().start();
	}
}
