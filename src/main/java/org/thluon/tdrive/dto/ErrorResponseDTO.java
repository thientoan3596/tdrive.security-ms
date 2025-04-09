package org.thluon.tdrive.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.thluon.tdrive.jackson.FieldErrorDeserializer;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {
    private String message;
    private String error;
    private HttpStatus status;
    @Builder.Default
    private boolean isFormValidationError=false;
    @JsonDeserialize(contentUsing = FieldErrorDeserializer.class)
    private List<FieldError> errors;
}
