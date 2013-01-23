/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp.application;

import com.google.inject.servlet.RequestScoped;
import com.vaadin.ui.Window;

/**
 * RequestData - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 23.01.13
 */
@RequestScoped
public class RequestData {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private AbstractMVPApplication application;
    private Window window;

	/*===========================================[ GETTER/SETTER ]================*/

    public AbstractMVPApplication getApplication() {
        return application;
    }

    public void setApplication(AbstractMVPApplication application) {
        this.application = application;
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }
}