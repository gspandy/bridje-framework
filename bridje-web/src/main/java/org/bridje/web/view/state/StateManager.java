/*
 * Copyright 2016 Bridje Framework.
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

package org.bridje.web.view.state;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.el.ElService;
import org.bridje.http.HttpReqParam;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.IocContext;
import org.bridje.web.WebScope;
import org.bridje.web.view.ViewUtils;

/**
 * The manager for the states of the web views, this component can create the
 * state map out of the given web request context.
 */
@Component
public class StateManager
{
    private static final Logger LOG = Logger.getLogger(StateManager.class.getName());

    @Inject
    private ElService elServ;

    private Map<Class<?>, Map<Field, String>> stateFields;

    /**
     * Creates a new map with the current state of the web request. All the
     * components that were created during the request that have fields annoted
     * as StateFields will be serialized to the result map, so it can be
     * persisted across multiple HTTP requests.
     *
     * @param ctx The current web request context.
     *
     * @return A map with the names of the fields and the serialized values.
     */
    public Map<String, String> createViewState(IocContext<WebScope> ctx)
    {
        if (stateFields == null) initStateFields(ctx);
        Map<Class<?>, Object> comps = ctx.find(StateListener.class).getStateComps();
        Map<String, String> result = new LinkedHashMap<>();
        if (comps != null) comps.forEach((c, i) -> fillStateValues(c, i, result));
        return result;
    }
    
    /**
     * 
     * @param map
     * @return 
     */
    public String toStateString(Map<String, String> map)
    {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry : entrySet)
        {
            try
            {
                if(!first) sb.append("&");
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()));
                first = false;
            }
            catch (Exception e)
            {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return sb.toString();
    }
    
    public String createStringViewState(IocContext<WebScope> ctx)
    {
        Map<String, String> map = createViewState(ctx);
        return toStateString(map);
    }
    
    private void fillStateValues(Class<?> comp, Object inst, Map<String, String> stateValues)
    {
        Map<Field, String> map = stateFields.get(comp);
        if (map != null) map.forEach((f, s) -> fillStateValues(inst, f, s, stateValues));
    }
    
    private void fillStateValues(Object inst, Field field, String state, Map<String, String> stateValues)
    {
        try
        {
            Object val = field.get(inst);
            String cv = elServ.convert(val, String.class);
            if (cv != null) stateValues.put(state, cv);
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Injects the state of the HTTP request send to the server, into the given
     * component.
     *
     * @param ctx      The IocContext for the web request.
     * @param clazz    The component class.
     * @param inst     The component instance.
     * @param req      The web request scope.
     */
    protected void injectState(IocContext<WebScope> ctx, Class<Object> clazz, Object inst, WebScope req)
    {
        if (stateFields == null) initStateFields(ctx);
        Map<Field, String> lst = stateFields.get(clazz);
        if (lst != null) lst.forEach((field, stateName) -> injectState(inst, field, stateName, req));
    }

    private void injectState(Object inst, Field field, String stateName, WebScope req)
    {
        try
        {
            String value = req.getStateValue(stateName);
            if (value != null)
            {
                Object cv = elServ.convert(value, field.getType());
                if (cv != null) field.set(inst, cv);
            }
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private String findStateFieldName(Class<Object> clazz, Field field)
    {
        return ViewUtils.simplifyParam(clazz.getName() + "." + field.getName());
    }

    private synchronized void initStateFields(IocContext<WebScope> ctx)
    {
        if (stateFields == null)
        {
            Map<Class<?>, Map<Field, String>> result = new HashMap<>();
            ctx.getClassRepository().forEachField(StateField.class, (Field field, Class component, StateField annot) ->
            {
                Map<Field, String> lst = result.get(component);
                if (lst == null)
                {
                    lst = new HashMap<>();
                    result.put(component, lst);
                }
                field.setAccessible(true);
                String name = findStateFieldName(component, field);
                while(lst.containsValue(name)) name += "_";
                lst.put(field, name);
            });
            stateFields = result;
        }
    }

}
