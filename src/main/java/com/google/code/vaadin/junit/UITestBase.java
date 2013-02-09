package com.google.code.vaadin.junit;

import com.google.code.vaadin.application.ui.ScopedUI;
import com.google.code.vaadin.application.ui.ScopedUIProvider;
import com.google.code.vaadin.application.uiscope.UIKey;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.mycila.testing.junit.MycilaJunitRunner;
import com.mycila.testing.plugin.guice.GuiceContext;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;
import org.junit.Before;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.mockito.Mockito.mock;

/**
 * Extend this class to test anything related to a Vaadin UI (or in the case of V7, as {@link ScopedUI}. Note that the
 * {@link UIScope} is not prepared until the {@link #uiSetup()} method is called, so subclasses must use providers if
 * they want to inject UIScoped objects - otherwise the injection happens before the UIScope context is ready.
 * <p>
 * A number of providers are made available by the class
 * 
 * @author David Sowerby 18 Jan 2013
 * 
 */
@RunWith(MycilaJunitRunner.class)
@GuiceContext(value = BaseMVPApplicationTestModule.class, stage = Stage.PRODUCTION)
public abstract class UITestBase {

	protected VaadinRequest mockedRequest = mock(VaadinRequest.class);

	@Inject
	protected ScopedUIProvider provider;

	@Inject
	protected Injector injector;

	protected UI ui;

    @Before
	public void uiSetup() {
		System.out.println("initialising test");

        //todo ui needs to be set first
		ui = createTestUI();
		CurrentInstance.set(UI.class, ui);
		//when(mockedRequest.getParameter("v-loc")).thenReturn(baseUri + "/");
		ui.doInit(mockedRequest, 1);
	}

	/**
	 * Use this method to create TestUI instances, rather than the UIProvider It simulates the creation of a new
	 * CurrentInstance (which happens for each request)
	 * 
	 * @return
	 */
	protected TestUI createTestUI() {
		CurrentInstance.set(UI.class, null);
		CurrentInstance.set(UIKey.class, null);
		return (TestUI) provider.createInstance(TestUI.class);
	}

    protected static class TestUI extends ScopedUI {

        private static final long serialVersionUID = 5297142885176304733L;

        @Override
        protected void init(VaadinRequest request) {

        }
    }
}
