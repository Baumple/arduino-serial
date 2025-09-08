import com.fazecast.jSerialComm.*;

import java.nio.charset.StandardCharsets;

import java.util.Scanner;

public class Main {

    private static final byte HEADER = 0x02;

    private static byte readByte(SerialPort port) {
        var b = new byte[] { 0 };
        port.readBytes(b, b.length);
        return b[0];
    }

    /// Blocks until it receives the header byte
    private static void awaitHeader(SerialPort port) {
        var header = new byte[] { 0 };
        do {
            port.readBytes(header, 1);
        } while (header[0] != HEADER);
    }

    private static void awaitOk(SerialPort port) {
        while (true) {
            var msg = readMessage(port);
            if (msg != null && msg.equals("OK"))
                return;
        }
    }

    private static void sendMessage(SerialPort port, String msg) {
        var bytes = msg.getBytes();
        var len = (byte) msg.length();

        var header = new byte[] { 0x02, len };
        port.writeBytes(header, 1);
        port.writeBytes(bytes, len);

        awaitOk(port);
    }

    private static String readMessage(SerialPort port) {
        awaitHeader(port);

        byte len = readByte(port);
        var buf = new byte[len];

        int nBytes = port.readBytes(buf, buf.length);
        if (nBytes != len) {
            return null;
        }

        return new String(buf, StandardCharsets.US_ASCII);
    }

    private static void awaitReady(SerialPort port) {
        while (true) {
            var msg = readMessage(port);
            if (msg != null && msg.equals("READY"))
                break;
        }
        System.out.println("Received READY");
    }

    private static void setupConnection(SerialPort port) {
        port.setParity(SerialPort.NO_PARITY);
        port.setBaudRate(9600);
        port.setNumDataBits(8);
        port.setNumStopBits(1);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
    }

    public static void main(String[] args) {
        var portString = System.getenv("ARDUINO_PORT");
        if (portString == null) {
            System.err.println("Environment variable `ARDUINO_PORT` not set.");
            return;
        }

        var arduinoPort = SerialPort.getCommPort(portString);
        setupConnection(arduinoPort);
        if (args.length > 1 && args[1].equals("repl"))
            repl(arduinoPort);

        try {
            arduinoPort.openPort();
            awaitReady(arduinoPort);
            sendMessage(arduinoPort, "HELLO");
            var msg = readMessage(arduinoPort);
            System.out.println(msg);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        } finally {
            arduinoPort.closePort();
        }
    }

    private static void repl(SerialPort port) {
        var scanner = new Scanner(System.in);

        while (true) {
            var line = scanner.nextLine().trim();
            sendMessage(port, msg);
        }
    }
}
