package loki.runtime.compilerapi.module;

import assembler.methoddescriptor.DescribingMethodClass;
import loki.runtime.unit.LModule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
@DescribingMethodClass(LModule.class)
public @interface ModuleConstructor {}
