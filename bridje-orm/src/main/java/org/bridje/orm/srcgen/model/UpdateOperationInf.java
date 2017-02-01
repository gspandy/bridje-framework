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

package org.bridje.orm.srcgen.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * This class represents an update operation for an Entity, The update operation
 * will be added to the ORM model class in the source code generation.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateOperationInf extends ParametizedOperationInf
{
    @Override
    public OperationType getOperationType()
    {
        return OperationType.UPDATE;
    }

    @Override
    public OperationInfBase clone(EntityInfBase entity)
    {
        SaveOperationInf result = new SaveOperationInf();
        clone(result, entity);
        return result;
    }
}
