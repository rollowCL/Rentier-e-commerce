package pl.coderslab.rentier.pojo;


import org.springframework.stereotype.Component;
import javax.validation.constraints.NotEmpty;

@Component
//@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Login {

    @NotEmpty
    private String emailLogin;

    @NotEmpty
    private String passwordLogin;

    public String getEmailLogin() {
        return emailLogin;
    }

    public void setEmailLogin(String emailLogin) {
        this.emailLogin = emailLogin;
    }

    public String getPasswordLogin() {
        return passwordLogin;
    }

    public void setPasswordLogin(String passwordLogin) {
        this.passwordLogin = passwordLogin;
    }
}
