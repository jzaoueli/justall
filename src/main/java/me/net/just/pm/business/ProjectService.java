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
    private EntityManager em;

    public void create( Project entity ) {
        em.persist( entity );
    }

    public void update( Project entity ) {
        em.merge( entity );
    }

    public void remove( Project entity ) {
        em.remove( em.merge( entity ) );
    }

    public Project find( Object id ) {
        return em.find( Project.class, id );
    }

    public Project addProjectItem( Project project, String description, String priority ) {
        project = em.merge( project );
        ProjectItem item = new ProjectItem( description, priority );
        project.add( item );
        return project;
    }

    public Project removeProjectItem( Project project, ProjectItem item ) {
        project = em.merge( project );
        project.remove( item );
        return project;
    }

    public Project findByName( String name ) throws NoSuchProjectException {
        List<Project> resultList = em.createNamedQuery( Project.FIND_BY_NAME, Project.class )
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
        return em.createNamedQuery( Project.FIND_ALL, Project.class ).getResultList();
    }

    public long count() {
        return ( (Long) em.createQuery( "select count(p) from Project p" ).getSingleResult() ).longValue();
    }

    public int getHighestProjectPosition() {
        Integer maxPosition =
                em.createQuery( "select max(p.position) from Project p", Integer.class )
                  .getSingleResult();
        return ( maxPosition == null ? 0 : maxPosition );
    }

    public Project getFirst() {
        try {
            return em.createNamedQuery( Project.FIND_ALL, Project.class )
                     .setMaxResults( 1 )
                     .getSingleResult();
        } catch ( NoResultException ex ) {
            return null;
        }
    }
}
