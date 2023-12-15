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

import dev.dragonstb.scribevttrpg.utils.Constants;
import java.util.Optional;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

/** This is a piece of content a game master has created for usage in one or more campaigns. This could be something
 * like a handouts, a token, or a special die.
 *
 * @author Dragonstb
 * @since 0.0.5;
 */
public abstract class ContentItem {

    /** The type of a content item. Each item must have exactly one invariant type. */
    @NonNull
    private final ContentType type;

    /** A unique name. Must be non-null. */
    @NonNull
    private String name;

    /** A description of the iitem. Might <i>not</i> exists. */
    private String description;

    /** Initializes with {@code description = null}.
     * @author Dragonstb
     * @since 0.0.5;
     * @param type Type of the content item. Must be non-null.
     * @param name Name of the content item. Must be non-null.
     */
    public ContentItem( @NonNull ContentType type, @NonNull String name ) {
        this( type, name, null );
    }

    /** Initializes
     * @author Dragonstb
     * @since 0.0.5;
     * @param type Type of the content item. Must be non-null.
     * @param name Name of the content item. Must be non-null.
     * @param description A short description of the content item. Not required,
     */
    public ContentItem( @NonNull ContentType type, @NonNull String name, String description ) {
        this.type = type;
        this.name = name;
        this.description = description;
    }

    /**
     * @since 0.0.5
     * @return A json representation of this content item.
     */
    abstract public JSONObject toJson();

    /** Creates a json containing the information given in the abstract superclass {@code ContentItem}. Subclasses
     * may use this method for getting a base json when overriding {@code toJson}, for example.
     * @author Dragonstb
     * @since 0.0.5;
     * @return A json with minimal contents.
     */
    final JSONObject getMinimalJson() {
        JSONObject obj = new JSONObject();
        obj.put( "name", name);
        obj.put( "desc", description != null ? description : Constants.EMPTY_STRING );
        obj.put( "type", type.toString() );
        return obj;
    }

    /** Gets the type.
     * @since 0.0.5;
     * @return The type of the content item
     */
    @NonNull
    public ContentType getType() {
        return type;
    }

    /**
     * @since 0.0.5;
     * @return The name of the content item.
     */
    public String getName() {
        return name;
    }

    /**
     * @since 0.0.5;
     * @param name New name of the content item. Must be non-null.
     */
    public void setName( @NonNull String name ) throws IllegalArgumentException {
        if( name == null ) {
            throw new IllegalArgumentException( "Argument cannot be null." );
        }
        this.name = name;
    }

    /** Gets the description.
     * @author Dragonstb
     * @since 0.0.5;
     * @return If a description exists, a non-empty optional containing the description. Otherwise, an empty optional
     * is returned.
     */
    public Optional<String> getDescription() {
        return Optional.ofNullable( description );
    }

    /**
     * @since 0.0.5;
     * @param description
     */
    public void setDescription( String description ) {
        this.description = description;
    }

}
