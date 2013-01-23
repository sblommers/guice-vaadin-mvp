/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

/**
 * AbstractView - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 23.01.13
 */
public abstract class AbstractView extends ViewComponent implements View {
    private boolean initialized;

    protected Class<? extends View> viewInterface;

    @Override
    public void openView() {
        if (viewInterface == null) {
            // Determine the view interface
            for (Class<?> clazz : getClass().getInterfaces()) {
                if (!clazz.equals(View.class)
                        && View.class.isAssignableFrom(clazz)) {
                    viewInterface = (Class<? extends View>) clazz;
                }
            }
        }
        if (!initialized) {
            initView();
            initialized = true;
            logger.info("View initialized: " + viewInterface);
        }

        fireViewEvent(viewInterface.getName() + AbstractPresenter.VIEW_OPEN,
                this);
        logger.info("View accessed: " + viewInterface);
    }

    /**
     * Initialize the view
     */
    protected abstract void initView();
}
