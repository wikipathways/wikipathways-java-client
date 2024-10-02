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
 * parent ontology term object
 * 
 * @author yihangx
 *
 */
public class WSParentOntologyTerm {

    private String wpid;
    private String ontology;
    private String id;
    private String name;
    private String parent;

    public WSParentOntologyTerm(String wpid, String ontology, String id, String name, String parent) {
        this.wpid = wpid;
        this.ontology = ontology;
        this.id = id;
        this.name = name;
        this.parent = parent;
    }

    public String getWpId() {
        return wpid;
    }

    public void setWpId(String wpid) {
        this.wpid = wpid;
    }

    public String getOntology() {
        return ontology;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String Parent) {
        this.parent = Parent;
    }

    @Override
    public String toString() {
        return wpid + "\t" + name + "\t" + id + "\t" + ontology + "\t" + parent;
    }
}