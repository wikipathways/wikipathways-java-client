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

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.pathvisio.wikipathways.webservice.WSPathwayInfo;
import org.wikipathways.client.test.utils.ConnectionSettings;

/**
 * JUnit Test for webservice function: getPathwaysByOntologyTerm
 * @author yihangx
 */
public class TestGetPathwaysByCommunity {

	private WikiPathwaysClient client;
	
	@Before
	public void setUp() throws Exception {
		client = ConnectionSettings.createClient();
	}

	@Test
	public void test() throws RemoteException {
		String tag = "AOP";
        WSPathwayInfo[] pathways = client.getPathwaysByCommunity(tag);

        assertNotNull(pathways);

        assertTrue(pathways.length > 0);

        Set<String> expectedPathwayIds = new HashSet<>(Arrays.asList("WP5035", "WP5228", "WP4891", "WP5091", "WP4010"));

        Set<String> foundPathwayIds = new HashSet<>();
        for (WSPathwayInfo pathway : pathways) {
            assertNotNull(pathway.getId());
            assertNotNull(pathway.getUrl());
            assertNotNull(pathway.getName());
            assertNotNull(pathway.getSpecies());
            assertNotNull(pathway.getRevision());

            foundPathwayIds.add(pathway.getId());
        }

        assertTrue(foundPathwayIds.containsAll(expectedPathwayIds));
    }
}

