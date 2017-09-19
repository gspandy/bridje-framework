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

package org.bridje.web.srcgen.editor;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.bridje.jfx.binding.BiContentConverter;
import org.bridje.jfx.binding.ExBindings;

public final class FtlIncludesTreeItem extends TreeItem<Object>
{
    private final ObservableList<String> includes;

    public FtlIncludesTreeItem(ObservableList<String> includes)
    {
        super("FTL Includes", UISuitesModel.ftlInclude(16));
        this.includes = includes;
        ExBindings.bindContentBidirectional(getChildren(), includes, new BiContentConverter<TreeItem<Object>, String>()
        {
            @Override
            public String convertFrom(TreeItem<Object> value)
            {
                return (String)value.getValue();
            }

            @Override
            public TreeItem<Object> convertTo(String value)
            {
                return new TreeItem<>(value, UISuitesModel.ftlInclude(16));
            }
        });
    }
}
