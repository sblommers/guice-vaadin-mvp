/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

import com.google.code.vaadin.mvp.eventhandling.EventType;
import com.google.code.vaadin.mvp.eventhandling.Observes;
import com.google.code.vaadin.mvp.eventhandling.events.DoSomethingEvent;
import com.google.code.vaadin.mvp.eventhandling.events.ModelEvent;
import com.google.code.vaadin.mvp.eventhandling.events.SharedModelEvent;

import javax.inject.Inject;

/**
 * @author Alexey Krylov (lexx)
 * @since 18.03.13
 */
public class PresenterWithSharedModel extends AbstractPresenter<SimpleView> {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final long serialVersionUID = 7321423295563181353L;

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    // ModelWithSingletonDomainService will be instance per-UI because Presenter created once per UI
    @Inject
    private ModelWithSingletonDomainService model;

    private int receivedModelEventsCounter;
    private int receivedSharedModelEventsCounter;

    /*===========================================[ CLASS METHODS ]================*/

    @Observes
    private void on(DoSomethingEvent event) {
        model.invokeSingletonDomainService();
    }

    @Observes(EventType.MODEL)
    private void on(ModelEvent event) {
        receivedModelEventsCounter++;
    }

    @Observes(EventType.SHARED_MODEL)
    private void on(SharedModelEvent event) {
        receivedSharedModelEventsCounter++;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void initPresenter() {

    }

    @Override
    public void viewOpened() {

    }

    /*===========================================[ GETTER/SETTER ]================*/

    public int getReceivedSharedModelEventsCounter() {
        return receivedSharedModelEventsCounter;
    }

    public int getReceivedModelEventsCounter() {
        return receivedModelEventsCounter;
    }
}