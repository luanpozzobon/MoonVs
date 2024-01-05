package luan.moonvs.models.exceptions;

public class IllegalIdException extends Throwable {
    public String message;
    public IllegalIdException(String message) {
        this.message = message;
    }
}
