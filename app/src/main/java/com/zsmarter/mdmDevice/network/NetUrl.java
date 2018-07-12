package com.zsmarter.mdmDevice.network;

import com.zsmarter.mdmDevice.network.bean.AddressFenceResponseBody;
import com.zsmarter.mdmDevice.network.bean.DeviceListResponseBody;
import com.zsmarter.mdmDevice.network.bean.DeviceLocationResponseBody;
import com.zsmarter.mdmDevice.network.bean.LoginPostBody;
import com.zsmarter.mdmDevice.network.bean.LoginResponseBody;
import com.zsmarter.mdmDevice.network.bean.OrganListBody;
import com.zsmarter.mdmDevice.network.bean.OrganListResponseBody;
import com.zsmarter.mdmDevice.network.bean.OrganizeDeviceListBody;
import com.zsmarter.mdmDevice.network.bean.OrganizeDeviceListResponseBody;
import com.zsmarter.mdmDevice.network.bean.UserInfo;
import com.zsmarter.mdmDevice.network.bean.UserInfoResponseBody;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by hecheng on 2018/6/8
 */
public interface NetUrl {
    @POST(UrlConfig.LOGIN_URL)
    Observable<LoginResponseBody> login(@Body LoginPostBody body);

    @POST(UrlConfig.USERINFO_URL)
    Observable<UserInfoResponseBody> userInfo(@Header("zs-token") String token);

    @POST(UrlConfig.DEVICELIST_URL)
    Observable<DeviceListResponseBody> deviceList(@Header("zs-token") String token, @Query("pageNo") int pageNo,
                                                  @Query("pageSize") int pageSize);

    @POST(UrlConfig.ORG_DEVICELIST_URL)
    Observable<DeviceListResponseBody> orgDeviceList(@Header("zs-token") String token,@Query("orgId") String orgId,
                                                     @Query("orgRange") String orgRange);

    @FormUrlEncoded
    @POST(UrlConfig.DEVICELOCATION_URL)
    Observable<DeviceLocationResponseBody> deviceLovation(@Header("zs-token") String token,
                                                          @Field("imei") String imei);


    @FormUrlEncoded
    @POST(UrlConfig.DEVICETIMELOCATION_URL)
    Observable<DeviceLocationResponseBody> deviceHistoryLocation(@Header("zs-token") String token,
                                                                 @Field("imei") String imei,
                                                                 @Field("startDate") String startDate,
                                                                 @Field("endDate") String endDate);

    @POST(UrlConfig.ORGANIZE_LIST_URL)
    Observable<OrganListResponseBody> organList(@Header("zs-token") String token, @Body OrganListBody body);


    @POST(UrlConfig.ADDRESS_FENCE_LIST_URL)
    Observable<AddressFenceResponseBody> addressFenceList(@Header("zs-token") String token);

//    @POST(UrlConfig.ORGANIZE_DEVICE_LIST_URL)
//    Observable<OrganizeDeviceListResponseBody> organDeviceList(@Header("zs-token") String token,
//                                                               @Body OrganizeDeviceListBody body);
}
