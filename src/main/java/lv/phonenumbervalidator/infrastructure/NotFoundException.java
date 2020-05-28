package lv.phonenumbervalidator.infrastructure;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
