import com.fazecast.jSerialComm.*;

public class enispenis {
    public static void main(String[] args) {
        var arduinoPort = SerialPort.getCommPort("/dev/ttyACM1");
        arduinoPort.setParity(SerialPort.NO_PARITY);
        arduinoPort.setBaudRate(9600);
        arduinoPort.setNumDataBits(8);
        arduinoPort.setNumStopBits(1);
        arduinoPort.openPort();

        arduinoPort.setRTS();
        System.out.println("DCD: " + arduinoPort.getDCD());
        System.out.println("DSR: " + arduinoPort.getDSR());
        System.out.println("DTR: " + arduinoPort.getDTR());
        System.out.println("RTS: " + arduinoPort.getRTS());

        try {
            var a = new byte[] { 'a' };
            arduinoPort.writeBytes(a, 1);
            arduinoPort.flushIOBuffers();
            System.out.println("Sent bytes: " + new String(a));
            var buf = new byte[256];
            while (arduinoPort.bytesAvailable() > 0) {
                arduinoPort.readBytes(buf, buf.length);
                System.out.println("Buffer: " + new String(buf));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            //TODO: handle exception
        }
    }
}
