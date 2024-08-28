package com.ifpb.edu.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Worker {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        // Criação da fábrica de conexões
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        // Criação da conexão
        try (Connection connection = connectionFactory.newConnection(); 
             Channel channel = connection.createChannel()) {
             
            // Declaração da fila
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            System.out.println("[*] Aguardando mensagens. Para sair, pressione CTRL + C");

            // Configuração do QoS
            channel.basicQos(1);

            // Definição do callback para o consumo de mensagens
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String mensagem = new String(delivery.getBody(), "UTF-8");

                System.out.println("[x] Recebido '" + mensagem + "'");
                try {
                    doWork(mensagem);
                } finally {
                    System.out.println("[x] Done");
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };

            // Consumo de mensagens
            channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {});
        }
    }

    // Método para simular o tempo de trabalho
    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}