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

package dev.dragonstb.scribevttrpg.content;

import dev.dragonstb.scribevttrpg.game.handouts.AbstractHandoutPiece;
import dev.dragonstb.scribevttrpg.game.handouts.ContainerHandout;
import dev.dragonstb.scribevttrpg.game.handouts.TextHandout;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Dragonstb
 * @since 0.0.5;
 */
public final class DefaultContentManager implements ContentManager {

    /** Access to persistent storage.*/
    // TODO: activate once we are ready. For now, we use hardcoded content.
//    @Autowired
//    private StorageInterface storage;

    /** List of <i>all</i> content items. */
    private final List<ContentItem> contents = new ArrayList<>(); // array list for maintaining the order

    /** Initializes.
     * @author Dragonstb
     * @since 0.0.5;
     */
    public DefaultContentManager() {
        loadContents();
    }

    @Override
    public List<ContentItem> getContents( String campaignName ) {
        // TODO: take campaign name into account
        return contents;
    }

    @Override
    public JSONArray getContentsAsJson( String campaignName ) {
        List<ContentItem> items = getContents( campaignName );
        JSONArray json = new JSONArray();

        items.forEach( item -> json.put( item.toJson() ) );
        return json;
    }



    @Override
    public List<AbstractHandoutPiece> getHandouts( String campaignName ) {
        return null;
    }

    /** Loads all contents and writes then into {@code this.contents}.
     * @author Dragonstb
     * @since 0.0.5;
     */
    private void loadContents() {
        // TODO: specify campaign
        // TODO: How to keep order when loading contents of campaigns one after another?
        // TODO: use implementation of StorageInterface for accessing actually stored content, rather than hardcoding
        // TODO: memory management, might not be required to have all content items in memory

        // TODO: take contents from campaign

        ContainerHandout skills;
        ContainerHandout genSkills;
        ContainerHandout comSkills;
        ContainerHandout magSkills;
        ContainerHandout traits;

        ContainerHandout blank = ContainerHandout.create( "Empty character sheet", "ho51" );
        {
        skills = ContainerHandout.create( "Skills", "ho52" );
        genSkills = ContainerHandout.create( "Generic Skills", "ho53" );
        comSkills = ContainerHandout.create( "Combat Skills", "ho54" );
        magSkills = ContainerHandout.create( "Magic Skills", "ho55" );
        traits = ContainerHandout.create( "Traits", "ho56" );
        blank.addPiece( skills, "ho51" );
        blank.addPiece( genSkills, "ho51ho52" );
        blank.addPiece( comSkills, "ho51ho52" );
        blank.addPiece( magSkills, "ho51ho52" );
        blank.addPiece( traits, "ho51" );
        }

        ContainerHandout skull = ContainerHandout.create( "Skull the Barbarian", "ho1" );
        {
        skills = ContainerHandout.create( "Skills", "ho2" );
        genSkills = ContainerHandout.create( "Generic Skills", "ho3" );
        comSkills = ContainerHandout.create( "Combat Skills", "ho4" );
        magSkills = ContainerHandout.create( "Magic Skills", "ho5" );
        traits = ContainerHandout.create( "Traits", "ho6" );
        TextHandout drink = TextHandout.create( "Drink", "ho7" );
        TextHandout endurance = TextHandout.create( "Endurance", "ho8" );
        TextHandout axes = TextHandout.create( "Axes", "ho9" );
        TextHandout swords = TextHandout.create( "Swords", "ho10" );
        TextHandout knuckles = TextHandout.create( "Bare Knuckles", "ho11" );
        TextHandout firebolt = TextHandout.create( "Firebolt", "ho12" );
        TextHandout lightningbolt = TextHandout.create( "Lightning Bolt", "ho13" );
        TextHandout honest = TextHandout.create( "Very honest", "ho14" );
        TextHandout strong = TextHandout.create( "Very strong", "ho15" );
        TextHandout impatient = TextHandout.create( "Not very patient", "ho16" );
        skull.addPiece( skills, "ho1" );
        skull.addPiece( genSkills, "ho1ho2" );
        skull.addPiece( comSkills, "ho1ho2" );
        skull.addPiece( magSkills, "ho1ho2" );
        skull.addPiece( drink, "ho1ho2ho3" );
        skull.addPiece( endurance, "ho1ho2ho3" );
        skull.addPiece( axes, "ho1ho2ho4" );
        skull.addPiece( swords, "ho1ho2ho4" );
        skull.addPiece( knuckles, "ho1ho2ho4" );
        skull.addPiece( firebolt, "ho1ho2ho5" );
        skull.addPiece( lightningbolt, "ho1ho2ho5" );
        skull.addPiece( traits, "ho1" );
        skull.addPiece( honest, "ho1ho6" );
        skull.addPiece( strong, "ho1ho6" );
        skull.addPiece( impatient, "ho1ho6" );
        }

        ContainerHandout thunder = ContainerHandout.create( "Thunder the Wizard", "ho21" );
        {
        skills = ContainerHandout.create( "Skills", "ho22" );
        genSkills = ContainerHandout.create( "Generic Skills", "ho23" );
        comSkills = ContainerHandout.create( "Combat Skills", "ho24" );
        magSkills = ContainerHandout.create( "Magic Skills", "ho25" );
        traits = ContainerHandout.create( "Traits", "ho26" );

        TextHandout intimidate= TextHandout.create( "Intimidate", "ho27" );
        TextHandout dancing= TextHandout.create( "Dancing", "ho28" );
        genSkills.addPiece( intimidate, genSkills.getId() );
        genSkills.addPiece( dancing, genSkills.getId() );
        skills.addPiece( genSkills, skills.getId() );

        TextHandout staffs= TextHandout.create( "Staffs", "ho29" );
        TextHandout bows = TextHandout.create( "Bows", "ho30" );
        comSkills.addPiece( staffs, comSkills.getId() );
        comSkills.addPiece( bows, comSkills.getId() );
        skills.addPiece( comSkills, skills.getId() );

        TextHandout fireball = TextHandout.create( "Fireball", "ho39" );
        TextHandout energyShield = TextHandout.create( "Energy Shield", "ho31" );
        TextHandout storm = TextHandout.create( "Storm", "ho32" );
        TextHandout thunderbolt = TextHandout.create( "Thunderbolt", "ho33" );
        TextHandout meteorStrike = TextHandout.create( "Meteor Strike", "ho34" );
        TextHandout healing = TextHandout.create( "Healing", "ho35" );
        magSkills.addPiece( fireball, magSkills.getId() );
        magSkills.addPiece( energyShield, magSkills.getId() );
        magSkills.addPiece( storm, magSkills.getId() );
        magSkills.addPiece( thunderbolt, magSkills.getId() );
        magSkills.addPiece( meteorStrike, magSkills.getId() );
        magSkills.addPiece( healing, magSkills.getId() );
        skills.addPiece( magSkills, skills.getId() );

        TextHandout ambitious = TextHandout.create( "Very ambitious", "ho36" );
        TextHandout stamina = TextHandout.create( "Lots of stamina", "ho37" );
        TextHandout rude = TextHandout.create( "Fairly rude", "ho38" );
        traits.addPiece( ambitious, traits.getId() );
        traits.addPiece( stamina, traits.getId() );
        traits.addPiece( rude, traits.getId() );

        thunder.addPiece( skills, thunder.getId() );
        thunder.addPiece( traits, thunder.getId() );
        }

        ContainerHandout quest = ContainerHandout.create( "Quest Leaflet", "ho41" );
        TextHandout questText = TextHandout.create( "Sinister howls from the wrecked castle at night fill the peaceful"
                + " villagers with fear. Coincidentally, there are also rumors about a great treasure in that "
                + "haunted ruin.", "ho42" );
        quest.addPiece( questText, quest.getId() );
        ContainerHandout letter = ContainerHandout.create( "A mysterious letter", "ho43" );
        TextHandout letterText = TextHandout.create( "Greeting Marty, I hope everything runs well in the mine? This is going to be "+
                            "great if we can find what we are looking for there. I remind you that it is of utmost "+
                            "importance that no one takes a notice of our actions! Remember what is at stake.", "ho44" );
        letter.addPiece( letterText, letter.getId() );

        ContentHandout blankContent = new ContentHandout( skull, "sheet template" );
        ContentHandout skullContent = new ContentHandout( skull, "character sheet" );
        ContentHandout thunderContent = new ContentHandout( thunder, "character sheet" );
        ContentHandout questContent = new ContentHandout( quest, "The first quest" );
        ContentHandout letterContent = new ContentHandout( letter, "Strange writing from the mayor about a mine near "
                + "the village" );

        List<ContentItem> items = new ArrayList<>();
        items.add( blankContent );
        items.add( skullContent );
        items.add( thunderContent );
        items.add( questContent );
        items.add( letterContent );

        items.forEach( item -> this.contents.add( item ) );
    }
}
