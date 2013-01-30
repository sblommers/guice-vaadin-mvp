/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
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
class LocalizableComponentsContainer {

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
