package edu.escuelaing.arem;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.net.*;
import java.io.*;
import java.util.HashMap;

public class HTTPServer {

    public void run () throws IOException{
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
            serverConnection(clientSocket);
            clientSocket.close();

        }

        serverSocket.close();
    }

    public void Json(String url , PrintWriter printWriter){
        

    }

    /**
     * Conectar el API weather
     */
    public void getResource(HashMap<String, String> hashMap, OutputStream out)throws IOException {
        String[] requests = hashMap.get("rq").split(":");
        PrintWriter printWriter = new PrintWriter(out, true);
        if (requests[1].contains("/clima")){
            String lugar = requests[1];
            String path = lugar;
            String[] route = path.split("\\/");
            String routee = route[route.length -1 ];

            String res = "";

            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?"+ routee + "&appid=bc8f73402409d799eeb090113506682e");
        }
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
        //getResource();
        in.close();
    }



    public void getResponse(){
        String outputLine = "HTTP/1.1 200 OK\r\n"
                +"Content-Type: text/html\r\n"
                +"\r\n"
                +"<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>clima </title>\n"
                + "</head>"
                +"</html>\n";

    }



}
