package reservation.dto;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * @author vvicario
 */
@Component
public class ApiError {

    private HttpStatus status;
    private String message;
//    private List<String> errors;

    public ApiError() {
        super();
    }

    public ApiError(final HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
     //   this.errors = errors;
    }

//    public ApiError(HttpStatus status, String message) {
//        super();
//        this.status = status;
//        this.message = message;
//      //  errors = Collections.singletonList(error);
//    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(final HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

//    public List<String> getErrors() {
//        return errors;
//    }

//    public void setErrors(final List<String> errors) {
//        this.errors = errors;
//    }

//    public void setError(final String error) {
//        errors = Collections.singletonList(error);
//    }

}