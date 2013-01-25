/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.mapping;

import com.google.code.vaadin.mvp.AbstractPresenter;
import com.google.code.vaadin.mvp.Observes;
import com.google.code.vaadin.mvp.View;
import com.google.code.vaadin.mvp.events.ViewOpenedEvent;
import com.google.inject.servlet.SessionScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MappingContext - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 25.01.13
 */
@SessionScoped
public class MappingContext {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final Logger logger = LoggerFactory.getLogger(MappingContext.class);

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private Map<View, AbstractPresenter> activeMappings;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public MappingContext() {
        activeMappings = new ConcurrentHashMap<View, AbstractPresenter>();
    }

    /*===========================================[ CLASS METHODS ]================*/

    protected void addMapping(View view, AbstractPresenter presenter) {
        activeMappings.put(view, presenter);
    }

    @Observes
    public void viewOpened(ViewOpenedEvent event) {
        logger.info("ViewOpenedEvent: " + event);

        AbstractPresenter abstractPresenter = activeMappings.get(event.getView());
        //5. Call viewOpened if appropriate event received from view
        abstractPresenter.viewOpened();
    }
}