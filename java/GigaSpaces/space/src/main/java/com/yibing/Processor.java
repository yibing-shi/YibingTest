package com.yibing;

import org.openspaces.events.EventTemplate;
import org.openspaces.events.adapter.SpaceDataEvent;

/**
 * Created by IntelliJ IDEA.
 * User: yibing
 * Date: 11-11-28
 * Time: 上午12:08
 * To change this template use File | Settings | File Templates.
 */
public class Processor {
    @EventTemplate
    public Data getTemplate() {
        return new Data();
    }

    @SpaceDataEvent
    public void eventListener(Data data) {
        System.out.println("Receive Data " + data.toString());
    }
}
