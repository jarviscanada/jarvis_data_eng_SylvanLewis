package ca.jrvs.insurance_api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Document("people")
public class Person {

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String firstName;
    private String lastName;
    private int age;
    private address address;
    private Date createdAt = new Date();
    private Boolean insurance;
    private List<car> cars = new ArrayList<>();

    public Person() {}

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public address getAddress() { return address; }
    public void setAddress(address address) { this.address = address; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Boolean getInsurance() { return insurance; }
    public void setInsurance(Boolean insurance) { this.insurance = insurance; }

    public List<car> getCars() { return cars; }
    public void setCars(List<car> cars) { this.cars = cars; }
}