/*
 * Copyright (c) www.bugull.com
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

package com.hoolai.bi.mongo.encoder;

import com.hoolai.bi.mongo.annotations.Id;
import com.hoolai.bi.mongo.cache.DaoCache;
import com.hoolai.bi.mongo.exception.IdException;
import com.hoolai.bi.mongo.access.InternalDao;
import com.hoolai.bi.mongo.utils.Operator;
import java.lang.reflect.Field;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

/**
 *
 * @author Frank Wen(xbwen@hotmail.com)
 */
public class IdEncoder extends AbstractEncoder{
    
    private final static Logger logger = LogManager.getLogger(IdEncoder.class.getName());
    
    private final Id id;
    
    public IdEncoder(Object obj, Field field){
        super(obj, field);
        id = field.getAnnotation(Id.class);
    }
    
    @Override
    public boolean isNullField(){
        return false;
    }
    
    @Override
    public String getFieldName(){
        return Operator.ID;
    }
    
    @Override
    public Object encode(){
        Object result = null;
        try{
            result = fixIdValue();
        }catch(IdException ex){
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }
    
    private Object fixIdValue() throws IdException {
        Object result = null;
        switch(id.type()){
            case AUTO_GENERATE:
                if(value == null){
                    result = new ObjectId();
                }else{
                    result = new ObjectId(value.toString());
                }
                break;
            case AUTO_INCREASE:
                if(value == null){
                    InternalDao dao = DaoCache.getInstance().get(clazz);
                    long max = dao.getMaxId();
                    if(max == 0){
                        result = id.start();
                    }else{
                        result = max + 1L;
                    }
                }else{
                    result = Long.parseLong(value.toString());
                }
                break;
            case USER_DEFINE:
                if(value == null){
                    throw new IdException("user-defined id doesn't have value!");
                }else{
                    result = value.toString();
                }
                break;
        }
        return result;
    }
    
}
