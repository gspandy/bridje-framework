<#include "utils.ftl" />
<@javaDocLicense model.license />

package ${model.packageName};

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * ${description!""}
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ${name} extends ${name}Base
{
}