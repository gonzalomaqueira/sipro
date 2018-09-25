package uy.edu.ude.sipro.utiles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.vaadin.ui.Upload.Receiver;

public class ReceptorArchivos implements Receiver {

	private OutputStream outputFile = null;
    
    private final String rutaArchivo;
    private final String prefijo;

    public ReceptorArchivos(final String rutaArchivo, String prefijo) {
        this.rutaArchivo = rutaArchivo;
        this.prefijo = prefijo;
    }

    @Override
    public OutputStream receiveUpload(String nombreArchivo, String strMIMEType) 
    {
        File file = null;
        try {
            file = new File(rutaArchivo + prefijo + nombreArchivo);
            if (!file.exists()) {
                file.createNewFile();
            }
            outputFile = new FileOutputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFile;
    }

    protected void finalize() {
        try {
            super.finalize();
            if (outputFile != null) {
                outputFile.close();
            }
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
    }
}
