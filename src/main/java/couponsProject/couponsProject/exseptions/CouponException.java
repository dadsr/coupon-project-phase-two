package couponsProject.couponsProject.exseptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CouponException extends RuntimeException {
    public CouponException(String message) {
        super(message);
        log.info("throwing CouponException message: {}", message);
    }
}
