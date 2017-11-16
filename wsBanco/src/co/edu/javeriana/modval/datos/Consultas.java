package co.edu.javeriana.modval.datos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceFactory;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class Consultas {
    public Consultas() {
        super();
    }
    
    public String crearConvenio(String codConvenio, String convenio, String parametros, String url, String tipoConsumo,
                                String operacion)throws IOException {
        File archivo = null;
        BufferedWriter bw = null;
        try{
            String resultado;
            String ruta = "/home/archivo.txt";
            //String ruta = "C:/Users/Jesus Bayona/Downloads/apache-tomcat-7.0.82/webapps/archivo.txt";
                    
            archivo = new File(ruta);
            if(archivo.exists()) {
                bw = new BufferedWriter(new FileWriter(archivo, true));
                bw.newLine();
                bw.write(codConvenio+";"+convenio+";"+parametros+";"+url+";"+tipoConsumo+";"+operacion);
                resultado = "Creado el Convenio";
            } else {
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write(codConvenio+";"+convenio+";"+parametros+";"+url+";"+tipoConsumo+";"+operacion);
                resultado = "Creado el Convenio";
            }
            return resultado;
        }catch(IOException e){
            return e.toString();
        }finally{
            if(null != bw){
                bw.close();
            }
        }
    }
    
    public String leerConvenio(String codigo, String valor)throws IOException, ServiceException {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        String retorno = null; 
        String rest = "REST";
        String linea = null;
        try{
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            //archivo = new File ("C:/Users/Jesus Bayona/Downloads/apache-tomcat-7.0.82/webapps/archivo.txt");
            archivo = new File ("/home/archivo.txt");
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String URL;
            String operacion;
            String[] arrConsulta;
            while((linea=br.readLine())!= null){
                if(!"".equalsIgnoreCase(linea)){
                    arrConsulta = linea.split(";");
                    if(codigo.equalsIgnoreCase(arrConsulta[0])){
                        URL =  arrConsulta[3];
                        operacion = arrConsulta[5];
                        if(rest.equalsIgnoreCase(arrConsulta[4])){
                            retorno = ConsultaRest(URL, codigo, valor, operacion);
                        }else{
                              retorno = ConsultaSOAP(URL, codigo, valor);
                        }
                    }
                }                
            }
           
            return retorno;
        }catch(IOException e){
            return e.toString();
        }finally{
            if(null != fr){
                fr.close();
            }
            if(null != br){
                br.close();
            }
           
       }
   }
    
    public String ConsultaRest(String URL , String codigo, String valor, String operacion)throws IOException {
        String respuesta;
        //String uri = "http://localhost:8080/suma/Calculadora";
        URL url = new URL(URL+operacion+codigo);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        
       if (connection.getResponseCode() != 200) {
               throw new RuntimeException("Failed : HTTP error code : "
                               + connection.getResponseCode());
       }
        
       BufferedReader br = new BufferedReader(new InputStreamReader(
                               (connection.getInputStream())));

       System.out.println("Output from Server .... \n");
       /*while ((respuesta = br.readLine()) != null) {
           respuesta = br.readLine();
       }  */  
       respuesta = br.readLine();
       connection.disconnect();
        return respuesta;
   }
    
    public String ConsultaSOAP (String URL , String codigo, String valor)throws IOException, ServiceException {
        String respuesta;
        //String uri = "http://localhost:8080/suma/Calculadora";
        String uri = URL;
        URL url = new URL(uri);
        /*HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        respuesta = connection.getResponseMessage();*/
        
        String namespace = "http://servicio/";
        String portName = "http://localhost:8080/suma/Calculadora";
        QName portQN = new QName(namespace, portName);
        String operationName = "sumarNumeros";
        // Setup the global JAXM message factory
            System.setProperty("javax.xml.soap.MessageFactory",
              "weblogic.webservice.core.soap.MessageFactoryImpl");
            // Setup the global JAX-RPC service factory
            System.setProperty( "javax.xml.rpc.ServiceFactory",
              "weblogic.webservice.core.rpc.ServiceFactoryImpl");
        ServiceFactory serviceFactory = ServiceFactory.newInstance();
        Service service = serviceFactory.createService(url, portQN);
         
        Call call = service.createCall();
        call.setPortTypeName(portQN);
        call.setOperationName(new QName(namespace, operationName));
        call.setProperty(Call.ENCODINGSTYLE_URI_PROPERTY, ""); 
        call.setProperty(Call.OPERATION_STYLE_PROPERTY, "wrapped");
        call.addParameter("param1", portQN,ParameterMode.IN);
        call.setReturnType(portQN);
        Object[] inParams = new Object[] {"Jane"};
        respuesta = (String) call.invoke(inParams);  
        
        return respuesta;
    }
}
