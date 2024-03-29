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

package com.google.code.vaadin.internal.preconfigured;

import com.google.code.vaadin.application.uiscope.UIScoped;
import com.google.code.vaadin.localization.TextBundle;
import com.google.code.vaadin.mvp.eventhandling.EventType;
import com.google.code.vaadin.mvp.eventhandling.Observes;
import com.google.code.vaadin.mvp.eventhandling.events.LocaleChangedEvent;
import com.google.inject.Inject;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry of localizable components. Used to perform auto-localization if locale is changed.
 *
 * @author Alexey Krylov
 * @see VaadinComponentsInjector
 * @since 31.01.13
 */
@UIScoped
class LocalizableComponentsRegistry {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private Logger logger;

    @Inject(optional = true)
    private TextBundle textBundle;

    private Map<Component, String> localizedCaptions;
    private Map<Label, String> localizedLabelValues;

	/*===========================================[ CONSTRUCTORS ]=================*/

    LocalizableComponentsRegistry() {
        localizedCaptions = new HashMap<>();
        localizedLabelValues = new HashMap<>();
        logger = LoggerFactory.getLogger(getClass());
    }

	/*===========================================[ CLASS METHODS ]================*/

    @Observes(EventType.VIEW)
    void localeChanged(LocaleChangedEvent localeChangedEvent) {
        logger.info("Locale changed to: " + localeChangedEvent.getLocale());
        if (textBundle != null) {
            localize();
        } else {
            logger.error("ERROR: No TextBundle implementation found");
        }
        logger.debug("Localization finished");
    }

    void localize() {
        for (Map.Entry<Component, String> entry : localizedCaptions.entrySet()) {
            entry.getKey().setCaption(textBundle.getText(entry.getValue()));
        }

        for (Map.Entry<Label, String> entry : localizedLabelValues.entrySet()) {
            entry.getKey().setValue(textBundle.getText(entry.getValue()));
        }
    }

    void addLocalizedCaption(Component component, String captionKey) {
        localizedCaptions.put(component, captionKey);
    }

    void addLocalizedLabelValue(Label label, String labelValueKey) {
        localizedLabelValues.put(label, labelValueKey);
    }
}
