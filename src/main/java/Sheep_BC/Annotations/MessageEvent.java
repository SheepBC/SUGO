package Sheep_BC.Annotations;

import Sheep_BC.Commands.Command_Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MessageEvent {
    String[] key() default "";
    String command() default "";
    String help() default "";

    Command_Type type() default Command_Type.BASIC;

}
