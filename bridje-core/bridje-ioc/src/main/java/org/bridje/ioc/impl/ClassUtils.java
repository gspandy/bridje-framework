/*
 * Copyright 2015 Bridje Framework.
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

package org.bridje.ioc.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/**
 *
 * @author Gilberto
 */
public class ClassUtils
{

    public static Class findClassFromType(Type supClass)
    {
        if(supClass instanceof Class)
        {
            return ((Class)supClass);
        }
        else if(supClass instanceof ParameterizedType)
        {
            Type rawType = ((ParameterizedType)supClass).getRawType();
            if(rawType instanceof Class)
            {
                return ((Class)rawType);
            }
        }
        return null;
    }    
}