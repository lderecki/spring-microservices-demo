package pl.lderecki.simpleapiclient.controller.session;


import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
@Data
public class SessionBackground {

    private byte[] image = null;
}
