package com.spaceApplication.client.space.presenters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;

/**
 * Created by Chris on 02.05.2018.
 */
public class BreadcrumbHeader {
    interface BreadcrumbHeaderUiBinder extends UiBinder<DivElement, BreadcrumbHeader> {
    }

    private static BreadcrumbHeaderUiBinder ourUiBinder = GWT.create(BreadcrumbHeaderUiBinder.class);

    public BreadcrumbHeader(String title) {

        DivElement rootElement = ourUiBinder.createAndBindUi(this);
        rootElement.getFirstChild().setNodeValue(title);
    }
}