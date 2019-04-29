package pin.com.libraryseatmanagementsystem.Bean;

public class OrderInfo {
    private String account;
    private String starttime;
    private String endtime;

    public OrderInfo() {
    }

    public OrderInfo(String account, String starttime, String endtime) {
        this.account = account;
        this.starttime = starttime;
        this.endtime = endtime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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
