package me.net.just.pm.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries( {
        @NamedQuery( name = Project.FIND_ALL, query = "select p from Project p order by p.position" ),
        @NamedQuery( name = Project.FIND_BY_NAME, query = "select p from Project p where p.name=:name" )
} )
public class Project implements Serializable {

    public static final String FIND_ALL = "Project.findAll";
    public static final String FIND_BY_NAME = "Project.findByName";

    private static final String[] PRIORITIES = {"niedrig", "normal", "hoch"};

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;


    @Column( unique = true )
    private int position;


    @Column( unique = true )
    private String name;

    @OneToMany( fetch = FetchType.EAGER, mappedBy = "project", orphanRemoval = true, cascade = CascadeType.ALL )
    private List<ProjectItem> items = new LinkedList<>();


    public void add( ProjectItem item ) {
        item.setProject( this );
        items.add( item );
    }

    public List<ProjectItem> getAll() {
        return Collections.unmodifiableList( items );
    }

    public int getSize() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }

    public String[] getPriorities() {
        return PRIORITIES;
    }

    public void remove( ProjectItem item ) {
        items.remove( item );
    }

    public int getPosition() {
        return position;
    }

    public void setPosition( int position ) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    @Override
    public boolean equals( Object object ) {
        if ( object == null ) {
            return false;
        }
        if ( getClass() != object.getClass() ) {
            return false;
        }
        final Project other = (Project) object;
        if ( this.id != other.id && ( this.id == null || !this.id.equals( other.id ) ) ) {
            return false;
        }
        return ( this.name == null ) ? ( other.name == null ) : this.name.equals( other.name );
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + ( this.id != null ? this.id.hashCode() : 0 );
        hash = 53 * hash + ( this.name != null ? this.name.hashCode() : 0 );
        hash = 53 * hash + ( this.items != null ? this.items.hashCode() : 0 );
        return hash;
    }

    @Override
    public String toString() {
        return "Project(" + id + "):" + getSize() + " entries: " + getAll();
    }
}
