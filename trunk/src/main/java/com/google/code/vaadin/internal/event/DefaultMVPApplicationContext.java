/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event;

import com.google.code.vaadin.internal.servlet.MVPApplicationContext;
import com.google.common.base.Preconditions;

import javax.inject.Inject;
import javax.inject.Provider;
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
class DefaultMVPApplicationContext implements MVPApplicationContext {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private Map<String, Collection> subscribersMap;
    private Provider<HttpSession> httpSessionProvider;

    /*===========================================[ CONSTRUCTORS ]=================*/

    @Inject
    protected void init(Provider<HttpSession> httpSessionProvider) {
        subscribersMap = new ConcurrentHashMap<String, Collection>();
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

    @Override
    public Collection getAndRemoveSessionScopedSubscribers(String sessionID) {
        Preconditions.checkArgument(sessionID != null, "SessionID can't be null");
        return subscribersMap.remove(sessionID);
    }

    @Override
    public Collection getSessionScopedSubscribers(String sessionID) {
        Preconditions.checkArgument(sessionID != null, "SessionID can't be null");
        return subscribersMap.get(sessionID);
    }
}
