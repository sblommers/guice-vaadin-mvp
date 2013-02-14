/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.application.ui;

import com.google.code.vaadin.application.MVPApplicationInitParameters;
import com.google.code.vaadin.application.uiscope.UIKey;
import com.google.code.vaadin.application.uiscope.UIKeyProvider;
import com.google.code.vaadin.application.uiscope.UIScope;
import com.google.code.vaadin.application.uiscope.UIScoped;
import com.google.code.vaadin.internal.eventhandling.sharedmodel.SharedEventBusSubscribersRegistry;
import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.EventBuses;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;
import org.slf4j.Logger;

import javax.inject.Named;
import java.util.Collection;

import static com.vaadin.server.ClientConnector.DetachListener;

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


	/*===========================================[ INSTANCE VARIABLES ]===========*/

    protected Logger logger;
    protected UIKeyProvider uiKeyProvider;
    protected Injector injector;
    protected Class uiClass;
    protected EventBus sharedEventBus;

	/*===========================================[ CONSTRUCTORS ]=================*/

    @Inject
    protected void init(Logger logger, Injector injector,
                        @Named(MVPApplicationInitParameters.P_APPLICATION_UI_CLASS) Class uiClass,
                        UIKeyProvider uiKeyProvider,
                        @EventBuses.SharedModelEventBus EventBus sharedEventBus) {
        Preconditions.checkArgument(ScopedUI.class.isAssignableFrom(uiClass), String.format("ERROR: %s is not subclass of ScopedUI", uiClass.getName()));

        this.logger = logger;
        this.injector = injector;
        this.uiClass = uiClass;
        this.uiKeyProvider = uiKeyProvider;
        this.sharedEventBus = sharedEventBus;
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

    public UI createInstance(Class<? extends UI> uiClass) {
        Preconditions.checkArgument(ScopedUI.class.isAssignableFrom(uiClass), "ERROR: Invalid configuration - using ScopedUIProvider to instantiate no ScopedUI subclass");

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

        ui.addDetachListener(createScopedUIDetachListener(uiClass, uiKey));

        logger.debug(String.format("Returning instance of [%s] with key [%s]", uiClass.getName(), uiKey));
        return ui;
    }

    protected DetachListener createScopedUIDetachListener(final Class<? extends UI> uiClass, final UIKey uiKey) {
        return new DetachListener() {
            private static final long serialVersionUID = -3087386509047842913L;

            @Override
            public void detach(ClientConnector.DetachEvent event) {
                logger.debug(String.format("Detaching [%s] with key [%s]", uiClass.getName(), uiKey));

                SharedEventBusSubscribersRegistry subscribersRegistry = injector.getInstance(SharedEventBusSubscribersRegistry.class);
                Collection<Object> uiScopedSubscribers = subscribersRegistry.removeAndGetSubscribers(uiKey);

                // Unsubscribe all non-singletons (UIScoped, nonscoped, etc) from SharedEventBus
                for (Object subscriber : uiScopedSubscribers) {
                    sharedEventBus.unsubscribe(subscriber);
                }

                logger.info(String.format("Detached [%s] with key [%s]", uiClass.getName(), uiKey));
            }
        };
    }
}