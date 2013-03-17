/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

import com.google.code.vaadin.mvp.eventhandling.events.DoSomethingEvent;

/**
 * @author Alexey Krylov (lexx)
 * @since 18.03.13
 */
public class SimpleViewImpl extends AbstractView implements SimpleView {

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void doSomething() {
        fireViewEvent(new DoSomethingEvent());
    }

    @Override
    protected void initView() {

    }
}
