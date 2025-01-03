/*
 * Copyright (c) 2024, Dragonstb
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

package dev.dragonstb.scribevttrpg.game.exceptions;

/** Exception thrown when adding a new (prospect) participant to a game would violate the uniqueness of the
 * participant identities. Short: There is already a participant with the same name.
 *
 * @author Dragonstb
 * @since 0.1.0;
 */
public class IdentityNotUniqueException extends RuntimeException {

    /** ID that is not unique. */
    private final String name;

    /**
     * @since 0.1.0
     * @param name ID that is not unique.
     */
    public IdentityNotUniqueException( String name ) {
        this.name = name;
    }

    /**
     * @since 0.1.0
     * @param name ID that is not unique.
     * @param message
     */
    public IdentityNotUniqueException( String name, String message ) {
        super( message );
        this.name = name;
    }

    /** Returns the non-unique id.
     * @since 0.1.0
     * @return ID that violates uniqueness.
     */
    public String getName() {
        return name;
    }

}
