/**
 * Open an HTTP connection to a remote webserver and request one webpage.
 *   NOTE: the HTTPConnection class hides the socket interface from 
 *         the GUI and presents a higher-level interface to be used by the
 *         WebClient GUI class.
 * The public interface for HTTPConnection is:
 *   constructor:  HTTPConnection(String URL)
 *   public     String requestWebpage()
 *   public     String refreshWebpage()
 *   public void close()
 *   public boolean readyToOpen
 */

import java.net.*;
import java.io.*;
import java.util.*;

public class HTTPConnection {
    /* The socket to the server */
    private Socket connection;

    /* The URL of the webpage */
    private String webpageURL;
    
    /* String Tokenizer to parse input URL */
    private StringTokenizer st;
    private StringTokenizer st2;
    
    private String hostName;
    private String filePath;
    //private boolean replyHasMoreLines;

    /* Streams for reading and writing via the socket */
    private BufferedReader fromServer;
    private DataOutputStream toServer;
    
    /* Optional Streams for reading and writing to the console for debugging*/
    private BufferedReader fromDebug;
    private DataOutputStream toDebug;

    private static final int HTTP_PORT = 80;
    private static final String CRLF = "\r\n";

    /* Are we connected? Used in close() to determine what to do. */
    public boolean readyToOpen = false;

    /* Create an HTTPConnection object. Create the socket and the 
       associated streams. Initialize HTTP connection. */
    public HTTPConnection(String URL) throws IOException {
        fromDebug = new BufferedReader(new InputStreamReader(System.in));
        toDebug = new DataOutputStream(System.out);
        
        /* We need the host name of the web server to
           establish the TCP connection. */       
        webpageURL = URL;
        hostName = parseHostName(webpageURL);
        filePath = parseFilePath(webpageURL);

        /* fill in - establish the connection by creating a socket */
        Socket clientSocket = new Socket(hostName, HTTP_PORT);
        toServer = new DataOutputStream(clientSocket.getOutputStream());
        fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        readyToOpen = true;
    }

    /* Send the correct HTTP request message that will request a web page
       and receive the HTTP response message. */
    public String requestWebpage() throws IOException {

    /* Send the HTTP header request message to open a webpage. Call
       sendCommand() to do the dirty work. Do NOT catch the
       exception thrown from sendCommand(). The calling function 
       will handle it.*/
    
        /* fill in */
        String reply = sendCommand("HEAD " + filePath +" HTTP/1.1" + CRLF + "Host: " + hostName + CRLF + "Connection: close" + CRLF, 200);
        
        return reply;
    }

    /* Send the correct HTTP-commands to refresh a webpage. */
    public String refreshWebpage() throws IOException {
    
    /* Send all the necessary commands to refresh a webpage. Call
       sendCommand() to do the dirty work. Do NOT catch the
       exception thrown from sendCommand(). The calling function 
       will handle it.*/
    
        /* fill in */
        return null;
    }

    /* Close the connection by closing the socket. */
    public void close() {
        readyToOpen = false;
        try {
            connection.close();
        } catch (IOException e) {
            System.out.println("Unable to close connection: " + e);
            readyToOpen = true;
        }
    }

    /* Given a constructed HTTP request message, this function will send it to 
       the server & then process the reply message:  Parse and verify that the 
       received reply code equals the expected code*/
    private String sendCommand(String command, int rc) throws IOException {
        String reply = "";
    /* Write command to server and read reply from server. */
        
        /* fill in */
        System.out.println();
        System.out.println("Sending request:");
        System.out.println(command);
        toServer.writeBytes(command + CRLF);

        while(true){
            String replyLine = fromServer.readLine();
            
            if(replyLine == null) //condition to break out of the loop at end of reply
                break;
                    
            System.out.println(replyLine);
            reply = reply + replyLine + CRLF;
            }
            
        //toDebug.writeBytes("This is the full reply: " + CRLF + reply);
    /* Check that the server's reply code is the same as the 
       parameter rc. If not, throw an IOException. */
        
        /* fill in */
        rc = parseReply(reply);
        
        if (rc != 200)
            throw new IOException("Error " + rc);
        
        return reply;
    }

    /* Parse the reply line from the server. Returns the reply 
       code. */
    private int parseReply(String reply) {

        /* fill in */
        st = new StringTokenizer(reply);
        st.nextToken();
        int rc = Integer.parseInt(st.nextToken());
        //System.out.println("rc = " + rc);
        return rc;
    }

    /* Parse the URL line and extract the host name part. */
    private String parseHostName(String URL) {

        st = new StringTokenizer(URL, "/");

        String firstToken = st.nextToken();
        if (firstToken.equals("http:")){
            System.out.println("-- http:// included in URL");
        //st.nextToken(); //crude implementation to skip over "HTTP:" token, edit later utilizing a conditional statement...
        String hostName = st.nextToken();
        return hostName;
        }
        else{
            hostName = firstToken;
            System.out.println("-- http:// excluded from URL");}
        System.out.println("hostName = " + hostName);
        return hostName;
    }

    /* Parse URL line and extract the file path part */
    private String parseFilePath(String URL) {
        String filePath = "";
        st2 = new StringTokenizer(URL, "/");
        String firstToken = st2.nextToken();
        if (firstToken.equals("http:")){
            st2.nextToken();}

        while (st2.hasMoreTokens()) {
            filePath = filePath + "/" + st2.nextToken();
            }

        if(filePath.equals("")){ filePath = "/index.html";}
            System.out.println("filePath = " + filePath);
            return filePath;
     }
    
    
    /* Destructor. Closes the connection if something bad happens. */
    protected void finalize() throws Throwable {
        if(readyToOpen) {
            close();
        }
        super.finalize();
    }
}
