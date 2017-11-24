<#ftl encoding="UTF-8">

package ${uisuite.package};

import java.util.Objects;
import javax.xml.bind.annotation.*;
import org.bridje.web.view.*;
import javax.annotation.Generated;

/**
 * Defines a meta information for the current view.
 */
@Generated(value = "org.bridje.web.srcgen.WebSourceGenerator", date = "${.now?string("yyyy-MM-dd")}", comments = "Generated by Bridje Web API")
@XmlAccessorType(XmlAccessType.FIELD)
public class ${uisuite.name}MetaTag implements MetaTag
{
    @XmlAttribute(name = "name")
    private String name;

    @XmlValue
    private String content;

    /**
     * The name of the meta tag.
     * 
     * @return The name of the meta tag.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of the meta tag.
     * 
     * @param name The name of the meta tag.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The content of the meta tag.
     * 
     * @return The content of the meta tag.
     */
    public String getContent()
    {
        return content;
    }

    /**
     * The name of the meta tag.
     * 
     * @param content The content of the meta tag.
     */
    public void setContent(String content)
    {
        this.content = content;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        final ${uisuite.name}MetaTag other = (${uisuite.name}MetaTag) obj;
        return Objects.equals(this.name, other.name);
    }
}
