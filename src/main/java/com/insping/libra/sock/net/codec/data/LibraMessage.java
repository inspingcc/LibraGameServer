package com.insping.libra.sock.net.codec.data;

import com.insping.Instances;

public class LibraMessage implements Instances {
	private LibraHead head = new LibraHead();// 消息头
	private Object body;// 消息体

	public LibraMessage() {

	}

	public LibraMessage(LibraHead head, Object body) {
		this.head = head;
		this.body = body;
	}


	public LibraHead getHead() {
		return head;
	}

	public void setHead(LibraHead head) {
		this.head = head;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

}
