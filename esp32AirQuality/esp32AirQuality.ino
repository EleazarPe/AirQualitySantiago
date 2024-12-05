#include <ArduinoJson.h>

#include <PubSubClient.h>
#include <WiFi.h>
#include <WiFiAP.h>
#include <WiFiClient.h>
#include <WiFiGeneric.h>
#include <WiFiMulti.h>
#include <WiFiScan.h>
#include <WiFiServer.h>
#include <WiFiSTA.h>
#include <WiFiType.h>
#include <WiFiUdp.h>

const char* ssid = ""; //
const char* password = "";//

// Broker MQTT
const char* mqtt_server = "192.168.1.8"; //192.168.1.8
const int mqtt_port = 1883;               // Puerto por defecto de MQTT
const char* mqtt_user = "user";        // Usuario RabbitMQ
const char* mqtt_password = "password"; // Contraseña RabbitMQ

WiFiClient espClient;
PubSubClient client(espClient);

void setup_wifi() {
  delay(10);
  Serial.println("Conectando a Wi-Fi...");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("Wi-Fi conectado");
}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Intentando conectar a MQTT...");
    if (client.connect("ESP32Client", mqtt_user, mqtt_password)) {
      Serial.println("Conectado");
    } else {
      Serial.print("Falló, rc=");
      Serial.print(client.state());
      Serial.println(" Intentando de nuevo en 5 segundos...");
      delay(5000);
    }
  }
}
  String localidades[] = {
    "Licey al Medio",
    "Los Jardines Metropolitanos",
    "Tamboril",
    "Hato del Yaque",
    "Las Charcas",
    "Villa Gonzales",
    "La Barranquita",
    "Arroyo Hondo",
    "La Canela",
    "Cienfuegos"
  };
    String contaminantes[] = {
    "PM1.0",
    "PM2.5",
    "PM10",
    "CO2",
    "O3",
    "CO",
    "CH4",
    "SO2",
    "N"
  };
void setup() {
  Serial.begin(115200);
  setup_wifi();
  client.setServer(mqtt_server, mqtt_port);
  
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
  int valor = 0;
  for(int i = 0; i < sizeof(localidades) / sizeof(localidades[0]); i++){
    for(int j = 0; j < sizeof(contaminantes) / sizeof(contaminantes[0]); j++){
      valor = 0;
      if(contaminantes[j] == "PM1.0"){
        valor = random(0,200);
      }else if(contaminantes[j] == "PM2.5"){
        valor = random(0,200);
      }else if(contaminantes[j] == "PM10"){
        valor = random(0,300);
      }else if(contaminantes[j] == "CO2"){
        valor = random(0,6000);
      }else if(contaminantes[j] == "O3"){
        valor = random(0,300);
      }else if(contaminantes[j] == "CO"){
        valor = random(0,20);
      }else if(contaminantes[j] == "CH4"){
        valor = random(0,200);
      }else if(contaminantes[j] == "SO2"){
        valor = random(0,100);
      }else if(contaminantes[j] == "N"){
        valor = random(0,300);
      }
      DynamicJsonDocument doc(1024);
      doc["terminal"] = localidades[i];
      doc["escala"] = contaminantes[j];
      doc["cantidad"] = valor;
      String output;
      serializeJson(doc, output);
      String message =  output;
      if (client.publish("hello", message.c_str())) {
        Serial.println("Mensaje enviado: " + message);
      } else {
        Serial.println("Fallo al enviar mensaje");
      }
   }
 }

  delay(60000);
}
