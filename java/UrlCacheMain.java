import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class UrlCacheMain {
    private static char getChar() {
        Reader reader = new InputStreamReader(System.in);
        char ch = 0;
        try {
            ch = (char)reader.read();
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return ch;
    }

    private static void tty(String mode) {
        String[] cmd = {"/bin/sh", "-c", "stty " + mode + " </dev/tty"};
        try {
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        } catch(InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("usage: <filename containing urls>");
            return;
        }

        final char ADD_URL = ';';
        final char RESET_GET_FROM_CACHE = ':';
        final char QUIT_CHAR = '-';
        UrlCache   urlCache = new UrlCache();

        System.out.println("populating lookup data structures...");

        String filename = args[0];
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(filename);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
            new BufferedReader(fileReader);

            String url;
            while((url = bufferedReader.readLine()) != null) {
                System.out.println(url);
                try {
                    urlCache.addToCache(url);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filename + "'");                
            return;
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + filename + "'");
            return;
        }

        System.out.println("finished populating lookup data structures - number of unique url: " + urlCache.unique);

        urlCache.initGetFromCache();
        boolean done = false;
        tty("raw");

        while (!done) {
            char c = getChar();
            switch(c) {
                case ADD_URL: {
                    System.out.print("url: ");
                    tty("cooked");
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    try {
                        String url = in.readLine();
                        urlCache.addToCache(url);
                    } catch(Exception e) {
                        System.out.println(e.getMessage());
                        continue;
                    } finally {
                        tty("raw");
                    }
                    // Fall through....
                }
                case RESET_GET_FROM_CACHE:
                    System.out.print("...resetting... ");
                    urlCache.initGetFromCache();
                    break;

                default:
                    if (c <= QUIT_CHAR) {
                        System.out.println();
                        done = true;
                    } else {
                        try {
                            System.out.print(urlCache.getFromCache(c) + ", ");
                        } catch (Exception e) {
                            System.out.println("getFromCache exception");
                            System.out.println(e.getMessage());
                        }
                    }
                    break;
            } 
        }

        tty("cooked");
        return;
    } 
};
