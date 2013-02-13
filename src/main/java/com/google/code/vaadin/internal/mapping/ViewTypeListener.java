/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.mapping;

import com.google.code.vaadin.mvp.AbstractPresenter;
import com.google.code.vaadin.mvp.AbstractView;
import com.google.code.vaadin.mvp.View;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ViewTypeListener - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 13.02.13
 */
class ViewTypeListener implements TypeListener {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private Provider<Injector> injectorProvider;

    protected Map<Class<? extends View>, Class<? extends AbstractPresenter>> viewPresenterMap;

	/*===========================================[ CONSTRUCTORS ]=================*/

    ViewTypeListener(Map<Class<? extends View>, Class<? extends AbstractPresenter>> viewPresenterMap) {
        this.viewPresenterMap = new ConcurrentHashMap<>(viewPresenterMap);
    }

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        encounter.register(new InjectionListener<I>() {
            @Override
            public void afterInjection(I injectee) {
                if (injectee instanceof View) {
                    View view = (View) injectee;
                    Class<? extends View> viewClass = view.getClass();

                    if (view instanceof AbstractView) {
                        viewClass = ((AbstractView) view).getViewInterface();
                    }

                    //4. Instantiate appropriate Presenter for View interface from event. Appropriate earlier created View will be injected - it's because SessionScope.
                    Class<? extends AbstractPresenter> presenterClass = viewPresenterMap.get(viewClass);
                    Injector injector = injectorProvider.get();

                    AccessibleViewProvider viewProvider = injector.getInstance(AccessibleViewProvider.class);
                    viewProvider.register(presenterClass,  view);

                    AbstractPresenter presenter = injector.getInstance(presenterClass);

                    AccessibleViewPresenterMappingRegistry mappingRegistry = injector.getInstance(AccessibleViewPresenterMappingRegistry.class);
                    mappingRegistry.registerMapping(view, presenter);

                    // Instantiate support for Presenter.viewOpened
                    injector.getInstance(ViewOpenedEventRedirector.class);
                }
            }
        });
    }
}
