package couponsProject.couponsProject.exseptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompanyException extends RuntimeException {
    public CompanyException(String message) {
        super(message);
        log.info("throwing CompanyException message: {}", message);
    }
}
