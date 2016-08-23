/*
 * Copyright 2015 Bridje Framework.
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

package org.bridje.vfs.impl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Ioc;
import org.bridje.vfs.Path;
import org.bridje.vfs.VfsSource;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileVisitor;
import org.bridje.vfs.VFolder;
import org.bridje.vfs.VFolderVisitor;

class PhysicalFolder extends PhysicalResource implements VFolder
{
    private static final Logger LOG = Logger.getLogger(PhysicalFolder.class.getName());

    public PhysicalFolder(VfsSource source, Path mountPath, Path path)
    {
        super(source, mountPath, path);
    }

    @Override
    public VFolder findFolder(String path)
    {
        return findFolder(new Path(path));
    }

    @Override
    public VFolder findFolder(Path path)
    {
        try
        {
            if(getSource().folderExists(getPhysicalPath(path)))
            {
                return instantiateChildFolder(path);
            }
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public VFile findFile(String path)
    {
        return findFile(new Path(path));
    }

    @Override
    public VFile findFile(Path path)
    {
        try
        {
            if(getSource().fileExists(getPhysicalPath(path)))
            {
                return instantiateChildFile(path);
            }
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<VFolder> listFolders()
    {
        return listFolders(null);
    }

    @Override
    public List<VFile> listFiles()
    {
        return listFiles(null);
    }
    
    @Override
    public List<VFolder> listFolders(String query)
    {
        List<VFolder> result = new LinkedList<>();
        try
        {
            if(getSource().folderExists(getPhysicalPath()))
            {
                List<String> folders = getSource().listFolders(getPhysicalPath());
                if(folders != null)
                {
                    for (String folder : folders)
                    {
                        VFolder vf = instantiateChildFolder(new Path(folder));
                        if(vf != null && (query == null 
                                || (vf.getPath().globMatches(query))))
                        {
                            result.add(vf);
                        }
                    }
                }
            }
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<VFile> listFiles(String query)
    {
        List<VFile> result = new LinkedList<>();
        try
        {
            if(getSource().folderExists(getPhysicalPath()))
            {
                List<String> files = getSource().listFiles(getPhysicalPath());
                if(files != null)
                {
                    for (String file : files)
                    {
                        VFile vf = instantiateChildFile(new Path(file));
                        if(vf != null && (query == null 
                                || vf.getPath().globMatches(query)))
                        {
                            result.add(vf);
                        }
                    }
                }
            }
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }
    
    private VFolder instantiateChildFolder(Path path)
    {
        return new PhysicalFolder(getSource(), getMountPath(), getRelativePath().join(path));
    }

    private VFile instantiateChildFile(Path path)
    {
        try
        {
            Object[] files = getSource().getFiles(getPhysicalPath(path));
            if(files != null && files.length > 0)
            {
                if(files.length == 1)
                {
                    return new PhysicalFile(files[0], getSource(), getMountPath(), getRelativePath().join(path));
                }
                if(files.length > 1)
                {
                    ProxyFile pf = new ProxyFile();
                    for (Object file : files)
                    {
                        pf.add(new PhysicalFile(file, getSource(), getMountPath(), getRelativePath().join(path)));
                    }
                    return pf;
                }
            }
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public <T> T readFile(String path, Class<T> resultCls) throws IOException
    {
        VFile file = findFile(path);
        return Ioc.context().find(VfsServiceImpl.class).readFile(file, resultCls);
    }

    @Override
    public <T> T readFile(Path path, Class<T> resultCls) throws IOException
    {
        VFile file = findFile(path);
        return Ioc.context().find(VfsServiceImpl.class).readFile(file, resultCls);
    }

    @Override
    public <T> void writeFile(String path, T contentObj) throws IOException
    {
        VFile file = findFile(path);
        Ioc.context().find(VfsServiceImpl.class).writeFile(file, contentObj);
    }

    @Override
    public <T> void writeFile(Path path, T contentObj) throws IOException
    {
        VFile file = findFile(path);
        Ioc.context().find(VfsServiceImpl.class).writeFile(file, contentObj);
    }

    @Override
    public void travel(VFileVisitor visitor)
    {
        travel(this, visitor);
    }

    @Override
    public void travel(VFolderVisitor visitor)
    {
        travel(this, visitor);
    }

    @Override
    public void travel(VFileVisitor visitor, String query)
    {
        travel(this, visitor, query);
    }

    @Override
    public void travel(VFolderVisitor visitor, String query)
    {
        travel(this, visitor, query);
    }

    @Override
    public VFile createNewFile(String filePath) throws IOException
    {
        return createNewFile(new Path(filePath));
    }

    @Override
    public VFolder mkDir(String folderPath) throws IOException
    {
        return mkDir(new Path(folderPath));
    }

    @Override
    public boolean canCreateNewFile(String filePath)
    {
        return canCreateNewFile(new Path(filePath));
    }

    @Override
    public boolean canMkDir(String folderPath)
    {
        return canMkDir(new Path(folderPath));
    }
        
    @Override
    public VFile createNewFile(Path filePath) throws IOException
    {
        Object file = getSource().createNewFile(getPhysicalPath(filePath));
        return new PhysicalFile(file, getSource(), getMountPath(), getRelativePath().join(filePath));
    }

    @Override
    public VFolder mkDir(Path folderPath) throws IOException
    {
        String folder = getSource().mkDir(getPhysicalPath(folderPath));
        return new PhysicalFolder(getSource(), getMountPath(), getRelativePath().join(folder));
    }

    @Override
    public boolean canCreateNewFile(Path filePath)
    {
        return getSource().canCreateNewFile(getPhysicalPath(filePath));
    }

    @Override
    public boolean canMkDir(Path folderPath)
    {
        return getSource().canMkDir(getPhysicalPath(folderPath));
    }

    @Override
    public <T> VFile createAndWriteNewFile(Path filePath, T contentObj) throws IOException
    {
        VFile file = createNewFile(filePath);
        if(!file.canOpenForWrite())
        {
            throw new IOException("The file cannot be open for writing");
        }
        writeFile(filePath, contentObj);
        return file;
    }

    @Override
    public <T> VFile createAndWriteNewFile(String filePath, T contentObj) throws IOException
    {
        return createAndWriteNewFile(new Path(filePath), contentObj);
    }
}
