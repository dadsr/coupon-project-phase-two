package couponsProject.couponsProject.exseptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginManagerException extends RuntimeException {
    public LoginManagerException(String message) {
        super(message);
        log.info("throwing LoginManagerException message: {}", message);
    }
}
