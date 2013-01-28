/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.mapping;

import com.google.code.vaadin.mvp.AbstractPresenter;
import com.google.code.vaadin.mvp.ViewPresenterMappingContext;
import com.google.code.vaadin.mvp.Observes;
import com.google.code.vaadin.mvp.events.ViewOpenedEvent;
import com.google.inject.servlet.SessionScoped;

import javax.inject.Inject;

/**
 * PresenterInitializer - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 25.01.13
 */
@SessionScoped
class ViewOpenedEventRedirector {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private ViewPresenterMappingContext mappingContext;

    /*===========================================[ CLASS METHODS ]================*/

    @Observes
    public void viewOpened(ViewOpenedEvent event) {
        AbstractPresenter abstractPresenter = mappingContext.getPresenterForView(event.getView());
        //5. Call viewOpened if appropriate event received from view
        abstractPresenter.viewOpened();
    }
}
