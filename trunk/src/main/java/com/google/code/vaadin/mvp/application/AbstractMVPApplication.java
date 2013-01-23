/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp.application;

import com.google.code.vaadin.mvp.GuiceVaadinMVPException;
import com.google.inject.Injector;
import com.vaadin.Application;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.Window;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AbstractMVPApplication - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 23.01.13
 */
public class AbstractMVPApplication extends Application implements
        HttpServletRequestListener {

    @Inject
    private BeanManager beanManager;

    @Inject
    private RequestData requestData;


    @Override
    public void close() {
        super.close();
        //beanStore.dereferenceAllBeanInstances();
    }

    @Override
    public void onRequestStart(HttpServletRequest request,
                               HttpServletResponse response) {
        requestData.setApplication(this);
    }

    Injector getInjector() {
        if (beanStore == null) {
            beanStore = new ApplicationBeanStore(beanManager);
        }
        return beanStore;
    }

    @Override
    public void onRequestEnd(HttpServletRequest request,
                             HttpServletResponse response) {
        // NOP
    }

    /**
     * Do not override unless you know what you are doing!
     */
    @Override
    public Window getWindow(String name) {
        Window window = getExistingWindow(name);
        if (window == null) {
            window = instantiateNewWindowIfNeeded(name);
            if (window != null) {
                if (window.getContent().getComponentIterator().hasNext()) {
                    throw new GuiceVaadinMVPException(
                            "instantiateNewWindowIfNeeded() should only be used "
                                    + "for instantiating new Windows. Populate the Window"
                                    + "in buildNewWindow(Window)");
                }
                window.setName(name);
                addWindow(window);
                requestData.setWindow(window);
                buildNewWindow(window);
                window.open(new ExternalResource(window.getURL()));
            }
        }
        requestData.setWindow(window);
        return window;
    }

    protected Window getExistingWindow(String name) {
        return super.getWindow(name);
    }

    /**
     * If multi-window support required, this method should be overridden to
     * return a new Window instance. The window should not have any content at
     * this point and most importantly, no VaadinScoped references should be
     * requested during this method. The created Window should be populated in
     * buildNewWindow(Window)
     *
     * @param name
     *
     * @return
     */
    protected Window instantiateNewWindowIfNeeded(String name) {
        return null;
    }

    /**
     * If multi-window support required, this method should be overridden to
     * populate the content of the window instantiated in
     * instantiateNewWindowIfNeeded(String).
     *
     * @param newWindow
     */
    protected void buildNewWindow(Window newWindow) {
        // NOP
    }

}