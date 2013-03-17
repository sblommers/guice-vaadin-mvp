/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

import com.google.code.vaadin.mvp.eventhandling.EventBusType;
import com.google.code.vaadin.mvp.eventhandling.EventBusTypes;
import com.google.code.vaadin.mvp.eventhandling.EventPublisher;
import com.google.code.vaadin.mvp.eventhandling.events.ModelEvent;

import javax.inject.Inject;

/**
 * @author Alexey Krylov (lexx)
 * @since 18.03.13
 */
public class ModelWithSingletonDomainService {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private SingletonDomainService domainService;

    @Inject
    @EventBusType(EventBusTypes.MODEL)
    private EventPublisher eventPublisher;

    /*===========================================[ CLASS METHODS ]================*/

    public void invokeSingletonDomainService(){
        eventPublisher.publish(new ModelEvent());
        domainService.doSomething();
    }
}
