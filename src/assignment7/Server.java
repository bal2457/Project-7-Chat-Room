package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

    public class Server extends Observable{

        private static ArrayList<PrintWriter> clientOutputStreams;
        private static HashMap<Integer, String> clientUsernames;

        static {
            clientUsernames = new HashMap<>();
        }

        private ClientObserver writer;

        public static void main(String[] args) {
            try {
                new Server().setUpNetworking();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static ArrayList<PrintWriter> getOutputStreams(){

            return clientOutputStreams;

        }

        public static ArrayList<String> getList(){
            ArrayList<String> list = new ArrayList<>();
            list.addAll(clientUsernames.values());
            return list;
        }

        public synchronized static void addUsername(String username, int clientNumber){
            clientUsernames.put(clientNumber, username);

        }
        public static boolean checkUsername(String username){

            return clientUsernames.containsValue(username);
        }

        private void setUpNetworking() throws Exception {
            clientOutputStreams = new ArrayList<>();
            @SuppressWarnings("resource")
            ServerSocket serverSock = new ServerSocket(4242);
            while (true) {
                Socket clientSocket = serverSock.accept();
                ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
                clientOutputStreams.add(writer);
                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                //System.out.println("got a connection");
            }
        }


        private void notifyClients(String message) {
            for (PrintWriter writer : clientOutputStreams) {
                //System.out.println("I wrote: "+message+" to "+writer.toString());
                writer.println(message);
                writer.flush();
            }
        }


        class ClientHandler implements Runnable {

            private BufferedReader reader;
            public ClientHandler(Socket clientSocket) throws IOException {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            }

            public void run() {
                try {
                    while (true){
                        String msg = reader.readLine();
                        //System.out.println("Server received " + msg);
                        notifyClients(msg);
                        //System.out.println("Server: I wrote " + msg + " to client.");

                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
