/*
 * Copyright (C) 2013 David Sowerby
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.google.code.vaadin.application.uiscope;

import com.google.code.vaadin.application.ui.ScopedUI;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class UIScope implements Scope {

    private static Logger logger = LoggerFactory.getLogger(UIScope.class);

    private static UIScope current;

    private final Map<UIKey, Map<Key<?>, Object>> cache = new TreeMap<>();

    public UIScope() {
        logger.debug("creating UIScope " + this);
    }

    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
        return new UIScopeProvider<>(key, unscoped);
    }

    private <T> Map<Key<?>, Object> getScopedObjectMap(UIKey uiKey) {
        // return an existing cache instance
        if (cache.containsKey(uiKey)) {
            Map<Key<?>, Object> scopedObjects = cache.get(uiKey);
            logger.debug("scope cache retrieved for UI key: " + uiKey);
            return scopedObjects;
        } else {

            return createCacheEntry(uiKey);
        }
    }

    public boolean isCacheHasEntryFor(UIKey uiKey) {
        return cache.containsKey(uiKey);
    }

    public boolean isCacheHasEntryFor(ScopedUI ui) {
        return isCacheHasEntryFor(ui.getInstanceKey());
    }

    public void startScope(UIKey uiKey) {
        if (!isCacheHasEntryFor(uiKey)) {
            createCacheEntry(uiKey);
        }
    }

    private Map<Key<?>, Object> createCacheEntry(UIKey uiKey) {
        Map<Key<?>, Object> uiEntry = new HashMap<>();
        cache.put(uiKey, uiEntry);
        logger.debug("created a scope cache for UIScope with key: " + uiKey);
        return uiEntry;
    }

    public void releaseScope(UIKey uiKey) {
        cache.remove(uiKey);
    }

    public static UIScope getCurrent() {
        if (current == null) {
            current = new UIScope();
        }
        return current;
    }

    /**
     * Removes all entries in the cache
     */
    public void flush() {
        cache.clear();
    }

    private class UIScopeProvider<T> implements Provider<T> {
        private final Key<T> key;
        private final Provider<T> unscoped;

        private UIScopeProvider(Key<T> key, Provider<T> unscoped) {
            this.key = key;
            this.unscoped = unscoped;
        }

        @Override
        public T get() {
            // get the scope cache for the current UI
            logger.debug("looking for a UIScoped instance of {}", key);

            // get the current UIKey. It should always be there, as it is created before the UI
            UIKey uiKey = CurrentInstance.get(UIKey.class);
            // this may be null if we are in the process of constructing the UI
            ScopedUI currentUI = (ScopedUI) UI.getCurrent();
            String msg = "This should not be possible, unless perhaps you are testing and have not set up the test fixture correctly.  Try sub-classing UITestBase and calling createTestUI() or createBasicUI() to prepare the UIScope correctly.  If you are not testing please report a bug";
            if (uiKey == null) {
                if (currentUI == null) {
                    throw new UIScopeException("UI and uiKey are null. " + msg);
                } else {
                    // this can happen when the framework switches UIs
                    uiKey = currentUI.getInstanceKey();
                    if (uiKey == null) {
                        throw new UIScopeException("uiKey is null and cannot be obtained from the UI. " + msg);
                    }
                }
            }

            // currentUI may be null if we are in the process of constructing the UI
            // if not null just check that it hasn't got out of sync with its uikey
            if (currentUI != null) {
                if (!uiKey.equals(currentUI.getInstanceKey())) {
                    throw new UIScopeException(
                            "The UI and its UIKey have got out of sync.  Results are unpredictable. " + msg);
                }
            }

            logger.debug("looking for cache for key: " + uiKey);
            Map<Key<?>, Object> scopedObjects = getScopedObjectMap(uiKey);
            // this line should fail tests but having trouble setting up a decent test. TestBench needed?
            // Map<Key<?>, Object> scopedObjects = getScopedObjectMap(CurrentInstance.get(UIKey.class));

            // retrieve an existing instance if possible

            @SuppressWarnings("unchecked")
            T current = (T) scopedObjects.get(key);

            if (current != null) {
                logger.debug("returning existing instance of " + current.getClass().getSimpleName());
                return current;
            }

            // or create the first instance and cache it
            current = unscoped.get();
            scopedObjects.put(key, current);
            logger.debug("new instance of " + current.getClass().getSimpleName() + " created, as none in cache");
            return current;
        }
    }
}