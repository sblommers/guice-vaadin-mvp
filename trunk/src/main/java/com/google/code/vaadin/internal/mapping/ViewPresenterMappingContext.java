/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.mapping;

import com.google.code.vaadin.mvp.AbstractPresenter;
import com.google.code.vaadin.mvp.View;
import com.google.inject.servlet.SessionScoped;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MappingContext - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 25.01.13
 */
@SessionScoped
public class ViewPresenterMappingContext {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private Map<View, AbstractPresenter> activeMappings;

    /*===========================================[ CONSTRUCTORS ]=================*/

    ViewPresenterMappingContext() {
        activeMappings = new ConcurrentHashMap<View, AbstractPresenter>();
    }

    /*===========================================[ CLASS METHODS ]================*/

    protected <V extends View, P extends AbstractPresenter<V>> void addMapping(V view, P presenter) {
        activeMappings.put(view, presenter);
    }

    public <P extends AbstractPresenter<V>, V extends View> P getPresenterForView(V view) {
        return (P) activeMappings.get(view);
    }
}