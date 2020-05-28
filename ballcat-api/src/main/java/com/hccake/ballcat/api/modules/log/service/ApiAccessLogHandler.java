package com.hccake.ballcat.api.modules.log.service;

import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.hccake.ballcat.common.modules.log.model.entity.ApiAccessLog;
import com.hccake.ballcat.api.modules.log.thread.ApiAccessLogSaveThread;
import com.hccake.ballcat.commom.log.access.handler.AccessLogHandler;
import com.hccake.ballcat.commom.log.util.LogUtils;
import com.hccake.ballcat.common.core.util.IPUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 访问日志
 *
 * @author hccake
 * @date 2019-10-16 16:09:25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiAccessLogHandler implements AccessLogHandler<ApiAccessLog> {
    private final ApiAccessLogSaveThread apiAccessLogSaveThread;

    /**
     * 生产一个日志
     *
     * @return accessLog
     * @param request 请求信息
     * @param response 响应信息
     * @param executionTime 执行时长
     * @param throwable 异常
     */
    @Override
    public ApiAccessLog prodLog(HttpServletRequest request, HttpServletResponse response, Long executionTime, Throwable throwable) {
        ApiAccessLog apiAccessLog = new ApiAccessLog()
                .setCreateTime(LocalDateTime.now())
                .setTime(executionTime)
                .setIp(IPUtil.getIpAddr(request))
                .setMethod(request.getMethod())
                .setUserAgent(request.getHeader("user-agent"))
                .setUri(URLUtil.getPath(request.getRequestURI()))
                .setMatchingPattern(String.valueOf(request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE)))
                .setErrorMsg(Optional.ofNullable(throwable).map(Throwable::getMessage).orElse(null))
                .setHttpStatus(response.getStatus())
                .setReqParams(JSONUtil.toJsonStr(request.getParameterMap()));

        // 非文件上传请求，记录body
        if (!LogUtils.isMultipartContent(request)){
            apiAccessLog.setReqBody(LogUtils.getRequestBody(request));
        }

        return apiAccessLog;
    }


    /**
     * 记录日志
     *
     * @param accessLog 访问日志
     */
    @Override
    public void saveLog(ApiAccessLog accessLog) {
        apiAccessLogSaveThread.putObject(accessLog);
    }


}