package sample.demo2;

public class Destinations {
    private Integer destinationId;
    private String country;
    private String city;


    public Destinations(Integer destinationId, String country, String city) {
        this.destinationId = destinationId;
        this.country = country;
        this.city = city;
    }

    public Integer getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Integer destinationId) {
        this.destinationId = destinationId;
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

}
