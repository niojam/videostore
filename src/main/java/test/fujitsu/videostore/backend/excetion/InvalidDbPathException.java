package test.fujitsu.videostore.backend.excetion;

import java.io.IOException;

public class InvalidDbPathException extends IOException {
    public InvalidDbPathException(String message) {
        super(message);
    }
}
