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

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Information about a condition.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlTransient
public abstract class ConditionStmt
{
    private static final Logger LOG = Logger.getLogger(ConditionStmt.class.getName());

    @XmlAttribute
    private String condition;

    @XmlElements(
    {
        @XmlElement(name = "or", type = OrStmt.class),
        @XmlElement(name = "orNot", type = OrNotStmt.class),
        @XmlElement(name = "and", type = AndStmt.class),
        @XmlElement(name = "andNot", type = AndNotStmt.class)
    })
    private List<ConditionStmt> content;

    @XmlTransient
    private String operand1;

    @XmlTransient
    private String operator;

    @XmlTransient
    private String operand2;

    /**
     * If the condition is nagated.
     * 
     * @return true the condition has a not befor.
     */
    public abstract boolean isNot();

    /**
     * Gets the boolan operator used to join this condition with a previous one.
     * 
     * @return The boolan operator used to join this condition with a previous one.
     */
    public abstract String getBooleanOperator();

    /**
     * The query that this condition belongs to.
     * 
     * @return The query that this condition belongs to.
     */
    public abstract QueryInf getQuery();

    /**
     * 
     * 
     * @param fillParams 
     */
    public void fillParams(Map<String, FieldInf> fillParams)
    {
        try
        {
            if(getParam() != null && getField() != null) fillParams.put(getParam(), getField());
            if(content != null)
            {
                for (ConditionStmt conditionStmt : content)
                {
                    conditionStmt.fillParams(fillParams);
                }
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * 
     * @return 
     */
    public String getCondition()
    {
        return condition;
    }

    /**
     * 
     * @param condition 
     */
    public void setCondition(String condition)
    {
        this.condition = condition;
    }

    /**
     * 
     * @return 
     */
    public List<ConditionStmt> getContent()
    {
        return content;
    }

    /**
     * 
     * @param content 
     */
    public void setContent(List<ConditionStmt> content)
    {
        this.content = content;
    }
    
    /**
     * 
     * @return 
     */
    public FieldInf getField()
    {
        if(operand1 == null) readCondition();
        return getQuery().getEntity().findField(operand1);
    }

    /**
     * 
     * @return 
     */
    public String getParam()
    {
        if(operand2 == null) readCondition();
        if(operand2.startsWith("$")) return operand2.substring(1);
        return null;
    }

    /**
     * 
     * @return 
     */
    public String getValue()
    {
        if(operand2 == null) readCondition();
        if(operand2.startsWith("$")) return operand2.substring(1);
        return operand2;
    }

    /**
     * 
     * @return 
     */
    public String getOperatorMethod()
    {
        if(operator == null) readCondition();
        if(operator.equals("!=")) return "ne";
        if(operator.equals("<")) return "lt";
        if(operator.equals(">")) return "gt";
        return "eq";
    }

    /**
     * 
     * @return 
     */
    public String getCompareOperator()
    {
        if(operator == null) readCondition();
        return operator;
    }

    private void readCondition()
    {
        if(operator == null) readCondition("!=");
        if(operator == null) readCondition("=");
        if(operator == null) readCondition("<");
        if(operator == null) readCondition(">");
    }

    private void readCondition(String op)
    {
        String[] split = condition.split(op);
        if(split.length == 2)
        {
            operand1 = split[0].trim();
            operator = op;
            operand2 = split[1].trim();
        }
    }
}
