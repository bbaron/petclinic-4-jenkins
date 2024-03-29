package com.example.petclinic.domain;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Transactional
public class VisitIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private VisitDataOnDemand dod;

	@Test
    public void testCountVisits() {
        Assert.assertNotNull("Data on demand for 'Visit' failed to initialize correctly", dod.getRandomVisit());
        long count = Visit.countVisits();
        Assert.assertTrue("Counter for 'Visit' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindVisit() {
        Visit obj = dod.getRandomVisit();
        Assert.assertNotNull("Data on demand for 'Visit' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Visit' failed to provide an identifier", id);
        obj = Visit.findVisit(id);
        Assert.assertNotNull("Find method for 'Visit' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Visit' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllVisits() {
        Assert.assertNotNull("Data on demand for 'Visit' failed to initialize correctly", dod.getRandomVisit());
        long count = Visit.countVisits();
        Assert.assertTrue("Too expensive to perform a find all test for 'Visit', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Visit> result = Visit.findAllVisits();
        Assert.assertNotNull("Find all method for 'Visit' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Visit' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindVisitEntries() {
        Assert.assertNotNull("Data on demand for 'Visit' failed to initialize correctly", dod.getRandomVisit());
        long count = Visit.countVisits();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Visit> result = Visit.findVisitEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Visit' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Visit' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Visit obj = dod.getRandomVisit();
        Assert.assertNotNull("Data on demand for 'Visit' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Visit' failed to provide an identifier", id);
        obj = Visit.findVisit(id);
        Assert.assertNotNull("Find method for 'Visit' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyVisit(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Visit' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        Visit obj = dod.getRandomVisit();
        Assert.assertNotNull("Data on demand for 'Visit' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Visit' failed to provide an identifier", id);
        obj = Visit.findVisit(id);
        boolean modified =  dod.modifyVisit(obj);
        Integer currentVersion = obj.getVersion();
        Visit merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Visit' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'Visit' failed to initialize correctly", dod.getRandomVisit());
        Visit obj = dod.getNewTransientVisit(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Visit' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Visit' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Visit' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        Visit obj = dod.getRandomVisit();
        Assert.assertNotNull("Data on demand for 'Visit' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Visit' failed to provide an identifier", id);
        obj = Visit.findVisit(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Visit' with identifier '" + id + "'", Visit.findVisit(id));
    }
}
