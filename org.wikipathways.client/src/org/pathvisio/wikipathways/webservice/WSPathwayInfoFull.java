package org.pathvisio.wikipathways.webservice;

/**
 * Contains basic information about a pathway
 * 
 * @author yihangx
 *
 */
public class WSPathwayInfoFull {

    private String id;
    private String url;
    private String name;
    private String species;
    private String revision;
    private String authors;       // New field for authors
    private String description;   // New field for description
    private String citedIn;       // New field for citedIn

    public WSPathwayInfoFull(String id, String url, String name, String species, String revision, String authors, String description, String citedIn) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.species = species;
        this.revision = revision;
        this.authors = authors;
        this.description = description;
        this.citedIn = citedIn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCitedIn() {
        return citedIn;
    }

    public void setCitedIn(String citedIn) {
        this.citedIn = citedIn;
    }

    @Override
    public String toString() {
        return id + ":" + revision + "\t" + name + "\t" + species + "\t" + url + "\t" + authors + "\t" + description + "\t" + citedIn;
    }
}
