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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Transactional
@Configurable
public class PetIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private PetDataOnDemand dod;

	@Test
    public void testCountPets() {
        Assert.assertNotNull("Data on demand for 'Pet' failed to initialize correctly", dod.getRandomPet());
        long count = Pet.countPets();
        Assert.assertTrue("Counter for 'Pet' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindPet() {
        Pet obj = dod.getRandomPet();
        Assert.assertNotNull("Data on demand for 'Pet' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Pet' failed to provide an identifier", id);
        obj = Pet.findPet(id);
        Assert.assertNotNull("Find method for 'Pet' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Pet' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllPets() {
        Assert.assertNotNull("Data on demand for 'Pet' failed to initialize correctly", dod.getRandomPet());
        long count = Pet.countPets();
        Assert.assertTrue("Too expensive to perform a find all test for 'Pet', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Pet> result = Pet.findAllPets();
        Assert.assertNotNull("Find all method for 'Pet' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Pet' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindPetEntries() {
        Assert.assertNotNull("Data on demand for 'Pet' failed to initialize correctly", dod.getRandomPet());
        long count = Pet.countPets();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Pet> result = Pet.findPetEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Pet' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Pet' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Pet obj = dod.getRandomPet();
        Assert.assertNotNull("Data on demand for 'Pet' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Pet' failed to provide an identifier", id);
        obj = Pet.findPet(id);
        Assert.assertNotNull("Find method for 'Pet' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyPet(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Pet' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        Pet obj = dod.getRandomPet();
        Assert.assertNotNull("Data on demand for 'Pet' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Pet' failed to provide an identifier", id);
        obj = Pet.findPet(id);
        boolean modified =  dod.modifyPet(obj);
        Integer currentVersion = obj.getVersion();
        Pet merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Pet' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'Pet' failed to initialize correctly", dod.getRandomPet());
        Pet obj = dod.getNewTransientPet(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Pet' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Pet' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Pet' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        Pet obj = dod.getRandomPet();
        Assert.assertNotNull("Data on demand for 'Pet' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Pet' failed to provide an identifier", id);
        obj = Pet.findPet(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Pet' with identifier '" + id + "'", Pet.findPet(id));
    }
}
