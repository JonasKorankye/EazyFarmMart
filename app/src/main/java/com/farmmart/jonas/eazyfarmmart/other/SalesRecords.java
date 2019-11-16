package com.farmmart.jonas.eazyfarmmart.other;

/**
 * Created by JONAS on 5/29/2019.
 */


import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Jonas Korankye on 07/10/19.
 *
 */

@IgnoreExtraProperties
public class SalesRecords {

    public String produce_name;
    public String produce_category;
    public String amount_sold;
    public String date_sold;
    public String bought_by;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public SalesRecords() {
    }

    public SalesRecords(String produce_name, String produce_category, String amount_sold, String date_sold, String bought_by) {
        this.produce_name = produce_name;
        this.produce_category = produce_category;
        this.amount_sold = amount_sold;
        this.date_sold = date_sold;
        this.bought_by = bought_by;
    }


    public String getProduce_name() {
        return produce_name;
    }

    public void setProduce_name(String produce_name) {
        this.produce_name = produce_name;
    }

    public String getProduce_category() {
        return produce_category;
    }

    public void setProduce_category(String produce_category) {
        this.produce_category = produce_category;
    }

    public String getAmount_sold() {
        return amount_sold;
    }

    public void setAmount_sold(String amount_sold) {
        this.amount_sold = amount_sold;
    }

    public String getDate_sold() {
        return date_sold;
    }

    public void setDate_sold(String date_sold) {
        this.date_sold = date_sold;
    }

    public String getBought_by() {
        return bought_by;
    }

    public void setBought_by(String bought_by) {
        this.bought_by = bought_by;
    }
}
