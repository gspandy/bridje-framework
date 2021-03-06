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

package org.bridje.orm;

import java.sql.SQLException;
import java.util.List;
import org.bridje.sql.Query;
import org.bridje.sql.SQL;

public class UsersModel extends UsersModelBase
{
    public List<User> findUsers(Paging paging) throws SQLException
    {
        Query query = SQL.select(User.TABLE.getColumns())
                        .from(User.TABLE)
                        .limit(paging.toLimit())
                        .toQuery();
        return env.fetchAll(query, this::parseUser);
    }

    public void saveUser(User user) throws SQLException
    {
        doSaveUser(user);
    }

    public void insertGroup(Group group) throws SQLException
    {
        doInsertGroup(group);
    }

    public void saveUserGroup(UserGroup userGroup) throws SQLException
    {
        doSaveUserGroup(userGroup);
    }
}
