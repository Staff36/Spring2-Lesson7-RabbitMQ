package Supplier;

import Common.CommandLineListener;

import java.util.Locale;

public class MQSupplier {

    public static void main(String[] args) {
        run();
    }

    public static void run(){
        CommandLineListener listener = new CommandLineListener();
        MessageSender sender = new MessageSender("localhost", "admin", "admin");
        String lineFromConsole = "";
        while (!lineFromConsole.equals("quit")){
            lineFromConsole = listener.getMsgFromConsole();
            int firstSpaceIndex = lineFromConsole.indexOf(" ");
            if (firstSpaceIndex > 0 && lineFromConsole.length() - 1 > firstSpaceIndex){
                String key = lineFromConsole.substring(0, firstSpaceIndex).toLowerCase(Locale.ROOT);
                String message = lineFromConsole.substring(firstSpaceIndex + 1);
                sender.putMessageIntoMQ(key, message);
            } else {
                System.out.println("Post must contains space");
            }
        }
    }
}
