package Supplier;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class MessageSender {
    private final String EXCHANGER_NAME = "topicExchanger";
    private final ConnectionFactory factory;
    public MessageSender(String host, String username, String password) {
        factory =  new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(username);
        factory.setPassword(password);
    }

    public void putMessageIntoMQ(String key, String message){
        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()){
                channel.exchangeDeclare(EXCHANGER_NAME, BuiltinExchangeType.TOPIC);
                channel.basicPublish(EXCHANGER_NAME, key, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("[" + key + "]MSG SENT: " + message);
        } catch (IOException | TimeoutException e) {
            new RuntimeException("Sending file was failed!", e);
        }

    }
}
