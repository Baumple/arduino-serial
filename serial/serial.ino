#include <string.h>

#define RED_LED 2
#define GREEN_LED 12

#define DEFAULT_DELAY 50

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

void blink(int led, int dl) {
    digitalWrite(led, HIGH);
    delay(dl);
    digitalWrite(led, LOW);
}

bool awaitReady() {
    char* msg = readMessage();
    return strcmp(msg, "READY") == 0;
}

void setup() {
    Serial.begin(9600);
    pinMode(RED_LED, OUTPUT);
    pinMode(GREEN_LED, OUTPUT);

    digitalWrite(RED_LED, LOW);
    digitalWrite(GREEN_LED, LOW);

    sendMessage("READY");
    while(!awaitReady());

    digitalWrite(GREEN_LED, HIGH);
    delay(100);
    digitalWrite(GREEN_LED, LOW);
}

void loop() {
    delay(100);
    // char* msg = readMessage();
    // if (strcmp(msg, "A") == 0) {
    //     digitalWrite(RED_LED, HIGH);
    //     sendMessage("OK");
    // } else {
    //     blink(RED_LED, 100);
    // }
    // free(msg);
}
