package couponsProject.couponsProject.services.login;

import couponsProject.couponsProject.services.AdminServices;
import couponsProject.couponsProject.services.CompanyServices;
import couponsProject.couponsProject.services.CustomerServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.security.auth.login.LoginException;


@AllArgsConstructor
@Slf4j
@Component
public class LoginManager {
    private final AdminServices adminServices;
    private final CompanyServices companyServices;
    private final CustomerServices customerServices;

    public ClientServices  login (String email, String password , ClientTypeEnum clientType) throws LoginException {
        log.info("Entering login for: {} using Email: {} Password: {}",clientType,email, password);

        switch (clientType.name()) {
            case "ADMINISTRATOR":{
                if(adminServices.login(email,password) == 1){
                    log.debug("login succeeded");
                    return adminServices;
                }
            }
            case "COMPANY":{
                int id = companyServices.login(email,password);
                if (companyServices.login(email,password) > 0) {
                    log.debug("login succeeded, company id {}", id);
                    return companyServices;
                }
            }
            case "CUSTOMER":{
                int id = customerServices.login(email,password);
                if (id > 0) {
                    log.debug("login succeeded, customer id {}", id);
                    return customerServices;
                }
            }
        }
        log.error("login failed for: {} using Email: {} Password: {}",clientType,email, password);
        throw new LoginException("Your account name or password is incorrect.");
    }
}
