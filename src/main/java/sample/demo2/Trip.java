package sample.demo2;

import java.util.Date;

public class Trip {
    private Integer tripId;
    private Date startingDate;
    private Date endDate;
    private Integer nrNights;

    public Trip(Integer tripId, Date startingDate, Date endDate, Integer nrNights) {
        this.tripId = tripId;
        this.startingDate = startingDate;
        this.endDate = endDate;
        this.nrNights = nrNights;
    }

    public Integer getNrNights() {
        return nrNights;
    }

    public void setNrNights(Integer nrNights) {
        this.nrNights = nrNights;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
