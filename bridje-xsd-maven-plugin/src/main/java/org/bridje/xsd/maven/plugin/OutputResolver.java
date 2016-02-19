package org.bridje.xsd.maven.plugin;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

/**
 * xsd schema output resolver fo the result xsd file.
 */
public class OutputResolver extends SchemaOutputResolver
{
    private final String baseDir;

    private String schemaName;

    public OutputResolver(String baseDir)
    {
        if (baseDir.endsWith(File.separator))
        {
            baseDir = baseDir.substring(0, baseDir.length() - File.separator.length());
        }

        this.baseDir = baseDir;
    }

    @Override
    public Result createOutput(String nsUri, String fileName) throws IOException
    {
        if(schemaName == null || schemaName.isEmpty())
        {
            setSchemaName(fileName);
        }
        String path = baseDir + File.separator + schemaName;
        File file = new File(path);
        if (file.exists())
        {
            file.delete();
        }

        file.createNewFile();
        
        return new StreamResult(file);
    }

    public String getSchemaName()
    {
        return schemaName;
    }
    
    public void setSchemaName(String schemaName)
    {
        if (schemaName == null || schemaName.isEmpty())
        {
            return;
        }
        if (!schemaName.endsWith(".xsd"))
        {
            schemaName += ".xsd";
        }

        this.schemaName = schemaName.substring(0, 1).toLowerCase() + schemaName.substring(1);
    }
}