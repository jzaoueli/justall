package me.net.just.pm.presentation;

import me.net.just.pm.business.NoSuchProjectException;
import me.net.just.pm.business.ProjectService;
import me.net.just.pm.model.Project;
import me.net.just.pm.model.ProjectItem;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * @author Michael Kulla (mkulla@gmx.de)
 */
@Named
@SessionScoped
public class ProjectPM implements Serializable {

    private String description;
    private String priority;
    private ProjectItem currentItem;
    private Project currentProject;
    private String projectName;
    private String renamedName;
    @EJB
    private ProjectService service;

    @PostConstruct
    private void init() {
        currentProject = service.getFirst();
    }

    public void add() {
        Project updatedProject = service.addProjectItem( currentProject, description, priority );
        currentProject = updatedProject;

        // reset values
        description = "";
        priority = "normal";
    }

    public void remove( ProjectItem item ) {
        Project updatedProject = service.removeProjectItem( currentProject, item );
        currentProject = updatedProject;
    }

    public String edit( ProjectItem item ) {
        this.currentItem = item;
        return "edititem";
    }

    public String commitEdit() {
        service.update( currentProject );
        return "main";
    }

    public void addProject() {
        if ( !contains( projectName ) ) {
            Project project = new Project();
            project.setName( projectName );
            project.setPosition( service.getHighestProjectPosition() + 1 );
            service.create( project );
            currentProject = project;
            projectName = "";
        } else {
            FacesContext.getCurrentInstance().addMessage( "projectForm:projectName", new FacesMessage( "Name bereits vorhanden" ) );
        }
    }

    public String removeProject() {
        service.remove( currentProject );
        currentProject = service.getFirst();
        return "main";
    }

    public String renameProject() {
        if ( !contains( renamedName ) ) {
            currentProject.setName( renamedName );
            service.update( currentProject );
            return "main";
        } else {
            FacesContext.getCurrentInstance().addMessage( "renameForm:projectName", new FacesMessage( "Name bereits vorhanden" ) );
            renamedName = "";
            return null;
        }
    }

    public void show( Project project ) {
        this.currentProject = service.find( project.getId() );
    }

    private boolean contains( String projectName ) {
        try {
            service.findByName( projectName );
            return true;
        } catch ( NoSuchProjectException ex ) {
            return false;
        }
    }

    // --- getters and setters ---
    public Project getCurrentProject() {
        return currentProject;
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

    public String[] getPriorities() {
        return currentProject.getPriorities();
    }

    public ProjectItem getCurrentItem() {
        return currentItem;
    }

    public List<Project> getProjects() {
        return service.findAll();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName( String projectName ) {
        this.projectName = projectName;
    }

    public String getHeadLine() {
        if ( currentProject != null && !currentProject.isEmpty() ) {
            return "Bisher in der Projektliste:";
        } else {
            return "Bisher noch keine Eintr�ge";
        }
    }

    public String getRenamedName() {
        return renamedName;
    }

    public void setRenamedName( String renamedName ) {
        this.renamedName = renamedName;
    }
}
