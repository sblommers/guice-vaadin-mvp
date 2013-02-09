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
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.vaadin.ui.Component;

import javax.inject.Inject;
import java.lang.reflect.Field;

/**
 * Listens for fields of type {@link Component} and marked with {@link Preconfigured} annotation.
 * For this kind of fields {@link VaadinComponentsInjector} will be registered.
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
class VaadinComponentsInjectionListener implements TypeListener {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private Provider<Injector> injectorProvider;

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public <T> void hear(TypeLiteral<T> type, TypeEncounter<T> encounter) {
        for (Field field : type.getRawType().getDeclaredFields()) {
            if (Component.class.isAssignableFrom(field.getType())
                    && field.isAnnotationPresent(Preconfigured.class)) {
                Preconfigured preconfigured = field.getAnnotation(Preconfigured.class);
                encounter.register(new VaadinComponentsInjector<T>(field, preconfigured, injectorProvider.get()));
            }
        }
    }
}