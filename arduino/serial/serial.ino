#include <string.h>

#define RED_LED 2
#define GREEN_LED 12

#define DEFAULT_DELAY 50

/// Waits until at least two bytes are available to read (HEADER, LEN)
/// Then allocates appropriate amount of memory and reads the incoming bytes
/// when there is LEN bytes available.
///
/// When reading is finished it sends back 'OK' to indicate that the message was
/// read successfully.
char* readMessage() {
    // we need at least the header and the len of the package
    while (Serial.available() < 2);
    while (Serial.read() != 0x02);

    int len = Serial.read();
    // wait until len bytes are available to read
    while (Serial.available() < len);

    char* buffer = malloc(len + 1);
    int nBytes = Serial.readBytes(buffer, len);
    buffer[len] = '\0';

    sendMessage("OK");
    return buffer;
}

char* buildMessagePacket(char* msg) {
    byte len = (byte) strlen(msg);
    char* buffer = (char*) malloc(2 + len);
    buffer[0] = 0x02;
    buffer[1] = len;
    for (byte i = 0; i < len; i++) {
      buffer[i + 2] = msg[i];
    }
    return buffer;
}

bool sendMessage(char* msg) {
    char* msgBuffer = buildMessagePacket(msg);
    Serial.write(msgBuffer, 2 + strlen(msg));
    free(msgBuffer);
}

void setup() {
    Serial.begin(9600);
    pinMode(RED_LED, OUTPUT);
    pinMode(GREEN_LED, OUTPUT);
    sendMessage("READY");
}

void loop() {
    char* msg = readMessage();
    if (strcmp(msg, "RED") == 0) {
      digitalWrite(RED_LED, HIGH);
    } else if (strcmp(msg, "GREEN") == 0) {
      digitalWrite(GREEN_LED, HIGH);
    }
    free(msg);
}
