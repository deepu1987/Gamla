package com.gamla.deepanshu.ProductDetail;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Deepanshu on 08-02-2018.
 */

public class PlantsListObject implements Serializable {


    private String _id;
    private String _name;
    private String _price;
    private String _image;
    private int _quantity;
    private String _description;
    private ArrayList<String> _gallryImage;
    public int get_quantity() {
        return _quantity;
    }

    public void set_quantity(int _quantity) {
        this._quantity = _quantity;
    }



    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }



    public ArrayList<String> get_gallryImage() {
        return _gallryImage;
    }

    public void set_gallryImage(ArrayList<String> _gallryImage) {
        this._gallryImage = _gallryImage;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_price() {
        return _price;
    }

    public void set_price(String _price) {
        this._price = _price;
    }

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }


}
