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

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The information for an index.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EntityIndexInf
{
    @XmlAttribute
    private String name;

    @XmlTransient
    private final Boolean unique;

    @XmlAttribute
    private String fields;

    @XmlTransient
    private EntityInf entity;

    /**
     * Default constructor.
     */
    public EntityIndexInf()
    {
        unique = false;
    }

    /**
     * Constructor with the unique type of the index.
     * 
     * @param unique If the index is unique or not.
     */
    public EntityIndexInf(Boolean unique)
    {
        this.unique = unique;
    }

    /**
     * The name of the index.
     * 
     * @return The name of the index.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of the index.
     * 
     * @param name The name of the index.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * If the index is unique.
     * 
     * @return true if the index is unique, false otherwise.
     */
    public Boolean getUnique()
    {
        return unique;
    }

    /**
     * The entity that this index belongs to.
     * 
     * @return The entity that this index belongs to.
     */
    public EntityInf getEntity()
    {
        return entity;
    }

    /**
     * The entity that this index belongs to.
     * 
     * @param entity The entity that this index belongs to.
     */
    public void setEntity(EntityInf entity)
    {
        this.entity = entity;
    }

    /**
     * The array of fields for this index.
     * 
     * @return The array of fields for this index.
     */
    public FieldInf[] getFields()
    {
        if(fields == null) return null;
        String[] cols = fields.split(",");
        return entity.findFields(cols);
    }

    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        entity = (EntityInf)parent;
    }
}
