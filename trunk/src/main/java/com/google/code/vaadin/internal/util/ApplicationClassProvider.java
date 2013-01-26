/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.util;

import com.google.code.vaadin.guice.AbstractMVPApplicationModule;
import com.google.code.vaadin.internal.servlet.MVPApplicationInitParameters;
import com.google.code.vaadin.mvp.MVPApplicationException;

import javax.servlet.ServletContext;

/**
 * ApplicationClassProvider - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 25.01.13
 */
public class ApplicationClassProvider {

	/*===========================================[ CONSTRUCTORS ]=================*/

    private ApplicationClassProvider() {
    }

	/*===========================================[ CLASS METHODS ]================*/

    public static Class<? extends AbstractMVPApplicationModule> getApplicationClass(ServletContext context) {
        try {
            return (Class<? extends AbstractMVPApplicationModule>) Class.forName(context.getInitParameter(MVPApplicationInitParameters.P_APPLICATION_MODULE));
        } catch (Exception e) {
            throw new MVPApplicationException(String.format("ERROR: Unable to instantiate class of [%s]. " +
                    "Please check your webapp deployment descriptor.", AbstractMVPApplicationModule.class.getName()), e);
        }
    }
}