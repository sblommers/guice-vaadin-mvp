/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.mapping;

import com.google.code.vaadin.application.uiscope.UIScoped;
import com.google.code.vaadin.mvp.AbstractPresenter;
import com.google.code.vaadin.mvp.View;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AccessibleViewProvider - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 14.02.13
 */

@UIScoped
class AccessibleViewProvider implements ViewProvider {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private final Map<Class<? extends AbstractPresenter>, View> presenterViewMap;

	/*===========================================[ CONSTRUCTORS ]=================*/

    AccessibleViewProvider() {
        presenterViewMap = new ConcurrentHashMap<>();
    }

	/*===========================================[ CLASS METHODS ]================*/

    void register(Class<? extends AbstractPresenter> presenterClass, View view) {
        presenterViewMap.put(presenterClass, view);

    }

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public <V extends View> V getView(AbstractPresenter<V> presenter) {
        return (V) presenterViewMap.get(presenter.getClass());
    }
}
