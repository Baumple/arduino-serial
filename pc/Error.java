import com.fazecast.jSerialComm.SerialPortInvalidPortException;

public sealed interface Error permits Error.InvalidSerialPort {
    String reason();

    public record InvalidSerialPort(SerialPortInvalidPortException exception) implements Error {
        @Override
        public String reason() {
            return exception.toString();
        }
    };
}
