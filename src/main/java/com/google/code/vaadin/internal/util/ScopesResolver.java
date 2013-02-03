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

package com.google.code.vaadin.internal.util;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Scope;
import com.google.inject.internal.LinkedBindingImpl;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletScopes;
import com.google.inject.servlet.SessionScoped;
import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.spi.ExposedBinding;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;

/**
 * GuiceHelper - TODO: description
 *
 * @author Alexey Krylov
 * @since 26.01.13
 */
public class ScopesResolver {

    /*===========================================[ CONSTRUCTORS ]=================*/

    private ScopesResolver() {
    }

	/*===========================================[ CLASS METHODS ]================*/

    public static boolean isSessionScoped(Binding<?> binding) {
        return isScopedWith(binding, Arrays.asList(ServletScopes.SESSION), Arrays.<Class<? extends Annotation>>asList(SessionScoped.class));
    }

    public static boolean isRequestScoped(Binding<?> binding) {
        return isScopedWith(binding, Arrays.asList(ServletScopes.REQUEST), Arrays.<Class<? extends Annotation>>asList(RequestScoped.class));
    }

    public static boolean isScopedWith(Binding<?> binding, Collection<Scope> scopes, Collection<Class<? extends Annotation>> scopeAnnotations) {
        Binding<?> bindingToAnalyze = binding;

        do {
            boolean scopeFound = bindingToAnalyze.acceptScopingVisitor(new ScopeAnalyzer(scopes, scopeAnnotations));
            if (scopeFound) {
                return true;
            }

            if (bindingToAnalyze instanceof LinkedBindingImpl) {
                LinkedBindingImpl<?> linkedBinding = (LinkedBindingImpl) bindingToAnalyze;
                Injector injector = linkedBinding.getInjector();
                if (injector != null) {
                    bindingToAnalyze = injector.getBinding(linkedBinding.getLinkedKey());
                    continue;
                }
            } else if (bindingToAnalyze instanceof ExposedBinding) {
                ExposedBinding<?> exposedBinding = (ExposedBinding) bindingToAnalyze;
                Injector injector = exposedBinding.getPrivateElements().getInjector();
                if (injector != null) {
                    bindingToAnalyze = injector.getBinding(exposedBinding.getKey());
                    continue;
                }
            }

            return false;
        } while (true);
    }

	/*===========================================[ INNER CLASSES ]================*/

    private static class ScopeAnalyzer implements BindingScopingVisitor<Boolean> {
        private final Collection<Class<? extends Annotation>> scopeAnnotations;
        private final Collection<Scope> scopes;

        private ScopeAnalyzer(Collection<Scope> scopes, Collection<Class<? extends Annotation>> scopeAnnotations) {
            this.scopeAnnotations = scopeAnnotations;
            this.scopes = scopes;
        }

        @Override
        public Boolean visitNoScoping() {
            return true;
        }

        @Override
        public Boolean visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation) {
            boolean visitScopeResult = false;
            if (scopeAnnotations != null) {
                for (Class<? extends Annotation> annotation : scopeAnnotations) {
                    if (scopeAnnotation.equals(annotation)) {
                        visitScopeResult = true;
                        break;
                    }
                }
            }

            return visitScopeResult;
        }

        @Override
        public Boolean visitScope(Scope scope) {
            for (Scope s : scopes) {
                if (scope.equals(s)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public Boolean visitEagerSingleton() {
            return true;
        }
    }
}
