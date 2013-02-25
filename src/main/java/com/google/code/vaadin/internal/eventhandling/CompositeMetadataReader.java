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

package com.google.code.vaadin.internal.eventhandling;

import com.google.code.vaadin.mvp.eventhandling.EventType;
import com.google.code.vaadin.mvp.eventhandling.Observes;
import com.google.common.base.Function;
import net.engio.mbassy.common.ReflectionUtils;
import net.engio.mbassy.listener.*;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.collect.Collections2.transform;

/**
 * MBassador metadata reader with {@link Observes} annotation support.
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
class CompositeMetadataReader extends MetadataReader {

  /*===========================================[ INSTANCE VARIABLES ]===========*/

    private EventType eventType;

    /*===========================================[ CONSTRUCTORS ]=================*/

    CompositeMetadataReader(EventType eventType) {
        this.eventType = eventType;
    }

    /*===========================================[ CLASS METHODS ]================*/

    @Override
    public List<MessageHandlerMetadata> getMessageHandlers(Class<?> target) {
        final Iterable<MessageHandlerMetadata> listenerMessageHandlers = new LinkedList(super.getMessageHandlers(target));
        Collection<MessageHandlerMetadata> observerHandlers = findObservesMethods(target);

        // Заменяем MessageHandlerMetadata взятые из @Observer на метаданные из @Listener
        return new ArrayList<>(transform(observerHandlers, new Function<MessageHandlerMetadata, MessageHandlerMetadata>() {
            @Override
            public MessageHandlerMetadata apply(@Nullable MessageHandlerMetadata input) {
                if (input != null) {
                    for (MessageHandlerMetadata listenerMessageHandler : listenerMessageHandlers) {
                        if (listenerMessageHandler.getHandler().equals(input.getHandler())) {
                            return listenerMessageHandler;
                        }
                    }
                }
                return input;
            }
        }));
    }

    protected Collection<MessageHandlerMetadata> findObservesMethods(Class<?> target) {
        // получаем все методы с аннотацией @Observes
        List<Method> observerMethods = ReflectionUtils.getMethods(MethodResolutionPredicates.getEventHandlersPredicate(eventType), target);
        // оставляем только те, которые наверху иерархии классов (самый верхний переопределяющий метод)
        Collection<Method> bottomMostHandlers = new LinkedList<>();
        for (Method handler : observerMethods) {
            if (!ReflectionUtils.containsOverridingMethod(observerMethods, handler)) {
                bottomMostHandlers.add(handler);
            }
        }

        Collection<MessageHandlerMetadata> filteredObserverHandlers = new LinkedList<>();

        // для каждого обработчика не будет переопределяющего метода с явно указанной аннотацией @Observes
        // но переопределяющий метод наследует конфигурацию переопределенного метода
        for (Method handler : bottomMostHandlers) {
            Observes observer = handler.getAnnotation(Observes.class);
            Listener listener = handler.getAnnotation(Listener.class);
            if (observer.enabled() && (listener == null || listener.enabled())) {
                Method overriddenHandler = ReflectionUtils.getOverridingMethod(handler, target);
                // Если обработчик переопределен, то он наследует от конфигурацию от родительского метода
                MessageHandlerMetadata handlerMetadata = new MessageHandlerMetadata(overriddenHandler == null ? handler : overriddenHandler,
                        getFilter(observer), new MappedListener());
                filteredObserverHandlers.add(handlerMetadata);
            }
        }
        return filteredObserverHandlers;
    }

    protected IMessageFilter[] getFilter(Observes observer) {
        return new IMessageFilter[0];
    }

    /*===========================================[ INNER CLASSES ]================*/

    @SuppressWarnings("ClassExplicitlyAnnotation")
    private static class MappedListener implements Listener {
        @Override
        public Filter[] filters() {
            return new Filter[0];
        }

        @Override
        public Mode delivery() {
            return Mode.Sequential;
        }

        @Override
        public boolean enabled() {
            return true;
        }

        @Override
        public int priority() {
            return 0;
        }

        @Override
        public boolean rejectSubtypes() {
            return false;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Listener.class;
        }
    }
}