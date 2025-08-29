int p2 = 2;
int p12 = 12;

void write_message(char* message) {
}

void lassLeuchtenallah(int dl){
  digitalWrite(p2, LOW);
  delay(dl);
  digitalWrite(p2, HIGH);
  digitalWrite(p12, LOW);
  delay(dl);
  digitalWrite(p12, HIGH);
}

void setup() {
  Serial.begin(9600);
  pinMode(p2, OUTPUT);
  pinMode(p12, OUTPUT);
  digitalWrite(p2, LOW);
  digitalWrite(p12, LOW);
}

bool received = false;
void loop() {
  if (Serial.available() > 0) {
    Serial.print("Dezz nuts");
    Serial.flush();
    received = true;
  }
  if (received) {
    lassLeuchtenallah(50);
  }
}
