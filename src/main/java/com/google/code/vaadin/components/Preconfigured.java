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

package com.google.code.vaadin.components;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Component;

import java.lang.annotation.*;

/**
 * Qualifier that can be used for declaratively defining Vaadin components configuration at Guice injection points.
 *
 * @author Alexey Krylov
 * @since 23.01.13
 */
//todo test injection of preconfigured into params
@Documented
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Preconfigured {
    /**
     * A key used for obtaining (localized) texts from TextBundle (assuming an
     * implementation of it is found). The acquired text is set as the
     * Component's caption.
     */
    String captionKey() default "";

    /**
     * A key used for obtaining (localized) texts from TextBundle (assuming an
     * implementation of it is found). The acquired text is set as the Label's
     * value.
     */
    String labelValueKey() default "";

    boolean immediate() default false;

    boolean nullSelectionAllowed() default true;

    String[] styleName() default {};

    boolean spacing() default false;

    boolean[] margin() default false;

    boolean sizeFull() default false;

    float height() default -1.0f;

    int heightUnits() default Sizeable.UNITS_PIXELS;

    float width() default -1.0f;

    int widthUnits() default Sizeable.UNITS_PIXELS;

    Class<? extends Component> implementation() default Component.class;

    boolean readOnly() default false;

    boolean enabled() default true;

    boolean visible() default true;

    String caption() default "";

    String debugId() default "";

    boolean sizeUndefined() default false;

    String description() default "";

    boolean required() default false;

    String requiredError() default "";

    boolean invalidAllowed() default true;

    boolean invalidCommitted() default false;

    boolean readTrough() default true;

    boolean writeTrough() default true;

    boolean validationVisible() default true;

    int tabIndex() default -1;

    boolean multiSelect() default false;

    boolean newItemsAllowed() default false;

    int itemCaptionMode() default -1;

    boolean localized() default true;
}
