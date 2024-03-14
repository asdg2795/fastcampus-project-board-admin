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
    // 메서드는 입력으로 받은 역할 집합을 데이터베이스에서 저장될 수 있는 형식의 문자열로 변환하여 반환

    @Override
    public Set<RoleType> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(DELIMETER)).map(RoleType::valueOf).collect(Collectors.toUnmodifiableSet());
    }
 	// 데이터베이스에서 가져온 문자열을 역할 집합으로 변환하여 반환
}
