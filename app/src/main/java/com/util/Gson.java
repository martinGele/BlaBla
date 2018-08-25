package com.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class Gson {

    public Gson(){
        getGson();
    }

     /*
     thru this method we are making serialization an deserialization to the POLO objects
     creating post and response objects
      */
    public static com.google.gson.Gson getGson() {
        return new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(final FieldAttributes fieldAttributes) {
                final Expose expose = fieldAttributes.getAnnotation(Expose.class);
                return (expose != null) && !expose.serialize();
            }

            @Override
            public boolean shouldSkipClass(final Class<?> aClass) {
                return false;
            }
        }).addDeserializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(final FieldAttributes fieldAttributes) {
                final Expose expose = fieldAttributes.getAnnotation(Expose.class);
                return (expose != null) && !expose.deserialize();
            }

            @Override
            public boolean shouldSkipClass(final Class<?> aClass) {
                return false;
            }
        }).create();
    }

}
