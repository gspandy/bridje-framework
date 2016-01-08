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

package org.bridje.ioc.impl;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.ClassRepository;
import org.bridje.ioc.IocContext;

class ContextImpl implements IocContext
{
    private static final Logger LOG = Logger.getLogger(ContextImpl.class.getName());
    
    private final String scope;

    private final ClassSet classSet;

    private final Instanciator creator;
    
    private final ServiceMap serviceMap;
    
    private final Container container;
    
    private final IocContext parent;
    
    public ContextImpl() throws IOException
    {
        this("APPLICATION");
    }
    
    private ContextImpl(String scope) throws IOException
    {
        this(scope, null, null);
    }
    
    private ContextImpl(String scope, Collection instances) throws IOException
    {
        this(scope, instances, null);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    private ContextImpl(String scope, Collection instances, IocContext parent) throws IOException
    {
        this.scope = scope;
        this.parent = parent;
        if(instances != null && !instances.isEmpty())
        {
            ClassSet instancesClassSet = new ClassSet(ClassUtils.findClasses(instances));
            classSet = new ClassSet(ClassSet.findByScope(scope), instancesClassSet);
            serviceMap = new ServiceMap(ServiceMap.findByScope(scope), instancesClassSet);
        }
        else
        {
            classSet = ClassSet.findByScope(scope);
            serviceMap = ServiceMap.findByScope(scope);
        }
        creator = new Instanciator(this, serviceMap);
        List allInstances = new ArrayList(instances != null ? instances.size() + 1 : 1);
        allInstances.add(this);
        if(instances != null)
        {
            allInstances.addAll(instances);
        }
        container = new Container(creator, allInstances);
    }

    @Override
    public <T> T find(Class<T> service)
    {
        T result = findInternal(service);
        if(result != null)
        {
            return result;
        }
        if(parent != null)
        {
            return parent.find(service);
        }
        return null;
    }

    @Override
    public <T> T findGeneric(Type service, Class<T> resultCls)
    {
        T result = findGenericInternal(service, resultCls);
        if(result != null)
        {
            return result;
        }
        if(parent != null)
        {
            return parent.findGeneric(service, resultCls);
        }
        return null;
    }

    @Override
    public <T> T findNext(Class<T> service, int priority)
    {
        return findNextGeneric(service, service, priority);
    }

    @Override
    public <T> T findNextGeneric(Type service, Class<T> resultCls, int priority)
    {
        T result = findNextGenericInternal(service, resultCls, priority);
        if(result != null)
        {
            return result;
        }
        if(parent != null)
        {
            return parent.findNextGeneric(service, resultCls, priority);
        }
        return null;
    }

    @Override
    public <T> T[] findAll(Class<T> service)
    {
        T[] result = findAllInternal(service);
        if(result.length > 0)
        {
            return result;
        }
        if(parent != null)
        {
            return parent.findAll(service);
        }
        return result;
    }

    @Override
    public <T> T[] findAllGeneric(Type service, Class<T> resultCls)
    {
        T[] result = findAllGenericInternal(service, resultCls);
        if(result.length > 0)
        {
            return result;
        }
        if(parent != null)
        {
            return parent.findAllGeneric(service, resultCls);
        }
        return result;
    }
    
    @Override
    public boolean existsComponent(Class cls)
    {
        if(classSet.contains(cls))
        {
            return true;
        }
        if(parent != null)
        {
            return parent.existsComponent(cls);
        }
        return false;
    }

    @Override
    public boolean exists(Type service)
    {
        if(serviceMap.exists(service))
        {
            return true;
        }
        if(parent != null)
        {
            return parent.exists(service);
        }
        return false;
    }

    @Override
    public IocContext getParent()
    {
        return parent;
    }

    @Override
    public IocContext createChild(String scope)
    {
        return createChild(scope, null);
    }

    @Override
    public IocContext createChild(String scope, Collection instances)
    {
        try
        {
            return new ContextImpl(scope, instances, this);
        }
        catch(Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    private <T> T findInternal(Class<T> service)
    {
        Class<? extends T> component = serviceMap.findOne(service);
        if(component != null)
        {
            return container.create(component);
        }
        return null;
    }

    private <T> T findGenericInternal(Type service, Class<T> resultCls)
    {
        Class component = serviceMap.findOne(service);
        if(component != null)
        {
            return (T)container.create(component);
        }
        return null;
    }

    private <T> T findNextGenericInternal(Type service, Class<T> resultCls, int priority)
    {
        Class component = serviceMap.findOne(service, priority);
        if(component != null)
        {
            return (T)container.create(component);
        }
        return null;
    }
    
    private <T> T[] findAllInternal(Class<T> service)
    {
        List<Class<?>> components = serviceMap.findAll(service);
        if(components != null)
        {
            ArrayList resultList = new ArrayList(components.size());
            for (Class component : components)
            {
                Object compInstance = container.create(component);
                if(compInstance != null)
                {
                    resultList.add(compInstance);
                }
            }
            T[] result = (T[])Array.newInstance(service, components.size());
            return (T[])resultList.toArray(result);
        }
        return (T[])Array.newInstance(service, 0);
    }
    
    private <T> T[] findAllGenericInternal(Type service, Class<T> resultClass)
    {
        List<Class<?>> components = serviceMap.findAll(service);
        if(components != null)
        {
            List resultList = new ArrayList(components.size());
            for (Class<?> component : components)
            {
                Object compInstance = container.create(component);
                if(compInstance != null)
                {
                    resultList.add(compInstance);
                }
            }
            T[] result = (T[])Array.newInstance(resultClass, components.size());
            return (T[])resultList.toArray(result);
        }
        return (T[])Array.newInstance(resultClass, 0);
    }

    @Override
    public ClassRepository getClassRepository()
    {
        return classSet;
    }

    @Override
    public String getScope()
    {
        return scope;
    }

    @Override
    public String toString()
    {
        return "IocContext: " + scope;
    }
}
