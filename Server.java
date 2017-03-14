/*
 * Server.java
 */

import java.io.*;
import java.net.*;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;

public class Server {

    public static final int SERVER_PORT = 7652;// The client connects with the server using this port number

    static Map<Integer, String> map = new LinkedHashMap<>();

    public static void main(String args[]) {
        Server ft = new Server();
        ServerSocket myServerice = null;
        String line;
        BufferedReader is;
        PrintStream os;
        Socket serviceSocket = null;
        try {
            File file = new File("AddressBook.txt");//Create instance of a file and check if it exists
            if (!file.exists()) {
                file.createNewFile();
            } else {

                ft.loadtoMap(file);
            }
        } catch (Exception e) {
            System.out.println("caught exception");
        }

        // Try to open a server socket 
        try {
            myServerice = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            System.out.println(e);
        }

        // Create a socket object from the ServerSocket to listen and accept connections.
        // Open input and output streams
        while (true) {
            try {
                serviceSocket = myServerice.accept();
                is = new BufferedReader(new InputStreamReader(serviceSocket.getInputStream()));
                os = new PrintStream(serviceSocket.getOutputStream());

                // As long as we receive data check the user input and perform appropriate condition
                while ((line = is.readLine()) != null) {
                    char c;
                    String content,cmd = " ";
                    int recordid = 0;
                    System.out.println("c:" + line);
                    //Check the user Input                  
                    if(line.length()>=4){
                    c = line.charAt(0);//get first char to switch to appropriate action
                    
                    }
                    else{
                     c='X';   
                    }
                    //take option and choose appropriate choice
                    switch (c) {

                        case 'A': // ADD COMMAND -Add record to the addressbook
                            cmd=line.substring(0,line.indexOf(' '));
                    content = line.substring(line.indexOf(' ') + 1);//The data after the command is taken
                            if (cmd.equals("ADD") && content.length() >= 12) {
                                int recid;//Record id returned after adding
                                String recstring;
                                recid = ft.addtolist(content);//add content to addressbook
                                os.println("200 OK");
                                recstring = "The new Record ID is " + recid;
                                os.println(recstring);
                                os.println("END");
                                os.flush();
                                System.out.println(" ADDED record");
                                break;
                            } else {
                                os.println("301 message format error ");
                                os.println("END");
                                break;
                            }
                        case 'D'://DELETE COMMAND
                            cmd=line.substring(0,line.indexOf(' '));
                    content = line.substring(line.indexOf(' ') + 1);//The data after the command is taken
                             if (cmd.equals("DELETE") && content.length() >= 4){
                            System.out.println("DELETING THE RECORD");
                            try {

                                recordid = Integer.parseInt(content);//Getting the record id

                            } catch (NumberFormatException e) {
                                System.out.println("Number format exception");
                            }
                            String result = " ";
                            result = ft.deletelist(recordid);
                            os.println(result);
                            os.println("END");
                            os.flush();
                            break;
                             }
                        else
                             {os.println("END");
                            os.flush();
                            break;
                             }
                             
                        case 'L': //LIST COMMAND
                            cmd=line;
                    
                            if(cmd.equals("LIST")){
                            System.out.println("iNSIDE LIST");
                            if (map.isEmpty()) {
                                os.println("NO CONTENTS TO SHOW");
                                os.println("END");
                            } else {
                                Iterator<Integer> keySetIterator = map.keySet().iterator();
                                int key;
                                while (keySetIterator.hasNext()) {
                                    key = keySetIterator.next();
                                    os.println(key + " " + map.get(key));
                                    os.flush();
                                }
                                os.println("END");
                            }
                            break;}
                            else{os.println("END"); break;}
                        case 'S': // SHUT DOWN
                            ft.savetofile();
                            os.println("200 OK");
                            os.println("END");
                            is.close();
                            os.close();
                            serviceSocket.close();

                            break;

                        case 'Q': //QUIT
                            ft.savetofile();
                            os.println("200 OK");
                            os.println("END");
                            os.flush();
                            is.close();
                            os.close();
                            serviceSocket.close();
                            System.exit(0);
                            break;
                        default:
                            os.println("300 invalid command");
                            System.out.println("****Invalid command*****");
                            os.println("Try entering the right command agian");
                            os.println("END");
                            os.flush();
                            break;
                    }
                    ft.savetofile();
                    System.out.println("waiting for command again");
                }

            } catch (IOException e) {
                System.out.println(e + "some problem in stream");

            }
        }
    }

    public int addtolist(String x) {
        if (map.isEmpty()) {
            System.out.println("Address book is empty");
            map.put(1001, x);
            return 1001;
        } else {
            Iterator<Integer> keySetIterator = map.keySet().iterator();
            int key = 0;
            while (keySetIterator.hasNext()) {
                key = keySetIterator.next();

            }
            map.put(key + 1, x);
            return key + 1;
        }

    }

    public void savetofile() {
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("AddressBook.txt")));

            for (Map.Entry<Integer, String> m : map.entrySet()) {
                pw.println(m.getKey() + " " + m.getValue());
            }

            pw.flush();
            pw.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void loadtoMap(File f) {
        try {

            BufferedReader in = new BufferedReader(new FileReader(f));

            String line = "";
            while ((line = in.readLine()) != null) {
                String first = line.substring(0, line.indexOf(' '));

                Integer id = Integer.valueOf(first);

                String value = line.substring(line.indexOf(' ') + 1);
                map.put(id, value);
            }

            in.close();
            //System.out.println(map.toString());
        } catch (Exception e) {
            System.out.println(e);

        }
    }

    public String deletelist(int i) {

        if (map.get(i) != null) {
            map.remove(i);
            return "200 ok";
        } else {
            return "401 no record found";
        }
    }
}
