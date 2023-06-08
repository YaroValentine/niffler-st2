package niffler.jupiter.annotation;

import niffler.jupiter.extensions.ApiLoginExtension;
import niffler.jupiter.extensions.BrowserExtension;
import niffler.jupiter.extensions.GenerateUserHibernateExtension;
import niffler.jupiter.extensions.GenerateUserJdbcExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({
        BrowserExtension.class,
        GenerateUserJdbcExtension.class,
        GenerateUserHibernateExtension.class,
        ApiLoginExtension.class
})
public @interface WebTest {

}
