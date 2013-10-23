#include <SoftwareSerial.h>
#include <TinyGPS.h>


#define RXPIN 2
#define TXPIN 3


#define TERMBAUD  115200


#define GPSBAUD  4800


//biblioteca emulacion puertos series
#include <SoftwareSerial.h>

//biblioteca manejo de tarjeta de control de motores
#include <PololuQik.h>

//objeto para controlar los motores
PololuQik2s12v10 controlMotores(2, 3, 5);

//velocidad de motores de (0,127) en un sentido y de (0,-127) en otro sentido
int velocidadM0=0;
int velocidadM1=0;


TinyGPS gps;

SoftwareSerial uart_gps(RXPIN, TXPIN);


void getgps(TinyGPS &gps);
int bulb1 = 13;

boolean envioGPS=false;


String trama[5];
String  message; // Mensaje que resivira las ordenes
int     commaPosition;  // Posicion del caracter que dividira las tramas
void setup(){
 
   Serial.begin(TERMBAUD);
  // Sets baud rate of your GPS
  uart_gps.begin(GPSBAUD);
 
  
  Serial.println("GeoDroidCx");
  Serial.println("Romel & Andrea");
 
  
}

void loop(){

  //Comprueba si hay algo disponible en el puerto serie
  if (Serial.available()==1){
    //Leemos el String enviado.
    String message=Serial.readString();
    //Lo recorremos y separamos cada uno de los mensajes
    for(int i=0; i<5;i++){
      commaPosition = message.indexOf(':');
      if(commaPosition != -1){
        trama[i]=message.substring(0,commaPosition);
          message = message.substring(commaPosition+1, message.length());
      }else{  
         if(message.length() > 0)
          trama[i]=message;  
      }
   }
   if (trama[4].toInt()==1) {
     envioGPS=true;
   }else 
     Serial.println("No llego nada:" + trama[0]);//Protocol doesn't recognize the commmand
  }
  while(envioGPS==true){
    while(uart_gps.available()){
        int c = uart_gps.read();    // Carga informacion disponible
        if(gps.encode(c)){      //si esta informacion es valida
          getgps(gps);         // entonces graba la informacion
        }
    }
  }
   }
 
void getgps(TinyGPS &gps){
  
  // Definimos las variables que podriamos utilizar
  float latitude, longitude;
  String tramaGPS="";
  // Then call this function
  gps.f_get_position(&latitude, &longitude);
  // You can now print variables latitude and longitude
  
   tramaGPS="#21#"+print_float(latitude, 10, 5)+print_float(longitude, 10, 5)+"\r";
  Serial.println(tramaGPS);
   envioGPS=false;

  /*if(aux==100){
  bandera=false;
  aux=0;
  }*/
  
}

void motores(){
  //velocidad M0 se le resta 32 que se sumaron del otro lado
  velocidadM0=((trama[1].toInt())-32)*2;

  //sentido M0 negativo
  if (trama[0].toInt()==0)
    {
    velocidadM0=velocidadM0*(-1);
    }

  //velocidad M1 se le resta 32 que se sumaron del otro lado
  velocidadM1=((trama[3].toInt())-32)*2;

  //sentido M1 negativo
  if (trama[2].toInt()==0)
    {
    velocidadM1=velocidadM1*(-1);
    }

  //velocidad enviada al motor 0
  controlMotores.setM0Speed(velocidadM0);
  //velocidad enviada al motor 1
  controlMotores.setM1Speed(velocidadM1);
  }

static String print_float(float val, int len, int prec)
  {
  char sz[32];
  String s="";
 
    //dato float, longitud, precision, char
    dtostrf(val, len, prec, sz);

    s=sz;
    return s;
    
  }

