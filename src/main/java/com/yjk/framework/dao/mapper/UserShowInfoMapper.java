package com.yjk.framework.dao.mapper;

import com.yjk.framework.dao.entity.UserShowInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jkyu1
 * @since 2018-10-22
 */
public interface UserShowInfoMapper extends BaseMapper<UserShowInfo> {
       
	/**
	 *  Query DB table <tt>user_show_info</tt> for records.
	 *
   	 *  <p>
   	 *  Description for this operation is<br>
   	 *  <tt></tt>
	 *  <p>
	 *  The sql statement for this operation is <br>
	 *  <tt>select * from user_show_info where id =? and show_sex = ?</tt>
	 *
	 *	@param id
	 *	@param showSex
	 *	@return UserShowInfo
	 
	 */	 
    public UserShowInfo selectByUserId(@Param("id")Integer id ,@Param("showSex")String showSex );
       
	/**
	 *  Query DB table <tt>user_show_info</tt> for records.
	 *
   	 *  <p>
   	 *  Description for this operation is<br>
   	 *  <tt></tt>
	 *  <p>
	 *  The sql statement for this operation is <br>
	 *  <tt>select * from user_show_info where id =? and show_sex = ?</tt>
	 *
	 *	@param id
	 *	@param showSex
	 *	@return UserShowInfo
	 
	 */	 
    public UserShowInfo selectById(@Param("id")Integer id ,@Param("showSex")String showSex );
    
}
