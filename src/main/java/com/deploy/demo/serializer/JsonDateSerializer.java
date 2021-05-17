package com.deploy.demo.serializer;

import com.deploy.demo.utils.DateTimeUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class JsonDateSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException {
		if (date != null) {
			gen.writeString(DateTimeUtils.getCurrentDateTime(date));
		} else {
			gen.writeNull();
		}
	}

}