package couponsProject.couponsProject.services;

import couponsProject.couponsProject.exseptions.LoginManagerException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Slf4j
@Component
@Scope("singleton")
public class LoginManager {
    private  AdminServices adminServices;
    private  CompanyServices companyServices;
    private  CustomerServices customerServices;


    /**
     * Authenticates a client based on their email, password, and client type.
     *
     * @param email      The email address of the client
     * @param password   The password of the client
     * @param clientType The type of client (ADMINISTRATOR, COMPANY, or CUSTOMER)
     * @return A ClientServices object corresponding to the authenticated client type
     * @throws LoginManagerException If authentication fails or an unexpected error occurs
     */
    public  ClientServices  login(String email, String password, ClientTypeEnum clientType) throws LoginManagerException {
        log.info("Entering login for: {} using Email: {} Password: {}",clientType,email, password);
        try {
            switch (clientType) {
                case ADMINISTRATOR: {
                    if (adminServices.login(email, password) == 1) {
                        log.debug("login succeeded");
                        return adminServices;
                    }
                }
                break;
                case COMPANY: {
                    int id = companyServices.login(email, password);
                    if (id > 0) {
                        log.debug("login succeeded, company id {}", id);
                        return companyServices;
                    }
                }
                break;
                case CUSTOMER: {
                    int id = customerServices.login(email, password);
                    if (id > 0) {
                        log.debug("login succeeded, customer id {}", id);
                        return customerServices;
                    }
                }
                break;
            }
        }catch (Exception e){}//LoginManagerException is next
        log.error("login failed for: {} using Email: {} Password: {}", clientType, email, password);
        throw new LoginManagerException("Your account name or password is incorrect.");
    }
}
