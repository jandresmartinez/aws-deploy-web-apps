package com.deploy.demo.serializer;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BlobStringConverter implements AttributeConverter<String, byte[]> {

	@Override
	public byte[] convertToDatabaseColumn(String dbData) {
		return dbData == null ? null : dbData.getBytes();
	}

	@Override
	public String convertToEntityAttribute(byte[] attribute) {
		return attribute == null ? null : new String(attribute);
	}
}
