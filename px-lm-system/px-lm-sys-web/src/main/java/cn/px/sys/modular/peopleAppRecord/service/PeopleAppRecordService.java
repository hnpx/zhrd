package cn.px.sys.modular.peopleAppRecord.service;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.px.sys.modular.peopleAppRecord.entity.PeopleAppRecordEntity;
import cn.px.sys.modular.peopleAppRecord.mapper.PeopleAppRecordMapper;
import cn.px.sys.modular.peopleAppRecord.vo.VideoInfo;
import cn.px.sys.modular.video.vo.LivePushParam;
import cn.px.sys.modular.video.vo.PresetLayoutConfig;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.trtc.v20190722.TrtcClient;
import com.tencentcloudapi.trtc.v20190722.models.*;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.MediaInfo;
import com.tencentcloudapi.vod.v20180717.models.SearchMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.SearchMediaResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.px.base.core.BaseServiceImpl;
import cn.px.base.Constants;
import org.springframework.cache.annotation.CacheConfig;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 群众预约记录(PeopleAppRecord)表服务实现类
 *
 * @author
 * @since 2021-05-13 15:56:13
 */
@Service("peopleAppRecordService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "peopleAppRecord")
public class PeopleAppRecordService extends BaseServiceImpl<PeopleAppRecordEntity, PeopleAppRecordMapper> {

    @Resource
    private PeopleAppRecordService peopleAppRecordService;
    @Resource
    private VodClient vodClient;
    @Resource
    private TrtcClient trtcClient;



    /**
     *
     * @param page
     * @param type (1.未开始2.已结束3.已拒绝)
     * @return
     */
   public Page<PeopleAppRecordEntity> getAppRecordList( Page page, Integer type, Long crowdId,  Long congressId ){
        return super.mapper.getAppRecordList(page,type,crowdId,congressId,new Date());

    }


   public List<PeopleAppRecordEntity> getAppNotice( Long crowdId,Long congressId){
        return super.mapper.getAppNotice(crowdId,congressId);
    }

   public int getAppNoticeNum(Long crowdId, Long congressId){
       return super.mapper.getAppNoticeNum(crowdId,congressId);
   }

    /**
     *
     * @param page
     * @return
     */
   public Page<PeopleAppRecordEntity> getAppRecordListBystatus( Page page){
       return super.mapper.getAppRecordListBystatus(page);
   }

    public int getCount(Long congressId,Integer status){
        return super.mapper.getCount(congressId,status);
    }

   public Page<Map<String, Object>> getStatusList( Page page, Long congressId, Integer status){
        return super.mapper.getStatusList(page,congressId,status);
    }

    /**
     * 直播结束
     *
     * @param param
     */
    public void liveEnd(LivePushParam param){
        ThreadUtil.execute(() -> {
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String[] ss = param.getStream_id().split("_");
            String taskId = ss[1];

           PeopleAppRecordEntity peopleAppRecordEntity = peopleAppRecordService.getAppByRoomNumber(taskId);

            if (peopleAppRecordEntity == null) {
                return;
            }


            // 从媒体资源库中获取混播视频资源
            String name = taskId + "_mix_file";
            String url = null;
            SearchMediaRequest req = new SearchMediaRequest();
//        String[] params=new String[]{"ss"};
//        req.set("NamePrefixes.N",params);
            try {
                SearchMediaResponse resp = vodClient.SearchMedia(req);
                MediaInfo[] infos = resp.getMediaInfoSet();
                for (MediaInfo info : infos) {
                    //info.getBasicInfo().getMediaUrl()
                    if (info.getBasicInfo().getName().startsWith(name)) {
                        url = info.getBasicInfo().getMediaUrl();
                        param.setVideo_url(url);
                        float f=info.getMetaData().getDuration();
                        int duration= (int) f;
                        param.setDuration(duration+"");
                        int start=Integer.parseInt(param.getStart_time());
                        param.setEnd_time((start+duration)+"");
                        //获取后写入混播资源信息
                        break;
                    }
//                System.out.println(info.getFileId()+" \t"+info.getBasicInfo().getName());
                }
            } catch (TencentCloudSDKException e) {
                e.printStackTrace();
            }
           //处理视频返回结果
            List<VideoInfo> videos = new ArrayList<>();
//            if (StrUtil.isEmpty(peopleAppRecordEntity.getUrl())) {
//                videos = new ArrayList<>();
//            } else {
//                videos = JSONUtil.toList(new JSONArray(peopleAppRecordEntity.getUrl()), VideoInfo.class);
//            }
            videos.add(new VideoInfo(param.getVideo_url(), Long.parseLong(param.getStart_time()), Long.parseLong(param.getEnd_time()), Integer.parseInt(param.getDuration()), param.getFile_id()));
            peopleAppRecordEntity.setUrl(JSONUtil.toJsonStr(videos));
            peopleAppRecordService.update(peopleAppRecordEntity);
        });
    }

    /**
     * 通过房间号 查找预约信息
     * @param roomNumber
     * @return
     */
   public PeopleAppRecordEntity getAppByRoomNumber( String roomNumber){
        return super.mapper.getAppByRoomNumber(roomNumber);
    }


    /**
     * 开始视频
     * @param roomNumber
     * @return
     */
    public boolean startVideo(String roomNumber){
        StartMCUMixTranscodeRequest request = new StartMCUMixTranscodeRequest();
        try {
            request.setSdkAppId(1400523912L);
            request.setRoomId(new Long(roomNumber));
            OutputParams op = new OutputParams();
            op.setStreamId(roomNumber + "_mix");
            op.setRecordId(roomNumber + "_mix_file");
            op.setPureAudioStream(0L);
            op.setRecordAudioOnly(0L);
            request.setOutputParams(op);
            EncodeParams ep = new EncodeParams();
            ep.setAudioSampleRate(12000L);
            ep.setVideoHeight(1280L);
            ep.setVideoWidth(720L);
            ep.setVideoGop(2L);
            ep.setVideoBitrate(1560L);
            ep.setVideoFramerate(15L);
            ep.setAudioSampleRate(48000L);
            ep.setBackgroundColor(0L);
            ep.setAudioBitrate(64L);
            ep.setAudioChannels(2L);
            request.setEncodeParams(ep);
            LayoutParams lp = new LayoutParams();
            request.setLayoutParams(lp);
            StartMCUMixTranscodeResponse response = trtcClient.StartMCUMixTranscode(request);
            System.out.println(response.getRequestId());
            return true;
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return false;
    }
}
