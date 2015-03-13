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

        c.addRegId("APA91bEnN36XGoVP3rKk84y7SS4efXkfqwrRMSS5ULsh_9ZN9JSb0mgSRMtl5OxwuRC13S__EU7YpjLHgDjjBWPfQkJrU9O0KJafo35ZpdA3Ze-Tz26M0esAqs_5bZpsdjUlaWzs6KRCL3ECK3G8rWUuL7twOebzWQ");
        c.createData("메시지 전송", "테스트 메시지");

        return c;
    }
}
