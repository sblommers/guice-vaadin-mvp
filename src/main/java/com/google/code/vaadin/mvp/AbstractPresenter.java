/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

import com.google.inject.Injector;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * AbstractPresenter - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 23.01.13
 */
public abstract class AbstractPresenter<T extends View> implements Serializable {
    @Inject
    protected transient Logger logger;

    @Inject
    private Injector injector;

    protected T view;

    public static final String VIEW_OPEN = "AbstractPresenter_vo";

    @SuppressWarnings("unchecked")
    @PostConstruct
    protected void postConstruct() {
        // ViewInterface must be defined
        //todo detect via reflection
        //view = (T) viewInstance.select(viewInterface).get();
        //TypeResolver typeResolver = new TypeResolver();
//listType =>List<Object>
        //ResolvedType listType = typeResolver.resolve(List.class);

        initPresenter();
        logger.info("Presenter initialized: " + getClass());
    }

    /**
     * Initializes the presenter.
     */
    protected abstract void initPresenter();

    /**
     * Performs view actions called each time the view is opened.
     */
    public abstract void viewOpened();
}
