package pin.com.libraryseatmanagementsystem.Bean;

import java.io.Serializable;

public class Seat implements Serializable{

    private int sid;
    private int rid;
    private int sstate;

    public Seat() {
    }

    public Seat(int sid, int rid, int sstate) {
        this.sid = sid;
        this.rid = rid;
        this.sstate = sstate;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }
    public int getSid() {
        return sid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }
    public int getRid() {
        return rid;
    }

    public void setSstate(int sstate) {
        this.sstate = sstate;
    }
    public int getSstate() {
        return sstate;
    }

}