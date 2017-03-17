package org.nature.platform.persistence.em;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.nature.core.Configuration;
import org.picketlink.annotations.PicketLink;

@ApplicationScoped
public class EntityManagerProvider {
	
	private final static String PERSISTENCE_UNIT_NAME = Configuration.ENTITYMANAGERAPP;
	
	private final static String PERSISTENCE_PICKETLINK_NAME = Configuration.ENTITYMANAGERPICKETLINK;

	@PersistenceContext(name = PERSISTENCE_UNIT_NAME)
	private EntityManager entityManager;
	
	@PersistenceContext(name = PERSISTENCE_PICKETLINK_NAME)
	private EntityManager picketlinkEntityManager;

	
	@Produces
	public EntityManager openEntityManager() {
		return entityManager;
	}
	
	

	protected void closeEntityManager(@Disposes  EntityManager appPu) {
		if (entityManager.isOpen()) {
			entityManager.close();
		}
	}
	

	
	
	
	@PicketLink
	@Produces
	public EntityManager openPlEntityManager() {
		return picketlinkEntityManager;
	}
	
	protected void closePlEntityManager( @Disposes @PicketLink EntityManager plPu) {
		if (picketlinkEntityManager.isOpen()) {
			picketlinkEntityManager.close();
		}
	}

}
