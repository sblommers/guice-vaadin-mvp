/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.eventhandling.view;

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
 * ViewEventBusSubscriber - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 14.02.13
 */
public class ViewEventBusSubscriber<T> implements MembersInjector<T> {

	/*===========================================[ STATIC VARIABLES ]=============*/

    private static final Logger logger = LoggerFactory.getLogger(ViewEventBusSubscriber.class);

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private Injector injector;

	/*===========================================[ CONSTRUCTORS ]=================*/

    public ViewEventBusSubscriber(Injector injector) {
        this.injector = injector;
    }

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void injectMembers(T instance) {
        Class<?> instanceClass = instance.getClass();
        if (!ReflectionUtils.getMethods(MethodResolutionPredicates.ViewEventHandlers, instanceClass).isEmpty()) {
            EventBus viewEventBus = injector.getInstance(Key.get(EventBus.class, EventBuses.ViewEventBus.class));
            viewEventBus.subscribe(instance);
            logger.info(String.format("[%s] subscribed to ViewEventBus [#%d]", instance.toString(), viewEventBus.hashCode()));
        }
    }
}