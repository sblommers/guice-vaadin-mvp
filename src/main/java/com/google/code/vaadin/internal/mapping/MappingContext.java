/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.mapping;

import com.google.code.vaadin.mvp.AbstractPresenter;
import com.google.code.vaadin.mvp.View;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MappingContext - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 25.01.13
 */
public class MappingContext {

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

    public AbstractPresenter getPresenterForView(View view) {
        return activeMappings.get(view);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MappingContext");
        sb.append("{activeMappings=").append(activeMappings);
        sb.append('}');
        return sb.toString();
    }
}