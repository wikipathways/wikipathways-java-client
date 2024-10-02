// WikiPathways Java library,
// Copyright 2014-2015 WikiPathways
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package org.pathvisio.wikipathways.webservice;

/**
 * contains a search result with external links from github repo
 * 
 * @author yihangx
 *
 */
public class WSSearchResultXref {
    private String id;
    private String url;
    private String name;
    private String species;
    private String revision;
    private String authors; // New field
    private String description; // New field

    // New fields
    private String ncbigene;
    private String ensembl;
    private String hgnc;
    private String uniprot;
    private String wikidata;
    private String chebi;
    private String inchikey;

    public WSSearchResultXref() {
    }

    public WSSearchResultXref(String id, String url, String name, String species, String revision,
            String authors, String description,
            String ncbigene, String ensembl, String hgnc, String uniprot,
            String wikidata, String chebi, String inchikey) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.species = species;
        this.revision = revision;
        this.authors = authors;
        this.description = description;
        this.ncbigene = ncbigene;
        this.ensembl = ensembl;
        this.hgnc = hgnc;
        this.uniprot = uniprot;
        this.wikidata = wikidata;
        this.chebi = chebi;
        this.inchikey = inchikey;
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

    public String getNcbigene() {
        return ncbigene;
    }

    public void setNcbigene(String ncbigene) {
        this.ncbigene = ncbigene;
    }

    public String getEnsembl() {
        return ensembl;
    }

    public void setEnsembl(String ensembl) {
        this.ensembl = ensembl;
    }

    public String getHgnc() {
        return hgnc;
    }

    public void setHgnc(String hgnc) {
        this.hgnc = hgnc;
    }

    public String getUniprot() {
        return uniprot;
    }

    public void setUniprot(String uniprot) {
        this.uniprot = uniprot;
    }

    public String getWikidata() {
        return wikidata;
    }

    public void setWikidata(String wikidata) {
        this.wikidata = wikidata;
    }

    public String getChebi() {
        return chebi;
    }

    public void setChebi(String chebi) {
        this.chebi = chebi;
    }

    public String getInchikey() {
        return inchikey;
    }

    public void setInchikey(String inchikey) {
        this.inchikey = inchikey;
    }
}
