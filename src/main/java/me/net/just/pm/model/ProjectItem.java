package me.net.just.pm.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class ProjectItem implements Serializable {

    private String description;

    private String priority;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @ManyToOne( optional = false )
    private Project project;

    public ProjectItem() {
    }

    public ProjectItem( String description, String priority ) {
        this.description = description;
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority( String priority ) {
        this.priority = priority;
    }

    public Project getProject() {
        return project;
    }

    public void setProject( Project project ) {
        this.project = project;
    }

    @Override
    public String toString() {
        return "ProjectItem [" + id + ", " + description + ", " + priority + "]";
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
        final ProjectItem other = (ProjectItem) object;
        return this.id == other.id || ( this.id != null && this.id.equals( other.id ) );
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + ( this.description != null ? this.description.hashCode() : 0 );
        hash = 89 * hash + ( this.priority != null ? this.priority.hashCode() : 0 );
        hash = 89 * hash + ( this.id != null ? this.id.hashCode() : 0 );
        return hash;
    }
}
