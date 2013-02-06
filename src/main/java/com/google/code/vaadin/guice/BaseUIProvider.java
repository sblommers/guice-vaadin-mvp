/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.guice;

import com.google.code.vaadin.internal.servlet.MVPApplicationInitParameters;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * AbstractUIProvider - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 07.02.13
 */
public class BaseUIProvider extends UIProvider {

	/*===========================================[ STATIC VARIABLES ]=============*/

    private static final long serialVersionUID = 4251780771194008820L;

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    @Named(MVPApplicationInitParameters.P_APPLICATION_UI_CLASS)
    private Class uiClass;

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        return uiClass;
    }
}
