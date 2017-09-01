package com.insping.libra.sock.net.codec.data;

public class UserInfo {
	private long uid;
	private int cid;
	private int eid;

	public UserInfo() {

	}


	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getEid() {
		return eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}
}
