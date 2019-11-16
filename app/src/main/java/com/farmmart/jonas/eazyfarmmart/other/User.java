package com.farmmart.jonas.eazyfarmmart.other;

/**
 * Created by JONAS on 5/29/2019.
 */


import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas Korankye on 07/10/19.
 *
 */

@IgnoreExtraProperties
public class User {

    public String name;
    public String email;
    public String user_type;
    public String user_phone;
    public String location;
    public String farm_name;
    public String farmer_delivery;
    public String about_farm;
    public String pass;
    private String pushId;
    public List<String> farm_products = new ArrayList<>();


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String email, String pass,String user_type, String user_phone, String location, String farm_name, String farm_delivery, String about_farm,ArrayList<String> farm_products) {
        this.name = name;
        this.email = email;
        this.user_type = user_type;
        this.user_phone = user_phone;
        this.location = location;
        this.farm_name = farm_name;
        this.farmer_delivery = farm_delivery;
        this.about_farm = about_farm;
        this.pass = pass;
        this.farm_products = farm_products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFarm_name() {
        return farm_name;
    }

    public void setFarm_name(String farm_name) {
        this.farm_name = farm_name;
    }

    public String getFarmer_delivery() {
        return farmer_delivery;
    }

    public void setFarmer_delivery(String farmer_delivery) {
        this.farmer_delivery = farmer_delivery;
    }

    public String getAbout_farm() {
        return about_farm;
    }

    public void setAbout_farm(String about_farm) {
        this.about_farm = about_farm;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public List<String> getFarm_products() {
        return farm_products;
    }

    public void setFarm_products(List<String> farm_products) {
        this.farm_products = farm_products;
    }

    public String getPushId(){
        return pushId;
    }

    public void setPushId(String pushId){
        this.pushId = pushId;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", user_type='" + user_type + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", location='" + location + '\'' +
                ", farm_name='" + farm_name + '\'' +
                ", farmer_delivery='" + farmer_delivery + '\'' +
                ", about_farm='" + about_farm + '\'' +
                ", pass='" + pass + '\'' +
                ", farm_products=" + farm_products +
                '}';
    }
}
