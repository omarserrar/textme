package com.omarserrar.textme.models.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;

public class UserFilters {
    public static MappingJacksonValue serializeAllExcept(Object o, String... args){

        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("MessageFilter", SimpleBeanPropertyFilter.serializeAllExcept("contacts"));

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(o);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }
}
