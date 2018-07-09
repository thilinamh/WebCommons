/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nwised.javax.commons.utils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thilina_h
 */
@ApplicationScoped
public class ConcurrentManager {

    @Resource //(lookup="java:comp/DefaultManagedExecutorService")  
    ManagedExecutorService managedExecutorService;

    public ConcurrentManager() {

    }
    
    @PostConstruct
    private void init() {
        Logger.getLogger(ConcurrentManager.class.getCanonicalName()).log(Level.INFO, "ConcurrentManager instantiated");
    }

    public ExecutorService getExecutor() {

        return managedExecutorService;
    }

    @PreDestroy
    void freeExecutorService(){

    }

}
