package com.farmmart.jonas.eazyfarmmart.other;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by JONAS on 6/8/2019.
 */

@IgnoreExtraProperties
public class Upload{

    public String name;
    public String price;
    public String url;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String name, String price, String url) {
        this.name = name;
        this.price = price;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}