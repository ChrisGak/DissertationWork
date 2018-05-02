package com.spaceApplication.client.space;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spaceApplication.client.consts.SpaceAppConstants;
import com.spaceApplication.client.consts.SpaceAppMessages;
import com.spaceApplication.client.exception.TetherSystemModelValueException;
import com.spaceApplication.client.space.controllers.RemoteCalculationControl;
import com.spaceApplication.client.space.controllers.SpaceApplicationService;
import com.spaceApplication.client.space.controllers.SpaceApplicationServiceAsync;
import com.spaceApplication.client.space.presenters.ModelParameterCtrlPresenter;
import com.spaceApplication.client.space.model.OrbitalElementsClient;
import com.spaceApplication.client.space.presenters.ApplicationContainer;

import java.util.logging.Logger;

import static com.spaceApplication.client.space.ui.components.UIConsts.*;

/**
 * Created by Chris
 */
public class SpaceAppEntryPoint implements EntryPoint {
    private static final Logger log = Logger.getLogger(String.valueOf(SpaceAppEntryPoint.class));

    // create an instance of the service proxy class by calling GWT.create(Class).
    private SpaceApplicationServiceAsync spaceApplicationServiceAsync = GWT.create(SpaceApplicationService.class);
    private HorizontalPanel mainPanel = new HorizontalPanel();
    private ClickHandler modelingHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            toggleContentBlocks(false);
            ApplicationContainer applicationContainer = new ApplicationContainer();
            RootPanel.get(APPLICATION_SELECTOR).add(applicationContainer);
        }
    };
    private ClickHandler navigationBarClickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            toggleContentBlocks(true);
        }
    };
    private SpaceAppConstants constants = GWT.create(SpaceAppConstants.class);
    private SpaceAppMessages messages = GWT.create(SpaceAppMessages.class);
    private Label errorMsgLabel = new Label(constants.testExample());
    // Set up the callback object.
    AsyncCallback<OrbitalElementsClient> calculationCallback = new AsyncCallback<OrbitalElementsClient>() {
        public void onFailure(Throwable caught) {
            String details = caught.getMessage();
            if (caught instanceof TetherSystemModelValueException) {
                details = "Model has exception:  '" + caught.getCause();
            }

            errorMsgLabel.setText("Error: " + details);
            errorMsgLabel.setText(messages.getErrorMessage(caught.getMessage()));
            errorMsgLabel.setVisible(true);

            mainPanel.add(errorMsgLabel);
            RootPanel.get(APPLICATION_SELECTOR).add(mainPanel);
        }

        @Override
        public void onSuccess(OrbitalElementsClient result) {
            mainPanel.add(RemoteCalculationControl.createAllResultPlotsTest(result));
            RootPanel.get(APPLICATION_SELECTOR).add(mainPanel);
        }
    };

    private void toggleContentBlocks(boolean toHideDynamic) {
        if (toHideDynamic) {
            RootPanel.get(APPLICATION_SELECTOR).setVisible(false);
            RootPanel.get(STATIC_CONTENT_SELECTOR).setVisible(true);
        } else {
            RootPanel.get(APPLICATION_SELECTOR).setVisible(true);
            RootPanel.get(STATIC_CONTENT_SELECTOR).setVisible(false);
        }
    }

    public void onModuleLoad() {
        Button modelingButton = Button.wrap(Document.get().getElementById("mainButton"));
        modelingButton.addClickHandler(modelingHandler);

        Anchor navigationBarMainWrapper = Anchor.wrap(Document.get().getElementById("navigationBar_main"));
        Anchor navigationBarTheoryWrapper = Anchor.wrap(Document.get().getElementById("navigationBar_theory"));
        Anchor navigationBarContactsWrapper = Anchor.wrap(Document.get().getElementById("navigationBar_contacts"));
        navigationBarMainWrapper.addClickHandler(navigationBarClickHandler);
        navigationBarTheoryWrapper.addClickHandler(navigationBarClickHandler);
        navigationBarContactsWrapper.addClickHandler(navigationBarClickHandler);
        toggleContentBlocks(true);
    }

    private void clearDynamicBlock() {
        RootPanel.get(APPLICATION_SELECTOR).clear();
    }

    private void initParameterCtrl() {
        final ModelParameterCtrlPresenter modelParameterCtrl = new ModelParameterCtrlPresenter();
        MenuBar modelCreationMenu = new MenuBar();
        Command createParameterCtrlCommand = new Command() {
            @Override
            public void execute() {
                mainPanel.clear();
                RootPanel.get(APPLICATION_SELECTOR).clear(true);
                mainPanel.add(modelParameterCtrl.initWidget());
                RootPanel.get(APPLICATION_SELECTOR).add(mainPanel);
            }
        };

        Command downloadModelCommand = new Command() {
            @Override
            public void execute() {
                mainPanel.clear();

                final FormPanel form = new FormPanel();
                form.setAction("/myFormHandler");
                form.setEncoding(FormPanel.ENCODING_MULTIPART);
                form.setMethod(FormPanel.METHOD_POST);
                form.setTitle("Загрузите файл");

                Image xlsImage = new Image("images/excel-xls-icon.png");
                xlsImage.setStyleName(imageStyle);
                xlsImage.setTitle("Microsoft Office Excel");

                VerticalPanel vpanel = new VerticalPanel();
                form.setWidget(vpanel);

                final FileUpload upload = new FileUpload();
                upload.setStyleName(BUTTON_STYLE_NAME);
                upload.setName("Файл");
                Button submit = new Button("Загрузить");
                submit.setStyleName(BUTTON_STYLE_NAME);
                vpanel.add(upload);
                vpanel.add(submit);
                final InfoPopup popup = new InfoPopup();
                popup.setStyleName(info_popup);

                popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                    public void setPosition(int offsetWidth, int offsetHeight) {
                        int left = (Window.getClientWidth() - offsetWidth) / 3;
                        int top = (Window.getClientHeight() - offsetHeight) / 3;
                        popup.setPopupPosition(left, top);
                    }
                });
                submit.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        popup.setInnerText("Файл загружается...");
                        form.submit();
                    }
                });

                form.addSubmitHandler(new FormPanel.SubmitHandler() {
                    public void onSubmit(FormPanel.SubmitEvent event) {
                        if (upload.getFilename().equals("")) {
                            popup.setInnerText("Выберите файл!!!");
                            event.cancel();
                        } else if (!upload.getFilename().contains(".xls")) {
                            popup.setInnerText("Поддерживаются файлы только формата xls");
                            event.cancel();
                        }
                    }
                });
                form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
                    public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                        popup.setInnerText(event.getResults());
                    }
                });

                HorizontalPanel hPanel = new HorizontalPanel();
                hPanel.add(new VerticalPanel());
                hPanel.add(form);
                hPanel.add(new VerticalPanel());
                hPanel.add(xlsImage);

                RootPanel.get(APPLICATION_SELECTOR).add(hPanel);
            }
        };

        MenuItem modelCreationDialog = new MenuItem(constants.modelCreation(), createParameterCtrlCommand);
        modelCreationDialog.setTitle(constants.modelCreationTitle());
        modelCreationDialog.setStyleName(squareButtonStyle);

        MenuItem modelDownloadingDialog = new MenuItem(constants.modelDownloading(), downloadModelCommand);
        modelDownloadingDialog.setTitle(constants.modelDownloadingTitle());
        modelDownloadingDialog.setStyleName(squareButtonStyle);

        modelCreationMenu.addItem(modelCreationDialog);
        modelCreationMenu.addSeparator();
        modelCreationMenu.addItem(modelDownloadingDialog);
    }

    private static class InfoPopup extends PopupPanel {
        private Label innerText = new Label();

        public InfoPopup() {
            // PopupPanel's constructor takes 'auto-hide' as its boolean
            // parameter. If this is set, the panel closes itself
            // automatically when the user clicks outside of it.
            super(true);

            // PopupPanel is a SimplePanel, so you have to set it's widget
            // property to whatever you want its contents to be.
            innerText = new Label("Click outside of this popup to close it");
            innerText.setStyleName(text_muted);
            setWidget(innerText);
        }

        public void setInnerText(String text) {
            innerText.setText(text);
            int width = 300;
            int len = (((text.length()) < width) ? 200 : ((text.length() / width) * 2));
            this.setSize(width + "px", len + "px");
            this.setAnimationEnabled(true);
            this.setGlassEnabled(true);
            this.setVisible(true);
            this.center();
            this.show();
        }
    }

}
