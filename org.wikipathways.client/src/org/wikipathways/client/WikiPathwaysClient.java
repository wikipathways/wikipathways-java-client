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
package org.wikipathways.client;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bridgedb.DataSource;
import org.bridgedb.Xref;
import org.bridgedb.bio.Organism;
import org.pathvisio.core.model.ConverterException;
import org.pathvisio.core.model.GpmlFormat;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.core.view.MIMShapes;
import org.pathvisio.wikipathways.webservice.WSOntologyTerm;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.pathvisio.wikipathways.webservice.WSPathwayHistory;
import org.pathvisio.wikipathways.webservice.WSPathwayInfo;
import org.pathvisio.wikipathways.webservice.WSSearchResult;
import org.pathvisio.wikipathways.webservice.WSSearchResultLiterature;
import org.pathvisio.wikipathways.webservice.WSSearchResultXref;
import org.pathvisio.wikipathways.webservice.WSSearchResultText;
import org.pathvisio.wikipathways.webservice.WikiPathwaysPortType;
import org.pathvisio.wikipathways.webservice.WikiPathwaysRESTBindingStub;

/**
 * A client API that provides access to the WikiPathways REST webservice.
 * For more documentation on the webservice, see:
 * http://wikipathways.org/index.php/Help:WikiPathways_Webservice/API
 * @author thomas
 * @author msk
 *
 */
public class WikiPathwaysClient {
	private WikiPathwaysPortType port;
	
	/**
	 * Create an instance of this class.
	 * @param portAddress The url that points to the WikiPathways webservice.
	 * @throws ServiceException
	 */
	public WikiPathwaysClient(URL portAddress) {
		MIMShapes.registerShapes();
		
		HttpClientBuilder builder = HttpClientBuilder.create();
		String proxyString = System.getenv("http_proxy");
		try {
			if (proxyString != null) {
				URL proxyURL = new URL(proxyString);
				HttpHost proxy = new HttpHost(proxyURL.getHost(), proxyURL.getPort());
				builder.setProxy(proxy);
			}
			builder.setConnectionManagerShared(true);
			try (CloseableHttpClient httpclient = builder.build()) {
				port = new WikiPathwaysRESTBindingStub(httpclient, portAddress.toString());
			}
		} catch (Exception exception) {
			System.out.println("ERROR while creating a HTTP client: " + exception.getMessage());
		}
	}

	/**
	 * Get a info about the pathway (without getting the actual
	 * GPML code).
	 */
	public WSPathwayInfo getPathwayInfo(String id) throws RemoteException {
		return port.getPathwayInfo(id);
	}
	
	/**
	 * Get a list of pathways tagged with any ontology term that is the 
	 * child of the given Ontology term.
	 */
	public WSPathwayInfo [] getPathwayByParentOntologyTerm(String term) throws RemoteException {
		return port.getPathwaysByParentOntologyTerm(term);
	}
	
	/**
	 * Get a list of pathways tagged with a given ontology term.
	 */
	public WSPathwayInfo [] getPathwayByOntologyTerm(String term) throws RemoteException {
		return port.getPathwaysByOntologyTerm(term);
	}
	
	/**
	 * get a list of all ontology terms for one pathway
	 */
	public WSOntologyTerm [] getOntologyTermsByPathway(String pwId) throws RemoteException {
		return port.getOntologyTermsByPathway(pwId);
	}

	/**
	 * Get a pathway from WikiPathways.
	 * @see #toPathway(WSPathway)
	 */
	public WSPathway getPathway(String id) throws RemoteException, ConverterException {
		return getPathway(id, 0);
	}

	/**
	 * List all pathways on WikiPathways
	 */
	public WSPathwayInfo[] listPathways() throws RemoteException {
		WSPathwayInfo[] r = port.listPathways(null);
		if(r == null) r = new WSPathwayInfo[0];
		return r;
	}

	/**
	 * List all pathways on WikiPathways for the given organism
	 * @param organism The organism to filter by.
	 * @return
	 * @throws RemoteException
	 */
	public WSPathwayInfo[] listPathways(Organism organism) throws RemoteException {
		WSPathwayInfo[] r = port.listPathways(organism.latinName());
		if(r == null) r = new WSPathwayInfo[0];
		return r;
	}

	/**
	 * Lists all available organisms on WikiPathways
	 * @throws RemoteException
	 */
	public String[] listOrganisms() throws RemoteException {
		String[] r = port.listOrganisms();
		if(r == null) r = new String[0];
		return r;
	}

	/**
	 * Get a specific revision of a pathway from WikiPathways
	 * @see #toPathway(WSPathway)
	 */
	public WSPathway getPathway(String id, int revision) throws RemoteException, ConverterException {
		WSPathway wsp = port.getPathway(id, revision);
		return wsp;
	}

	/**
	 * returns the history of a specified pathway
	 */
	public WSPathwayHistory getPathwayHistory(String id, Date start) throws RemoteException {
		String timestamp = dateToTimestamp(start);
		WSPathwayHistory hist = port.getPathwayHistory(id, timestamp);
		return hist;
	}

	/**
	 * 
	 * @param fileType
	 * @param id
	 * @return
	 * @throws RemoteException
	 */
	public byte[] getPathwayAs(String fileType, String id) throws RemoteException {
		return port.getPathwayAs(fileType, id);
	}

	/**
	 * downloads and saves a pathway in different file formats
	 */
	public void downloadPathwayAs(File output, String fileType, String id) throws IOException {
		byte[] data = getPathwayAs(fileType, id);
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(output));
		out.write(data);
		out.close();
	}

	/**
	 * Get a list of external references on the pathway (gene, protein or metabolite ids),
	 * translated to the given database system.
	 * @param id The pathway id
	 * @param dataSource The data source to translate to (e.g. BioDataSource.ENTREZ_GENE)
	 * @return The identifiers of the external references.
	 * @throws RemoteException
	 */
	public String[] getXrefList(String id, DataSource dataSource) throws RemoteException {
		String[] xrefs = port.getXrefList(id, dataSource.getSystemCode());
		if(xrefs == null) xrefs = new String[0];
		return xrefs;
	}
	
	/**
	 * get a user name by orcid id
	 */
	public String getUserByOrcid(String orcid) throws RemoteException {
		return port.getUserByOrcid(orcid);
	}

	/**
	 * Utility method to create a pathway model from the webservice class
	 * WSPathway.
	 * @param wsp The WSPathway object returned by the webservice.
	 * @return The org.pathvisio.model.Pathway model representation of the GPML code.
	 * @throws ConverterException
	 */
	public static Pathway toPathway(WSPathway wsp) throws ConverterException {
		Pathway p = new Pathway();
		p.readFromXml(new StringReader(wsp.getGpml()), true);
		return p;
	}

	private static String dateToTimestamp(Date date) {
		// turn Date into expected timestamp format, in GMT:
		SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(date);
	}

	public WSSearchResultText[] findPathwaysByText(String query) throws RemoteException {
		WSSearchResultText[] r = port.findPathwaysByText(query, null);
		if(r == null) r = new WSSearchResultText[0];
		return r;
	}

	public WSSearchResultText[] findPathwaysByText(String query, String field) throws RemoteException {
		WSSearchResultText[] r =  port.findPathwaysByText(query, field);
		if(r == null) r = new WSSearchResultText[0];
		return r;
	}

	/**
	 * Search for pathways containing one of the given xrefs by taking
	 * into account cross-references to other database systems.
	 * @param xrefs
	 * @return
	 * @throws RemoteException
	 */
	public WSSearchResultXref[] findPathwaysByXref(Xref... xrefs) throws RemoteException {
		String[] ids = new String[xrefs.length];
		String[] codes = new String[xrefs.length];
		for(int i = 0; i < xrefs.length; i++) {
			ids[i] = xrefs[i].getId();
			DataSource ds = xrefs[i].getDataSource();
			if(ds == null) {
				codes[i] = null;
			} else {
				codes[i] = ds.getSystemCode();
			}
		}
		WSSearchResultXref[] r =  port.findPathwaysByXref(ids, codes);
		if(r == null) r = new WSSearchResultXref[0];
		return r;
	}
	
	public WSSearchResult[] findInteractions(String query) throws RemoteException {
		WSSearchResult[] r = port.findInteractions(query);
		if(r == null) r = new WSSearchResult[0];
		return r;
	}
	
	/**
	 * search by literature reference
	 */
	public WSSearchResultLiterature[] findPathwaysByLiterature(String query) throws RemoteException {
		WSSearchResultLiterature[] r = port.findPathwaysByLiterature(query);
		if(r == null) r = new WSSearchResultLiterature[0];
		return r;        
	}

}