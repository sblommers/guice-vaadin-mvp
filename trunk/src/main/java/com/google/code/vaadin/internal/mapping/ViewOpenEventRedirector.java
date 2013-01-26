/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.mapping;

import com.google.code.vaadin.mvp.AbstractPresenter;
import com.google.code.vaadin.mvp.Observes;
import com.google.code.vaadin.mvp.View;
import com.google.code.vaadin.mvp.events.ViewOpenedEvent;
import com.google.inject.Injector;
import org.slf4j.Logger;

import java.util.Map;

/**
 * PresenterInitializer - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 25.01.13
 */

public class ViewOpenEventRedirector {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    protected Logger logger;
    protected Map<Class<? extends View>, Class<? extends AbstractPresenter>> viewPresenterMap;
    protected Injector injector;
    protected Class applicationClass;

    /*===========================================[ CONSTRUCTORS ]=================*/


    @Observes
    public void viewOpened(ViewOpenedEvent event) {
        Class<? extends View> viewInterface = event.getViewInterface();
        Class<? extends AbstractPresenter> presenterClass = viewPresenterMap.get(viewInterface);
        //5. Call viewOpened if appropriate event received from view
        injector.getInstance(presenterClass).viewOpened();
    }
}
