package com.alfa1.cv.backoffice.views.upload;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.DomEventListener;

import java.io.IOException;

public class UploadField extends CustomField<byte[]> {

    Upload upload;

    public UploadField(String name, String... fileTypes) {
        upload = new Upload(new MemoryBuffer());
        upload.setAcceptedFileTypes(fileTypes);

        upload.setMaxFiles(1);
        upload.setMaxFileSize(10 * 1024 * 1024);

        upload.getElement().addEventListener("upload-abort", (DomEventListener) domEvent -> {
            upload.setReceiver(new MemoryBuffer());
            setValue(new byte[0]);
        });

        upload.addFinishedListener(e -> {
            try {
                byte[] value = ((MemoryBuffer) upload.getReceiver()).getInputStream().readAllBytes();
                setValue(value);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        upload.addFileRejectedListener(e -> Notification
                .show(e.getErrorMessage(), 2000, Notification.Position.BOTTOM_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_ERROR));

        add(new Label(name), upload);
    }

    @Override
    protected byte[] generateModelValue() {
        return getValue();
    }

    @Override
    protected void setPresentationValue(byte[] inputStream) {
        // Not needed
    }

}
