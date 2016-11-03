/*
 * Copyright 2016 Bridje Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bridje.orm;

/**
 * Represents a database column associated with a field of an Entity, or a
 * function column. Objects from this class will be generated by the ORM api so
 * the user can use then for building the querys for the Entities of his data
 * model.
 *
 * @param <T> The type of the column.
 */
public interface Column<T> extends SQLWritable
{
    /**
     * Gets the java type for this column.
     *
     * @return The java type for this column.
     */
    Class<T> getType();

    /**
     * Creates a new equals "=" condition with this column as a left operand and
     * the provided value as the right operand.
     *
     * @param value The right operand for the condition.
     * @return The new created condition.
     */
    Condition eq(T value);
    
    /**
     * Creates a new equals "=" condition with this column as a left operand and
     * the given column as the right operand.
     *
     * @param column The right operand for the condition.
     * @return The new created condition.
     */
    Condition eq(TableColumn<?, ?> column);

    /**
     * Creates a new "IS NULL" condition with this column as a left operand and
     * the provided value as the left operand.
     *
     * @return The new created condition.
     */
    Condition isNull();

    /**
     * Creates a new "IS NOT NULL" condition with this column as a left operand and
     * the provided value as the left operand.
     *
     * @return The new created condition.
     */
    Condition isNotNull();

    /**
     * Creates a new not equals "&lt;&gt;" condition with this column as a left
     * operand and the provided value as the left operand.
     *
     * @param value The right operand for the condition.
     * @return The new created condition.
     */
    Condition ne(T value);

    /**
     * Creates a new in "IN" condition with this column as a left operand and
     * the provided values as parameters.
     *
     * @param values The in parameters.
     * @return The new created condition.
     */
    Condition in(T... values);

    /**
     * Creates a new not in "NOT IN" condition with this column as a left operand and
     * the provided values as parameters.
     *
     * @param values The in parameters.
     * @return The new created condition.
     */
    Condition notIn(T... values);

    /**
     * Creates a new ascending order by statement that can be user to order a
     * query that involves the entity of this column.
     *
     * @return The new ascending OrderBy statement.
     */
    OrderBy asc();

    /**
     * Creates a new descending order by statement that can be user to order a
     * query that involves the entity of this column.
     *
     * @return The new descending OrderBy statement.
     */
    OrderBy desc();
}
