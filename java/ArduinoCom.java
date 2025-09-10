import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Logger;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;

public class ArduinoCom implements Closeable {
    /**
     * The byte which is at the start of a message packet
     */
    private static final byte HEADER_BYTE = 0x02;

    private final SerialPort port;

    /**
     * @param portString String - The system's device identifier
     */
    public ArduinoCom(String portString) throws SerialPortInvalidPortException {
        var port = SerialPort.getCommPort(portString);
        port.setParity(SerialPort.NO_PARITY);
        port.setBaudRate(9600);
        port.setNumDataBits(8);
        port.setNumStopBits(1);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
        port.openPort();
        this.port = port;
    }

    /**
     * Read a byte
     */
    private byte readByte() {
        var b = new byte[1];
        port.readBytes(b, b.length);
        return b[0];
    }

    /**
     * Blocks until the header byte is received
     */
    private void awaitHeader() {
        var header = new byte[] { 0 };
        do {
            port.readBytes(header, 1);
        } while (header[0] != HEADER_BYTE);
    }

    /**
     * Block until the Arduino sends a message packet containing "READY"
     */
    public void awaitReady() {
        while (true) {
            var msg = readMessage();
            if (msg != null && msg.equals("READY"))
                break;
        }
        System.out.println("Received READY");
    }

    /**
     * Block until a message packet containing "OK" has been received
     */
    private void awaitOk() {
        while (true) {
            var msg = readMessage();
            if (msg != null && msg.equals("OK"))
                return;
        }
    }

    /**
     * Reads a message packet sent by the Arduino.
     */
    public String readMessage() {
        awaitHeader();
        byte len = readByte();
        var buf = new byte[len];
        int nBytes = port.readBytes(buf, buf.length);
        if (nBytes != len)
            return null;

        return new String(buf, StandardCharsets.US_ASCII);
    }

    private byte[] buildMessagePacket(String msg) {
        var buf = new byte[2 + msg.length()];
        buf[0] = HEADER_BYTE;
        buf[1] = (byte) msg.length();
        System.arraycopy(msg.getBytes(), 0, buf, 2, msg.length());
        return buf;
    }

    /**
     * Send a message packet and wait for the Arduino to respond with "OK"
     *
     * @param msg String - The message to send
     */
    public void sendMessage(String msg) {
        var bytes = buildMessagePacket(msg);
        port.writeBytes(bytes, bytes.length);
        awaitOk();
    }

    @Override
    public void close() throws IOException {
        this.port.closePort();
    }

}
