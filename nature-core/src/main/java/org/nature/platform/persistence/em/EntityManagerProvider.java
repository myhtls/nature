package org.nature.platform.persistence.em;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class EntityManagerProvider {
	
	private final static String PERSISTENCE_UNIT_NAME = "appPm";

	@PersistenceContext(name = PERSISTENCE_UNIT_NAME)
	private EntityManager entityManager;

	@Produces
	public EntityManager openEntityManager() {
		return entityManager;
	}

	protected void closeEntityManager(@Disposes EntityManager pmsPu) {
		if (entityManager.isOpen()) {
			entityManager.close();
		}
	}

}
