package com.talk.common.gcm;

/**
 * Created by jinwoo on 2015-03-11.
 */
public class AppTest {
    public static void main( String[] args )
    {
        System.out.println( "Sending POST to GCM" );

        String apiKey = "AIzaSyADK_frqsKz8Jb_SEgpkcvsGkIC9561LII";
        GcmVo gcmVo = createGcmVo();

        PostGcm.post(apiKey, gcmVo);
    }

    public static GcmVo createGcmVo(){

        GcmVo c = new GcmVo();

        c.addRegId("APA91bHioSymVMsISoNsPcWqzqc4pFeiuuvRj9NwHU3H_9NMQzofiuK2Toce8u7TRn_Bo5AgbGLISMfS9NZGXdl4CBgpGRvNelhF7AGbF9_rxG1OWIj2bgaGngPCkl2lWyIrs9cFRb4vGyGN0bjG_KZTHIEHevE_8w");
        c.createData("메시지 전송", "테스트 메시지");

        return c;
    }
}
