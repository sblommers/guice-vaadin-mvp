/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.eventhandling.sharedmodel;

import com.google.code.vaadin.internal.eventhandling.MethodResolutionPredicates;
import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.EventBuses;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import net.engio.mbassy.common.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SharedModelEventBusSubscriber - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 14.02.13
 */
public class SharedModelEventBusSubscriber<T> implements MembersInjector<T> {

	/*===========================================[ STATIC VARIABLES ]=============*/

    private static final Logger logger = LoggerFactory.getLogger(SharedModelEventBusSubscriber.class);

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private Injector injector;

	/*===========================================[ CONSTRUCTORS ]=================*/

    public SharedModelEventBusSubscriber(Injector injector) {
        this.injector = injector;
    }

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void injectMembers(T instance) {
        Class<?> instanceClass = instance.getClass();
        if (!ReflectionUtils.getMethods(MethodResolutionPredicates.SharedModelEventHandlers, instanceClass).isEmpty()) {
            EventBus modelEventBus = injector.getInstance(Key.get(EventBus.class, EventBuses.SharedModelEventBus.class));
            modelEventBus.subscribe(instance);

            // Register instance as SharedEventBus subscriber to unsubscribe later on UI.detach
            injector.getInstance(AccessibleSharedEventBusSubscribersRegistry.class).registerSubscriber(instance);

            logger.info(String.format("[%s] subscribed to SharedModelEventBus [#%d]", instance, modelEventBus.hashCode()));
        }
    }
}
