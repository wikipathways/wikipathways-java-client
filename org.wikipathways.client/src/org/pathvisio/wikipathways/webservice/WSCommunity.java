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
 * community object
 * @author yihangx
 *
 */
public class WSCommunity {

    private String name;
	private String title;
    private String description;
    private String tag;
    private String editors;

    public WSCommunity(String name, String title, String description, String tag, String editors) {
        this.name = name;
    	this.title = title;
    	this.description = description;
    	this.tag = tag;
        this.editors = editors;
    }

    public String getName() {
        return name;
    }

    public void setWpId(String name) {
        this.name = name;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getEditors() {
        return editors;
    }

    public void setEditors(String editors) {
        this.editors = editors;
    }
    
    
    @Override
    public String toString() {
    	return name+ "\t" + title + "\t" + description + "\t" + tag + "\t" + editors;
    }
}