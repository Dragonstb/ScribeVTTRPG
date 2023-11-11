package dev.dragonstb.scribevttrpg;

import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Dragonstb
 * @since 0.0.1;
 */
public class Test_LocaleConverter {

    private LocaleConverter converter;

    @BeforeEach
    public void beforeEach() {
        converter = new LocaleConverter();
    }

    @Test
    public void testConvert_English() {
        Locale loc = converter.convert("en,fr,it");
        assertNotNull(loc);
        assertEquals(Locale.ENGLISH, loc);
    }

    @Test
    public void testConvert_German() {
        Locale loc = converter.convert("de,en,fr,it");
        assertNotNull(loc);
        assertEquals(Locale.GERMAN, loc);
    }

    @Test
    public void testConvert_null() {
        Locale loc = converter.convert(null);
        assertNotNull(loc);
        assertEquals(Locale.ENGLISH, loc);
    }

}
