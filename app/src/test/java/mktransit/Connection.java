package mktransit;

public class Connection {
    private String to;
    private int time;
    private String type;
    private String line;

    public Connection(String to, int time, String type, String line) {
        this.to = to;
        this.time = time;
        this.type = type;
        this.line = line;
    }

    public String getTo() {
        return to;
    }

    public int getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getLine() {
        return line;
    }
}
