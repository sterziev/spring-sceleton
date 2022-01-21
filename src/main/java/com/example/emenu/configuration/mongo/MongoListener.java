package com.example.emenu.configuration.mongo;

import com.example.emenu.model.AbstractAuditingDocument;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class MongoListener extends AbstractMongoEventListener<AbstractAuditingDocument> {

	@EventListener
	public void onBeforeConvert(BeforeConvertEvent<AbstractAuditingDocument> event) {
		AbstractAuditingDocument source = (AbstractAuditingDocument) event.getSource();
		if (source.getUuid() == null) {
			source.setUuid(UUID.randomUUID().toString());
		}
	}
}
