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

import com.google.code.vaadin.components.Preconfigured;
import com.google.code.vaadin.localization.TextBundle;
import com.google.code.vaadin.mvp.MVPApplicationException;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.MembersInjector;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Injects configured Vaadin control into the {@link Field} marked with {@link Preconfigured} annotation.
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
class VaadinComponentsInjector<T> implements MembersInjector<T> {

  	/*===========================================[ STATIC VARIABLES ]=============*/

    private static final Logger logger = LoggerFactory.getLogger(VaadinComponentsInjector.class);

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private Field field;
    private Preconfigured preconfigured;
    private final Injector injector;

    /*===========================================[ CONSTRUCTORS ]=================*/

    VaadinComponentsInjector(Field field, Preconfigured preconfigured, Injector injector) {
        this.field = field;
        this.preconfigured = preconfigured;
        this.injector = injector;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void injectMembers(T instance) {
        try {
            Component component = (Component) field.getType().newInstance();
            field.setAccessible(true);
            field.set(instance, configureComponent(component));
        } catch (Exception e) {
            throw new MVPApplicationException(e);
        }
    }

    /**
     * Applies @Preconfigured attributes to Vaadin Components.
     *
     * @param c
     *
     * @return
     */
    @SuppressWarnings("OverlyComplexMethod")
    private <T extends Component> T configureComponent(T c) {
        T component = c;

        if (!preconfigured.implementation().equals(Component.class)) {
            if (component.getClass().isAssignableFrom(
                    preconfigured.implementation())) {
                try {
                    component = (T) preconfigured.implementation()
                            .newInstance();
                } catch (Exception e) {
                    throw new MVPApplicationException(e);
                }
            }
        }
        configureComponentApi(component, preconfigured);

        if (component instanceof com.vaadin.ui.Field) {
            configureFieldApi((com.vaadin.ui.Field) component, preconfigured);
        }
        if (component instanceof AbstractField) {
            configureAbstractFieldApi((AbstractField) component,
                    preconfigured);
        }
        if (component instanceof AbstractComponent) {
            ((AbstractComponent) component).setImmediate(preconfigured
                    .immediate());
        }
        if (component instanceof AbstractSelect) {
            configureAbstractSelectApi((AbstractSelect) component,
                    preconfigured);
        }

        MarginInfo mi = null;
        boolean[] margin = preconfigured.margin();
        if (margin.length == 1) {
            mi = new MarginInfo(margin[0]);
        } else if (margin.length == 2) {
            mi = new MarginInfo(margin[0], margin[1], margin[0],
                    margin[1]);
        } else if (margin.length == 3) {
            mi = new MarginInfo(margin[0], margin[1], margin[2],
                    margin[1]);
        } else if (margin.length == 4) {
            mi = new MarginInfo(margin[0], margin[1], margin[2],
                    margin[3]);
        }

        if (mi != null) {
            if (component instanceof AbstractOrderedLayout) {
                ((AbstractOrderedLayout) component).setMargin(mi);
            } else if (component instanceof GridLayout) {
                ((GridLayout) component).setMargin(mi);
            }
        }

        if (component instanceof AbstractOrderedLayout) {
            ((AbstractOrderedLayout) component)
                    .setSpacing(preconfigured.spacing());
        }

        return component;
    }

    private void configureComponentApi(Component component, Preconfigured preconfigured) {
        component.setEnabled(preconfigured.enabled());
        component.setVisible(preconfigured.visible());
        component.setReadOnly(preconfigured.readOnly());

        String[] styleName = preconfigured.styleName();
        if (styleName.length > 0) {
            for (String style : styleName) {
                component.addStyleName(style);
            }
        }

        configureLocalization(component, preconfigured);

        String id = preconfigured.id();
        if (!id.isEmpty()) {
            component.setId(id);
        }

        if (preconfigured.sizeFull()) {
            component.setSizeFull();
        } else if (preconfigured.sizeUndefined()) {
            component.setSizeUndefined();
        } else {
            float width = preconfigured.width();
            if (width > -1.0f) {
                Sizeable.Unit widthUnits = preconfigured.widthUnits();
                component.setWidth(width, widthUnits);
            }
            float height = preconfigured.height();
            if (height > -1.0f) {
                Sizeable.Unit heightUnits = preconfigured.heightUnits();
                component.setHeight(height, heightUnits);
            }
        }
    }

    private void configureLocalization(Component component, Preconfigured preconfigured) {
        //Injector injector = InjectorProvider.getInjector(servletContext);
        TextBundle textBundle = null;
        try {
            textBundle = injector.getInstance(TextBundle.class);
        } catch (ConfigurationException e) {
            logger.error("ERROR: No TextBundle implementation registered!", e);
        }
        LocalizableComponentsRegistry componentsRegistry = injector.getInstance(LocalizableComponentsRegistry.class);

        String caption = preconfigured.caption();
        if (caption.isEmpty()) {
            String captionKey = preconfigured.captionKey();
            if (!captionKey.isEmpty()) {
                if (textBundle != null) {
                    component.setCaption(textBundle.getText(captionKey));
                    if (preconfigured.localized()) {
                        componentsRegistry.addLocalizedCaption(component, captionKey);
                    }
                } else {
                    component.setCaption(String.format("%s: No TextBundle implementation found!", captionKey));
                }
            }
        } else {
            component.setCaption(caption);
        }

        if (component instanceof Label) {
            String labelValueKey = preconfigured.labelValueKey();
            if (!labelValueKey.isEmpty()) {
                Label label = (Label) component;
                if (textBundle != null) {
                    label.setValue(textBundle.getText(labelValueKey));
                    if (preconfigured.localized()) {
                        componentsRegistry.addLocalizedLabelValue((Label) component, labelValueKey);
                    }
                } else {
                    label.setValue(String.format("%s: No TextBundle implementation found!", labelValueKey));
                }
            }
        }
    }
    //TODO check with cdi
    private static void configureAbstractFieldApi(AbstractField abstractField, Preconfigured preconfigured) {
        if (!(abstractField instanceof Form)) {
            abstractField.setInvalidAllowed(preconfigured.invalidAllowed());
        }
        abstractField.setInvalidCommitted(preconfigured.invalidCommitted());
        abstractField.setValidationVisible(preconfigured.validationVisible());
        if (preconfigured.tabIndex() > -1) {
            abstractField.setTabIndex(preconfigured.tabIndex());
        }
    }

    private static void configureFieldApi(com.vaadin.ui.Field field, Preconfigured preconfigured) {
        String requiredError = preconfigured.requiredError();
        if (!requiredError.isEmpty()) {
            field.setRequiredError(requiredError);
        }

        field.setRequired(preconfigured.required());
    }

    private static void configureAbstractSelectApi(AbstractSelect abstractSelect,
                                                   Preconfigured preconfigured) {
        abstractSelect.setNullSelectionAllowed(preconfigured
                .nullSelectionAllowed());
        abstractSelect.setMultiSelect(preconfigured.multiSelect());
        abstractSelect.setNewItemsAllowed(preconfigured.newItemsAllowed());
        // if (preconfigured.itemCaptionMode() > -1) {
        abstractSelect.setItemCaptionMode(preconfigured.itemCaptionMode());
        // }
    }
}
