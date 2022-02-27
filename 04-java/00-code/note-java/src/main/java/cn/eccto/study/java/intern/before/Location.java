package cn.eccto.study.java.intern.before;

/**
 * description
 *
 * @author chen 2022/02/22 21:56
 */
public class Location {
    private SharedLocation sharedLocation;
    double longitude;
    double latitude;

    public SharedLocation getSharedLocation() {
        return sharedLocation;
    }

    public void setSharedLocation(SharedLocation sharedLocation) {
        this.sharedLocation = sharedLocation;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
