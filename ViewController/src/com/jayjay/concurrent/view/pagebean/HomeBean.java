package com.jayjay.concurrent.view.pagebean;

import com.jayjay.concurrent.view.work.CentralContentTask;
import com.jayjay.concurrent.view.work.LeftContentTask;

import com.jayjay.concurrent.view.work.RightContentTask;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import oracle.adf.view.rich.component.rich.RichPoll;

import oracle.adf.view.rich.context.AdfFacesContext;

import org.apache.myfaces.trinidad.event.PollEvent;

public class HomeBean {
    private String leftContent;
    private String rightContent;
    private String mainContent;
    private Future<String> leftF;
    private Future<String> rightF;
    private Future<String> mainF;
    
    public HomeBean() {
        this.PerformTasks();   
    }
    
    public void PerformTasks() {
        System.out.println("start work" + new java.util.Date());
        ExecutorService es = Executors.newFixedThreadPool(3);
        
        //start on work: left content
        LeftContentTask leftTask = new LeftContentTask();
        leftF = es.submit(leftTask);
        
        //start on work: right content
        RightContentTask rightTask = new RightContentTask();
        rightF = es.submit(rightTask);
        
        //start on work: main content
        CentralContentTask mainTask = new CentralContentTask();
        mainF = es.submit(mainTask);
        
        es.shutdown();//important: need to shut down the ExecutorService otherwise memory leak would occur.


//        try {
//            leftContent = leftF.get();
//            rightContent = rightF.get();
//            mainContent = mainF.get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        
        System.out.println("End work" + new java.util.Date());

    }
    
    public void Refresh(PollEvent pollEvent) {
        
        System.out.println("====start poll event @ ====" + new Date());
        
        this.setMainContent();
        this.setRightContent();
        this.SetLeftContent();
        
        //currently there is an issue with the ADF poll component at the time of the writing
        //af:poll remains active even after the timeout period should have canceled any further events to the poll listener.
        //a workaround is to reset the interval to "-1" (to disable the polling) after the poll finish all his job.
        if (leftContent!=null && rightContent!=null && mainContent !=null) {
            RichPoll pollComp = (RichPoll)pollEvent.getComponent(); 
            pollComp.setInterval(-1);
            AdfFacesContext.getCurrentInstance().addPartialTarget(pollComp);
        }
        
        System.out.println("====end poll event @ ====" + new Date());
    }
    
    public void SetLeftContent() {
        if (leftContent==null || leftContent.isEmpty()) {
            try {
                if (leftF.isDone())
                leftContent = leftF.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } 
    }
    
    public void setRightContent() {
        if (rightContent==null || rightContent.isEmpty()) {
            try {
                if (rightF.isDone())
                rightContent = rightF.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }    
    }
    
    public void setMainContent() {
        if (mainContent==null || mainContent.isEmpty()) {
            try {
                if (mainF.isDone())
                mainContent = mainF.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }    
    }
    
    public String getLeftContent() {return leftContent;}
    
    public String getRightContent() {return rightContent;}
    
    public String getMainContent() {return mainContent;}
    
//    public String getLeftContent() {
//        String content = "Hello, I am from the left side.";
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return content;
//        
//    }
//    
//    public String getRightContent() {
//        String content = "Hello, I am from the right side.";
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return content;    
//    }
//    
//    public String getMainContent() {
//        String content = "Hello, I am the central part.";
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return content;    
//    }


}
