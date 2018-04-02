package com.spaceApplication.client.space.presenters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;

/**
 * Created by Chris on 02.04.2018.
 */
public class PageSidebar {
    interface PageSidebarUiBinder extends UiBinder<DivElement, PageSidebar> {
    }

    private static PageSidebarUiBinder ourUiBinder = GWT.create(PageSidebarUiBinder.class);

    public PageSidebar() {
        DivElement rootElement = ourUiBinder.createAndBindUi(this);
    }
}