package com.gamla.deepanshu.home;

import java.io.Serializable;

/**
 * Created by Deepanshu on 21-05-2018.
 */

public class PromotionProductBean implements Serializable {
    private String _id;
    private String _promotionCategoryId;
    private String _productid;
    private String _promotionCategoryName;
    private String _promotionName;
    private String _promotionImageUrl;
    private String _discount;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_promotionCategoryId() {
        return _promotionCategoryId;
    }

    public void set_promotionCategoryId(String _promotionCategoryId) {
        this._promotionCategoryId = _promotionCategoryId;
    }

    public String get_productid() {
        return _productid;
    }

    public void set_productid(String _productid) {
        this._productid = _productid;
    }

    public String get_promotionCategoryName() {
        return _promotionCategoryName;
    }

    public void set_promotionCategoryName(String _promotionCategoryName) {
        this._promotionCategoryName = _promotionCategoryName;
    }

    public String get_promotionName() {
        return _promotionName;
    }

    public void set_promotionName(String _promotionName) {
        this._promotionName = _promotionName;
    }

    public String get_promotionImageUrl() {
        return _promotionImageUrl;
    }

    public void set_promotionImageUrl(String _promotionImageUrl) {
        this._promotionImageUrl = _promotionImageUrl;
    }

    public String get_discount() {
        return _discount;
    }

    public void set_discount(String _discount) {
        this._discount = _discount;
    }

}
