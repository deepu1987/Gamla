package com.gamla.deepanshu.home;

import java.io.Serializable;

/**
 * Created by Deepanshu on 11-02-2018.
 */

public class ProductCatagoryListBean2 implements Serializable {

    private String _catagoryName;

    public String get_catagoryName() {
        return _catagoryName;
    }

    public void set_catagoryName(String _catagoryName) {
        this._catagoryName = _catagoryName;
    }

    public int get_categoryImage() {
        return _categoryImage;
    }

    public void set_categoryImage(int _categoryImage) {
        this._categoryImage = _categoryImage;
    }

    private int _categoryImage;
}
