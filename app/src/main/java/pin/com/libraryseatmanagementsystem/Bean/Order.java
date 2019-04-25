package pin.com.libraryseatmanagementsystem.Bean;

import java.io.Serializable;

public class Order implements Serializable{
	private int oid;
	private int rid;
	private int sid;
	private String ordertime;
	private String starttime;
	private String endtime;

	public Order() {
		super();
	}


	public Order(int oid, int rid, int sid, String ordertime, String starttime, String endtime) {
		super();
		this.oid = oid;
		this.rid = rid;
		this.sid = sid;
		this.ordertime = ordertime;
		this.starttime = starttime;
		this.endtime = endtime;
	}


	public int getOid() {
		return oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}


	public String getOrdertime() {
		return ordertime;
	}


	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}


	public String getStarttime() {
		return starttime;
	}


	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}


	public String getEndtime() {
		return endtime;
	}


	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	
	
}
