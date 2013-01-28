/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.mapping;

import com.google.code.vaadin.mvp.EventBus;
import com.google.code.vaadin.mvp.EventBuses;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SessionContext - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 27.01.13
 */
@Singleton
public class MVPApplicationContext {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private Map<String, Collection> subscribersMap;
    private EventBus globalModelEventBus;
    private Provider<HttpSession> httpSessionProvider;

    /*===========================================[ CONSTRUCTORS ]=================*/

    @Inject
    public void init(@EventBuses.GlobalModelEventBus EventBus globalModelEventBus, Provider<HttpSession> httpSessionProvider) {
        subscribersMap = new ConcurrentHashMap<String, Collection>();
        this.globalModelEventBus = globalModelEventBus;
        this.httpSessionProvider = httpSessionProvider;
    }

    /*===========================================[ CLASS METHODS ]================*/

    public void registerSessionScopedSubscriber(Object subscriber) {
        String sessionID = httpSessionProvider.get().getId();
        Collection subscribers = subscribersMap.get(sessionID);
        if (subscribers == null) {
            subscribers = Collections.synchronizedList(new ArrayList());
            subscribersMap.put(sessionID, subscribers);
        }

        subscribers.add(subscriber);
    }

    public void destroySession(String sessionID) {
        Collection subscribers = subscribersMap.remove(sessionID);
        if (subscribers != null) {
            for (Object subscriber : subscribers) {
                globalModelEventBus.unsubscribe(subscriber);
            }
        }
    }
}
