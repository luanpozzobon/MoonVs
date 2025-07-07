package lpz.moonvs.infra.exception;

public class DataAccessException extends RuntimeException {
    public static final String SAVE = "error.data.save";
    public static final String SELECT = "error.data.select";
    public static final String UPDATE = "error.data.update";
    public static final String DELETE = "error.data.delete";

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public static DataAccessException save(Throwable cause) {
        return new DataAccessException(SAVE, cause);
    }

    public static DataAccessException select(Throwable cause) {
        return new DataAccessException(SELECT, cause);
    }

    public static DataAccessException update(Throwable cause) {
        return new DataAccessException(UPDATE, cause);
    }

    public static DataAccessException delete(Throwable cause) {
        return new DataAccessException(DELETE, cause);
    }
}
