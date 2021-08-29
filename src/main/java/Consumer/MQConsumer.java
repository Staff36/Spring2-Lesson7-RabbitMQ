package Consumer;

import Common.CommandLineListener;

public class MQConsumer {
    public static void main(String[] args) {
        run();

    }

    private static void run() {
        try (MessageReceiver receiver = new MessageReceiver("localhost", "admin", "admin")){
        CommandLineListener listener = new CommandLineListener();
        receiver.displayReceivedMessage();
        String msgFromConsole = "";
        while (!msgFromConsole.equals("quit")){
             msgFromConsole= listener.getMsgFromConsole();
            if (msgFromConsole.length() < 3 || !msgFromConsole.contains(" ")){
                System.out.println("Syntax error");
                continue;
            }
            if (msgFromConsole.startsWith("s ")){
                receiver.subscribeCategory(msgFromConsole.substring(2));
            } else if (msgFromConsole.startsWith("u ")){
                receiver.unsubscribeCategory(msgFromConsole.substring(2));
            } else {
                System.out.println("Unknown command");
            }
        }
        } catch (Exception e) {
            new RuntimeException("Closing method MessageReceiver throw exception", e);
        }
    }

}
