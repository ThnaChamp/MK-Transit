package mktransit;

import java.util.List;

public class Line {
    private String name;
    private String color;
    private List<Station> stations;

    //Constructore
    public Line(String name, String color, List<Station> stations) {
        this.name = name;
        this.color = color;
        this.stations = stations;
    }
    
    //Method
    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void addStation(Station station) {
        stations.add(station);
    }
}
