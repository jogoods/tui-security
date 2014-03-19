package tuisolutions.tuisecurity.utils.backup;

public class Message extends Resources {
    
    private String address;
    private long date;
    private String content;
    private int read;
    private int locked;
    private int status;
    private String type;
    
    public Message(String address, long date, String content, int read, int locked, int status, String type) {
        super();
        this.address = address;
        this.date = date;
        this.content = content;
        this.read = read;
        this.locked = locked;
        this.status = status;
        this.type = type;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public long getDate() {
        return date;
    }
    
    public void setDate(long date) {
        this.date = date;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public int getRead() {
        return read;
    }
    
    public void setRead(int read) {
        this.read = read;
    }
    
    public int getLocked() {
        return locked;
    }
    
    public void setLocked(int locked) {
        this.locked = locked;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
}
