package com.spaceApplication.client.space.presenters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;


/**
 * Created by Chris
 */
public class ApplicationContainer extends Composite {

    private static ApplicationContainerUiBinder ourUiBinder = GWT.create(ApplicationContainerUiBinder.class);
    @UiField
    HTMLPanel pageContainer;
    @UiField
    HTMLPanel pageContent;
    @UiField
    HTMLPanel pageSidebarPanel;
    private PageSidebar pageSidebarContent;
      
    public ApplicationContainer() {
        initWidget(ourUiBinder.createAndBindUi(this));
        initWidget();
    }

    public void clear() {
        pageContent.clear();
        final ModelParameterCtrlPresenter tetherModelModelParameterCtrl = new ModelParameterCtrlPresenter();
        pageContent.add(tetherModelModelParameterCtrl.initWidget());
    }

    public PageSidebar getPageSidebarContent() {
        return pageSidebarContent;
    }

    private void initWidget() {
        pageSidebarContent = new PageSidebar();
        pageSidebarPanel.add(pageSidebarContent);
        final ModelParameterCtrlPresenter tetherModelModelParameterCtrl = new ModelParameterCtrlPresenter();
        pageContent.add(tetherModelModelParameterCtrl.initWidget());
    }

    interface ApplicationContainerUiBinder extends UiBinder<HTMLPanel, ApplicationContainer> {
    }
}