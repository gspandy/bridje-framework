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

package org.bridje.sql.dialect;

import java.sql.JDBCType;
import java.util.List;
import org.bridje.sql.Column;
import org.bridje.sql.Table;

public class MySQLDialect implements SQLDialect
{
    @Override
    public void writeObjectName(StringBuilder builder, String name)
    {
        builder.append('`');
        builder.append(name);
        builder.append('`');
    }

    @Override
    public void writeLimit(StringBuilder builder, int offset, int count)
    {
        builder.append(" LIMIT ");
        builder.append(offset);
        if(count > 0)
        {
            builder.append(", ");
            builder.append(count);
        }
    }

    @Override
    public void createTable(StringBuilder builder, Table table)
    {
        builder.append("CREATE TABLE ");
        writeObjectName(builder, table.getName());
        builder.append(" (\n");
    }

    @Override
    public void createColumn(StringBuilder builder, List<Object> params, Column<?> column, boolean isKey)
    {
        builder.append(" ");
        writeObjectName(builder, column.getName());
        builder.append(" ");
        builder.append(createType(column));
        builder.append(createIsNull(column, isKey));
        builder.append(createDefault(column, isKey, params));
        builder.append(createAutoIncrement(column));
        builder.append(",\n");
    }

    @Override
    public void primaryKey(StringBuilder builder, Column<?> column)
    {
        builder.append(" PRIMARY KEY (");
        writeObjectName(builder, column.getName());
        builder.append(")\n)");
    }

    @Override
    public void alterTable(StringBuilder builder, Table table)
    {
        builder.append("ALTER TABLE ");
        writeObjectName(builder, table.getName());
        builder.append(" \n");
    }

    @Override
    public void addColumn(StringBuilder builder, List<Object> params, Column<?> column, boolean isLast)
    {
        builder.append(" ADD COLUMN ");
        writeObjectName(builder, column.getName());
        builder.append(" ");
        builder.append(createType(column));
        builder.append(createIsNull(column, false));
        builder.append(createDefault(column, false, params));
        builder.append(createAutoIncrement(column));
        if(!isLast) builder.append(",");
        builder.append("\n");
    }

    @Override
    public void dropColumn(StringBuilder builder, Column<?> column, boolean isLast)
    {
        builder.append(" DROP COLUMN ");
        writeObjectName(builder, column.getName());
        if(!isLast) builder.append(",");
        builder.append("\n");
    }

    @Override
    public void changeColumn(StringBuilder builder, List<Object> params, Column<?> column, String oldColumn, boolean isLast)
    {
        builder.append(" CHANGE COLUMN ");
        writeObjectName(builder, oldColumn);
        builder.append(" ");
        writeObjectName(builder, column.getName());
        builder.append(" ");
        builder.append(createType(column));
        builder.append(createIsNull(column, false));
        builder.append(createDefault(column, false, params));
        builder.append(createAutoIncrement(column));
        if(!isLast) builder.append(",");
        builder.append("\n");
    }

    private String createType(Column<?> column)
    {
        switch(column.getJdbcType())
        {
            case BIT:
            case TINYINT:
            case SMALLINT:
            case INTEGER:
            case BIGINT:
                if(column.getLength() > 0)
                {
                    return column.getJdbcType().getName() + "(" + column.getLength() + ")";
                }
                break;
            case FLOAT:
            case DOUBLE:
            case DECIMAL:
                if(column.getLength() > 0 && column.getPresicion() > 0)
                {
                    return column.getJdbcType().getName() + "(" + column.getLength() + ", " + column.getPresicion() + ")";
                }
                break;
            case VARCHAR:
            case NVARCHAR:
                if(column.getLength() > 21844)
                {
                    return "TEXT";
                }
                if(column.getLength() > 65535)
                {
                    return "MEDIUMTEXT";
                }
                if(column.getLength() > 16777215)
                {
                    return "LONGTEXT";
                }
                if(column.getLength() > 0)
                {
                    return "VARCHAR(" + column.getLength() + ")";
                }
                return "VARCHAR";
            case CHAR:
            case NCHAR:
                if(column.getLength() > 0)
                {
                    return "CHAR(" + column.getLength() + ")";
                }
                return "CHAR";
            case LONGNVARCHAR:
            case LONGVARCHAR:
                return "LONGTEXT";
            default:
                break;
        }
        return column.getJdbcType().getName();
    }

    private String createIsNull(Column<?> column, boolean isKey)
    {
        if(column.isAllowNull() && !isKey) return " NULL";
        return " NOT NULL";
    }

    private String createDefault(Column<?> column, boolean isKey, List<Object> params)
    {
        if(column.getDefValue() != null)
        {
            params.add(isKey);
            return "DEFAULT ?";
        }
        if(isKey) return "";
        String def;
        if(column.getJdbcType()== JDBCType.TIMESTAMP
                || column.getJdbcType() == JDBCType.TIMESTAMP_WITH_TIMEZONE)
        {
            def = "'0000-00-00 00:00:00'";
        }
        else
        {
            def = "NULL";
        }
        return " DEFAULT " + def;
    }

    private String createAutoIncrement(Column<?> column)
    {
        if(column.isAutoIncrement()) return " AUTO_INCREMENT";
        return "";
    }
}