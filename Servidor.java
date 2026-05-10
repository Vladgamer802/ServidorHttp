import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Servidor {
    public static void main (String[] args) throws Exception{
        ServerSocket servidor = new ServerSocket(8080);
        System.out.println("Servidor escuchando en puerto 8080...");

        while(true) {
            Socket conexion = servidor.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            OutputStream out = conexion.getOutputStream();

            String linea = in.readLine();
            if (linea == null){
                conexion.close();
                continue;
            }
            System.out.println("Peticion recibida" + linea);

        String[] partes = linea.split(" ");
        String ruta = partes[1];

        String cabeceras;
        byte[] contenido;

        switch (ruta) {
            case "/":
                contenido = Files.readAllBytes(Paths.get("index.html"));
                cabeceras =  "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/html\r\n" +
                            "Content-Length: " + contenido.length + "\r\n" +
                            "\r\n";

                out.write(cabeceras.getBytes());
                out.write(contenido);
                break;

            case "/password": 
                String pass = "PSP_DAM";
                contenido = pass.getBytes();
                cabeceras =    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/plain\r\n" +
                            "Content-Length: " + contenido.length + "\r\n" +
                            "\r\n";

                out.write(cabeceras.getBytes());
                out.write(contenido);
                break;
        
            default:
                String error = "<h1>404 NOT FOUND</h1>";
                contenido = error.getBytes();
                cabeceras = "HTTP/1.1 404 Not Found\r\n" +
                            "Content-Type: text/html\r\n" +
                            "Content-Length: " + contenido.length + "\r\n" +
                            "\r\n";
                out.write(cabeceras.getBytes());
                out.write(contenido);    
                break;
        }
          conexion.close();  
        }
    }  
}
