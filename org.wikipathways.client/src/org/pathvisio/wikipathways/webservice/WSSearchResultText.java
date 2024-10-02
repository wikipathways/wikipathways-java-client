package org.pathvisio.wikipathways.webservice;

/**
 * contains a search result with text information from github repo
 * 
 * @author yihangx
 *
 */
public class WSSearchResultText {
    private String id;
    private String url;
    private String name;
    private String species;
    private String revision;
    private String authors;
    private String description;
    private String datanodes;
    private String annotations;
    private String citedIn;

    public WSSearchResultText() {
    }

    public WSSearchResultText(String id, String url, String name, String species, String revision,
            String authors, String description, String datanodes, String annotations, String citedIn) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.species = species;
        this.revision = revision;
        this.authors = authors;
        this.description = description;
        this.datanodes = datanodes;
        this.annotations = annotations;
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

    public String getDatanodes() {
        return datanodes;
    }

    public void setDatanodes(String datanodes) {
        this.datanodes = datanodes;
    }

    public String getAnnotations() {
        return annotations;
    }

    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }

    public String getCitedIn() {
        return citedIn;
    }

    public void setCitedIn(String citedIn) {
        this.citedIn = citedIn;
    }

    @Override
    public String toString() {
        return "WSSearchResultText{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", revision='" + revision + '\'' +
                ", authors='" + authors + '\'' +
                ", description='" + description + '\'' +
                ", datanodes='" + datanodes + '\'' +
                ", annotations='" + annotations + '\'' +
                ", citedIn='" + citedIn + '\'' +
                '}';
    }
}
