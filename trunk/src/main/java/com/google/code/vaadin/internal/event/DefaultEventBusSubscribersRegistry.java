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

package com.google.code.vaadin.internal.event;

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
 * @author Alexey Krylov
 * @since 27.01.13
 */
class DefaultEventBusSubscribersRegistry implements EventBusSubscribersRegistry {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private Map<String, Collection> subscribersMap;
    private Provider<HttpSession> httpSessionProvider;

    /*===========================================[ CONSTRUCTORS ]=================*/

    @Inject
    protected void init(Provider<HttpSession> httpSessionProvider) {
        subscribersMap = new ConcurrentHashMap<>();
        this.httpSessionProvider = httpSessionProvider;
    }

    /*===========================================[ CLASS METHODS ]================*/

    public void registerUIScopedSubscriber(Object subscriber) {
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
