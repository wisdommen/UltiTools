package com.ultikits.ultitools.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CmdExecutor {
    String function() default "";
    String permission();
    String description();
    String alias();
}
