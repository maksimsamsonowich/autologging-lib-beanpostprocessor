package eu.senla;

import eu.senla.annotation.Observe;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
public class Some {

    @Observe
    public void somesoem() {
        System.out.println("WOW!");
    }

}
