import java.io.*;
import java.net.*;
import java.awt.Desktop;

public class Cliente {
    public static void main (String[] args ) throws Exception{

        try {
            String password = hacerPeticion("http://localhost:8080/password");
            System.out.println("Contraseña recibida: " + password);

            String html = hacerPeticion("http://localhost:8080/");
            System.out.println("Página HTML recibida");

            File archivo = new File("pagina_recibida.html");
            FileWriter fw = new FileWriter(archivo);
            fw.write(html);
            fw.close();

            System.out.println("HTML guardado correctamente");

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(archivo);
                System.out.println("Archivo abierto en navegador");
            } else {
                System.out.println("El archivo no se puede abrir automáticamente");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String hacerPeticion(String urlStr) throws Exception {

        URI url = new URI(urlStr);
        URL urlObj = url.toURL();
        HttpURLConnection conexion = (HttpURLConnection)urlObj.openConnection();
        conexion.setRequestMethod("GET");

        int codigoRespuesta = conexion.getResponseCode();
        String tipo = conexion.getContentType();

        System.out.println("Código recibido: " + codigoRespuesta);
        System.out.println("Content-Type: " + tipo);

        if (codigoRespuesta != 200 ){
            System.out.println("Error, el servidor devuelve código: " + codigoRespuesta);
            return "";
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        StringBuilder respuesta = new StringBuilder();
        String linea;

        while ((linea = in.readLine()) != null){
            respuesta.append(linea).append("\n");
        }

        in.close();

        if (tipo.contains("text/plain")){
            return respuesta.toString().trim();
        }

        if (tipo.contains("text/html")) {
            return respuesta.toString();
        }

        return "";
    }
}