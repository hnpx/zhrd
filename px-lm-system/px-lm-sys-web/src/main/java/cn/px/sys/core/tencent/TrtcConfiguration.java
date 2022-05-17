package cn.px.sys.core.tencent;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.trtc.v20190722.TrtcClient;
import com.tencentcloudapi.vod.v20180717.VodClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class TrtcConfiguration {
    @Resource
    private TrtcProperties trtcProperties;

    /**
     * 业务根
     *
     * @return
     */
    @Bean
    public Credential getCredential() {
        Credential cred = new Credential(this.trtcProperties.getSecretId(), this.trtcProperties.getSecretKey());
        return cred;
    }

    /**
     * 视频点播业务
     *
     * @param credential
     * @return
     */
    @Bean
    public VodClient getVodClient(Credential credential) {
        return new VodClient(credential, this.trtcProperties.getRegion());
    }

    @Bean
    public TrtcClient getTrtcClient(Credential credential) {
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("trtc.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new TrtcClient(credential, this.trtcProperties.getRegion(), clientProfile);
    }

}
