package com.yibing;

import com.gigaspaces.annotation.pojo.SpaceRouting;

/**
 * Created by IntelliJ IDEA.
 * User: yibing
 * Date: 11-11-27
 * Time: 下午11:06
 */
public class Data {
    private Integer id;

    Data() {
        id = null;
    }

    Data(Integer id) {
        this.id = id;
    }

    @SpaceRouting
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Data:" + id;
    }
}
