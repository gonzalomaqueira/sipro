package uy.edu.ude.sipro.reportes;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

/*************************************************************************

Clase que contiene la lógica centralizada para la generación de reportes

**************************************************************************/
public class ReportGenerator {

    private static final Logger log = LoggerFactory.getLogger(ReportGenerator.class);

    public ReportGenerator() {
    }

    public void executeReport(String templatePath, OutputStream outputStream, Map<String, Object> parametros, List<? extends Object> datos) throws JRException
    {
        JasperDesign jasperDesign = loadTemplate(templatePath);
        setTempDirectory(templatePath);
        JasperReport jasperReport = compileReport(jasperDesign);
        JRBeanCollectionDataSource datosDS = new JRBeanCollectionDataSource(datos);
        JasperPrint jasperPrint = fillReport(jasperReport, parametros, datosDS);
        exportReportToPdf(jasperPrint, outputStream);
    }

    public void executeReportNoDS(String templatePath, OutputStream outputStream, Map<String, Object> parametros) throws JRException
    {
        JasperDesign jasperDesign = loadTemplate(templatePath);
        setTempDirectory(templatePath);
        JasperReport jasperReport = compileReport(jasperDesign);
        JasperPrint jasperPrint = fillReportNoDS(jasperReport, parametros);
        exportReportToPdf(jasperPrint, outputStream);
    }

    private JasperPrint fillReportNoDS(JasperReport jasperReport, Map<String, Object> parametros)
    {
        JasperPrint jasperPrint = null;
        try 
        {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource(1));
        } 
        catch (JRException e)
        {
            log.error("Error in filling the report..... " + e.getMessage());
        }
        return (jasperPrint);
    }

    private JasperDesign loadTemplate(String templatePath)
    {
        JasperDesign jasperDesign = null;
        File templateFile = new File(getClass().getClassLoader().getResource(templatePath).getFile());
        System.out.println("ABSOLUTE PATH: " + templateFile.getAbsolutePath());
        if (templateFile.exists())
        {
            try
            {
                jasperDesign = JRXmlLoader.load(templateFile);
            } 
            catch (JRException e)
            {
                log.error("Error in loading the template... " + e.getMessage());
            }
        } 
        else
            log.error("Error, the file dont exists");
        return (jasperDesign);
    }

    private JasperReport compileReport(JasperDesign jasperDesign)
    {
        JasperReport jasperReport = null;
        try 
        {
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
        } 
        catch (JRException e)
        {
            log.error("Error in compiling the report.... " + e.getMessage());
        }
        return (jasperReport);
    }

    private JasperPrint fillReport(JasperReport jasperReport, Map<String, Object> parametros, JRBeanCollectionDataSource datos)
    {
        JasperPrint jasperPrint = null;
        try
        {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datos);
        } 
        catch (JRException e)
        {
            log.error("Error in filling the report..... " + e.getMessage());
        }
        return (jasperPrint);
    }

    private void exportReportToPdf(JasperPrint jasperPrint, OutputStream outputStream) throws JRException
    {
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        exporter.exportReport();
    }

    private void setTempDirectory(String templatePath)
    {
        File templateFile = new File(templatePath);
        if (templateFile.exists())
        {
            log.info("Setting parentDirectory: " + templateFile.getParent());
            System.setProperty("jasper.reports.compile.temp", templateFile.getParent());
        }
    }
}
