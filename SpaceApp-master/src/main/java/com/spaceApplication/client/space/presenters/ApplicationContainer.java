package com.spaceApplication.client.space.presenters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.spaceApplication.client.space.controllers.RemoteCalculationControl;
import com.spaceApplication.client.space.ui.components.UIConsts;


/**
 * Created by Chris
 */
public class ApplicationContainer extends Composite {

    interface ApplicationContainerUiBinder extends UiBinder<HTMLPanel, ApplicationContainer> {
    }

    private static ApplicationContainerUiBinder ourUiBinder = GWT.create(ApplicationContainerUiBinder.class);

    public ApplicationContainer() {
        initWidget(ourUiBinder.createAndBindUi(this));
        initWidget();
    }

    @UiField
    HTMLPanel pageContainer;
    @UiField
    HTMLPanel pageContent;
    @UiField
    HTMLPanel pageSidebar;

    private void initWidget(){
        PageSidebar pageSidebarContent = new PageSidebar();
        pageSidebar.add(pageSidebarContent);
        final ModelParameterCtrlPresenter tetherModelModelParameterCtrl = new ModelParameterCtrlPresenter();
        pageContent.add(tetherModelModelParameterCtrl.initWidget());
    }
}