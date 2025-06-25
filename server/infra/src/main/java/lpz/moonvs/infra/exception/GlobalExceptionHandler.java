package lpz.moonvs.infra.exception;

import lpz.moonvs.domain.auth.exception.UserAlreadyExistsException;
import lpz.moonvs.domain.auth.exception.UserDoesNotExistsException;
import lpz.moonvs.domain.playlist.exception.PlaylistAlreadyExistsException;
import lpz.moonvs.domain.playlist.exception.PlaylistItemNotFoundException;
import lpz.moonvs.domain.playlist.exception.PlaylistNotFoundException;
import lpz.moonvs.domain.seedwork.exception.DomainException;
import lpz.moonvs.domain.seedwork.exception.DomainValidationException;
import lpz.moonvs.domain.seedwork.exception.NoAccessToResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static ProblemDetail getBaseProblemDetail(final HttpStatus status, final Exception ex) {
        final var problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getLocalizedMessage());
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    private static ProblemDetail getDomainProblemDetail(final HttpStatus status, final DomainException ex) {
        final var problemDetail = getBaseProblemDetail(status, ex);
        problemDetail.setProperty("errors", ex.getErrors());

        return problemDetail;
    }

    private void logDomain(final ProblemDetail problemDetail, final DomainException ex) {
        logger.warn("""
                {}
                Errors: {}
                """, problemDetail.getDetail(), ex.getErrors());
    }

    private void logError(final Exception ex) {
        logger.error("Unexpected Exception:", ex);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExistsException(final UserAlreadyExistsException ex) {
        final var problemDetail = getDomainProblemDetail(HttpStatus.CONFLICT, ex);
        this.logDomain(problemDetail, ex);
        
        return problemDetail;
    }

    @ExceptionHandler(UserDoesNotExistsException.class)
    public ProblemDetail handleUserDoesNotExistsException(final UserDoesNotExistsException ex) {
        final var problemDetail = getDomainProblemDetail(HttpStatus.NOT_FOUND, ex);
        this.logDomain(problemDetail, ex);

        return problemDetail;
    }

    @ExceptionHandler(PlaylistAlreadyExistsException.class)
    public ProblemDetail handlePlaylistAlreadyExistsException(final PlaylistAlreadyExistsException ex) {
        final var problemDetail = getDomainProblemDetail(HttpStatus.CONFLICT, ex);
        this.logDomain(problemDetail, ex);

        return problemDetail;
    }

    @ExceptionHandler(PlaylistNotFoundException.class)
    public ProblemDetail handlePlaylistNotFoundException(final PlaylistNotFoundException ex) {
        final var problemDetail = getDomainProblemDetail(HttpStatus.NOT_FOUND, ex);
        this.logDomain(problemDetail, ex);

        return problemDetail;
    }

    @ExceptionHandler(PlaylistItemNotFoundException.class)
    public ProblemDetail handlePlaylistItemNotFoundException(final PlaylistItemNotFoundException ex) {
        final var problemDetail = getDomainProblemDetail(HttpStatus.NOT_FOUND, ex);
        this.logDomain(problemDetail, ex);

        return problemDetail;
    }

    @ExceptionHandler(NoAccessToResourceException.class)
    public ProblemDetail handleNoAccessToResourceException(final NoAccessToResourceException ex) {
        final var problemDetail = getDomainProblemDetail(HttpStatus.FORBIDDEN, ex);
        this.logDomain(problemDetail, ex);

        return problemDetail;
    }

    @ExceptionHandler(DomainValidationException.class)
    public ProblemDetail handleDomainValidationException(final DomainValidationException ex) {
        final var problemDetail = getDomainProblemDetail(HttpStatus.BAD_REQUEST, ex);
        this.logDomain(problemDetail, ex);

        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(final IllegalArgumentException ex) {
        final var problemDetail = getBaseProblemDetail(HttpStatus.BAD_REQUEST, ex);
        this.logError(ex);

        return problemDetail;
    }

    @ExceptionHandler(SQLException.class)
    public ProblemDetail handleSQLException(final SQLException ex) {
        return handleGenericException(ex);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(final Exception ex) {
        final var problemDetail = getBaseProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        this.logError(ex);

        return problemDetail;
    }
}