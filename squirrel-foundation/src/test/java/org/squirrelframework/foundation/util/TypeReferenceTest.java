package org.squirrelframework.foundation.util;

import org.junit.Test;

import java.lang.reflect.Type;

public class TypeReferenceTest {

    private TypeReference<String> typeRef = new TypeReference<String>() {
    };

    @Test
    public void getType() {
        Type type = typeRef.getType();
        System.out.println("type = " + type);
    }

    @Test
    public void getRawType() {
        Class<String> rawType = typeRef.getRawType();
        System.out.println("rawType = " + rawType);
    }
}