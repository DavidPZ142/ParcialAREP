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

    public void Json(String url , PrintWriter printWriter) throws IOException {
        String input = null;
        StringBuffer JSON = new StringBuffer();
        URL url2 = new URL(url);
        URLConnection urlConnection = url2.openConnection();
        InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
        try  {
            BufferedReader reader = new BufferedReader(inputStreamReader);
            while ((input = reader.readLine()) != null) {
                JSON.append(input);
            }
            } catch (IOException i ) {
            System.err.println(i );
        }
        System.out.println(JSON);
        printWriter.println( JSON );
    }


    /**
     * Conectar el API weather
     */
    public void getResource(HashMap<String, String> hashMap, OutputStream out)throws IOException {
        String[] requests = hashMap.get("rq").split(":");
        System.out.println(requests.length);
        PrintWriter printWriter = new PrintWriter(out, true);
        if (requests[1].contains("/clima")){
            String lugar = requests[1];
            String path = lugar;
            String[] route = path.split("\\/");
            String routee = route[route.length -1 ];
            String res = "";
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?"+ routee + "&appid=bc8f73402409d799eeb090113506682e");
            getResponse(printWriter);
            out.close();
        }
        else if (requests[1].contains("/consulta?lugar=")){
            String path = requests[1].replace("/consulta?lugar=","");
            String newUrl = "http://api.openweathermap.org/data/2.5/weather?"+ path + "&appid=bc8f73402409d799eeb090113506682e";
            Json(newUrl, printWriter);
            out.close();
        }
        printWriter.close();
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
        getResource(hashMap,out);
        in.close();
    }



    public void getResponse(PrintWriter printWriter){
        String outputLine = "HTTP/1.1 200 OK\r\n"
                +"Content-Type: text/html\r\n"
                +"\r\n"
                + "<meta charset=\"UTF-8\">"
                +"<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css\" integrity=\"sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO\" crossorigin=\"anonymous\">\n"
                +"<script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\" integrity=\"sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo\" crossorigin=\"anonymous\"></script>\n"
                +"<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js\" integrity=\"sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49\" crossorigin=\"anonymous\"></script>\n"
                +"<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js\" integrity=\"sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy\" crossorigin=\"anonymous\"></script>\n"
                +"<head>"
                + "<title>clima </title>\n"
                + "</head>"
                +"</html>\n";

        printWriter.println(outputLine);

    }
}
