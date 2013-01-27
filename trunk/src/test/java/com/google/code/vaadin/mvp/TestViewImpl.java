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

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void openContract(){
        fireViewEvent(new ContactOpenedEvent());
    }

    /*===========================================[ CLASS METHODS ]================*/

    @Override
    protected void initView() {

    }
}
