package com.mycompany.calcreflex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class CalcReflexBEServer {

    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        while (running = true) {

            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;
            boolean isfirstLine = true;
            String firstLine = "";

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (isfirstLine) {
                    firstLine = inputLine;
                    isfirstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }
            URI requrl = getReqUrl(firstLine);

            if (requrl.getPath().startsWith("/compreflex")) {
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: application/json\r\n"
                        + "\r\n"
                        + "{\"array\":[1,2,3],\"name\":\"Daniela\",\"color\":\"gold\",\"null\":null,\"number\":123,\"object\":{\"a\":\"b\",\"c\":\"d\"},\"string\":\"Hello World\"}";
            }else{
                outputLine = getHtmlClient();
            }
            

            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    public static String getHtmlClient() {
        String htmlcode
                = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <title>Form Example</title>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        <h1>Method not Found</h1>\n"      
                + "\n"
                + "    </body>\n"
                + "</html>";
        return htmlcode;
    }

    public static URI getReqUrl(String firstLine) throws  URISyntaxException {
        String rurl = firstLine.split(" ")[1];
        return new URI(rurl);
    }
    
 
}
