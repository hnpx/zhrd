package cn.px.sys.modular.peopleInformationSet.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.px.sys.modular.peopleInformationSet.entity.PeopleInformationSetEntity;
import cn.px.sys.modular.peopleInformationSet.mapper.PeopleInformationSetMapper;
import cn.px.sys.modular.peopleInformationSet.utils.Aliyun;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import cn.px.base.core.BaseServiceImpl;
import cn.px.base.Constants;
import org.springframework.cache.annotation.CacheConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 人大信息设置(PeopleInformationSet)表服务实现类
 *
 * @author
 * @since 2021-05-13 16:08:45
 */
@Service("peopleInformationSetService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "peopleInformationSet")
public class PeopleInformationSetService extends BaseServiceImpl<PeopleInformationSetEntity, PeopleInformationSetMapper> {

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId ;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret ;
    @Value("${aliyun.signName}")
    private String signName ;
    private DefaultProfile profile;
    private IAcsClient client;
    @Autowired
    private RedisTemplate redisTemplate;
    public void initConfig(){
        this.profile = DefaultProfile.getProfile(
                "cn-hangzhou",
                accessKeyId,
                accessKeySecret);
        this.client = new DefaultAcsClient(profile);
    }

    public Map<String,String> sendSms(String phone, String temp, Map<String,String> param){
        initConfig();
        CommonRequest request=new CommonRequest();
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("PhoneNumbers",phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", temp);
        if(param!=null){
            JSONObject obj= (JSONObject) JSONObject.toJSON(param);
            request.putQueryParameter("TemplateParam", obj.toJSONString());
        }
        CommonResponse commonResponse = null;
        Map<String,String> result=new HashMap<>();
        try {
            commonResponse = client.getCommonResponse(request);
            String data = commonResponse.getData();
            String sData = data.replaceAll("'\'", "");
            Gson gson = new Gson();
            Map map = gson.fromJson(sData, Map.class);
            if(map.get("Message").toString().equals("OK")&&map.get("Code").equals("OK")){
                result.put("success", "1");
            }else{
                result.put("success", "0");
                result.put("msg", map.get("Message").toString());
            }
        } catch (ClientException e) {
            e.printStackTrace();
            result.put("msg", e.getErrorDescription());
            result.put("success", "0");
        }
        return result;
    }

    public int sendVerifySms(String phone)throws Exception{
        Map<String ,String > result=new HashMap<>();
        Map<String ,String > param=new HashMap<>();
        param.put("code", RandomUtil.randomNumbers(6));
        if(StrUtil.isNotEmpty(phone)){
            result=sendSms(phone, Aliyun.VERIFY_CODE,param);
        }else{
            result.put("success","0");
            result.put("msg", "请输入手机号");
        }
        if(result.get("success").equals("1")){
            System.out.println("验证码======="+param.get("code"));
            redisTemplate.opsForValue().set(phone,param.get("code"),120, TimeUnit.SECONDS);
            return 1;
        }else {
            throw new Exception(result.get("msg"));
        }

    }

    public Object getRandom(int length){
        String base = "123456789123456789123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < length; ++i) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb;
    }



}
