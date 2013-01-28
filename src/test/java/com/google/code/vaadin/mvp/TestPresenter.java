/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

import com.google.code.vaadin.mvp.events.ContactOpenedEvent;
import com.google.code.vaadin.mvp.events.DomainEvent;

import javax.inject.Inject;

/**
 * TestPresenter - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
public class TestPresenter extends AbstractPresenter<TestView> {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final long serialVersionUID = -6223121159195154865L;

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private boolean contactOpened;
    private boolean domainEventReceived;
    private boolean viewOpened;

    @Inject
    private TestDomainService domainService;

    /*===========================================[ CLASS METHODS ]================*/

    @Observes
    private void buttonClicked(ContactOpenedEvent contactOpenedEvent) {
        domainService.doSomething();
        contactOpened = true;
    }

    @Observes
    private void domainEventReceived(DomainEvent domainEvent) {
        domainEventReceived = true;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void initPresenter() {

    }

    @Override
    public void viewOpened() {
        viewOpened = true;
    }

    /*===========================================[ GETTER/SETTER ]================*/

    public boolean isContactOpened() {
        return contactOpened;
    }

    public boolean isViewOpened() {
        return viewOpened;
    }

    public boolean isDomainEventReceived() {
        return domainEventReceived;
    }
}