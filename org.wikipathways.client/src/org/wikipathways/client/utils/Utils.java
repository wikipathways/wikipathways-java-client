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

package org.wikipathways.client.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.net.URL;

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
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.json.JSONObject;
import org.pathvisio.wikipathways.webservice.WSHistoryRow;
import org.pathvisio.wikipathways.webservice.WSIndexField;
import org.pathvisio.wikipathways.webservice.WSNamespaces;
import org.pathvisio.wikipathways.webservice.WSOntologyTerm;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.pathvisio.wikipathways.webservice.WSPathwayHistory;
import org.pathvisio.wikipathways.webservice.WSPathwayInfo;
import org.pathvisio.wikipathways.webservice.WSSearchResult;
import org.pathvisio.wikipathways.webservice.WSSearchResultText;
import org.pathvisio.wikipathways.webservice.WSSearchResultXref;


public class Utils {
	
	public static Document connect(String url, HttpClient client) throws IOException, JDOMException {
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = client.execute(httpget);
		HttpEntity entity = response.getEntity();
		InputStream instream = entity.getContent();
		try {
			String content = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
			String line;
			while((line = reader.readLine()) != null) {
				content = content + line + "\n";
			}
			reader.close();

			SAXBuilder jdomBuilder = new SAXBuilder();
			Document jdomDocument = jdomBuilder.build(new StringReader(content));
			return jdomDocument;
		} finally {
			instream.close();
		}
	}
	
	public static String update(String url, HttpClient client, Map<String, String> attributes) throws Exception {

		HttpPost httpost = new HttpPost(url);
		// Adding all form parameters in a List of type NameValuePair
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String key : attributes.keySet()) {
			nvps.add(new BasicNameValuePair(key, attributes.get(key)));
		}
		httpost.addHeader("Accept-Encoding", "application/xml");
		httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		HttpResponse response = client.execute(httpost);

		SAXBuilder jdomBuilder = new SAXBuilder();
		Document jdomDocument = jdomBuilder.build(response.getEntity().getContent());
		String success = jdomDocument.getRootElement().getChildText("success", WSNamespaces.NS1);
		return success;
	}
	
	public static WSPathwayInfo parseWSPathwayInfo(Element pathwayInfo) {
		
		String identifier = pathwayInfo.getChildText("id", WSNamespaces.NS2);
		String url = pathwayInfo.getChildText("url", WSNamespaces.NS2);
		String name = pathwayInfo.getChildText("name", WSNamespaces.NS2);
		String species = pathwayInfo.getChildText("species", WSNamespaces.NS2);
		String revision = pathwayInfo.getChildText("revision", WSNamespaces.NS2);
		
		return new WSPathwayInfo(identifier, url, name, species, revision);
	}
	
	public static WSIndexField parseWSIndexField(Element indexField) {
		String name = indexField.getChildText("name", WSNamespaces.NS2);
		String values = indexField.getChildText("values", WSNamespaces.NS2);
		String [] v = new String [1];
		v[0] = values;
		WSIndexField field = new WSIndexField(name, v);
		return field;
	}
	
	public static WSSearchResult parseWSSearchResult(Element searchResult) {
		WSSearchResult res = new WSSearchResult();
		String score = searchResult.getChildText("score", WSNamespaces.NS2);
		res.setScore(Double.parseDouble(score));
		
		List<Element> fieldList = searchResult.getChildren("fields", WSNamespaces.NS2);
		WSIndexField[] fields = new WSIndexField[fieldList.size()];
		for(int i = 0; i < fieldList.size(); i++) {
			WSIndexField f  = parseWSIndexField(fieldList.get(i));
			fields[i] = f;
		}
		res.setFields(fields);
		
		String id = searchResult.getChildText("id", WSNamespaces.NS2);
		String url = searchResult.getChildText("url", WSNamespaces.NS2);
		String name = searchResult.getChildText("name", WSNamespaces.NS2);
		String species = searchResult.getChildText("species", WSNamespaces.NS2);
		String revision = searchResult.getChildText("revision", WSNamespaces.NS2);

		res.setId(id);
		res.setName(name);
		res.setUrl(url);
		res.setSpecies(species);
		res.setRevision(revision);
		
		return res;
	}
	
	public static WSPathway parsePathway(Element root) throws UnsupportedEncodingException {
 		String name = root.getAttributeValue("Name");        
   		String organism = root.getAttributeValue("Organism"); 
    	String version = root.getAttributeValue("Version");   
    
    	String[] versionParts = version.split("_");
    	String wpId = versionParts[0];  
    	String revisionId = versionParts[1].substring(1);  

    	String pathwayUrl = "https://www.wikipathways.org/pathways/" + wpId + ".html";
    
    	XMLOutputter xmlOutputter = new XMLOutputter(Format.getRawFormat());
    	String gpml = xmlOutputter.outputString(root);
    
    	return new WSPathway(gpml, wpId, pathwayUrl, name, organism, revisionId);
	}
	
	public static WSPathwayHistory parsePathwayHistory(Element hist) {
		String id = hist.getChildText("id", WSNamespaces.NS2);
		String url = hist.getChildText("url", WSNamespaces.NS2);
		String name = hist.getChildText("name", WSNamespaces.NS2);
		String species = hist.getChildText("species", WSNamespaces.NS2);
		String revision = hist.getChildText("revision", WSNamespaces.NS2);
		
		List<Element> list = hist.getChildren("history", WSNamespaces.NS2);
		WSHistoryRow[] histRows = new WSHistoryRow[list.size()];
		for (int i = 0; i < list.size(); i++) {
			histRows[i] = Utils.parseHistoryRow(list.get(i));
		}
		
		return new WSPathwayHistory(histRows, id, url, name, species, revision);
	}
	
	public static WSHistoryRow parseHistoryRow(Element historyRow) {
		String revision = historyRow.getChildText("revision", WSNamespaces.NS2);
		String comment = historyRow.getChildText("comment", WSNamespaces.NS2);
		String user = historyRow.getChildText("user", WSNamespaces.NS2);
		String timestamp = historyRow.getChildText("timestamp", WSNamespaces.NS2);
		
		return new WSHistoryRow(revision, comment, user, timestamp);
	}

	public static String downloadFile(String fileUrl) throws Exception {
        StringBuilder result = new StringBuilder();
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(fileUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("Failed to fetch file from " + fileUrl + ", HTTP code: " + responseCode);
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

        } finally {
            if (reader != null) {
                reader.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return result.toString();
    }

	public static WSSearchResultXref parseWSSearchResultFromJson(JSONObject json) {
		String id = json.getString("id");
		String url = json.getString("url");
		String name = json.getString("name");
		String species = json.getString("species");
		String revision = json.getString("revision");
		
		// Retrieve the new fields
		String authors = json.optString("authors", ""); // Use optString in case the field is missing
		String description = json.optString("description", "");
		String ncbigene = json.optString("ncbigene", "");
		String ensembl = json.optString("ensembl", "");
		String hgnc = json.optString("hgnc", "");
		String uniprot = json.optString("uniprot", "");
		String wikidata = json.optString("wikidata", "");
		String chebi = json.optString("chebi", "");
		String inchikey = json.optString("inchikey", "");
	
		// Create a new WSSearchResult object with all fields
		return new WSSearchResultXref(
			id, 
			url, 
			name, 
			species, 
			revision, 
			authors, 
			description, 
			ncbigene, 
			ensembl, 
			hgnc, 
			uniprot, 
			wikidata, 
			chebi, 
			inchikey
		);
	}
	
public static WSSearchResultText parseWSSearchResultTextFromJson(JSONObject json) {
    // Parse each field from the JSON object
    String id = json.optString("id", "");
    String url = json.optString("url", "");
    String name = json.optString("name", "");
    String species = json.optString("species", "");
    String revision = json.optString("revision", "");
    String authors = json.optString("authors", "");
    String description = json.optString("description", "");
    String datanodes = json.optString("datanodes", "");
    String annotations = json.optString("annotations", "");
    String citedIn = json.optString("citedIn", "");

    // Return a new WSSearchResultText object populated with the parsed fields
    return new WSSearchResultText(id, url, name, species, revision, authors, description, datanodes, annotations, citedIn);
}

	
}
