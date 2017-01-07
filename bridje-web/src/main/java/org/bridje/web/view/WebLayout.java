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

package org.bridje.web.view;

import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.*;

/**
 * Represents a view of the application, views are render by themes and are
 * composed from controls. The views are inmutables so once defined they will
 * stay the same at runtime.
 */
@XmlRootElement(name = "layout")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebLayout extends AbstractWebView
{
    @XmlElements(
        @XmlElement(name = "meta", type = MetaTag.class)
    )
    private List<MetaTag> metaTags;

    /**
     * Gets a list of meta information tags information to be rendered with views that extends this layout.
     *
     * @return A list of meta information tags assigned to views.
     */
    public List<MetaTag> getMetaTags()
    {
        if (metaTags == null)
        {
            metaTags = Collections.emptyList();
        }
        return metaTags;
    }
}
