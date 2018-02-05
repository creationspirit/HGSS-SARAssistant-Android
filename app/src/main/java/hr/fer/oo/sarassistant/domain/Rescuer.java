package hr.fer.oo.sarassistant.domain;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nameless on 24.1.2018..
 */

public class Rescuer implements Parcelable {

    private long id;
    private String name;
    private String surname;
    private boolean available;

    private double lon;
    private double lat;
    private float distance;

    public Rescuer(long id, String name, String surname, boolean available, double lat, double lon) {
        this.name = name;
        this.surname = surname;
        this.available = available;
        this.lon = lon;
        this.lat = lat;
        this.distance = Float.NaN;
        this.id = id;
    }

    public Rescuer(Parcel in) {
        id = in.readLong();
        name = in.readString();
        surname = in.readString();
        available = in.readByte() != 0;
        lon = in.readDouble();
        lat = in.readDouble();
        this.distance = Float.NaN;
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

    public LatLng getLatLng() {
        return new LatLng(lat, lon);
    }

    public long getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(surname);
        parcel.writeByte((byte) (available ? 1 : 0));
        parcel.writeDouble(lon);
        parcel.writeDouble(lat);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Rescuer>() {

        @Override
        public Rescuer createFromParcel(Parcel parcel) {
            return new Rescuer(parcel);
        }

        @Override
        public Rescuer[] newArray(int i) {
            return new Rescuer[i];
        }
    };

    public float setDistanceFrom(double lat, double lon) {
        float[] results = new float[3];
        Location.distanceBetween(lat, lon, this.lat, this.lon, results);
        this.distance = results[0];
        return results[0];
    }

    public float getDistance(double lat, double lon) {
        if(this.distance != Float.NaN) return this.distance;
        else return setDistanceFrom(lat, lon);
    }

    public float getCachedDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Rescuer{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", available=" + available +
                ", lon=" + lon +
                ", lat=" + lat +
                ", distance=" + distance +
                '}';
    }
}
