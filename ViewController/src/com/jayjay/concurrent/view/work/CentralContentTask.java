package com.jayjay.concurrent.view.work;

import java.util.Date;
import java.util.concurrent.Callable;

public class CentralContentTask implements Callable {
    public CentralContentTask() {
        super();
    }

    public Object call() throws Exception {
        
        Date start = new java.util.Date();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Date end = new java.util.Date();
        Double delta = new Double(0.001*(end.getTime() - start.getTime()));
        
        String content = "Hello, I am the central part. my time cost - " + delta + " seconds";
        return content; 
    }
}
