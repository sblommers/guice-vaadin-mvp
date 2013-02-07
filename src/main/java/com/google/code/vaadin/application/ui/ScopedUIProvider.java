/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.application.ui;

import com.google.code.vaadin.application.uiscope.UIKey;
import com.google.code.vaadin.application.uiscope.UIKeyProvider;
import com.google.code.vaadin.application.uiscope.UIScope;
import com.google.code.vaadin.application.uiscope.UIScoped;
import com.google.code.vaadin.internal.servlet.MVPApplicationInitParameters;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;

/**
 * A Vaadin UI provider which supports the use of Guice scoped UI (see {@link UIScoped}). If you do not need UIScope,
 * then just extend from UIProvider directly
 * <p/>
 * Subclasses should implement getUIClass(UIClassSelectionEvent event) to provide logic for selecting the UI class.
 *
 * @author Alexey Krylov (AleX)
 * @since 08.02.13
 */
public class ScopedUIProvider extends UIProvider {

	/*===========================================[ STATIC VARIABLES ]=============*/

    private static final long serialVersionUID = -5773777009877153344L;

    private static Logger logger = LoggerFactory.getLogger(ScopedUIProvider.class);

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private UIKeyProvider uiKeyProvider;
    private Class uiClass;
    protected Injector injector;

	/*===========================================[ CONSTRUCTORS ]=================*/

    @Inject
    protected void init(Injector injector,
                               @Named(MVPApplicationInitParameters.P_APPLICATION_UI_CLASS) Class uiClass,
                               UIKeyProvider uiKeyProvider) {

        Preconditions.checkArgument(ScopedUI.class.isAssignableFrom(uiClass), String.format("ERROR: %s is not subclass of ScopedUI", uiClass.getName()));
        this.injector = injector;
        this.uiClass = uiClass;
        this.uiKeyProvider = uiKeyProvider;
    }

	/*===========================================[ CLASS METHODS ]================*/

    @Override
    public UI createInstance(UICreateEvent event) {
        return createInstance(event.getUIClass());
    }

    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        return uiClass;
    }

    protected UI createInstance(Class<? extends UI> uiClass) {
        UIKey uiKey = uiKeyProvider.get();
        // hold the key while UI is created
        CurrentInstance.set(UIKey.class, uiKey);
        // and set up the scope
        UIScope scope = UIScope.getCurrent();
        scope.startScope(uiKey);

        // create the UI
        ScopedUI ui = (ScopedUI) injector.getInstance(uiClass);
        ui.setInstanceKey(uiKey);
        ui.setScope(scope);
        logger.debug("returning instance of " + ui.getClass().getName() + " with key " + uiKey);
        return ui;
    }
}