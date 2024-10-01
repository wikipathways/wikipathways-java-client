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

import java.io.ByteArrayOutputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WikiPathwaysPortType extends Remote {
    public WSSearchResultXref[] findPathwaysByXref(String[] ids, String[] codes) throws RemoteException;
    public WSParentOntologyTerm[] getPathwaysByParentOntologyTerm(String term) throws RemoteException;
    public WSPathwayInfo getPathwayInfo(String pwId) throws RemoteException;
    public WSPathwayInfo[] listPathways(String organism) throws RemoteException;
    public WSSearchResultLiterature[] findPathwaysByLiterature(String query) throws RemoteException;
    public String[] listOrganisms() throws RemoteException;
    public WSPathwayHistory getPathwayHistory(String pwId, String timestamp) throws RemoteException;
    public String[] getXrefList(String pwId, String code) throws RemoteException;
    public WSSearchResultText[] findPathwaysByText(String query, String species) throws RemoteException;
    public byte[] getPathwayAs(String fileType, String pwId) throws RemoteException;
    public WSSearchResult[] findInteractions(String query) throws RemoteException;
    public WSPathway getPathway(String pwId, int revision) throws RemoteException;
    public WSOntologyTerm[] getOntologyTermsByPathway(String pwId) throws RemoteException;
    public WSPathwayInfo[] getPathwaysByOntologyTerm(String term) throws RemoteException;
    public String getUserByOrcid(String orcid) throws RemoteException;
}