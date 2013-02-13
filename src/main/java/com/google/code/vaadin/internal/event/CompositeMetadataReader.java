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

package com.google.code.vaadin.internal.event;

import com.google.common.base.Predicate;
import com.sun.istack.internal.Nullable;
import net.engio.mbassy.common.ReflectionUtils;
import net.engio.mbassy.listener.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.collect.Collections2.filter;

/**
 * CompositeMetadataReader - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
class CompositeMetadataReader extends MetadataReader {

    /*===========================================[ CLASS METHODS ]================*/

    @Override
    public List<MessageHandlerMetadata> getMessageHandlers(Class<?> target) {
        final List<MessageHandlerMetadata> messageHandlers = new LinkedList(super.getMessageHandlers(target));

        // get all methods with @Observes annotation
        List<Method> observerMethods = ReflectionUtils.getMethods(MethodResolutionPredicates.AllEventHandlers, target);
        // retain only those that are at the bottom of their respective class hierarchy (deepest overriding method)
        List<Method> bottomMostHandlers = new LinkedList<Method>();
        for (Method handler : observerMethods) {
            if (!ReflectionUtils.containsOverridingMethod(observerMethods, handler)) {
                bottomMostHandlers.add(handler);
            }
        }

        List<MessageHandlerMetadata> filteredObserverHandlers = new LinkedList<>();
        // for each handler there will be no overriding method that specifies @Observes annotation
        // but an overriding method does inherit the listener configuration of the overwritten method
        for (Method handler : bottomMostHandlers) {
            Method overriddenHandler = ReflectionUtils.getOverridingMethod(handler, target);
            // if a handler is overwritten it inherits the configuration of its parent method
            MessageHandlerMetadata handlerMetadata = new MessageHandlerMetadata(overriddenHandler == null ? handler : overriddenHandler,
                    new IMessageFilter[0], new MappedListener());
            filteredObserverHandlers.add(handlerMetadata);
        }

        /**
         *  Remove from all @Observer handlers methods with @Listener annotation.
         *  This is required to allow user to define one method with two annotation
         */
        messageHandlers.addAll(filter(filteredObserverHandlers, new Predicate<MessageHandlerMetadata>() {
            @Override
            public boolean apply(@Nullable MessageHandlerMetadata observerHandler) {
                for (MessageHandlerMetadata messageHandler : messageHandlers) {
                    if (messageHandler.getHandler().equals(observerHandler.getHandler())) {
                        return false;
                    }
                }
                return true;
            }
        }));

        return messageHandlers;
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