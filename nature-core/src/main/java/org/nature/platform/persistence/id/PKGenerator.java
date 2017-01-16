package org.nature.platform.persistence.id;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;


public class PKGenerator implements IdentifierGenerator ,Configurable{
	
	private final static String ENTITY_NAME_KEY = "jpa_entity_name";
	private final static String ENTITY_PATH_KEY = "entity_name";
	private final static String ENTITY_TABLE_KEY = "target_table";
	private final static String ENTITY_FIELD_KEY = "target_column";
	
	private String entity_name_value = null;
	private String entity_path_value = null;
	private String entity_table_value = null;
	private String entity_field_value = null;
	
	public PKGenerator(){
		
	}
	
	public static PKGenerator buildSessionFactoryUniqueIdentifierGenerator() {
		final PKGenerator generator = new PKGenerator();
		return generator;
	}

	@Override
	public Serializable generate(SharedSessionContractImplementor  session, Object object) throws HibernateException {
		Connection conn = session.connection();
		return generatePK( conn , object );
	}
	
	public synchronized Serializable generate(SessionImplementor  session, Object object) throws HibernateException {
		Connection conn = session.connection();
		return generatePK( conn , object );
	}
	
	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
		entity_name_value = params.getProperty( ENTITY_NAME_KEY );
		entity_path_value = params.getProperty( ENTITY_PATH_KEY );
		entity_table_value = params.getProperty( ENTITY_TABLE_KEY );
		entity_field_value = params.getProperty( ENTITY_FIELD_KEY );
	}
	
private Serializable generatePK(Connection  conn, Object object) throws HibernateException {
		
		String entity_prefix = "NATURE";
		
		String sql = "SELECT MAX("+ entity_field_value + ") as MaxId FROM " + entity_table_value;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet result = pstmt.executeQuery();
			
			String maxId = result.next() ? result.getString("MaxId") : null;
			
			String newId = maxId == null ? NatureIdGenerator.genDefaultId( entity_prefix ) : NatureIdGenerator.genNextIdWithPrefix( maxId );
			
		
			
			return newId;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new HibernateException("Query Max Id Failed : " + e.getMessage() );
			
		}
	}

}
