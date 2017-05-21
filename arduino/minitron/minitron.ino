#include <Wire.h>
//#include <Adafruit_GFX.h>
#include <Adafruit_IS31FL3731.h>

//////////////////////
// Library Includes //
//////////////////////
// SoftwareSerial is required (even you don't intend on
// using it).
#include <SoftwareSerial.h> 
#include <SparkFunESP8266WiFi.h>

//////////////////////////////
// Constant Definitions //
//////////////////////////////

// Replace SSID and PWD with the appropriate values for
// your WiFi network.
const char mySSID[] = "SSID";
const char myPSK[] = "PASSWD";

// Replace DEVICE_CODE with your device code.
const char deviceCode[] = "DEVICECODE";
const int buttonPin = 5;

// If you're using the full breakout...
Adafruit_IS31FL3731 matrix = Adafruit_IS31FL3731();

// If you're using the FeatherWing version
//Adafruit_IS31FL3731_Wing matrix = Adafruit_IS31FL3731_Wing();

//////////////////
// HTTP Strings //
//////////////////
const char destServer[] = "minitron.herokuapp.com";

int channel=0;

void initializeESP8266()
{
  // esp8266.begin() verifies that the ESP8266 is operational
  // and sets it up for the rest of the sketch.
  // It returns either true or false -- indicating whether
  // communication was successul or not.
  // true
  int test = esp8266.begin();
  if (test != true)
  {
    Serial.println(F("Error talking to ESP8266."));
  }
  Serial.println(F("ESP8266 Shield Present"));
}


void setup() {
  pinMode(buttonPin, INPUT_PULLUP);
  Serial.begin(9600);
  Serial.println("ISSI manual animation test");
  if (! matrix.begin()) {
    Serial.println("IS31 not found");
    while (1);
  }
  Serial.println("IS31 Found!");

  Serial.begin(9600);
  
  // initializeESP8266() verifies communication with the WiFi
  // shield, and sets it up.
  initializeESP8266();
  Serial.println("Initialization complete.");
  // connectESP8266() connects to the defined WiFi network.
  connectESP8266();

  // displayConnectInfo prints the Shield's local IP
  // and the network it's connected to.
  displayConnectInfo();  

}

void displayConnectInfo()
{
  char connectedSSID[24];
  memset(connectedSSID, 0, 24);
  // esp8266.getAP() can be used to check which AP the
  // ESP8266 is connected to. It returns an error code.
  // The connected AP is returned by reference as a parameter.
  int retVal = esp8266.getAP(connectedSSID);
  if (retVal > 0)
  {
    Serial.print(F("Connected to: "));
    Serial.println(connectedSSID);
  }

  // esp8266.localIP returns an IPAddress variable with the
  // ESP8266's current local IP address.
  IPAddress myIP = esp8266.localIP();
  Serial.print(F("My IP: ")); Serial.println(myIP);
}

String getTheMessage()
{
  String httpRequest = "GET /message/" + String(deviceCode) + "/" + String(channel) + " HTTP/1.1\r\n"
                       "Host: minitron.herokuapp.com\r\n\r\n";
                           //"Connection: close\r\n";              //Note that this fails on heroku.

  Serial.println("About to request: " + httpRequest);
  ESP8266Client client;

  int retVal = client.connect(destServer, 80);
  if (retVal <= 0)
  {
    Serial.println(F("Failed to connect to server."));
    String msg = "...";
    return msg;
  }

  // print and write can be used to send data to a connected
  // client connection.
  Serial.println("Making request...");
  
  client.print(httpRequest);
  Serial.println("Response returned.");
  
  // available() will return the number of characters
  // currently in the receive buffer.
  String w = "";
  boolean disp = false;
  Serial.println("Reading response.");
  while (client.available()) {
    char c = client.read();
    Serial.print(c);
    if (c=='|') {
      if (disp) {
        disp=false;
    } else
      disp=true;
    } 
    if ((c!='|') && (disp)) w+=c;
  }
  
  if (client.connected())
    client.stop(); // stop() closes a TCP connection.

  return w;
}


void connectESP8266()
{
  int retVal = esp8266.getMode();
  if (retVal != ESP8266_MODE_STA)
  { // If it's not in station mode.
    retVal = esp8266.setMode(ESP8266_MODE_STA);
    if (retVal < 0)
    {
      Serial.println(F("Error setting mode."));
    }
  }
}
  
void matrixPrint(String str) {
  if (str.length()==0) { str = "...";}
  for(int pos=0;pos<str.length();pos++) {
    char subc = str[pos];  
    //Serial.println("About to display: " + subc);
    int offset = -10;
    matrix.setTextSize(2);
    matrix.setTextWrap(false);  // we dont want text to wrap so it scrolls nicely
    matrix.setTextColor(32);
    matrix.setRotation(1);
    for (int8_t x=7; x>=offset; x--) {
      matrix.clear();
      matrix.setCursor(x,0);
      matrix.print(subc);
      delay(5);
    }
  }
}  


void loop() {
  Serial.println("Looping...");
  int buttonState = digitalRead(buttonPin);
  while (buttonState == LOW) {
    channel+=1;
    if(channel>=12) channel=0;
    matrixPrint(String(channel));
    Serial.println(String(channel));
    buttonState = digitalRead(buttonPin);
    delay(1500);
  }
  
  String theMessage = getTheMessage();
  if(theMessage.length()>0) {
  for(int i=0;i<3;i++) {
    matrixPrint(theMessage);
    matrixPrint("  "); 
    }
    delay(1000);
  }
  
}


