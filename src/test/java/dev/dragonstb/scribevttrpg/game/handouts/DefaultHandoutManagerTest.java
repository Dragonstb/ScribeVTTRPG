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
package dev.dragonstb.scribevttrpg.game.handouts;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dragonstb
 */
public class DefaultHandoutManagerTest {

    private DefaultHandoutManager manager;

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        manager = new DefaultHandoutManager();
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testAddHandout_ok() {
        String label = "thisisacreativelabel";
        String id = "123456789";
        ContainerHandout con = ContainerHandout.create( label, id );

        String textId1 = id+"text1";
        TextHandout text1 = TextHandout.create( "texttexttext", textId1 );
        con.addPiece( text1, id );

        String textId2 = id + "text2";
        TextHandout text2 = TextHandout.create( "moretexttexttext", textId2 );
        con.addPiece( text2, id );

        manager.addHandout( con );

        List<ContainerHandout> handouts = manager.getAllHandouts();
        assertEquals( 1, handouts.size(), "wrong size of handouts" );
        assertTrue( handouts.get(0) == con, "parent container is not in the list of handouts");

        Map<String, HandoutAccessGuard> map = manager.getGuards();
        assertEquals( 3, map.size(), "wrong number of guards" );

        assertTrue( map.containsKey(id), "id of container not in map" );
        assertNotNull( map.get(id), "container guard not in map" );
        assertTrue( map.get(id).getPiece() == con, "container is not guarded by container guard" );

        assertTrue( map.containsKey(textId1), "id of text1 not in map" );
        assertNotNull( map.get(textId1), "text1 guard not in map" );
        assertTrue( map.get(textId1).getPiece() == text1, "text1 is not guarded by text1 guard" );

        assertTrue( map.containsKey(textId2), "id of text2 not in map" );
        assertNotNull( map.get(textId2), "text2 guard not in map" );
        assertTrue( map.get(textId2).getPiece() == text2, "text2 is not guarded by text2 guard" );

    }

    @Test
    public void testAddHandout_id_occupied() {
        String label1 = "thisisacreativelabel";
        String id = "123456789";
        ContainerHandout con1 = ContainerHandout.create( label1, id );

        String label2 = "keep it simple";
        ContainerHandout con2 = ContainerHandout.create( label2, id );

        manager.addHandout( con1 );

        Throwable fail = assertThrows( IllegalArgumentException.class, () -> manager.addHandout( con2 ) );
        assertEquals( "Key already in use.", fail.getMessage() );
    }


}
