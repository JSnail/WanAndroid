package com.snail.allrefresh;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IntDef(value = {Style.NORMAL, Style.IN_UP})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Style {
        int NORMAL = 0;
        int IN_UP = 1;
}
