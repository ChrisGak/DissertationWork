package com.spaceApplication.client.space.presenters;

import com.google.gwt.core.client.GWT;
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
    private static PageSidebarUiBinder ourUiBinder = GWT.create(PageSidebarUiBinder.class);
    @UiField
    Anchor importModel;
    @UiField
    Anchor exportResults;
    public PageSidebar() {
        initWidget(ourUiBinder.createAndBindUi(this));
        initWidget();
    }

    public void enableExportResultsAnchor(String exportResultsHref) {
        exportResults.setHref(exportResultsHref);
        exportResults.getElement().getParentElement().removeClassName("disables");
        exportResults.getElement().getParentElement().addClassName("active-page");
    }

    private void initWidget() {
    }

    interface PageSidebarUiBinder extends UiBinder<Widget, PageSidebar> {
    }
}