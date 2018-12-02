package uy.edu.ude.sipro.reportes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;

import net.sf.jasperreports.engine.JRException;

/**
 * Utilitarios para constuir reportes Jasper con Vaadin-
 *
 */
public class ReportsUtil {

    public void prepareForPdfReport(String reportTemplate, String reportOutputFilename, Button buttonToExtend, Map<String, Object> parametros,
            List<? extends Object> datos) {
        reportOutputFilename += ("_" + getDateAsString() + ".pdf");
        StreamResource myResource = createPdfResource(reportTemplate, reportOutputFilename, parametros, datos);
        FileDownloader fileDownloader = new FileDownloader(myResource);
        fileDownloader.extend(buttonToExtend);

    }

    public void prepareForPdfReport(Button buttonToExtend, StreamResource resource) {
        FileDownloader fileDownloader = new FileDownloader(resource);
        fileDownloader.extend(buttonToExtend);

    }

    public StreamResource createPdfResource(final String templatePath, String reportFileName, Map<String, Object> parametros, List<? extends Object> datos) {
        return new StreamResource(new StreamResource.StreamSource() {
            private static final long serialVersionUID = 1L;

            @Override
            public InputStream getStream() {
                ByteArrayOutputStream pdfBuffer = new ByteArrayOutputStream();
                ReportGenerator reportGenerator = new ReportGenerator();

                try {
                    reportGenerator.executeReport(templatePath, pdfBuffer, parametros, datos);
                } catch (JRException e) {
                    // TODO
                }
                return new ByteArrayInputStream(pdfBuffer.toByteArray());
            }
        }, reportFileName);
    }

    private String getDateAsString() {
        return (String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) + String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1)
                + String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                + String.valueOf(Calendar.getInstance().get(Calendar.MINUTE)) + String.valueOf(Calendar.getInstance().get(Calendar.SECOND)));
    }
}
