package com.spaceApplication.client.space.presenters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Chris
 */
public class PageSidebar extends Composite {
    interface PageSidebarUiBinder extends UiBinder<Widget, PageSidebar> {
    }
    @UiField
    Anchor importModel;
    @UiField
    Anchor exportResults;

    private static PageSidebarUiBinder ourUiBinder = GWT.create(PageSidebarUiBinder.class);

    public PageSidebar() {
        initWidget(ourUiBinder.createAndBindUi(this));
        initWidget();
    }

    private ClickHandler showTestSampleClickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {

        }
    };

    public void enableExportResultsAnchor(String exportResultsHref){
        exportResults.setHref(exportResultsHref);
        exportResults.getElement().getParentElement().getParentElement().removeClassName("disables");
        exportResults.getElement().getParentElement().getParentElement().addClassName("active-page");
    }

    private void initWidget() {
    }
}