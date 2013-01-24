/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.components;

import com.google.code.vaadin.TextBundle;
import com.google.code.vaadin.components.Preconfigured;
import com.google.code.vaadin.mvp.MVPApplicationException;
import com.google.inject.MembersInjector;
import com.vaadin.ui.*;

import java.lang.reflect.Field;

/**
 * VaddinControlsInjector - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 24.01.13
 */
public class VaddinComponentsInjector<T> implements MembersInjector<T> {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private Field field;
    private Preconfigured preconfigured;
    private TextBundle textBundle;

	/*===========================================[ CONSTRUCTORS ]=================*/

    public VaddinComponentsInjector(Field field, Preconfigured preconfigured, TextBundle textBundle) {
        this.field = field;
        this.textBundle = textBundle;
        this.preconfigured = preconfigured;
    }

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void injectMembers(T t) {
        try {
            Component component = (Component) field.getType().newInstance();
            field.set(t, configureComponent(component));
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
        if (component instanceof AbstractLayout) {
            boolean[] margin = preconfigured.margin();
            if (margin.length == 1) {
                ((AbstractLayout) component).setMargin(margin[0]);
            } else if (margin.length == 2) {
                Layout.MarginInfo mi = new Layout.MarginInfo(margin[0],
                        margin[1], margin[0], margin[1]);
                ((AbstractLayout) component).setMargin(mi);
            } else if (margin.length == 3) {
                Layout.MarginInfo mi = new Layout.MarginInfo(margin[0],
                        margin[1], margin[2], margin[1]);
                ((AbstractLayout) component).setMargin(mi);
            } else if (margin.length == 4) {
                Layout.MarginInfo mi = new Layout.MarginInfo(margin[0],
                        margin[1], margin[2], margin[3]);
                ((AbstractLayout) component).setMargin(mi);
            }
        }
        if (component instanceof AbstractOrderedLayout) {
            ((AbstractOrderedLayout) component)
                    .setSpacing(preconfigured.spacing());
        }
        return component;
    }

    @SuppressWarnings("OverlyComplexMethod")
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

        String caption = preconfigured.caption();
        if (caption.isEmpty()) {
            String captionKey = preconfigured.captionKey();
            if (!captionKey.isEmpty()) {
                component.setCaption(textBundle.getText(captionKey));
            }
        } else {
            component.setCaption(caption);
        }

        if (component instanceof Label) {
            String labelValueKey = preconfigured.labelValueKey();
            if (!labelValueKey.isEmpty()) {
                ((Label) component).setValue(textBundle.getText(labelValueKey));
            }
        }

        String debugId = preconfigured.debugId();
        if (!debugId.isEmpty()) {
            component.setDebugId(debugId);
        }

        if (preconfigured.sizeFull()) {
            component.setSizeFull();
        } else if (preconfigured.sizeUndefined()) {
            component.setSizeUndefined();
        } else {
            float width = preconfigured.width();
            if (width > -1.0f) {
                int widthUnits = preconfigured.widthUnits();
                component.setWidth(width, widthUnits);
            }
            float height = preconfigured.height();
            if (height > -1.0f) {
                int heightUnits = preconfigured.heightUnits();
                component.setHeight(height, heightUnits);
            }
        }
    }

    private static void configureFieldApi(com.vaadin.ui.Field field, Preconfigured preconfigured) {
        String description = preconfigured.description();
        if (!description.isEmpty()) {
            field.setDescription(description);
        }

        String requiredError = preconfigured.requiredError();
        if (!requiredError.isEmpty()) {
            field.setRequiredError(requiredError);
        }

        field.setRequired(preconfigured.required());
    }

    private static void configureAbstractFieldApi(AbstractField abstractField, Preconfigured preconfigured) {
        if (!(abstractField instanceof Form)) {
            abstractField.setInvalidAllowed(preconfigured.invalidAllowed());
        }
        abstractField.setInvalidCommitted(preconfigured.invalidCommitted());
        abstractField.setReadThrough(preconfigured.readTrough());
        abstractField.setWriteThrough(preconfigured.writeTrough());
        abstractField.setValidationVisible(preconfigured.validationVisible());
        if (preconfigured.tabIndex() > -1) {
            abstractField.setTabIndex(preconfigured.tabIndex());
        }
    }

    private static void configureAbstractSelectApi(AbstractSelect abstractSelect, Preconfigured preconfigured) {
        abstractSelect.setNullSelectionAllowed(preconfigured
                .nullSelectionAllowed());
        abstractSelect.setMultiSelect(preconfigured.multiSelect());
        abstractSelect.setNewItemsAllowed(preconfigured.newItemsAllowed());
        if (preconfigured.itemCaptionMode() > -1) {
            abstractSelect.setItemCaptionMode(preconfigured.itemCaptionMode());
        }
    }
}