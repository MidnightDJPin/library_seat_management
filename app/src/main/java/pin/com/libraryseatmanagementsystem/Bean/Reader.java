package pin.com.libraryseatmanagementsystem.Bean;



import java.io.Serializable;

public class Reader implements Serializable{
	private int rid;
	private String account;
	private String password;
	private String rname;
	private String phone;
	private String email;
	private boolean admin;
	private boolean rstate;
	private String bantime;
	
	public Reader() {
		super();
	} 

	public Reader(int rid, String account, String password, String rname, String phone, String email, boolean admin,
			boolean rstate, String bantime) {
		super();
		this.rid = rid;
		this.account = account;
		this.password = password;
		this.rname = rname;
		this.phone = phone;
		this.email = email;
		this.admin = admin;
		this.rstate = rstate;
		this.bantime = bantime;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isRstate() {
		return rstate;
	}

	public void setRstate(boolean rstate) {
		this.rstate = rstate;
	}

	public String getBantime() {
		return bantime;
	}

	public void setBantime(String bantime) {
		this.bantime = bantime;
	}


}
