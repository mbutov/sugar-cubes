package org.sugarcubes.s2;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import static java.io.ObjectStreamConstants.TC_ARRAY;
import static java.io.ObjectStreamConstants.TC_BASE;
import static java.io.ObjectStreamConstants.TC_BLOCKDATA;
import static java.io.ObjectStreamConstants.TC_BLOCKDATALONG;
import static java.io.ObjectStreamConstants.TC_CLASS;
import static java.io.ObjectStreamConstants.TC_CLASSDESC;
import static java.io.ObjectStreamConstants.TC_ENDBLOCKDATA;
import static java.io.ObjectStreamConstants.TC_ENUM;
import static java.io.ObjectStreamConstants.TC_EXCEPTION;
import static java.io.ObjectStreamConstants.TC_LONGSTRING;
import static java.io.ObjectStreamConstants.TC_MAX;
import static java.io.ObjectStreamConstants.TC_NULL;
import static java.io.ObjectStreamConstants.TC_OBJECT;
import static java.io.ObjectStreamConstants.TC_PROXYCLASSDESC;
import static java.io.ObjectStreamConstants.TC_REFERENCE;
import static java.io.ObjectStreamConstants.TC_RESET;
import static java.io.ObjectStreamConstants.TC_STRING;

public enum ZTag {

    /**
     * Null object reference.
     */
    NULL(TC_NULL),

    /**
     * Reference to an object already written into the stream.
     */
    REFERENCE(TC_REFERENCE),

    /**
     * new Class Descriptor.
     */
    CLASSDESC(TC_CLASSDESC),

    /**
     * new Object.
     */
    OBJECT(TC_OBJECT),

    /**
     * new String.
     */
    STRING(TC_STRING),

    /**
     * new Array.
     */
    ARRAY(TC_ARRAY),

    /**
     * Reference to Class.
     */
    CLASS(TC_CLASS),

    /**
     * Block of optional data. Byte following tag indicates number
     * of bytes in this block data.
     */
    BLOCKDATA(TC_BLOCKDATA),

    /**
     * End of optional block data blocks for an object.
     */
    ENDBLOCKDATA(TC_ENDBLOCKDATA),

    /**
     * Reset stream context. All handles written into stream are reset.
     */
    RESET(TC_RESET),

    /**
     * long Block data. The long following the tag indicates the
     * number of bytes in this block data.
     */
    BLOCKDATALONG(TC_BLOCKDATALONG),

    /**
     * Exception during write.
     */
    EXCEPTION(TC_EXCEPTION),

    /**
     * Long string.
     */
    LONGSTRING(TC_LONGSTRING),

    /**
     * new Proxy Class Descriptor.
     */
    PROXYCLASSDESC(TC_PROXYCLASSDESC),

    /**
     * new Enum constant.
     *
     * @since 1.5
     */
    ENUM(TC_ENUM);

    private final byte tag;

    ZTag(int tag) {
        this.tag = (byte) tag;
    }

    public byte getTag() {
        return tag;
    }

    private static final ZTag[] TAGS = new ZTag[TC_MAX - TC_BASE + 1];

    static {
        for (ZTag tag : values()) {
            TAGS[tag.getTag() - TC_BASE] = tag;
        }
    }

    public static ZTag valueOf(byte tag) throws IOException {
        try {
            return TAGS[tag - TC_BASE];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new StreamCorruptedException(String.format("Invalid tag: 0x%02X.", tag));
        }
    }

    public static final Set<ZTag> ANY_TAGS = of(values());
    public static final Set<ZTag> CLASSDESC_TAGS = of(NULL, REFERENCE, CLASSDESC);
    public static final Set<ZTag> ANY_OBJECT_TAGS = of(NULL, REFERENCE, OBJECT, STRING, ARRAY, CLASS, LONGSTRING, ENUM);
    public static final Set<ZTag> ANY_OBJECT_RESET_TAGS = of(NULL, REFERENCE, OBJECT, STRING, ARRAY, CLASS, RESET, LONGSTRING, ENUM);
    public static final Set<ZTag> NOT_NULL_CLASSDESC_TAGS = of(REFERENCE, CLASSDESC, PROXYCLASSDESC);
    public static final Set<ZTag> NOT_NULL_STRING_TAGS = of(REFERENCE, STRING, LONGSTRING);
    public static final Set<ZTag> BLOCKDATA_TAGS = of(BLOCKDATA, BLOCKDATALONG, ENDBLOCKDATA);

    private static Set<ZTag> of(ZTag... tags) {
        Set<ZTag> set = EnumSet.noneOf(ZTag.class);
        set.addAll(Set.of(tags));
        return Collections.unmodifiableSet(set);
    }

}
