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

@XmlAccessorType(XmlAccessType.FIELD)
public class SelectQueryInf extends QueryInf
{
    @XmlAttribute
    private boolean withPaging;

    private WhereStmt where;

    @XmlTransient
    private EntityInf entity;
    
    @XmlAttribute
    private String fetch;

    public boolean isWithPaging()
    {
        return withPaging;
    }

    public void setWithPaging(boolean withPaging)
    {
        this.withPaging = withPaging;
    }

    @Override
    public String getQueryType()
    {
        return "select";
    }

    public WhereStmt getWhere()
    {
        return where;
    }

    public void setWhere(WhereStmt where)
    {
        this.where = where;
    }

    public FieldInf getFetchField()
    {
        if(fetch == null) return null;
        return getEntity().findField(fetch);
    }

    public String getFetch()
    {
        return fetch;
    }

    public void setFetch(String fetch)
    {
        this.fetch = fetch;
    }

    @Override
    public EntityInf getEntity()
    {
        return entity;
    }

    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        entity = (EntityInf)parent;
    }
}
