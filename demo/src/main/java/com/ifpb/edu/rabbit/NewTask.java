package com.ifpb.edu.rabbit;

import com.rabbitmq.client.Channel;
 import com.rabbitmq.client.Connection;
 import com.rabbitmq.client.ConnectionFactory;
 import com.rabbitmq.client.MessageProperties;


 public class NewTask {


  private static final String TASK_QUEUE_NAME = "task_queue";


  public static void main (String [] argv) throws Exception {
      ConnectionFactory connectionFactory = new ConnectionFactory();
      connectionFactory.setHost("localhost");
      connectionFactory.setPort(5672);

      try (Connection connection = connectionFactory.newConnection ();
         Channel channel = connection.createChannel ()) {
        channel.queueDeclare (TASK_QUEUE_NAME, true, false, false, null);


        String mensagem = String.join("", argv);


        channel.basicPublish ("", TASK_QUEUE_NAME,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                mensagem.getBytes("UTF-8"));
        System.out.println ("[x] Enviado '" + mensagem + "Louise Fernandes Caetano");
    }
  }


 }

