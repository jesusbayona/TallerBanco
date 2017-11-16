package co.edu.javeriana.modval.servicios;

import co.edu.javeriana.modval.datos.Consultas;

import java.io.IOException;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

import javax.xml.rpc.ServiceException;

/**
 *
 * @author Jesus Bayona
 */

@WebService(serviceName = "wsBanco")
public class Banco {
    public Banco() {
        super();
    }
    
    Consultas lConsultas = new Consultas();
        /**
         * This is a sample web service operation
         * @param convenio
         * @param parametros
         * @param url
         * @param tipoConsumo
         * @return 
         * @throws java.io.IOException 
         */
        @WebMethod(operationName = "nuevoConvenio")
        public String nuevoConvenio(@WebParam(name = "codigoConvenio") String codConvenio,
                                    @WebParam(name = "nombreConvenio") String convenio,
                                    @WebParam(name = "parametros") String parametros,
                                    @WebParam(name = "url") String url,
                                    @WebParam(name = "tipoConsumo") String tipoConsumo,
                                    @WebParam(name = "operacion") String operacion) throws IOException {
            String retorno;        
            try{
                retorno = lConsultas.crearConvenio(codConvenio, convenio, parametros, url, tipoConsumo,operacion);   
            }catch (IOException ex) {
                return ex.getMessage();
            }
            return retorno;
        }
        
    @WebMethod(operationName = "pagarServicio")
    public String pagarServicio(@WebParam(name = "tipoFactura") String tipoFactura,
                                @WebParam(name = "valorPago") String valorPago,
                                @WebParam(name = "codigo") String codigo) throws IOException, ServiceException {
        String  resultado;
        try{
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            resultado = lConsultas.leerConvenio(codigo, valorPago);
            return resultado;
        }catch(IOException e){
            return e.toString();
        }
    }
}
