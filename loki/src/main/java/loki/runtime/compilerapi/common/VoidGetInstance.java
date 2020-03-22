package loki.runtime.compilerapi.common;

import assembler.methoddescriptor.DescribingMethodClass;
import loki.runtime.unit.data.singleton.LVoid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@DescribingMethodClass(LVoid.class)
public @interface VoidGetInstance {}
