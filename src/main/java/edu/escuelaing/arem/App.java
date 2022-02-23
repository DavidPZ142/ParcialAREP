package edu.escuelaing.arem;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {

        HTTPServer httpServer = new HTTPServer();
        httpServer.run();
    }
}
