package baktulan.instagram.exception.handler;

import baktulan.instagram.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse alreadyExistException(AlreadyExistException alreadyExistException){
        return ExceptionResponse
                .builder()
                .httpStatus(HttpStatus.CONFLICT)
                .className(alreadyExistException.getClass().getSimpleName())
                .message(alreadyExistException.getMessage())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFoundException(NotFoundException notFoundException){
        return ExceptionResponse
                .builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .className(notFoundException.getClass().getSimpleName())
                .message(notFoundException.getMessage())
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse badRequestException(BadRequestException badRequestException){
        return ExceptionResponse
                .builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .className(badRequestException.getClass().getSimpleName())
                .message(badRequestException.getMessage())
                .build();
    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse accessDeniedException(AccessDeniedException accessDeniedException){
        return ExceptionResponse
                .builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .className(accessDeniedException.getClass().getSimpleName())
                .message(accessDeniedException.getMessage())
                .build();
    }
    @ExceptionHandler(BadCredentialException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse badCredentialException(BadCredentialException badCredentialException){
        return ExceptionResponse
                .builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .className(badCredentialException.getClass().getSimpleName())
                .message(badCredentialException.getMessage())
                .build();
    }


}
