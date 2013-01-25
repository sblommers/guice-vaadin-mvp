/*
 * Copyright (C) 2013 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.code.vaadin.mvp;

import com.netflix.governator.lifecycle.LifecycleManager;
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
 * @author Alexey Krylov
 * @since 23.01.13
 */
public abstract class AbstractMVPApplication extends Application implements
        HttpServletRequestListener {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final long serialVersionUID = -9162640299567428524L;

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private RequestContext requestContext;

    @Inject
    private LifecycleManager lifecycleManager;

    /*===========================================[ CONSTRUCTORS ]=================*/

    @Override
    public void init() {
/*
        try {
            lifecycleManager.start();
        } catch (Exception e) {
            throw new MVPApplicationException(e);
        }
*/

        initApplication();
    }

    /*===========================================[ CLASS METHODS ]================*/

    protected abstract void initApplication();

    @Override
    public void close() {
        super.close();
/*
        // support for @PostConstruct
        lifecycleManager.close();
*/
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
                    throw new MVPApplicationException(
                            "instantiateNewWindowIfNeeded() should only be used "
                                    + "for instantiating new Windows. Populate the Window"
                                    + "in buildNewWindow(Window)");
                }
                window.setName(name);
                addWindow(window);
                requestContext.setWindow(window);
                buildNewWindow(window);
                window.open(new ExternalResource(window.getURL()));
            }
        }
        requestContext.setWindow(window);
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

    /*===========================================[ INTERFACE METHODS ]============*/

    public void onRequestStart(HttpServletRequest request,
                               HttpServletResponse response) {
        requestContext.setApplication(this);
    }

    public void onRequestEnd(HttpServletRequest request,
                             HttpServletResponse response) {
        // NOP
    }
}