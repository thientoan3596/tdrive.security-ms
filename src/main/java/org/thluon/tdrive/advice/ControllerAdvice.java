package org.thluon.tdrive.advice;

import com.github.thientoan3596.dto.ErrorResponseDTO;
import com.github.thientoan3596.exception.UniqueKeyViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<ErrorResponseDTO>> handleWebExchangeBind(WebExchangeBindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        return Mono.just(new ResponseEntity<>(ErrorResponseDTO.builder().errors(fieldErrors).isFormValidationError(true).status(HttpStatus.BAD_REQUEST).build(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(LockedException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    public ResponseEntity<ErrorResponseDTO> handleLockedException(LockedException e) {
        return new ResponseEntity<>(
                ErrorResponseDTO.builder().
                        status(HttpStatus.LOCKED)
                        .message(e.getMessage())
                        .errors(
                                List.of(
                                        new FieldError("email", "email", null, false, null, null, e.getMessage())))
                        .build(), HttpStatus.LOCKED);
    }
    @ExceptionHandler(UniqueKeyViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleBUniqueKeyViolation(UniqueKeyViolationException e) {
        FieldError fieldError = new FieldError(e.getModelName(), e.getFieldName(), e.getRejectedValue(), false, null, null, e.getMessage());
        return new ResponseEntity<>(ErrorResponseDTO.builder().isFormValidationError(true).errors(List.of(fieldError)).status(HttpStatus.BAD_REQUEST).build(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(BadCredentialsException e) {
        return new ResponseEntity<>(ErrorResponseDTO.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .error("Invalid Credentials")
                .message(e.getMessage()).build(), HttpStatus.UNAUTHORIZED);
    }
}
