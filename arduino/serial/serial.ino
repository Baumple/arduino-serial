#include <string.h>

#define RED_LED 2
#define GREEN_LED 12

#define DEFAULT_DELAY 50

void blinkTimes(int times) {
    digitalWrite(RED_LED, HIGH);
    for (int i = 0; i < times; i++) {
        digitalWrite(GREEN_LED, HIGH);
        delay(1000);
        digitalWrite(GREEN_LED, LOW);
        delay(1000);
    }
    digitalWrite(RED_LED, LOW);
    digitalWrite(GREEN_LED, LOW);
}

void sendMessage(char* msg) {
    byte len = strlen(msg);
    Serial.write(0x02);
    Serial.write(len);
    Serial.print(msg);
}

void sendInvalidMessage(char* msg) {
    int len = strlen(msg) - 1;
    Serial.write(0x02);
    Serial.write(len);
    Serial.print(msg);
}

char* readMessage() {
    while (Serial.available() == 0);
    while (Serial.read() != 0x02);

    int len = Serial.read();
    char* buffer = malloc(len);
    int nBytes = Serial.readBytes(buffer, len);

    if (nBytes != len) return NULL;
    else return buffer;
}

bool awaitReady() {
    char* msg = readMessage();

    blinkTimes(25);

    return strcmp(msg, "READY") == 0;
}

void setup() {
    Serial.begin(9600);
    pinMode(RED_LED, OUTPUT);
    pinMode(GREEN_LED, OUTPUT);

    sendMessage("READY");
    while (!awaitReady());
}

void loop() {
}
