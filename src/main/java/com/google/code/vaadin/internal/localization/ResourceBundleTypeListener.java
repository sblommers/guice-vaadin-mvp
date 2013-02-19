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

package com.google.code.vaadin.internal.localization;

import com.google.code.vaadin.localization.InjectBundle;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import java.lang.reflect.Field;
import java.util.ResourceBundle;

/**
 * Listens for {@link ResourceBundle} with {@link InjectBundle} annotation and registers {@link ResourceBundleInjector}
 * for them.
 *
 * @author Alexey Krylov
 * @since 07.02.13
 */
class ResourceBundleTypeListener implements TypeListener {

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public <T> void hear(TypeLiteral<T> type, TypeEncounter<T> encounter) {
        for (Field field : type.getRawType().getDeclaredFields()) {
            if (ResourceBundle.class.isAssignableFrom(field.getType())
                    && field.isAnnotationPresent(InjectBundle.class)) {
                InjectBundle injectBundle = field.getAnnotation(InjectBundle.class);
                encounter.register(new ResourceBundleInjector<T>(field, injectBundle));
            }
        }
    }
}