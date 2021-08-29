package Consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

public class MessageReceiver implements AutoCloseable{

    private final String EXCHANGER_NAME = "topicExchanger";
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private String queueName;
    private List<String> keys;

    public MessageReceiver(String host, String username, String password) {
        try {
        factory =  new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(username);
        factory.setPassword(password);
        connection= factory.newConnection();
        channel = connection.createChannel();
        queueName = channel.queueDeclare().getQueue();
        keys = new ArrayList<>();
        channel.exchangeDeclare(EXCHANGER_NAME, BuiltinExchangeType.TOPIC);
        } catch (IOException | TimeoutException e) {
            new RuntimeException("Initializing Connection or Channel was failed!", e);
        }
    }

    public void subscribeCategory(String key){
        try {
            keys.add(key.toLowerCase(Locale.ROOT));
            channel.queueBind(queueName, EXCHANGER_NAME, key.toLowerCase(Locale.ROOT));
            System.out.println("You have already subscribed on " + key + " news");
        } catch (IOException e) {
            new RuntimeException("Subscribing was failed!", e);
        }
    }

    public void unsubscribeCategory(String key){
        if (keys.remove(key.toLowerCase(Locale.ROOT))){
            try {
                channel.queueUnbind(queueName, EXCHANGER_NAME, key.toLowerCase(Locale.ROOT));
                System.out.println("You have already unsubscribed of " + key + " news");
            } catch (IOException e) {
                new RuntimeException("Unsubscribing was failed!", e);
            }
        }
    }

    public void displayReceivedMessage(){
        try {
            DeliverCallback callback = (consumerTag, delivery) -> System.out.println(new String(delivery.getBody(), StandardCharsets.UTF_8));
            channel.basicConsume(queueName, true, callback, consumerTag ->{});
        } catch (IOException e) {
            new RuntimeException("Receiving message from RabbitMQ was failed!", e);
        }
    }

    @Override
    public void close() throws Exception {
        channel.close();
        connection.close();
    }
}
