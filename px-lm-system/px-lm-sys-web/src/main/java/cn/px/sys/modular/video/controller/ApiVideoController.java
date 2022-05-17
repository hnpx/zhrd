package cn.px.sys.modular.video.controller;

import cn.px.sys.modular.peopleAppRecord.service.PeopleAppRecordService;
import cn.px.sys.modular.video.vo.LivePushParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 视频服务接口
 */
@RestController()
@RequestMapping("/api/video")
public class ApiVideoController  {

    @Autowired
    private PeopleAppRecordService peopleAppRecordService;

    /**
     * 视频关闭后的通知消息
     * TODO 缺少异常情况处理，如果出现异常情况需要有补偿机制，处理
     *
     * @return
     */
    @RequestMapping("/live-push")
    public Object endNotify(@RequestBody LivePushParam param) {
        peopleAppRecordService.liveEnd(param);
        return "success";
    }

    /**
     * 视频混流开启
     * @param roomNumber
     * @return
     */
    @PostMapping("/video/mixedio")
    public Object mixedio(String roomNumber){
        Boolean b =   peopleAppRecordService.startVideo(roomNumber);
        return b;
    }

}
