package com.deploy.demo.domain;

import com.deploy.demo.enums.WebAppStatus;
import com.deploy.demo.serializer.BlobStringConverter;
import com.deploy.demo.serializer.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "web_application")
public class WebApplication implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "name", nullable = false, length = 60)
	private String name;

	@Column(name = "url", length = 120)
	private String url;

	@Column(name = "message")
	private String message;

	@UpdateTimestamp
	@JsonSerialize(using = JsonDateSerializer.class, as = Date.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "started_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date startedDate;

	@Column(name = "state")
	private Integer state=  WebAppStatus.IN_PROGRESS.getStatus();

	@Convert(converter = BlobStringConverter.class)
	@Column(name = "user_data", nullable = true)
	private String userData;

	@Column(name = "instance_id")
	private String instanceId;


	
}