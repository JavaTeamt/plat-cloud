package com.czkj.common.entity.vo;


import com.czkj.common.entity.TabCustomer;
import com.czkj.common.entity.TabSubscriber;

import java.io.Serializable;


/**
 * @author SunMin
 * @description 实体封装作为参数
 * @create 2020/4/16
 * @since 1.0.0
 */
public class CustomerAndUser implements Serializable {
    //用户
    private TabSubscriber tabSubscriber;
    //客户
    private TabCustomer tabCustomer;


    public TabSubscriber getTabSubscriber() {
        return tabSubscriber;
    }

    public void setTabSubscriber(TabSubscriber tabSubscriber) {
        this.tabSubscriber = tabSubscriber;
    }

    public TabCustomer getTabCustomer() {
        return tabCustomer;
    }

    public void setTabCustomer(TabCustomer tabCustomer) {
        this.tabCustomer = tabCustomer;
    }

    @Override
    public String toString() {
        return "CustomerAndUser{" +
                "tabSubscriber=" + tabSubscriber +
                ", tabCustomer=" + tabCustomer +
                '}';
    }
}
