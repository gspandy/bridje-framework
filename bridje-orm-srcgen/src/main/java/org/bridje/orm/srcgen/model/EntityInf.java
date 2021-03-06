/*
 * Copyright 2017 Bridje Framework.
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

package org.bridje.orm.srcgen.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Information for an entity of the model.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EntityInf
{
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String table;

    @XmlTransient
    private ModelInf model;

    @XmlAttribute(name = "description")
    private String description;

    private EntityInfKey key;

    @XmlElementWrapper(name = "fields")
    @XmlElements(
    {
        @XmlElement(name = "relation", type = RelationField.class),
        @XmlElement(name = "boolean", type = BooleanField.class),
        @XmlElement(name = "number", type = NumberField.class),
        @XmlElement(name = "string", type = StringField.class),
        @XmlElement(name = "date", type = DateField.class)
    })
    private List<FieldInf> fields;

    @XmlElementWrapper(name = "indexes")
    @XmlElements(
    {
        @XmlElement(name = "index", type = EntityIndexInf.class),
        @XmlElement(name = "unique", type = EntityUniqueIndexInf.class)
    })
    private List<EntityIndexInf> indexes;

    @XmlElementWrapper(name = "queries")
    @XmlElements(
    {
        @XmlElement(name = "select", type = SelectQueryInf.class),
        @XmlElement(name = "count", type = CountQueryInf.class),
        @XmlElement(name = "selectOne", type = SelectOneQueryInf.class),
        @XmlElement(name = "update", type = UpdateQueryInf.class),
        @XmlElement(name = "delete", type = DeleteQueryInf.class)
    })
    private List<QueryInf> queries;

    @XmlTransient
    private List<FieldInf> allFields;

    /**
     * The name for the entity.
     *
     * @return The name for the entity.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name for the entity.
     *
     * @param name The name for the entity.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The name of the table for this entity.
     *
     * @return The name of the table for this entity.
     */
    public String getTable()
    {
        if (table == null)
        {
            table = Utils.toSQLName(name);
        }
        return table;
    }

    /**
     * The name of the table for this entity.
     *
     * @param table The name of the table for this entity.
     */
    public void setTable(String table)
    {
        this.table = table;
    }

    /**
     * The full java name for this entity.
     *
     * @return The full java name for this entity.
     */
    public String getFullName()
    {
        return model.getPackage() + "." + this.getName();
    }

    /**
     * The model this entity belongs to.
     *
     * @return The model this entity belongs to.
     */
    public ModelInf getModel()
    {
        return model;
    }

    /**
     * The model this entity belongs to.
     *
     * @param model The model this entity belongs to.
     */
    public void setModel(ModelInf model)
    {
        this.model = model;
    }

    /**
     * The description of this entity.
     *
     * @return The description of this entity.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * The description of this entity.
     *
     * @param description The description of this entity.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * The key field for this entity.
     *
     * @return The key field for this entity.
     */
    public FieldInf getKey()
    {
        return key.getField();
    }

    /**
     * The key field for this entity.
     *
     * @param key The key field for this entity.
     */
    public void setKey(FieldInf key)
    {
        if (this.key == null)
        {
            this.key = new EntityInfKey();
        }
        this.key.setField(key);
    }

    /**
     * The list of fields for this entity.
     *
     * @return The list of fields for this entity.
     */
    public List<FieldInf> getFields()
    {
        return fields;
    }

    /**
     * The list of fields for this entity.
     *
     * @param fields The list of fields for this entity.
     */
    public void setFields(List<FieldInf> fields)
    {
        this.fields = fields;
    }

    /**
     * Gets all the fields of this entity, including the key field.
     *
     * @return All the fields of this entity, including the key field.
     */
    public List<FieldInf> getAllFields()
    {
        if (allFields == null)
        {
            allFields = new ArrayList<>();
            allFields.add(key.getField());
            allFields.addAll(fields);
        }
        return allFields;
    }

    /**
     * Gets all the non-autoincrement fields of this entity.
     *
     * @return All the non-autoincrement fields of this entity.
     */
    public List<FieldInf> getNonAiFields()
    {
        return getAllFields()
                .stream()
                .filter(f -> !f.isAutoIncrement())
                .collect(Collectors.toList());
    }

    /**
     * Gets all the queries configured for this entity.
     *
     * @return All the queries configured for this entity.
     */
    public List<QueryInf> getQueries()
    {
        if (queries == null)
        {
            queries = new ArrayList<>();
        }
        return queries;
    }

    /**
     * Sets all the queries configured for this entity.
     *
     * @param queries All the queries configured for this entity.
     */
    public void setQueries(List<QueryInf> queries)
    {
        this.queries = queries;
    }

    /**
     * Gets all the indexes configured for this entity.
     *
     * @return All the indexes configured for this entity.
     */
    public List<EntityIndexInf> getIndexes()
    {
        if (indexes == null)
        {
            indexes = new ArrayList<>();
        }
        return indexes;
    }

    /**
     * Sets all the indexes configured for this entity.
     *
     * @param indexes All the indexes configured for this entity.
     */
    public void setIndexes(List<EntityIndexInf> indexes)
    {
        this.indexes = indexes;
    }

    /**
     * Gets all the foreign keys configured for this entity.
     *
     * @return All the foreign keys configured for this entity.
     */
    public List<RelationField> getForeignKeys()
    {
        return fields.stream()
                .filter(f -> f instanceof RelationField)
                .map(f -> (RelationField) f)
                .collect(Collectors.toList());
    }

    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        setModel((ModelInf) parent);
    }

    /**
     * Finds the given fields.
     *
     * @param names The names of the fields.
     *
     * @return The fields that where found.
     */
    public FieldInf[] findFields(String[] names)
    {
        FieldInf[] fieldsArr = new FieldInf[names.length];
        int i = 0;
        for (String fieldName : names)
        {
            fieldsArr[i] = findField(fieldName);
            i++;
        }
        return fieldsArr;
    }

    /**
     * Finds the given field by its name.
     *
     * @param fieldName The name of the field.
     *
     * @return The field found, or null if it does not exists.
     */
    public FieldInf findField(String fieldName)
    {
        return getAllFields().stream()
                .filter(f -> f.getName().equalsIgnoreCase(fieldName))
                .findFirst()
                .orElse(null);
    }

}
