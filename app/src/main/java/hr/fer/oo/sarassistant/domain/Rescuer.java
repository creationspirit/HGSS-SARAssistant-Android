package hr.fer.oo.sarassistant.domain;

/**
 * Created by nameless on 24.1.2018..
 */

public class Rescuer {

    private String name;
    private String surname;
    private boolean available;

    private double lon;
    private double lat;

    public Rescuer(String name, String surname, boolean available, double lon, double lat) {
        this.name = name;
        this.surname = surname;
        this.available = available;
        this.lon = lon;
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getFullName() {
        return name + " " + surname;
    }
}
