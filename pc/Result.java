public sealed interface Result<T> permits Result.Success, Result.Failure {
    public record Success<T>(T value) implements Result<T> {
    }

    public record Failure<T>(Error value) implements Result<T> {
    }
}
