package com.gamla.deepanshu.DeliveryAdress;

import java.io.Serializable;

/**
 * Created by Deepanshu on 12-04-2018.
 */

public class AdressBean implements Serializable {
    private String _id;
    private String _fullname;
    private String _mobilenumber;
    private String _pincode;
    private String _adress1;
    private String _adress2;
    private String _city;
    private String _state;
    private String _status;

    public String get_status() {
        return _status;
    }
    public void set_status(String _status) {
        this._status = _status;
    }
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_fullname() {
        return _fullname;
    }

    public void set_fullname(String _fullname) {
        this._fullname = _fullname;
    }

    public String get_mobilenumber() {
        return _mobilenumber;
    }

    public void set_mobilenumber(String _mobilenumber) {
        this._mobilenumber = _mobilenumber;
    }

    public String get_pincode() {
        return _pincode;
    }

    public void set_pincode(String _pincode) {
        this._pincode = _pincode;
    }

    public String get_adress1() {
        return _adress1;
    }

    public void set_adress1(String _adress1) {
        this._adress1 = _adress1;
    }

    public String get_adress2() {
        return _adress2;
    }

    public void set_adress2(String _adress2) {
        this._adress2 = _adress2;
    }

    public String get_city() {
        return _city;
    }

    public void set_city(String _city) {
        this._city = _city;
    }

    public String get_state() {
        return _state;
    }

    public void set_state(String _state) {
        this._state = _state;
    }


}
