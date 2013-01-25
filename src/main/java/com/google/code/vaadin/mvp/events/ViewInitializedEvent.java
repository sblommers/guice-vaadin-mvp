/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp.events;

import com.google.code.vaadin.mvp.View;

/**
 * ViewInitializedEvent - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 25.01.13
 */
public class ViewInitializedEvent {
    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private final View view;
    private final Class<? extends View> viewInterface;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public ViewInitializedEvent(Class<? extends View> viewInterface, View view) {
        this.viewInterface = viewInterface;
        this.view = view;
    }

    /*===========================================[ GETTER/SETTER ]================*/

    public View getView() {
        return view;
    }

    public Class<? extends View> getViewInterface() {
        return viewInterface;
    }
}
