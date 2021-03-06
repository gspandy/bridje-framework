<#include "./ObjectUtils.ftl" />

package ${object.package};

<#list model.includes![] as inc>
import ${inc.fullName};
</#list>
<#list object.includes![] as inc>
import ${inc.fullName};
</#list>
import javafx.beans.property.*;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.annotation.Generated;

/**
 * ${object.description!}
 */
@Generated(value = "org.bridje.jfx.srcgen.JFxSourceGenerator", date = "${.now?string("yyyy-MM-dd")}", comments = "Generated by Bridje JavaFx API")
public class ${object.name}<#if object.base??> extends ${object.base}</#if>
{
    <#list object.properties as property>
    private final ${property.propertyDec} ${property.name}Property = new ${property.propertyDimDec}();

    </#list>

    <#list object.properties as property>
    /**
     * Gets the property object for the ${property.name} field.
     * ${property.description!}
     *
     * @return The property objecto for the ${property.name} field
     */
    public ${property.propertyDec} ${property.name}Property()
    {
        return this.${property.name}Property;
    }

    /**
     * Gets the value of the ${property.name} field.
     * ${property.description!}
     *
     * @return The value of the ${property.name} field
     */
    public ${property.javaType} get${property.name?cap_first}()
    {
        return this.${property.name}Property.get();
    }

    /**
     * Sets the value of the ${property.name} field.
     * ${property.description!}
     *
     * @param ${property.name} The value of the ${property.name} field.
     */
    public void set${property.name?cap_first}(${property.javaType} ${property.name})
    {
        this.${property.name}Property.set(${property.name});
    }

    </#list>
    <#list object.mappings![] as mapping>
    public static ${object.name} from${mapping.target}(${mapping.target} object)
    {
        ${object.name} result = new ${object.name}();
        from${mapping.target}(object, result);
        return result;
    }

    public static ${mapping.target} to${mapping.target}(${object.name} object)
    {
        ${mapping.target} result = new ${mapping.target}();
        to${mapping.target}(object, result);
        return result;
    }

    public static void from${mapping.target}(${mapping.target} object, ${object.name} result)
    {
        <#if mapping.base?? && object.base??>
        ${object.base}.from${mapping.base}((${mapping.base})object, (${object.base})result);
        </#if>
        <#list mapping.properties![] as prop>
        <#if object.isList(prop.source)>
        <#if object.isObject(prop.source)>
        <#assign srcObject = object.findObject(prop.source) />
        result.set${prop.source?cap_first}(${srcObject.name}.from${prop.type!}List(object.get${prop.target?cap_first}()));
        <#else>
        result.set${prop.source?cap_first}(FXCollections.observableList(new ArrayList<>()));
        if(object.get${prop.target?cap_first}() != null) result.get${prop.source?cap_first}().addAll(object.get${prop.target?cap_first}());
        </#if>
        <#else>
        <#if object.isObject(prop.source)>
        <#assign srcObject = object.findObject(prop.source) />
        result.set${prop.source?cap_first}(${srcObject.name}.from${prop.type!}(object.get${prop.target?cap_first}()));
        <#else>
        result.set${prop.source?cap_first}(object.get${prop.target?cap_first}());
        </#if>
        </#if>
        </#list>
    }

    public static void to${mapping.target}(${object.name} object, ${mapping.target} result)
    {
        <#if mapping.base?? && object.base??>
        ${object.base}.to${mapping.base}((${object.base})object, (${mapping.base})result);
        </#if>
        <#list mapping.properties![] as prop>
        <#if object.isList(prop.source)>
        <#if object.isObject(prop.source)>
        <#assign srcObject = object.findObject(prop.source) />
        result.set${prop.target?cap_first}(${srcObject.name}.to${prop.type!}List((object.get${prop.source?cap_first}())));
        <#else>
        result.set${prop.source?cap_first}(new ArrayList<>());
        if(object.get${prop.source?cap_first}() != null) result.get${prop.source?cap_first}().addAll(object.get${prop.source?cap_first}());
        </#if>
        <#else>
        <#if object.isObject(prop.source)>
        <#assign srcObject = object.findObject(prop.source) />
        result.set${prop.target?cap_first}(${srcObject.name}.to${prop.type!}(object.get${prop.source?cap_first}()));
        <#else>
        result.set${prop.target?cap_first}(object.get${prop.source?cap_first}());
        </#if>
        </#if>
        </#list>
    }

    public static ObservableList<${object.name}> from${mapping.target}List(List<${mapping.target}> objects)
    {
        ObservableList<${object.name}> result = FXCollections.observableList(new ArrayList<>());
        if(objects != null)
        {
            for(${mapping.target} item : objects)
            {
                result.add(from${mapping.target}(item));
            }
        }
        return result;
    }

    public static List<${mapping.target}> to${mapping.target}List(ObservableList<${object.name}> objects)
    {
        List<${mapping.target}> result = new ArrayList<>();
        if(objects != null)
        {
            for(${object.name} item : objects)
            {
                result.add(to${mapping.target}(item));
            }
        }
        return result;
    }

    </#list>
    <#if object.keyProperty??>
    @Override
    public int hashCode()
    {
        if(get${object.keyProperty.name?cap_first}() == null)
        {
            return super.hashCode();
        }
        return get${object.keyProperty.name?cap_first}().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ${object.name} other = (${object.name}) obj;
        return Objects.equals(this.get${object.keyProperty.name?cap_first}(), other.get${object.keyProperty.name?cap_first}());
    }

    </#if>
    <#if object.toStringProperty??>
    @Override
    public String toString()
    {
        <#if object.toStringProperty.type != "String">
        if(get${object.toStringProperty.name?cap_first}() != null) return get${object.toStringProperty.name?cap_first}().toString();
        return null;
        <#else>
        return get${object.toStringProperty.name?cap_first}();
        </#if>
    }

    </#if>
}