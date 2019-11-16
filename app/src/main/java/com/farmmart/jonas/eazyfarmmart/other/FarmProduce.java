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
public class FarmProduce {

    public String produce_name;
    public String produce_category;
    public String produce_sellprice;
    public String produce_qty;





    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public FarmProduce() {
    }





    public FarmProduce(String produce_name, String produce_category, String produce_sellprice, String produce_qty) {
        this.produce_name = produce_name;
        this.produce_category = produce_category;
        this.produce_sellprice = produce_sellprice;
        this.produce_qty = produce_qty;
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

    public String getProduce_sellprice() {
        return produce_sellprice;
    }

    public void setProduce_sellprice(String produce_sellprice) {
        this.produce_sellprice = produce_sellprice;
    }

    public String getProduce_qty() {
        return produce_qty;
    }

    public void setProduce_qty(String produce_qty) {
        this.produce_qty = produce_qty;
    }



}
