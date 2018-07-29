package me.net.just.pm.business;

import me.net.just.pm.model.Project;
import me.net.just.pm.model.ProjectItem;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ProjectService {

    @PersistenceContext
    private EntityManager entityManager;

    public void create( Project entity ) {
        entityManager.persist( entity );
    }

    public void update( Project entity ) {
        entityManager.merge( entity );
    }

    public void remove( Project entity ) {
        entityManager.remove( entityManager.merge( entity ) );
    }

    public Project find( Object id ) {
        return entityManager.find( Project.class, id );
    }

    public Project addProjectItem( Project project, String description, String priority ) {
        project = entityManager.merge( project );
        ProjectItem item = new ProjectItem( description, priority );
        project.add( item );
        return project;
    }

    public Project removeProjectItem( Project project, ProjectItem item ) {
        project = entityManager.merge( project );
        project.remove( item );
        return project;
    }

    public Project findByName( String name ) throws NoSuchProjectException {
        List<Project> resultList = entityManager.createNamedQuery( Project.FIND_BY_NAME, Project.class )
                                                .setParameter( "name", name )
                                                .getResultList();
        if ( resultList != null && resultList.size() > 1 ) {
            throw new IllegalStateException( "project named " + name + " was found more than once." );
        }
        if ( resultList != null && resultList.size() == 1 ) {
            return resultList.get( 0 );
        }
        throw new NoSuchProjectException( "project " + name + " doesn't exist." );
    }

    public List<Project> findAll() {
        return entityManager.createNamedQuery( Project.FIND_ALL, Project.class ).getResultList();
    }

    public long count() {
        return (Long) entityManager.createQuery( "select count(p) from Project p" ).getSingleResult();
    }

    public int getHighestProjectPosition() {
        Integer maxPosition =
                entityManager.createQuery( "select max(p.position) from Project p", Integer.class )
                             .getSingleResult();
        return ( maxPosition == null ? 0 : maxPosition );
    }

    public Project getFirst() {
        try {
            return entityManager.createNamedQuery( Project.FIND_ALL, Project.class )
                                .setMaxResults( 1 )
                                .getSingleResult();
        } catch ( NoResultException ex ) {
            return null;
        }
    }
}
