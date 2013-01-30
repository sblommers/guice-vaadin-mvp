/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

import com.google.code.vaadin.mvp.events.ContactOpenedEvent;

/**
 * TestView - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
public class TestViewImpl extends AbstractView implements TestView {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final long serialVersionUID = 4317442441310926792L;

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private boolean localChangedEventReceived;

	/*===========================================[ CLASS METHODS ]================*/

    @Override
    protected void localize() {
        localChangedEventReceived = true;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void openContact() {
        fireViewEvent(new ContactOpenedEvent());
    }

    /*===========================================[ CLASS METHODS ]================*/

    @Override
    protected void initView() {

    }

	/*===========================================[ GETTER/SETTER ]================*/

    @Override
    public boolean isLocaleChangedEventReceived() {
        return localChangedEventReceived;
    }
}
