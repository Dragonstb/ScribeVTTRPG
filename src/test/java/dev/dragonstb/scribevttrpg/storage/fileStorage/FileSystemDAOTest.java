/*
 * Copyright (c) 2023, Dragonstb
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package dev.dragonstb.scribevttrpg.storage.fileStorage;

import dev.dragonstb.scribevttrpg.campaigns.Campaign;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

/**
 *
 * @author Dragonstb
 * @since 0.0.3
 */
@AutoConfigureMockMvc
@SpringBootTest
public class FileSystemDAOTest {

    /* Some values from the test mock campaigns table. */
    private static final String UID_A = "A";
    private static final String UID_B = "B";
    private static final String CPG_A_1 = "CPG_A_1";
    private static final String SYS_A_1 = "SYS_A_1";
    private static final String CPG_A_2 = "CPG_A_2";
    private static final String SYS_A_2 = "SYS_A_2";
    private static final String CPG_B = "CPG_B";
    private static final String SYS_B = "SYS_B";
    private static final Campaign CAMPAIGN_A1 = Campaign.create(CPG_A_1, SYS_A_1);
    private static final Campaign CAMPAIGN_A2 = Campaign.create(CPG_A_2, SYS_A_2);
    private static final Campaign CAMPAIGN_B = Campaign.create(CPG_B, SYS_B);

    /** argument for addressing the campaigns table when mocking the loadAsResource(). */
    private static final String CAMPAIGN_ARG = "tables"+System.getProperty("file.separator")+"campaigns";
    /** File location of mock campaigns table. */
    private static final String CAMPAIGN_FILE = "file://" + new File(".").getAbsolutePath()
            + "/src/test/resources/storage/fileSystemStorage/campaigns.table";

    private FileSystemDAO dao;

    @MockBean
    private StorageServiceInterface storageService;

    @BeforeEach
    public void setUp() {
        dao = new FileSystemDAO(storageService);
    }

    @Test
    public void testGetUidByUsername_existingUid() {
        String uid = dao.getUidByUsername("1234");
        assertNull(uid);
    }

    @Test
    public void testGetUidByUsername_nonExistingUid() {
        String uid = dao.getUidByUsername("xxxxx");
        assertNull(uid);
    }

    @Test
    public void testGetUidByUsername_null() {
        String uid = dao.getUidByUsername(null);
        assertNull(uid);
    }

    @Test
    public void testGetAllCampaigns_existingUserA() {
        Resource res;
        try {
            res = new UrlResource(CAMPAIGN_FILE);
        } catch (MalformedURLException e) {
            fail("Malformed URL exception when forging resource: " + e.getLocalizedMessage());
            return;
        }

        given( storageService.loadAsResource(CAMPAIGN_ARG) ).willReturn( res );

        List<Campaign> list = dao.getAllCampaigns(UID_A);

        assertNotNull(list);
        assertEquals(2, list.size(), "unexpected length");

        Campaign cmp0 = list.get(0);
        Campaign cmp1 = list.get(1);

        if( cmp0.getName().equals(CAMPAIGN_A1.getName()) ) {
            assertEquals( CAMPAIGN_A1.getSystem(), cmp0.getSystem() );
            assertEquals( CAMPAIGN_A2.getName(), cmp1.getName() );
            assertEquals( CAMPAIGN_A2.getSystem(), cmp1.getSystem() );
        }
        else if( cmp0.getName().equals(CAMPAIGN_A2.getName()) ) {
            assertEquals( CAMPAIGN_A2.getSystem(), cmp0.getSystem() );
            assertEquals( CAMPAIGN_A1.getName(), cmp1.getName() );
            assertEquals( CAMPAIGN_A1.getSystem(), cmp1.getSystem() );
        }
        else {
            fail("no matching name for campaigns");
        }
    }

    @Test
    public void testGetAllCampaigns_existingUserB() {
        Resource res;
        try {
            res = new UrlResource(CAMPAIGN_FILE);
        } catch (MalformedURLException e) {
            fail("Malformed URL exception when forging resource: " + e.getLocalizedMessage());
            return;
        }

        given( storageService.loadAsResource(CAMPAIGN_ARG) ).willReturn( res );

        List<Campaign> list = dao.getAllCampaigns(UID_B);

        assertNotNull(list);
        assertEquals(1, list.size(), "unexpected length");

        Campaign cmp1 = list.get(0);

        assertEquals( CAMPAIGN_B.getName(), cmp1.getName() );
        assertEquals( CAMPAIGN_B.getSystem(), cmp1.getSystem() );
    }

    @Test
    public void testGetAllCampaigns_nonExistingUser() {
        Resource res;
        try {
            res = new UrlResource(CAMPAIGN_FILE);
        } catch (MalformedURLException e) {
            fail("Malformed URL exception when forging resource: " + e.getLocalizedMessage());
            return;
        }

        given( storageService.loadAsResource(CAMPAIGN_ARG) ).willReturn( res );

        List<Campaign> list = dao.getAllCampaigns("xxxxxxxxx");

        assertNotNull(list);
        assertEquals(0, list.size(), "unexpected length");

    }

    @Test()
    public void testGetAllCampaigns_null() {
        Resource res;
        try {
            res = new UrlResource(CAMPAIGN_FILE);
        } catch (MalformedURLException e) {
            fail("Malformed URL exception when forging resource: " + e.getLocalizedMessage());
            return;
        }

        given( storageService.loadAsResource(CAMPAIGN_ARG) ).willReturn( res );

        IllegalArgumentException iae = assertThrows(
                IllegalArgumentException.class,
                () -> dao.getAllCampaigns(null)
        );

        assertTrue( iae.getMessage().equals("Argument must not be null!") );
    }

}
