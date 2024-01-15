package sample.demo2;

import java.util.Date;

public class Booking {
    private int bookingId;
    private String country;
    private String city;
    private String hotelName;
    private Integer hotelPrice;
    private Integer nrNights;
    private Date startingDate;
    private Date endDate;

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

    public Integer getNrNights() {
        return nrNights;
    }

    public void setNrNights(Integer nrNights) {
        this.nrNights = nrNights;
    }

    public Booking(int bookingId, String country, String city, String hotelName, Integer hotelPrice, Integer nrNights, Date startingDate, Date endDate) {
        this.bookingId = bookingId;
        this.country = country;
        this.city = city;
        this.hotelName = hotelName;
        this.hotelPrice = hotelPrice;
        this.nrNights=nrNights;
        this.startingDate=startingDate;
        this.endDate=endDate;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Integer getHotelPrice() {
        return hotelPrice;
    }

    public void setHotelPrice(Integer hotelPrice) {
        this.hotelPrice = hotelPrice;
    }

    public Integer calculateTotalPrice() {
        try {
            int totalCost = hotelPrice * nrNights;
            return totalCost;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
