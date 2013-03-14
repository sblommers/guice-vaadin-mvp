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

package com.google.code.vaadin.application.ui;

import com.google.code.vaadin.application.MVPApplicationInitParameters;
import com.google.code.vaadin.application.uiscope.UIKey;
import com.google.code.vaadin.application.uiscope.UIScope;
import com.google.code.vaadin.application.uiscope.UIScoped;
import com.google.code.vaadin.internal.eventhandling.AbstractEventBusModule;
import com.google.code.vaadin.internal.eventhandling.configuration.EventBusBinder;
import com.google.code.vaadin.internal.eventhandling.configuration.EventBusTypes;
import com.google.code.vaadin.internal.eventhandling.sharedmodel.SharedEventBusSubscribersRegistry;
import com.google.code.vaadin.internal.uiscope.UIKeyProvider;
import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;
import org.slf4j.Logger;

import javax.inject.Named;

import static com.vaadin.server.ClientConnector.DetachListener;

/**
 * A Vaadin UI provider which supports the use of Guice scoped UI (see {@link UIScoped}).
 * <p/>
 * Subclasses should implement getUIClass(UIClassSelectionEvent event) to provide logic for selecting the UI class.
 *
 * @author Alexey Krylov
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
    protected EventBusBinder eventBusBinder;

	/*===========================================[ CONSTRUCTORS ]=================*/

    @Inject
    protected void init(Logger logger, Injector injector,
                        @Named(MVPApplicationInitParameters.P_APPLICATION_UI_CLASS) Class uiClass,
                        UIKeyProvider uiKeyProvider,
                        EventBusBinder eventBusBinder) {
        Preconditions.checkArgument(ScopedUI.class.isAssignableFrom(uiClass), String.format("ERROR: %s is not subclass of ScopedUI", uiClass.getName()));

        this.logger = logger;
        this.injector = injector;
        this.uiClass = uiClass;
        this.uiKeyProvider = uiKeyProvider;
        this.eventBusBinder = eventBusBinder;
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

                // If Shared Model EventBus is present
                if (eventBusBinder.getBinding(EventBusTypes.SHARED_MODEL) != null) {
                    SharedEventBusSubscribersRegistry subscribersRegistry = injector.getInstance(SharedEventBusSubscribersRegistry.class);
                    Iterable<Object> uiScopedSubscribers = subscribersRegistry.removeAndGetSubscribers(uiKey);

                    EventBus sharedEventBus = injector.getInstance(Key.get(EventBus.class, AbstractEventBusModule.eventBusType(EventBusTypes.SHARED_MODEL)));
                    // Unsubscribe all non-singletons (UIScoped, nonscoped, etc) from SharedEventBus
                    for (Object subscriber : uiScopedSubscribers) {
                        sharedEventBus.unsubscribe(subscriber);
                    }
                }

                logger.info(String.format("Detached [%s] with key [%s]", uiClass.getName(), uiKey));
            }
        };
    }
}