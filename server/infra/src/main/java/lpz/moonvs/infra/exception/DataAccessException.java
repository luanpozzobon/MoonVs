package lpz.moonvs.infra.exception;

public class DataAccessException extends RuntimeException {
    public interface Schema {
        String SAVE = "error.data.save";
        String SELECT = "error.data.select";
        String UPDATE = "error.data.update";
        String DELETE = "error.data.delete";
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public static DataAccessException save(Throwable cause) {
        return new DataAccessException(Schema.SAVE, cause);
    }

    public static DataAccessException select(Throwable cause) {
        return new DataAccessException(Schema.SELECT, cause);
    }

    public static DataAccessException update(Throwable cause) {
        return new DataAccessException(Schema.UPDATE, cause);
    }

    public static DataAccessException delete(Throwable cause) {
        return new DataAccessException(Schema.DELETE, cause);
    }
}
