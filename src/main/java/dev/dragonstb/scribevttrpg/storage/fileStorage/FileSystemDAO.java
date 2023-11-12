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
import dev.dragonstb.scribevttrpg.storage.StorageInterface;
import dev.dragonstb.scribevttrpg.utils.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

/** Access to a file system based database.
 *
 * @author Dragonstb
 * @since 0.0.2;
 */
@Repository
public class FileSystemDAO implements StorageInterface{

    /** Path of table with user data, relative to the file storage root path. */
    private final String userTable = "tables"+System.getProperty("file.separator")+"user";

    /** Splits the value of two columns in a line. */
    private static final String COLUMN_SPLIT = ";;;";

    private final String campaignTable = "tables"+System.getProperty("file.separator")+"campaigns";
    private final List<String> campaignColumns = new ArrayList<>();
    private static final String CAMPAIGN_UID_KEY = "uid";
    private static final String CAMPAIGN_NAME_KEY = "name";
    private static final String CAMPAIGN_SYSTEM_KEY = "system";

    /** A service for loading and storing files from/to the file system. */
    private final FileStorageService storageService;

    @Autowired
    public FileSystemDAO(FileStorageService storageService) {
        this.storageService = storageService;
        campaignColumns.add(CAMPAIGN_UID_KEY);
        campaignColumns.add(CAMPAIGN_NAME_KEY);
        campaignColumns.add(CAMPAIGN_SYSTEM_KEY);
    }

    @Override
    public String getUidByUsername(String userName) {
        return null;
    }

    @Override
    public List<Campaign> getAllCampaigns(String uid) {
        Resource file = storageService.loadAsResource(campaignTable);
        String content;
        try {
            content = file.getContentAsString(Constants.DEFAULT_CHARSET);
        } catch (IOException e) {
            throw new RuntimeException("Could not get file content as string", e);
        }

        String[] lines = content.split(System.getProperty("line.separator"));
        List<Campaign> campaigns = new ArrayList<>();
        int uidAt = campaignColumns.indexOf( CAMPAIGN_UID_KEY );
        int nameAt = campaignColumns.indexOf( CAMPAIGN_NAME_KEY );
        int systemAt = campaignColumns.indexOf( CAMPAIGN_SYSTEM_KEY );
        if(uidAt < 0 || nameAt < 0 || systemAt < 0) {
            throw new RuntimeException("key not set in campaigns table; uid at "+uidAt+", name at "+nameAt
                    +", system at "+systemAt);
        }

        for (int line = 1; line < lines.length; line++) { // skip first line
            String[] values = lines[line].split(COLUMN_SPLIT);

            if(values.length != campaignColumns.size()) {
                throw new RuntimeException("Invalid amount of cell values in line "+(line+1)+" of campaign table: "
                        + "expected "+campaignColumns.size()+", but are "+values.length);
            }

            if( uid.equals(values[uidAt]) ) {
                String name = values[nameAt];
                String system = values[systemAt];
                if( name != null ) {
                    Campaign campaign = Campaign.create(name, system);
                    campaigns.add(campaign);
                }
            }
        }

        return campaigns;
    }


}
