package org.pathvisio.wikipathways.webservice;

/**
 * Contains a search result with literature information from the WP webservice.
 */
public class WSSearchResultLiterature {

    private String id;
    private String url;
    private String name;
    private String species;
    private String revision;
    private String authors;
    private String description;
    private String refs;
    private String[] citations;

    public WSSearchResultLiterature() { }

    public WSSearchResultLiterature(String id, String url, String name, String species, String revision,
                                    String authors, String description, String refs, String[] citations) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.species = species;
        this.revision = revision;
        this.authors = authors;
        this.description = description;
        this.refs = refs;
        this.citations = citations;
    }

    // Getter and Setter methods

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

    public String getRefs() {
        return refs;
    }

    public void setRefs(String refs) {
        this.refs = refs;
    }

    public String[] getCitations() {
        return citations;
    }

    public void setCitations(String[] citations) {
        this.citations = citations;
    }
}
