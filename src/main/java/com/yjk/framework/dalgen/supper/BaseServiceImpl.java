package com.yjk.framework.dalgen.supper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BaseServiceImpl<T> implements BaseService<T> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
	@Override
	public List<T> selectList(Object o) throws BusinessException {
		return null;
	}

	@Override
	public T selectByPrimaryKey(Integer id) throws BusinessException {
		return null;
	}

	@Override
	public Integer insert(T t) throws BusinessException {
		return null;
	}

	@Override
	public Integer deleteByPrimaryKey(T t) throws BusinessException {
		return null;
	}

	@Override
	public Integer updateByPrimaryKey(T t) throws BusinessException {
		return null;
	}

}
