package uy.edu.ude.sipro.ui;

import java.util.List;

import com.vaadin.data.ValidationResult;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

public class UIUtiles
{
    public static void mostrarNotificacion(String titulo, String mensaje, Type tipoNotificacion) {
        Notification notification = new Notification(titulo, "<span>" + mensaje + "</span>", tipoNotificacion);
        notification.setDescription("<span>" + mensaje + "</span>");
        notification.setHtmlContentAllowed(true);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(5000);
        notification.show(Page.getCurrent());
    }

    public static void navegarVista(String vistaDestino) {
        UI.getCurrent().getNavigator().navigateTo(vistaDestino);
    }

    public static void mostrarErrorValidaciones(List<ValidationResult> erroresValidacion) {
        Notification notification = new Notification("Error de validación", Type.ERROR_MESSAGE);
        StringBuilder sbValidaciones = new StringBuilder();
        sbValidaciones.append("<ul style=\"text-align: left;\">");
        for (ValidationResult vr : erroresValidacion) {
            sbValidaciones.append("<li>").append(vr.getErrorMessage()).append("</li>");
        }
        sbValidaciones.append("</ul>");
        notification.setDescription(sbValidaciones.toString());
        notification.setHtmlContentAllowed(true);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(5000);
        notification.show(Page.getCurrent());
    }

    public static void mostrarErrores(List<String> erroresValidacion) {
        Notification notification = new Notification("Error de validación", Type.ERROR_MESSAGE);
        StringBuilder sbValidaciones = new StringBuilder();
        sbValidaciones.append("<ul style=\"text-align: left;\">");
        for (String vr : erroresValidacion) {
            sbValidaciones.append("<li>").append(vr).append("</li>");
        }
        sbValidaciones.append("</ul>");
        notification.setDescription(sbValidaciones.toString());
        notification.setHtmlContentAllowed(true);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(5000);
        notification.show(Page.getCurrent());
    }
}
