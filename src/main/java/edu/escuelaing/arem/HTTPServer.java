package edu.escuelaing.arem;
import java.net.*;
import java.io.*;
import java.util.HashMap;

public class HTTPServer {

    public static void main(String [] args) throws IOException{
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(35000);
        }catch(IOException e){
            System.err.println("Could not listen on port: 35000");
            System.exit(1);
        }

        boolean run = true;
        while ( run){
            Socket clientSocket = null;
            try{
                System.out.println("Listo para recibir ... ");
                clientSocket = serverSocket.accept();
            } catch (IOException e){
                System.err.println("Accept Failed");
                System.exit(1);
            }
            clientSocket.close();
        }
        serverSocket.close();
    }

    /**
     * Conectar el API weather
     */
    public void getResource(){

    }

    public void serverConnection(Socket clientSocket) throws IOException {
        OutputStream out = clientSocket.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String input;
        String output;
        HashMap<String, String> hashMap = new HashMap<String , String>();
        boolean a = false;
        while((input = in.readLine())!= null){
            if (!a){
                hashMap.put("rq", input);
                a = true;
            }
            else{
                String [] line = input.split(":");
                if (1 < line.length){
                    hashMap.put(line[0],line[1]);
                }
            }
            if (!in.ready()){
                break;
            }
        }
        getResource();
        in.close();
    }

    public void getResponse(){
        String outputLine = "HTTP/1.1 200 OK\r\n"
                +"Content-Type: text/html\r\n"
                +"\r\n"
                +"<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Consultor de clima </title>\n"
                + "</head>"
                +"</html>\n";

    }



}
