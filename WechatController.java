package com.dengtacj.tzw.controller;


import com.dengtacj.tzw.base.domain.AjaxResult;
import com.dengtacj.tzw.base.page.TableDataInfo;
import com.dengtacj.tzw.base.util.ContentValues;
import com.dengtacj.tzw.base.util.WxConfigUtil;
import com.dengtacj.tzw.domain.wechat.*;
import com.dengtacj.tzw.service.IWechatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wechat")
@Api(value = "微信相关接口", description = "微信相关接口")
public class WechatController extends CommonController {

    @Autowired
    private IWechatService wechatService;

    /**
     * 扫码进入
     */
    @PostMapping("/openArticle")
    @ApiOperation(value = "扫码进入", notes = "扫码进入")
    public AjaxResult openArticle(@RequestBody AppReadInfo info) {
        wechatService.addReadInfo(info);
        return AjaxResult.success();
    }

    /**
     * 用户退出
     */
    @PostMapping("/closeArticle")
    @ApiOperation(value = "用户退出", notes = "用户退出")
    public AjaxResult closeArticle(@RequestBody AppReadInfo info) {
        wechatService.updateReadInfo(info);
        return AjaxResult.success();
    }

    /**
     * 用户点赞/取消点赞
     */
    @PostMapping("/setArticleLike")
    @ApiOperation(value = "用户点赞", notes = "用户点赞")
    public AjaxResult setArticleLike(@RequestBody ArticleLikesInInfo info) {
        wechatService.setUserArticlesLikesInfo(info);
        return AjaxResult.success();
    }

    /**
     * 查询点赞状态
     */
    @PostMapping("/getArticleLike")
    @ApiOperation(value = "用户点赞", notes = "用户点赞")
    public AjaxResult getArticleLike(@RequestBody ArticleLikes info) {
        int count = wechatService.getUserArticlesLikesInfo(info);
        return AjaxResult.success(count);
    }

    /**
     * 统计列表
     */
    @PostMapping("/getVisitsInfo")
    @ApiOperation(value = "统计列表", notes = "统计列表")
    public AjaxResult getVisitsInfo(@RequestBody VisitInInfo info) {

        TableDataInfo datas = new TableDataInfo();
        try {
            List<ReadInfo> visitList = wechatService.getVisitsInfo(info);
            long count = wechatService.getVisitsInfoCount(info);
            datas.setRows(visitList);
            datas.setTotal(count);
            return AjaxResult.success(datas);
        } catch (Exception e) {
            return AjaxResult.error();
        }
    }

    /**
     * 统计详情
     */
    @PostMapping("/getVisitDetail")
    @ApiOperation(value = "统计详情", notes = "统计详情")
    public AjaxResult getVisitDetail(@RequestBody VisitInDetail info) {
        AppReadInfo visitDetail = wechatService.getVisitDetail(info.getId());
        return AjaxResult.success(visitDetail);
    }

    /**
     * 访客统计列表
     */
    @PostMapping("/getVisitorInfo")
    @ApiOperation(value = "访客统计列表", notes = "访客统计列表")
    public AjaxResult getVisitorInfo(@RequestBody VisitorInInfo info) {

        TableDataInfo datas = new TableDataInfo();
        try {
            List<VisitorInfo> visitorList = wechatService.getVisitorInfo(info);
            long count = wechatService.getVisitorInfoCount(info);
            datas.setRows(visitorList);
            datas.setTotal(count);
            return AjaxResult.success(datas);
        } catch (Exception e) {
            return AjaxResult.error();
        }
    }

    @RequestMapping(value = "/share", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> share(HttpServletRequest request) {
        String appUrl = request.getParameter("url");
        if (request.getParameter("code") != null) {
            appUrl += "&code=" + request.getParameter("code");
        }
        if (request.getParameter("state") != null) {
            appUrl += "&state=" + request.getParameter("state");
        }
        return WxConfigUtil.getSignature(appUrl, ContentValues.APPID, ContentValues.SECRET/*, urlTemp, urlpath*/);
    }

}
