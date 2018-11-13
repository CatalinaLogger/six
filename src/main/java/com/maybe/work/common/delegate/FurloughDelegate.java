package com.maybe.work.common.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class FurloughDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("---------------------------------------------");
        System.out.println();
        System.out.println("Hello Service " + this.toString() + "Is Saying Hello To Every One !");
        System.out.println("---------------------------------------------");
        System.out.println();
    }
}
