package com.be3short.obj.execution;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(
{ ElementType.METHOD })
public @interface MethodId
{

	String id();

	String label() default "";

	String location() default "";

	String[] locationTypes() default
	{ "" };//String[1]);
}
