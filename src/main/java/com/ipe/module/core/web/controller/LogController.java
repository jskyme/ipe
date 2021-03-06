package com.ipe.module.core.web.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ipe.common.util.ZipUtil;
import com.ipe.module.core.service.LogService;
import com.ipe.module.core.web.util.BodyWrapper;
import com.ipe.module.core.web.util.RestRequest;
import com.ipe.module.core.web.util.WebUtil;

/**
 * Created with IntelliJ IDEA.
 * Role: tangdu
 * Date: 13-9-7
 * Time: 下午10:27
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/log")
public class LogController extends AbstractController {

    @Autowired
    private LogService logService;

    /**
     * 登录日志
     * @param LOGGER
     * @param rest
     * @return
     */
    @RequestMapping(value = {"/loginlist"})
    public
    @ResponseBody
    BodyWrapper loginList(String params, RestRequest rest) {
        try {
        	if(StringUtils.isNotBlank(params)){
        		JSONObject p=JSONObject.parseObject(params);
        		List<Object> parry=new ArrayList<Object>();
        		parry.add("L1");
        		StringBuffer hql=new StringBuffer("and  logType=?");
        		if(p.containsKey("accessPerson") && StringUtils.isNotBlank(p.getString("accessPerson"))){
        			hql.append(" and accessPerson like ?");
        			parry.add("%"+p.getString("accessPerson"));
        		}
        		if(p.containsKey("operate")&& StringUtils.isNotBlank(p.getString("operate"))){
        			hql.append(" and operate like ?");
        			parry.add("%"+p.getString("operate"));
        		}
        		if(p.containsKey("accessIp")&& StringUtils.isNotBlank(p.getString("accessIp"))){
        			hql.append(" and accessIp like ?");
        			parry.add("%"+p.getString("accessIp"));
        		}
        		if(p.containsKey("accessTimeStart")&& StringUtils.isNotBlank(p.getString("accessTimeStart"))){//
        			hql.append(" and accessTime >= ?");
        			parry.add(p.getDate("accessTimeStart"));
        		}
        		if(p.containsKey("accessTimeEnd")&& StringUtils.isNotBlank(p.getString("accessTimeEnd"))){
        			hql.append(" and accessTime < ?");
        			parry.add(p.getDate("accessTimeEnd"));
        		}
        		if(p.containsKey("leaveTimeStart")&& StringUtils.isNotBlank(p.getString("leaveTimeStart"))){//
        			hql.append(" and leaveTime >= ?");
        			parry.add(p.getDate("leaveTimeStart"));
        		}
        		if(p.containsKey("leaveTimeEnd")&& StringUtils.isNotBlank(p.getString("leaveTimeEnd"))){
        			hql.append(" and leaveTime < ?");
        			parry.add(p.getDate("leaveTimeEnd"));
        		}
                logService.where(rest.getPageModel(),hql.toString(),parry);
        	}else{
                logService.where(rest.getPageModel()," and  logType=?","L1");
        	}
        	return success(rest.getPageModel());
        } catch (Exception e) {
            LOGGER.error("query error", e);
            return failure(e);
        }
    }

    /**
     * 操作日志
     * @param LOGGER
     * @param rest
     * @return
     */
    @RequestMapping(value = {"/buslist"})
    public
    @ResponseBody
    BodyWrapper busList(String params, RestRequest rest) {
        try {
        	if(StringUtils.isNotBlank(params)){
        		JSONObject p=JSONObject.parseObject(params);
        		List<Object> parry=new ArrayList<Object>();
        		parry.add("L2");
        		StringBuffer hql=new StringBuffer("and  logType=?");
        		if(p.containsKey("accessPerson") && StringUtils.isNotBlank(p.getString("accessPerson"))){
        			hql.append(" and accessPerson like ?");
        			parry.add("%"+p.getString("accessPerson"));
        		}
        		if(p.containsKey("accessMethod") && StringUtils.isNotBlank(p.getString("accessMethod"))){
        			hql.append(" and accessMethod like ?");
        			parry.add("%"+p.getString("accessMethod"));
        		}
        		if(p.containsKey("operate")&& StringUtils.isNotBlank(p.getString("operate"))){
        			hql.append(" and operate like ?");
        			parry.add("%"+p.getString("operate"));
        		}
        		if(p.containsKey("accessIp")&& StringUtils.isNotBlank(p.getString("accessIp"))){
        			hql.append(" and accessIp like ?");
        			parry.add("%"+p.getString("accessIp"));
        		}
        		if(p.containsKey("accessTimeStart")&& StringUtils.isNotBlank(p.getString("accessTimeStart"))){//
        			hql.append(" and accessTime >= ?");
        			parry.add(p.getDate("accessTimeStart"));
        		}
        		if(p.containsKey("accessTimeEnd")&& StringUtils.isNotBlank(p.getString("accessTimeEnd"))){
        			hql.append(" and accessTime < ?");
        			parry.add(p.getDate("accessTimeEnd"));
        		}
        		if(p.containsKey("leaveTimeStart")&& StringUtils.isNotBlank(p.getString("leaveTimeStart"))){//
        			hql.append(" and leaveTime >= ?");
        			parry.add(p.getDate("leaveTimeStart"));
        		}
        		if(p.containsKey("leaveTimeEnd")&& StringUtils.isNotBlank(p.getString("leaveTimeEnd"))){
        			hql.append(" and leaveTime < ?");
        			parry.add(p.getDate("leaveTimeEnd"));
        		}
                logService.where(rest.getPageModel(),hql.toString(),parry);
        	}else{
                logService.where(rest.getPageModel()," and  logType=?","L2");
        	}
            return success(rest.getPageModel());
        } catch (Exception e) {
            LOGGER.error("query error", e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/del"})
    public
    @ResponseBody
    BodyWrapper del(String[] ids, RestRequest rest) {
        try {
            logService.delete(ids);
            return success();
        } catch (Exception e) {
            LOGGER.error("del error", e);
            return failure(e);
        }
    }


    @Value("#{app.logs_filepath}")
    private String logsPath;

    /**
     * 下载日志文件
     * @param response
     */
    @RequestMapping(value = {"/downlogs"})
    public void downlogs(HttpServletResponse response) {
        try {
            File file = new File(logsPath);
            if(file.canRead()){
                WebUtil.setDownHeader(response, file.getName().trim()+".zip");
                ZipUtil.zipFiles(logsPath, response.getOutputStream());
            }else{
            	super.downFileError(response);
            }
        } catch (Exception e) {
            LOGGER.error("del error", e);
            super.downFileError(response);
        }
    }
}
