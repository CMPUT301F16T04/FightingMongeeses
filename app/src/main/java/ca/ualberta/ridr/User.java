package ca.ualberta.ridr;

import java.sql.RowIdLifetime;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import io.searchbox.annotations.JestId;

/**
 * Created by mackenzie on 12/10/16.
 */
public class User {
    private String name;
    private Date dateOfBirth;
    private String creditCard;
    private String email;
    private String phoneNumber;
    private UUID id;

    public User(String name, Date dateOfBirth, String creditCard, String email, String phoneNumber){
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.creditCard = creditCard;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.id = UUID.randomUUID();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isOffline() {
        return false;
    }

    public void goOffline() {
    }

    public void goOnline() {
    }

    public String getStartLocation() {
        return "";
    }

    public String getDestination() {
        return "";
    }

    public boolean isEqual(User user) {
        return this.id.equals(user);
    }
}
