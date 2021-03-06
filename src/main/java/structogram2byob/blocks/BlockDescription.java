package structogram2byob.blocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import structogram2byob.ScratchType;


/**
 * A "block description" is the user-facing part of a block, i.e. the labels and
 * parameter acceptors making up its visual format.
 */
public class BlockDescription
{
    private final List<Object> parts;

    /**
     * @param parts The parts this description consists of.
     */
    private BlockDescription(List<Object> parts)
    {
        this.parts = Collections.unmodifiableList(new ArrayList<>(parts));
    }

    /**
     * @return The number of separate parts this description has.
     */
    public int countParts()
    {
        return parts.size();
    }

    /**
     * Checks whether the part at the given index is a parameter, as opposed to
     * a plain label.
     *
     * @param index The part index.
     * @return Whether the part is a parameter.
     */
    public boolean isParameter(int index)
    {
        return parts.get(index) instanceof Parameter;
    }

    /**
     * Obtains the label at the given index. Note that depending on the block
     * description, parameters can be labeled as well.
     *
     * @param index The part index.
     * @return The part's label.
     */
    public String getLabel(int index)
    {
        if (isParameter(index)) {
            return ((Parameter) parts.get(index)).label;
        }
        return (String) parts.get(index);
    }

    /**
     * Obtains the type of the parameter part at the given index.
     *
     * @param index The part index.
     * @return The parameter type.
     *
     * @throws IllegalArgumentException If the part is not a parameter.
     */
    public ScratchType getType(int index)
    {
        if (!isParameter(index)) {
            throw new IllegalArgumentException("tried accessing label as parameter");
        }
        return ((Parameter) parts.get(index)).type;
    }

    /**
     * Checks whether the parameter part at the given index accepts a variable
     * number of values.
     *
     * @param index The part index.
     * @return Whether the parameter accepts a variable number of values.
     *
     * @throws IllegalArgumentException If the part is not a parameter.
     */
    public boolean isList(int index)
    {
        if (!isParameter(index)) {
            throw new IllegalArgumentException("tried accessing label as parameter");
        }
        return ((Parameter) parts.get(index)).list;
    }

    /**
     * Checks whether this description is compatible to the given one in the
     * sense that all the labels match, and all parameter types are compatible
     * (via {@link ScratchType#isAssignableFrom(ScratchType)}).
     *
     * @param o The block description to check assignability for.
     * @return Whether the description is compatible with this one.
     */
    public boolean isAssignableFrom(BlockDescription o)
    {
        final int iMax = countParts(), jMax = o.countParts();
        int i, j;

        for (i = 0, j = i; i < iMax && j < jMax; ++i) {

            if (isParameter(i)) {
                ScratchType paramType = getType(i);

                if (isList(i)) {
                    // consume all params that match
                    while (j < jMax && o.isParameter(j) && paramType.isAssignableFrom(o.getType(j))) {
                        ++j;
                    }
                    continue;
                }

                // check param types compatible
                if (!o.isParameter(j) || !paramType.isAssignableFrom(o.getType(j))) {
                    return false;
                }
            } else if (o.isParameter(j) || !getLabel(i).equals(o.getLabel(j))) {
                // labels must match for non-parameters
                return false;
            }

            ++j;
        }

        // verify that both descriptions were iterated over completely
        return i == iMax && j == jMax;
    }

    /**
     * Converts this description into a "user spec" as employed by custom blocks
     * (e.g. {@code "foo (bar) baz"} becomes {@code "foo %bar baz"}).
     *
     * @return This description as a user spec.
     */
    public String toUserSpec()
    {
        return parts.stream().map(p -> {
            if (p instanceof Parameter) {
                return "%" + ((Parameter) p).label;
            }
            return (String) p;
        }).collect(Collectors.joining(" "));
    }

    /**
     * Converts this description back to a builder for further editing. Changes
     * to the builder do not write back to this instance.
     *
     * @return An editable builder containing all parts of this description.
     */
    public Builder toBuilder()
    {
        Builder b = new Builder();
        b.parts.addAll(parts);

        return b;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(parts);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BlockDescription other = (BlockDescription) obj;
        return Objects.equals(parts, other.parts);
    }

    @Override
    public String toString()
    {
        return parts.stream().map(Object::toString).collect(Collectors.joining(" "));
    }

    /**
     * Builder class for convenient construction of block descriptions.
     */
    public static class Builder
    {
        private final List<Object> parts = new ArrayList<>();

        /**
         * Adds a parameter part of the given type to the object being built.
         *
         * @param type The value type accepted by the parameter.
         * @return This instance.
         */
        public Builder param(ScratchType type)
        {
            parts.add(new Parameter(type, false, null));
            return this;
        }

        /**
         * Adds a labeled parameter part of the given type to the object being
         * built.
         *
         * @param type The value type accepted by the parameter.
         * @param label The parameter's label.
         * @return This instance.
         */
        public Builder param(ScratchType type, String label)
        {
            parts.add(new Parameter(type, false, label));
            return this;
        }

        /**
         * Adds a parameter part accepting a variable number of values of the
         * given type to the object being built.
         *
         * @param type The value type accepted by the parameter.
         * @return This instance.
         */
        public Builder paramList(ScratchType type)
        {
            parts.add(new Parameter(type, true, null));
            return this;
        }

        /**
         * Adds a labeled parameter part accepting a variable number of values
         * of the given type to the object being built.
         *
         * @param type The value type accepted by the parameter.
         * @param label The parameter's label.
         * @return This instance.
         */
        public Builder paramList(ScratchType type, String label)
        {
            parts.add(new Parameter(type, true, label));
            return this;
        }

        /**
         * Adds a label part to the object being built.
         *
         * @param label The label.
         * @return This instance.
         */
        public Builder label(String label)
        {
            parts.add(label);
            return this;
        }

        /**
         * Constructs a block description from the parts added to this builder.
         *
         * @return A block description.
         */
        public BlockDescription build()
        {
            return new BlockDescription(parts);
        }
    }

    /**
     * Class used in block descriptions for specifying parameters.
     */
    private static class Parameter
    {
        private final ScratchType type;
        private final boolean list;
        private final String label;

        /**
         * Constructs a new parameter that accepts the given type, may be a
         * list, and has the given label.
         *
         * @param type The type of value that is accepted.
         * @param list Whether the param accepts a variable number of values.
         * @param label The parameter's label. May be null if there is no label.
         */
        public Parameter(ScratchType type, boolean list, String label)
        {
            this.type = type;
            this.list = list;
            this.label = label;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(label, list, type);
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Parameter other = (Parameter) obj;
            return Objects.equals(label, other.label) && list == other.list && type == other.type;
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();

            sb.append('(');
            sb.append(type.name().toLowerCase());
            if (list) {
                sb.append("...");
            }
            sb.append(')');

            return sb.toString();
        }
    }
}
