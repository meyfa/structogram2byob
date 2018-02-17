package structogram2byob.blocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import structogram2byob.ScratchType;


public class BlockDescriptionTest
{
    @Test
    public void countsParts()
    {
        BlockDescription obj = new BlockDescription.Builder().label("foo")
                .param(ScratchType.ANY).paramList(ScratchType.NUMBER).build();

        assertEquals(3, obj.countParts());
    }

    @Test
    public void returnsPartType()
    {
        BlockDescription obj = new BlockDescription.Builder().label("foo")
                .param(ScratchType.ANY).paramList(ScratchType.NUMBER).build();

        assertFalse(obj.isParameter(0));
        assertTrue(obj.isParameter(1));
        assertTrue(obj.isParameter(2));
    }

    @Test
    public void returnsLabel()
    {
        BlockDescription obj = new BlockDescription.Builder().label("foo")
                .param(ScratchType.ANY, "testparam")
                .paramList(ScratchType.NUMBER).build();

        assertEquals("foo", obj.getLabel(0));
        assertEquals("testparam", obj.getLabel(1));
        assertNull(obj.getLabel(2));
    }

    @Test
    public void returnsType()
    {
        BlockDescription obj = new BlockDescription.Builder().label("foo")
                .param(ScratchType.ANY).paramList(ScratchType.NUMBER).build();

        try {
            obj.getType(0);
            fail("not thrown");
        } catch (IllegalArgumentException e) {
        }

        assertSame(ScratchType.ANY, obj.getType(1));
        assertSame(ScratchType.NUMBER, obj.getType(2));
    }

    @Test
    public void returnsIsList()
    {
        BlockDescription obj = new BlockDescription.Builder().label("foo")
                .param(ScratchType.ANY).paramList(ScratchType.NUMBER).build();

        try {
            obj.isList(0);
            fail("not thrown");
        } catch (IllegalArgumentException e) {
        }

        assertFalse(obj.isList(1));
        assertTrue(obj.isList(2));
    }

    @Test(timeout = 1000)
    public void checksAssignability()
    {
        BlockDescription obj1, obj2;

        // different counts
        obj1 = new BlockDescription.Builder().label("foo").build();
        obj2 = new BlockDescription.Builder().label("foo").label("bar").build();
        assertFalse(obj1.isAssignableFrom(obj2));
        assertFalse(obj2.isAssignableFrom(obj1));

        // different labels
        obj1 = new BlockDescription.Builder().label("foo").label("bar").build();
        obj2 = new BlockDescription.Builder().label("foo").label("baz").build();
        assertFalse(obj1.isAssignableFrom(obj2));
        assertFalse(obj2.isAssignableFrom(obj1));

        // label vs parameter
        obj1 = new BlockDescription.Builder().label("foo").label("bar").build();
        obj2 = new BlockDescription.Builder().label("foo")
                .param(ScratchType.ANY, "bar").build();
        assertFalse(obj1.isAssignableFrom(obj2));
        assertFalse(obj2.isAssignableFrom(obj1));

        // incompatible param types
        obj1 = new BlockDescription.Builder().label("foo")
                .param(ScratchType.BOOLEAN, "bar").build();
        obj2 = new BlockDescription.Builder().label("foo")
                .param(ScratchType.NUMBER).build();
        assertFalse(obj1.isAssignableFrom(obj2));
        assertFalse(obj2.isAssignableFrom(obj1));

        // equal labels and compatible types
        obj1 = new BlockDescription.Builder().label("foo")
                .param(ScratchType.ANY, "bar").build();
        obj2 = new BlockDescription.Builder().label("foo")
                .param(ScratchType.NUMBER).build();
        assertTrue(obj1.isAssignableFrom(obj2));
        assertTrue(obj2.isAssignableFrom(obj1));

        // param lists
        obj1 = new BlockDescription.Builder().label("foo")
                .paramList(ScratchType.ANY).label("bar").build();
        obj2 = new BlockDescription.Builder().label("foo")
                .param(ScratchType.NUMBER).param(ScratchType.TEXT).label("bar")
                .build();
        assertTrue(obj1.isAssignableFrom(obj2));
        assertFalse(obj2.isAssignableFrom(obj1));

        // param lists at end
        obj1 = new BlockDescription.Builder().label("foo")
                .paramList(ScratchType.ANY).build();
        obj2 = new BlockDescription.Builder().label("foo")
                .param(ScratchType.NUMBER).param(ScratchType.TEXT).build();
        assertTrue(obj1.isAssignableFrom(obj2));
        assertFalse(obj2.isAssignableFrom(obj1));

        // empty param lists
        obj1 = new BlockDescription.Builder().label("foo")
                .paramList(ScratchType.ANY).label("bar").build();
        obj2 = new BlockDescription.Builder().label("foo").label("bar").build();
        assertTrue(obj1.isAssignableFrom(obj2));
        assertFalse(obj2.isAssignableFrom(obj1));

        // empty param lists plus parameter
        obj1 = new BlockDescription.Builder().label("foo")
                .paramList(ScratchType.NUMBER).param(ScratchType.TEXT).build();
        obj2 = new BlockDescription.Builder().label("foo")
                .param(ScratchType.TEXT).build();
        assertTrue(obj1.isAssignableFrom(obj2));
        assertFalse(obj2.isAssignableFrom(obj1));

        // param lists plus parameter
        obj1 = new BlockDescription.Builder().label("foo")
                .paramList(ScratchType.NUMBER).param(ScratchType.TEXT).build();
        obj2 = new BlockDescription.Builder().label("foo")
                .param(ScratchType.NUMBER).param(ScratchType.NUMBER)
                .param(ScratchType.TEXT).build();
        assertTrue(obj1.isAssignableFrom(obj2));
        assertFalse(obj2.isAssignableFrom(obj1));
    }

    @Test
    public void convertsToUserSpec()
    {
        BlockDescription obj = new BlockDescription.Builder().label("foo")
                .param(ScratchType.ANY, "testparam")
                .paramList(ScratchType.NUMBER, "p2").build();

        assertEquals("foo %testparam %p2", obj.toUserSpec());
    }

    @Test
    public void convertsToBuilder()
    {
        BlockDescription obj = new BlockDescription.Builder().label("foo")
                .param(ScratchType.ANY, "testparam")
                .paramList(ScratchType.NUMBER).build();

        BlockDescription.Builder b = obj.toBuilder();

        BlockDescription obj2 = b.build();
        assertEquals(3, obj2.countParts());

        b.label("bar");
        BlockDescription obj3 = b.build();
        assertEquals(3, obj.countParts());
        assertEquals(3, obj2.countParts());
        assertEquals(4, obj3.countParts());
    }

    @Test
    public void implementsSymmetricHashCode()
    {
        BlockDescription obj1 = new BlockDescription.Builder().label("foo")
                .param(ScratchType.ANY, "testparam")
                .paramList(ScratchType.NUMBER).build();
        BlockDescription obj2 = new BlockDescription.Builder().label("foo")
                .param(ScratchType.ANY, "testparam")
                .paramList(ScratchType.NUMBER).build();

        assertTrue(obj1.hashCode() == obj2.hashCode());
    }

    @Test
    public void implementsEquals()
    {
        BlockDescription obj1 = new BlockDescription.Builder().label("foo")
                .param(ScratchType.ANY, "testparam")
                .paramList(ScratchType.NUMBER).build();
        BlockDescription obj2 = new BlockDescription.Builder().label("foo")
                .param(ScratchType.ANY, "testparam")
                .paramList(ScratchType.NUMBER).build();

        BlockDescription obj3 = new BlockDescription.Builder().label("foo")
                .param(ScratchType.ANY, "otherparam")
                .paramList(ScratchType.NUMBER).build();

        BlockDescription obj4 = new BlockDescription.Builder().label("foo")
                .param(ScratchType.ANY, "testparam").param(ScratchType.NUMBER)
                .build();

        BlockDescription obj5 = new BlockDescription.Builder().label("foo")
                .param(ScratchType.ANY, "otherparam").paramList(ScratchType.ANY)
                .build();

        assertTrue(obj1.equals(obj1));

        assertTrue(obj1.equals(obj2));
        assertTrue(obj2.equals(obj1));

        assertFalse(obj1.equals(null));
        assertFalse(obj1.equals(new Object()));

        assertFalse(obj1.equals(obj3));
        assertFalse(obj3.equals(obj1));

        assertFalse(obj1.equals(obj4));
        assertFalse(obj4.equals(obj1));

        assertFalse(obj1.equals(obj5));
        assertFalse(obj5.equals(obj1));
    }

    @Test
    public void convertsToString()
    {
        BlockDescription obj = new BlockDescription.Builder().label("foo")
                .param(ScratchType.ANY, "testparam")
                .paramList(ScratchType.NUMBER, "p2").build();

        assertEquals("foo (any) (number...)", obj.toString());
    }
}
