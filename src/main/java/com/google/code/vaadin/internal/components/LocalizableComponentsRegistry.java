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

package com.google.code.vaadin.internal.components;

import com.google.code.vaadin.TextBundle;
import com.google.code.vaadin.mvp.Observes;
import com.google.code.vaadin.mvp.events.LocaleChangedEvent;
import com.google.inject.servlet.SessionScoped;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * LocalizableComponentsContainer - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 31.01.13
 */
@SessionScoped
class LocalizableComponentsRegistry {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private Logger logger;

    private TextBundle textBundle;

    private Map<Component, String> localizedCaptions;
    private Map<Label, String> localizedLabelValues;

	/*===========================================[ CONSTRUCTORS ]=================*/

    @Inject
    protected void init(Logger logger, TextBundle textBundle) {
        this.logger = logger;
        this.textBundle = textBundle;
        localizedCaptions = new HashMap<Component, String>();
        localizedLabelValues = new HashMap<Label, String>();
    }

	/*===========================================[ CLASS METHODS ]================*/

    @Observes
    void localeChanged(LocaleChangedEvent localeChangedEvent) {
        logger.info("Locale changed to: " + localeChangedEvent.getLocale());
        localize();
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
