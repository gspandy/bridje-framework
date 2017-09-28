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

package org.bridje.sql;

import java.sql.JDBCType;

public class Group
{
    public static final Table TABLE = SQL.buildTable("groups")
                                            .number("id", SQL.buildType(Long.class, JDBCType.BIGINT), true, false, true, true, null)
                                            .string("title", SQL.buildType(String.class, JDBCType.VARCHAR, 150), false, true, true, null)
                                            .build();

    public static final NumberColumn<Long> ID = TABLE.getNumberColumn("id", Long.class);

    public static final StringColumn<String> TITLE = TABLE.getStringColumn("title", String.class);
}
