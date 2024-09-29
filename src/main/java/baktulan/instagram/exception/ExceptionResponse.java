package baktulan.instagram.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionResponse {
    private HttpStatus httpStatus;
    private String className;
    private String message;
}
