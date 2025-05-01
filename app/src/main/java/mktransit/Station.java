package mktransit;

import java.util.List;

public class Station {
    private String id;
    private String name;
    private boolean interchange = false;
    private List<Connection> connections;
    private Line line;

    public Station(String id, String name, boolean interchange, List<Connection> connections) {
        this.id = id;
        this.name = name;
        this.interchange = interchange;
        this.connections = connections;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isInterchange() {
        return interchange;
    }

    public List<Connection> getConnections() {
        return connections;
    }
    
    public void setLine(Line line) {
        this.line = line;
    }
}
