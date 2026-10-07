package ca.jrvs.insurance_api.model;

public class address {

    private int streetNumber;
    private String streetName;
    private String city;
    private String country;
    private String postalCode;

    public address() {}

    public int getStreetNumber() { return streetNumber; }
    public void setStreetNumber(int streetNumber) { this.streetNumber = streetNumber; }

    public String getStreetName() { return streetName; }
    public void setStreetName(String streetName) { this.streetName = streetName; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

}