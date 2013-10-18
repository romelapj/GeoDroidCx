#include <SoftwareSerial.h>
#include <TinyGPS.h>


#define RXPIN 2
#define TXPIN 3


#define TERMBAUD  115200


#define GPSBAUD  4800


TinyGPS gps;

SoftwareSerial uart_gps(RXPIN, TXPIN);


void getgps(TinyGPS &gps);
int bulb1 = 13;

boolean envioGPS=false;


String trama[5];
String  text = "Peter,Paul,Mary";  // an example string
String  message = text; // holds text not yet split
int     commaPosition;  // the position of the next comma in the string
void setup(){
 
  pinMode(bulb1,OUTPUT);
   Serial.begin(TERMBAUD);
  // Sets baud rate of your GPS
  uart_gps.begin(GPSBAUD);
 
  //resest the WT-11 module
  
   
  Serial.begin(115200);
  Serial.println("Envio Bluetooth");
  Serial.println("Romel & Andrea");
 
  
}

void loop(){

  //Check if ther's anything available on the serial port
  if (Serial.available()==1){
    //we do read the character
    String message=Serial.readString();
    Serial.println(message);
    for(int i=0; i<5;i++){
      commaPosition = message.indexOf(':');
      if(commaPosition != -1){
        trama[i]=message.substring(0,commaPosition);
        int aux=(int)trama[i];
          message = message.substring(commaPosition+1, message.length());
      }else{  // here after the last comma is found
         if(message.length() > 0)
           Serial.println(message);  // if there is text after the last comma,
                                     // print it
      }
   }

    //char character = Serial.read();
    //Serial.println("Esto es lo que envio"+character);
    //if we're not recieving a command we check if the character read is the beginning of a command (@)
                
        if (trama[0]=='A') {envioGPS=true;} //Switch-off bulb1
       
                                           
        else Serial.println("No llego nada:" + trama[0]);//Protocol doesn't recognize the commmand
        //End executing command
        //Reset the command-string
        //character='';
      }
   while(envioGPS==true){
    while(uart_gps.available())     // While there is data on the RX pin...
    {
        int c = uart_gps.read();    // load the data into a variable...
        if(gps.encode(c))      // if there is a new valid sentence...
        {
          getgps(gps);         // then grab the data.
        }
    }
  }
   }
 
void getgps(TinyGPS &gps)
{
  // To get all of the data into varialbes that you can use in your code, 
  // all you need to do is define variables and query the object for the 
  // data. To see the complete list of functions see keywords.txt file in 
  // the TinyGPS and NewSoftSerial libs.
  
  // Define the variables that will be used
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

static String print_float(float val, int len, int prec)
  {
  char sz[32];
  String s="";
 
    //dato float, longitud, precision, char
    dtostrf(val, len, prec, sz);

    s=sz;
    return s;
    
  }

