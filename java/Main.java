import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var portString = System.getenv("ARDUINO_PORT");
        if (portString == null) {
            System.err.println("Environment variable `ARDUINO_PORT` not set.");
            return;
        }

        try (var arduino = new ArduinoCom(portString)) {
            arduino.awaitReady();
            if (args.length > 0 && args[0].equals("repl")) {
                repl(arduino);
                return;
            }
            arduino.sendMessage("RED");
            var msg = arduino.readMessage();
            System.out.println(msg + " " + Arrays.toString(msg.getBytes()));
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private static void repl(ArduinoCom arduino) {
        try (var scanner = new Scanner(System.in)) {
            while (true) {
                var line = scanner.nextLine().trim();
                arduino.sendMessage(line);
                var msg = arduino.readMessage();
                System.out.println(msg + " <- " + Arrays.toString(msg.getBytes()));
            }
        }
    }
}
