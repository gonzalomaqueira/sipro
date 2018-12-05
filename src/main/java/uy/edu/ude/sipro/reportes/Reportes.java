package uy.edu.ude.sipro.reportes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import uy.edu.ude.sipro.valueObjects.ProyectoVO;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class Reportes {
	
	public void generarReporte(List<ProyectoVO> lista) throws JRException, FileNotFoundException
	{
		InputStream arq = new FileInputStream("/reportes/reporteTemplate.jrxml");
		//InputStream arq = getClass().getResourceAsStream("/reportes/reporteTemplate.jrxml");
		JasperReport report = JasperCompileManager.compileReport(arq);
		JasperPrint print = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(lista));
		JasperExportManager.exportReportToPdfFile(print, "C:\\reportes\\reporte1.pdf");
	}

}
 