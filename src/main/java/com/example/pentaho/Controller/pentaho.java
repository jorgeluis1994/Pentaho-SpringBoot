package com.example.pentaho.Controller;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class pentaho {

    @Autowired
    private ResourceLoader resourceLoader;
    @PostMapping("/executeKtr")
    public String executeKtr() {
        try {
            // Inicializar el entorno de Kettle
            KettleEnvironment.init();

            // Cargar el archivo KTR desde la carpeta resources
            Resource resource = resourceLoader.getResource("classpath:forms.ktr");

            // Crear un objeto TransMeta para cargar el archivo KTR
            TransMeta transMeta = new TransMeta(resource.getInputStream(), null, true, null, null);

            // Crear una instancia de Trans con el objeto TransMeta
            Trans trans = new Trans(transMeta);

            // Ejecutar la transformación
            trans.execute(null);
            trans.waitUntilFinished();

            // Verificar si la transformación se ejecutó correctamente
            if (trans.getErrors() == 0) {
                return "Transformación KTR ejecutada con éxito";
            } else {
                return "Error al ejecutar la transformación KTR";
            }
        } catch (KettleException e) {
            e.printStackTrace();
            return "Error al ejecutar la transformación KTR: " + e.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error al cargar el archivo KTR: " + ex.getMessage();
        }
    }

     @PostMapping("/executeJob")
    public String executeJob() {
        try {
            // Inicializar el entorno de Kettle
            KettleEnvironment.init();

            // Cargar el archivo de job desde la carpeta resources
            Resource resource = resourceLoader.getResource("classpath:archivo.job");

            // Obtener la ruta absoluta del archivo de job
            String jobFilePath = resource.getFile().getAbsolutePath();

            // Crear un objeto JobMeta para cargar el archivo de job
            JobMeta jobMeta = new JobMeta(jobFilePath, null);

            // Crear una instancia de Job con el objeto JobMeta
            Job job = new Job(null, jobMeta);

            // Iniciar la ejecución del job
            job.start();
            job.waitUntilFinished();

            // Verificar si hubo errores durante la ejecución del job
            if (job.getErrors() == 0) {
                return "Job ejecutado con éxito";
            } else {
                return "Error al ejecutar el job";
            }
        } catch (KettleException e) {
            e.printStackTrace();
            return "Error al ejecutar el job: " + e.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error al cargar el archivo de job: " + ex.getMessage();
        }
    }
   }
    

    


