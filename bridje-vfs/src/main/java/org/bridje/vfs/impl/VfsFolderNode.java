
package org.bridje.vfs.impl;

import org.bridje.vfs.GlobExpr;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VfsSource;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class VfsFolderNode extends VfsNode
{
    private final List<VfsNode> childs = new ArrayList<>();

    private final Map<String, VfsNode> childsMap = new HashMap<>();

    public VfsFolderNode(String name)
    {
        super(name);
    }

    public Iterable<VfsNode> getChilds()
    {
        return childs;
    }

    public VfsNode getChild(String name)
    {
        return childsMap.get(name);
    }

    public void addChild(VfsNode node)
    {
        node.setParent(this);
        childs.add(node);
        childsMap.put(node.getName(), node);
    }

    public void removeChild(VfsNode node)
    {
        node.setParent(this);
        childs.remove(node);
        childsMap.remove(node.getName());
    }

    public void mount(Path path, VfsSource source) throws FileNotFoundException
    {
        if(path == null || path.isRoot()) throw new FileNotFoundException("Could not mount the source in this folder.");
        if(path.isLast())
        {
            if(getChild(path.getName()) == null)
            {
                addChild(new VfsSourceNode(path.getName(), source));
            }
            else
            {
                throw new FileNotFoundException("Could not mount the source in " + getPath() + " folder.");
            }
        }
        else
        {
            String first = path.getFirstElement();
            VfsNode child = getChild(first);
            if(child == null)
            {
                child = new VfsFolderNode(first);
                addChild(child);
                ((VfsFolderNode)child).mount(path.getNext(), source);
            }
            else
            {
                if(child instanceof VfsFolderNode)
                {
                    ((VfsFolderNode)child).mount(path.getNext(), source);
                }
                else
                {
                    throw new FileNotFoundException("Could not find the folder.");
                }
            }
        }
    }

    @Override
    public boolean isDirectory(Path path)
    {
        if(path == null || path.isRoot())return true;
        VfsNode child = getChild(path.getFirstElement());
        if(child == null) return false;
        return child.isDirectory(path.getNext());
    }

    @Override
    public boolean isFile(Path path)
    {
        if(path.isLast()) return false;
        String first = path.getFirstElement();
        VfsNode child = getChild(first);
        if(child == null) return false;
        return child.isFile(path.getNext());
    }

    @Override
    public boolean exists(Path path)
    {
        if(path.isLast()) return getChild(path.getName()) != null;
        String first = path.getFirstElement();
        VfsNode child = getChild(first);
        if(child == null) return false;
        return child.exists(path.getNext());
    }

    @Override
    public boolean canWrite(Path path)
    {
        if(path == null || path.isRoot())return false;
        String first = path.getFirstElement();
        VfsNode child = getChild(first);
        return child.canWrite(path.getNext());
    }

    @Override
    public boolean canRead(Path path)
    {
        if(path == null || path.isRoot()) return false;
        String first = path.getFirstElement();
        VfsNode child = getChild(first);
        return child.canRead(path.getNext());
    }

    @Override
    public String[] list(Path path)
    {
        if(path == null)
        {
            String[] result = new String[childs.size()];
            childs.stream().map(c -> c.getName()).collect(Collectors.toList()).toArray(result);
            return result;
        }
        else
        {
            String first = path.getFirstElement();
            VfsNode child = getChild(first);
            return child.list(path.getNext());
        }
    }

    @Override
    public InputStream openForRead(Path path)
    {
        if(path == null || path.isRoot()) return null;
        String first = path.getFirstElement();
        VfsNode child = getChild(first);
        if(child == null) return null;
        return child.openForRead(path.getNext());
    }

    @Override
    public OutputStream openForWrite(Path path)
    {
        if(path == null || path.isRoot()) return null;
        String first = path.getFirstElement();
        VfsNode child = getChild(first);
        if(child == null) return null;
        return child.openForWrite(path.getNext());
    }

    @Override
    public VFile[] search(GlobExpr globExpr, Path path)
    {
        if(path == null || path.isRoot()) return null;
        VfsNode child = getChild(path.getFirstElement());
        if(child == null) return null;
        return child.search(globExpr, path.getNext());
    }

    @Override
    public boolean createNewFile(Path path)
    {
        if(path == null || path.isRoot()) return false;
        VfsNode child = getChild(path.getFirstElement());
        if(child == null) return false;
        return child.createNewFile(path.getNext());
    }

    @Override
    public boolean delete(Path path)
    {
        if(path == null || path.isRoot()) return false;
        VfsNode child = getChild(path.getFirstElement());
        if(child == null) return false;
        return child.createNewFile(path.getNext());
    }

    @Override
    public boolean mkdir(Path path)
    {
        if(path == null || path.isRoot()) return false;
        VfsNode child = getChild(path.getFirstElement());
        if(child == null) return false;
        return child.mkdir(path.getNext());
    }
}
