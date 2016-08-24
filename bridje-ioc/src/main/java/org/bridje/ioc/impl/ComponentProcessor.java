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

import java.io.IOException;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.type.MirroredTypeException;
import org.bridje.ioc.CompFileAnnotProcHelper;
import org.bridje.ioc.Component;

/**
 * Annotations processor for the {@link Component} annotation.
 */
@SupportedAnnotationTypes("org.bridje.ioc.Component")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ComponentProcessor extends CompFileAnnotProcHelper
{
    public static final String COMPONENTS_RESOURCE_FILE = "BRIDJE-INF/ioc/components.properties";

    @Override
    public String getFileName()
    {
        return COMPONENTS_RESOURCE_FILE;
    }

    @Override
    public void processElement(Element element) throws IOException
    {
        //Get the @Component annotation for the current element.
        Component annot = element.getAnnotation(Component.class);
        String clsName = element.toString();
        String scope = "";
        try
        {
            annot.scope();
        }
        catch (MirroredTypeException e)
        {
            scope = e.getTypeMirror().toString();
        }
        appendClass(clsName, scope);        
    }
}
