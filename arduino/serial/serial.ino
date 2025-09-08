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
    while (Serial.available() < 2 
        && Serial.read() != 0x02);

    int len = Serial.read();
    // wait until len bytes are available to read
    while (Serial.available() < len);

    char* buffer = malloc(len + 1);
    int nBytes = Serial.readBytes(buffer, len);
    buffer[len] = '\0';

    sendMessage("OK");
    return buffer;
}

bool sendMessage(char* msg) {
    byte len = strlen(msg);
    Serial.write(0x02);
    Serial.write(len);
    Serial.print(msg);
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
    }
}
