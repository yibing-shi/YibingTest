package com.yibing;

import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: yibing
 * Date: 11-11-27
 * Time: 上午12:35
 * To change this template use File | Settings | File Templates.
 */
public class Feeder implements InitializingBean, DisposableBean{

    private GigaSpace gigaSpace;

    private int feedCount;

    @Required
    public void setGigaSpace (GigaSpace gigaSpace) {
        this.gigaSpace = gigaSpace;
    }

    @Required
    public void setFeedCount(int feedCount) {
        this.feedCount = feedCount;
    }

    public void destroy() throws Exception {

    }

    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < feedCount; ++i) {
                    gigaSpace.write(new Data(new Random().nextInt(9999)));
                    System.out.println(String.format("Write data %4d to space"));
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
