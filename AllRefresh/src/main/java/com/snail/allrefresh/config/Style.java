package com.snail.allrefresh.config;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IntDef(value = {Style.NORMAL, Style.IN_ABOVE})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Style {
        int NORMAL = 0;
        int IN_ABOVE = 1;
        int IN_LOWER =2;
}
