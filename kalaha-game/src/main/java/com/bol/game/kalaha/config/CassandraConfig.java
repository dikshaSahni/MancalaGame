
package com.bol.game.kalaha.config;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackages = "com.bol.game.kalaha.respository")
public class CassandraConfig extends AbstractCassandraConfiguration {

	@Value("${spring.data.cassandra.contact-points:127.0.0.1}")
	private String contactPoints;

	@Value("${spring.data.cassandra.port:9042}")
	private int port;

	@Value("${spring.data.cassandra.keyspace:newKeySpace}")
	private String keySpace;

	/*
	 * @Value("${spring.data.cassandra.username}") private String username;
	 * 
	 * @Value("${spring.data.cassandra.password}") private String password;
	 */

	@Value("${spring.data.cassandra.schema-action}")
	private String schemaAction;

	@Override
	protected String getKeyspaceName() {
		return keySpace;
	}

	@Override
	protected String getContactPoints() {
		return contactPoints;
	}

	@Override
	protected int getPort() {
		return port;
	}

	@Override
	public SchemaAction getSchemaAction() {
		return SchemaAction.CREATE_IF_NOT_EXISTS;
	}

	@Override
	public String[] getEntityBasePackages() {
		return new String[] { "com.bol.game.kalaha.model" };
	}

	@Override
	protected String getLocalDataCenter() {
		return "datacenter1";
	}

	@Override
	protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
		return Collections.singletonList(CreateKeyspaceSpecification.createKeyspace(keySpace).ifNotExists()
				.with(KeyspaceOption.DURABLE_WRITES, true).withSimpleReplication());
	}

	/*
	 * @Bean CqlSessionBuilderCustomizer authCustomizer(CassandraProperties
	 * properties) { return (builder) -> builder.withAuthCredentials(username,
	 * password); }
	 */

}
