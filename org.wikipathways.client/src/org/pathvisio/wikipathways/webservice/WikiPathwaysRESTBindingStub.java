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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.wikipathways.client.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * connects to the new WikiPathways REST
 * webservice 
 * @author msk
 *
 */
public class WikiPathwaysRESTBindingStub implements WikiPathwaysPortType {

	private HttpClient client;
	private String baseUrl;
	private String BASE_URL_JSON = "https://www.wikipathways.org/json/";
	private String BASE_ASSETS = "https://www.wikipathways.org/wikipathways-assets/pathways/";


	public WikiPathwaysRESTBindingStub(HttpClient client, String baseUrl) {
		this.client = client;
		this.baseUrl = baseUrl;
	}
	
	@Override
	public WSSearchResult[] findInteractions(String query)
			throws RemoteException {
		query = query.replace(" ", "+");
		String url = baseUrl + "/findInteractions?query=" + query;
		try {
			
			Document jdomDocument = Utils.connect(url, client);
			Element root = jdomDocument.getRootElement();
			List<Element> list = root.getChildren("result", WSNamespaces.NS1);
			WSSearchResult [] res = new WSSearchResult[list.size()];
			for (int i = 0; i < list.size(); i++) {
				res[i] = Utils.parseWSSearchResult(list.get(i));
			}
			return res;
		} catch (Exception e) {
			throw new RemoteException("Error while processing " + url + ": " + e.getMessage(), e.getCause());
		}
	}
	
	@Override
	public WSSearchResult[] findPathwaysByLiterature(String query)
			throws RemoteException {
		query = query.replace(" ", "+");
		String url = baseUrl + "/findPathwaysByLiterature?query=" + query;
		try {
			Document jdomDocument = Utils.connect(url, client);
			Element root = jdomDocument.getRootElement();
			List<Element> list = root.getChildren("result", WSNamespaces.NS1);
			WSSearchResult [] res = new WSSearchResult[list.size()];
			for (int i = 0; i < list.size(); i++) {
				res[i] = Utils.parseWSSearchResult(list.get(i));
			}
			return res;
		} catch (Exception e) {
			throw new RemoteException("Error while processing " + url + ": " + e.getMessage(), e.getCause());
		}
	}
	
	@Override
	public WSSearchResult[] findPathwaysByText(String query, String species) throws RemoteException {
		query = query.replace(" ", "+");
		String url = baseUrl + "/findPathwaysByText?query=" + query;
		if(species != null) {
			species = species.replace(" ", "+");
			url = url + "&species=" + species;
		}
		try {
			Document jdomDocument = Utils.connect(url, client);
			Element root = jdomDocument.getRootElement();
			List<Element> list = root.getChildren("result", WSNamespaces.NS1);
			WSSearchResult [] res = new WSSearchResult[list.size()];
			for (int i = 0; i < list.size(); i++) {
				res[i] = Utils.parseWSSearchResult(list.get(i));
			}
			return res;
		} catch (Exception e) {
			throw new RemoteException("Error while processing " + url + ": " + e.getMessage(), e.getCause());
		}
	}
	
	@Override
	public WSSearchResultXref[] findPathwaysByXref(String[] ids, String[] codes) throws RemoteException {
		if (ids == null || ids.length == 0) {
			throw new RemoteException("Must provide an identifier to query, e.g., ENSG00000100031");
		}
		if (codes == null || codes.length == 0) {
			throw new RemoteException("Must provide a systemCode, e.g., En");
		}
	
		// Mapping system codes to JSON keys
		Map<String, String> codeList = new HashMap<>();
		codeList.put("En", "ensembl");
		codeList.put("L", "ncbigene");
		codeList.put("H", "hgnc");
		codeList.put("U", "uniprot");
		codeList.put("Wd", "wikidata");
		codeList.put("Ce", "chebi");
		codeList.put("Ik", "inchikey");
	
		// Fixed URL
		String url = BASE_URL_JSON + "findPathwaysByXref.json";
	
		try {
			// Fetching JSON response
			String jsonResponse = Utils.downloadFile(url);
	
			// Parsing the JSON response
			JSONObject jsonObject = new JSONObject(jsonResponse);
			JSONArray pathwayInfoArray = jsonObject.getJSONArray("pathwayInfo");
	
			// List to hold the filtered results
			List<WSSearchResultXref> filteredResults = new ArrayList<>();
	
			// Iterate through the JSON array
			for (int i = 0; i < pathwayInfoArray.length(); i++) {
				JSONObject pathwayInfo = pathwayInfoArray.getJSONObject(i);
	
				// Check each system code provided
				for (int j = 0; j < codes.length; j++) {
					String codeKey = codeList.get(codes[j]);
					if (codeKey != null && pathwayInfo.has(codeKey)) {
						String codeValues = pathwayInfo.getString(codeKey);
	
						// Check if any of the provided ids match the code values
						for (String id : ids) {
							if (codeValues.toLowerCase().contains(id.toLowerCase())) {
								// Parse and add the result if a match is found
								filteredResults.add(Utils.parseWSSearchResultFromJson(pathwayInfo));
								break; // Stop checking this pathway if a match is found
							}
						}
					}
				}
			}
	
			// Convert the list to an array and return it
			return filteredResults.toArray(new WSSearchResultXref[0]);
	
		} catch (Exception e) {
			throw new RemoteException("Error while processing " + url + ": " + e.getMessage(), e);
		}
	}
	


	
	
	
	@Override
	public WSOntologyTerm[] getOntologyTermsByPathway(String pwId) throws RemoteException {
		String url = baseUrl + "/getOntologyTermsByPathway?pwId=" + pwId;
		try {
			Document jdomDocument = Utils.connect(url, client);
			Element root = jdomDocument.getRootElement();
			List<Element> list = root.getChildren("terms", WSNamespaces.NS1);
			WSOntologyTerm [] terms = new WSOntologyTerm[list.size()];
			for (int i = 0; i < list.size(); i++) {
				terms[i] = Utils.parseOntologyTerm(list.get(i));
			}
			return terms;
		} catch (Exception e) {
			throw new RemoteException("Error while processing " + url + ": " + e.getMessage(), e.getCause());
		}
	}
	
	@Override
	public WSPathway getPathway(String pwId, int revision) throws RemoteException {
		String url = BASE_ASSETS + pwId + "/" + pwId + ".gpml";
		try {
			Document jdomDocument = Utils.connect(url, client);
			Element root = jdomDocument.getRootElement();  

			return Utils.parsePathway(root);
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("Error while processing " + url + ": " + e.getMessage(), e);
		}
	}

	@Override
	public byte[] getPathwayAs(String fileType, String pwId, int revision) throws RemoteException {
		InputStream instream = null;
		String url = baseUrl;
		if(url.contains("webservice/webservice.php")) {
			url = url.replace("webservice/webservice.php", "wpi.php");
		} else if(url.contains("webservicetest")) {
			url = url.replace("webservicetest/webservice.php", "wpi.php");
		} else if(url.contains("webservice.wikipathways.org")) {
			url = "http://www.wikipathways.org/wpi/wpi.php";
		}
		url = url + "?action=downloadFile&type=" + fileType + "&pwTitle=Pathway:" + pwId + "&oldid=" + revision;
		try {
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = client.execute(httpget);
			HttpEntity entity = response.getEntity();
			instream = entity.getContent();
			
			return IOUtils.toByteArray(instream);
		} catch(Exception e) {
			throw new RemoteException("Error while processing " + url + ": " + e.getMessage(), e.getCause());
		} finally {
			try {
				if(instream !=  null) {
					instream.close();
				}
			} catch (Exception e) {
					throw new RemoteException("Error while processing " + url + ": " + e.getMessage(), e.getCause());
			}
		}
	}
	
	@Override
	public WSPathwayInfo[] getPathwaysByOntologyTerm(String term) throws RemoteException {
		term = term.replace(" ", "+");
		String url = baseUrl + "/getPathwaysByOntologyTerm?term=" + term;
		try {
			Document jdomDocument = Utils.connect(url, client);
			Element root = jdomDocument.getRootElement();
			List<Element> list = root.getChildren("pathways", WSNamespaces.NS1);
			WSPathwayInfo [] info = new WSPathwayInfo[list.size()];
			for (int i = 0; i < list.size(); i++) {
				info[i] = Utils.parseWSPathwayInfo(list.get(i));
			}
			return info;
		} catch (Exception e) {
			throw new RemoteException("Error while processing " + url + ": " + e.getMessage(), e.getCause());
		}
	}
	
	@Override
	public WSPathwayInfo[] getPathwaysByParentOntologyTerm(String term) throws RemoteException {		
		String url = baseUrl + "/getPathwaysByParentOntologyTerm?term=" + term;
		try {
			Document jdomDocument = Utils.connect(url, client);
			Element root = jdomDocument.getRootElement();
			List<Element> list = root.getChildren("pathways", WSNamespaces.NS1);
			WSPathwayInfo [] info = new WSPathwayInfo[list.size()];
			for (int i = 0; i < list.size(); i++) {
				info[i] = Utils.parseWSPathwayInfo(list.get(i));
			}
			return info;
		} catch (Exception e) {
			throw new RemoteException("Error while processing " + url + ": " + e.getMessage(), e.getCause());
		}
	}
	
	@Override
	public WSPathwayHistory getPathwayHistory(String pwId, String timestamp) throws RemoteException {
		String url = baseUrl + "/getPathwayHistory?pwId=" + pwId + "&timestamp=" + timestamp;
		try {
			Document jdomDocument = Utils.connect(url, client);
			Element root = jdomDocument.getRootElement();
			Element hist = root.getChild("history", WSNamespaces.NS1);
			return Utils.parsePathwayHistory(hist);
		} catch (Exception e) {
			throw new RemoteException("Error while processing " + url + ": " + e.getMessage(), e.getCause());
		}
	}
	
	@Override
	public WSPathwayInfo getPathwayInfo(String pwId) throws RemoteException {
		try {
			String jsonString = jsonGet(BASE_URL_JSON + "getPathwayInfo.json");
	
			JSONObject jsonObject = new JSONObject(jsonString);
	
			JSONArray pathwayInfoArray = jsonObject.getJSONArray("pathwayInfo");
	
			for (int i = 0; i < pathwayInfoArray.length(); i++) {
				JSONObject pathwayInfo = pathwayInfoArray.getJSONObject(i);
				
				if (pathwayInfo.getString("id").equals(pwId)) {
					String id = pathwayInfo.getString("id");
					String url = pathwayInfo.getString("url");
					String name = pathwayInfo.getString("name");
					String species = pathwayInfo.getString("species");
					String revision = pathwayInfo.getString("revision");
					return new WSPathwayInfo(id, url, name, species, revision);
				}
			}
				throw new RemoteException("Pathway with id " + pwId + " not found.");
	
		} catch (Exception e) {
			throw new RemoteException("Error while processing " + BASE_URL_JSON + "getPathwayInfo.json" + ": " + e.getMessage(), e.getCause());
		}
	}
	
	@Override
	public String getUserByOrcid(String orcid) throws RemoteException {
		String url = baseUrl + "/getUserByOrcid?orcid=" + orcid;
		try {
			Document jdomDocument = Utils.connect(url, client);
			Element root = jdomDocument.getRootElement();
			
			return root.getChildText("Result", WSNamespaces.NS1).substring(5);
		} catch (Exception e) {
			throw new RemoteException("Error while processing " + url + ": " + e.getMessage(), e.getCause());
		}
	}

	@Override
	public String[] getXrefList(String pwId, String systemCode) throws RemoteException {
		if (pwId == null || pwId.isEmpty()) {
			throw new RemoteException("Must provide a pathway identifier, e.g., WP554");
		}
		if (systemCode == null || systemCode.isEmpty()) {
			throw new RemoteException("Must provide a systemCode, e.g., L");
		}
	
		Map<String, String> codeList = new HashMap<>();
		codeList.put("En", "Ensembl");
		codeList.put("L", "NCBI gene");
		codeList.put("H", "HGNC");
		codeList.put("U", "UniProt");
		codeList.put("Wd", "Wikidata");
		codeList.put("Ce", "ChEBI");
		codeList.put("Ik", "InChI");
	
		if (!codeList.containsKey(systemCode)) {
			throw new RemoteException("Must provide a supported systemCode, e.g., L (see docs)");
		}
	
		String url = BASE_ASSETS + pwId + "/" + pwId + "-datanodes.tsv";
	
		try {
			String tsvData = Utils.downloadFile(url);
	
			List<String[]> rows = parseTsv(tsvData);
	
			String columnName = codeList.get(systemCode);
			List<String> xrefs = extractColumnData(rows, columnName);
	
			Set<String> uniqueXrefs = new HashSet<>(xrefs);
	
			Set<String> compactXrefs = uniqueXrefs.stream()
												  .map(xref -> xref.replaceAll(".*:", ""))  // Remove everything before ':'
												  .collect(Collectors.toSet());
	
			return compactXrefs.toArray(new String[0]);
	
		} catch (Exception e) {
			throw new RemoteException("Error while processing " + url + ": " + e.getMessage(), e);
		}
	}
	
	private List<String[]> parseTsv(String tsvData) {
		return Arrays.stream(tsvData.split("\n"))
					 .map(row -> row.split("\t"))
					 .collect(Collectors.toList());
	}
	
	private List<String> extractColumnData(List<String[]> rows, String columnName) {
		String[] header = rows.get(0);
		int columnIndex = -1;
		for (int i = 0; i < header.length; i++) {
			if (header[i].equals(columnName)) {
				columnIndex = i;
				break;
			}
		}
	
		if (columnIndex == -1) {
			return new ArrayList<>();  
		}
	
		List<String> result = new ArrayList<>();
		for (int i = 1; i < rows.size(); i++) {
			String[] row = rows.get(i);
	
			if (row.length > columnIndex) {
				String value = row[columnIndex];
				if (value != null && !value.isEmpty()) {
					result.add(value);
				}
			}
		}
	
		return result;
	}
	

	@Override
	public String[] listOrganisms() throws RemoteException {
    try {
        String jsonString = jsonGet(BASE_URL_JSON+ "listOrganisms.json");

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray organismArray = jsonObject.getJSONArray("organisms");

        String[] organisms = new String[organismArray.length()];
        for (int i = 0; i < organismArray.length(); i++) {
            organisms[i] = organismArray.getString(i);
        }
        return organisms;
    } catch (Exception e) {
        throw new RemoteException("Error while processing listOrganisms: " + e.getMessage(), e);
    }
}

	
@Override
public WSPathwayInfo[] listPathways(String organism) throws RemoteException {    
    try {
        String jsonString = jsonGet(BASE_URL_JSON+ "listPathways.json");

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray organismsArray = jsonObject.getJSONArray("organisms");

        List<WSPathwayInfo> pathwayInfoList = new ArrayList<>();

        for (int i = 0; i < organismsArray.length(); i++) {
            JSONObject organismObj = organismsArray.getJSONObject(i);
            JSONArray pathwaysArray = organismObj.getJSONArray("pathways");

            for (int j = 0; j < pathwaysArray.length(); j++) {
                JSONObject pathwayObj = pathwaysArray.getJSONObject(j);

                String id = pathwayObj.getString("id");
                String name = pathwayObj.getString("name");
                String species = pathwayObj.getString("species");
                String urlPath = pathwayObj.getString("url");
                String revision = pathwayObj.getString("revision");

                if (organism == null || organism.isEmpty() || species.equalsIgnoreCase(organism)) {
                    WSPathwayInfo pathwayInfo = new WSPathwayInfo(id, urlPath, name, species, revision);
                    pathwayInfoList.add(pathwayInfo);
                }
            }
        }

        // Convert the list to an array and return it
        return pathwayInfoList.toArray(new WSPathwayInfo[0]);
    } catch (Exception e) {
        // Handle exceptions and wrap them in a RemoteException
        throw new RemoteException("Error while processing listPathways: " + e.getMessage(), e);
    }
}


	 private String jsonGet(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();
        return content.toString();
    }

	

}
