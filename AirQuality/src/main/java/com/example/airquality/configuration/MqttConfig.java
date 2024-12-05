package com.example.airquality.configuration;

import com.example.airquality.model.Mediciones;
import com.example.airquality.service.MedicionesServices;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;


@Configuration
public class MqttConfig {
    @Value("${mqtt.server}")
    String servidor;
    @Value("${mqtt.user}")
    String usuario;
    @Value("${mqtt.password}")
    String contrasena;
    @Value("${mqtt.topic}")
    String cola;
    @Autowired
    private MedicionesServices medicionesServices;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{ servidor });
        options.setUserName(usuario);
        options.setPassword(contrasena.toCharArray());
        options.setAutomaticReconnect(true);
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public IntegrationFlow mqttInFlow() {
        return IntegrationFlow.from(mqttInbound())
                .transform(p -> p)
                .handle(message -> {
                    System.out.println("Mensaje recibido: " + message.getPayload());
                    JsonObject jsonObject = JsonParser.parseString(message.getPayload().toString()).getAsJsonObject();
                    medicionesServices.save(new Mediciones(jsonObject.get("terminal").getAsString(), jsonObject.get("escala").getAsString(), jsonObject.get("cantidad").getAsInt()));
                })
                .get();
    }
    private LoggingHandler logger() {
        LoggingHandler loggingHandler = new LoggingHandler("INFO");
        loggingHandler.setLoggerName("siSample");
        return loggingHandler;
    }
    @Bean
    public MessageProducerSupport mqttInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("siSampleConsumer",
                mqttClientFactory(), cola);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        return adapter;
    }

}
//medicionesServices.save(new Mediciones("Dispositivo", "Escala", 10));
