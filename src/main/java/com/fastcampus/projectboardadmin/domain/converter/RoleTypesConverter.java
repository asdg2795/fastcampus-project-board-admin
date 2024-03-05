package com.fastcampus.projectboardadmin.domain.converter;

import com.fastcampus.projectboardadmin.domain.constant.RoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.management.relation.Role;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class RoleTypesConverter implements AttributeConverter<Set<RoleType>, String> {

    private static final String DELIMETER = ",";

    @Override
    public String convertToDatabaseColumn(Set<RoleType> attribute) {
        return attribute.stream().map(RoleType::name).sorted().collect(Collectors.joining(DELIMETER));
    }

    @Override
    public Set<RoleType> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(DELIMETER)).map(RoleType::valueOf).collect(Collectors.toUnmodifiableSet());
    }
}
