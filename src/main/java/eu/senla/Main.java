package eu.senla;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class Main {

    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main.class);

        Some some = applicationContext.getBean(Some.class);

        System.out.println(some.toString());

        some.somesoem();
    }
}
