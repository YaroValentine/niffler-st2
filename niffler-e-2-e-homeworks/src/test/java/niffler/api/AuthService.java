package niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.*;

public interface AuthService {

    // PRECONDITION
    // empty browser/session


    // REQUEST 1
    // http://127.0.0.1:9000/oauth2/authorize?response_type=code
    // &client_id=client
    // &scope=openid
    // &redirect_uri=http://127.0.0.1:3000/authorized
    // &code_challenge=V48NCt3SmmbGcFvkMX6hYFeiwfqejzZitIV2ucjcfqA
    // &code_challenge_method=S256
    // --------------------------------------------------------------
    @GET("/oauth2/authorize")
    Call<Void> authorize(
            @Query("response_type") String responseType,
            @Query("client_id") String clientId,
            @Query("scope") String scope,
            @Query(value = "redirect_uri", encoded = true) String redirectUri,
            @Query("code_challenge") String codeChallenge,
            @Query("code_challenge_method") String codeChallengeMethod
    );

    // RESPONSE:
    //                  0.1) 302 - http://127.0.0.1:9000/login
    //                  0.2) Set-Cookie: JSESSIONID=D6D863AA1D342A7AECBC5B6C148774A6; Path=/
    // response 0.1 -   1.0) Set-Cookie: XSRF-TOKEN=420351da-f5bc-4123-9232-823ae7b9d977; Path=/


    // REQUEST 2
    // POST 302 (уходят куки)
    //      @UrlEncoded(payload)
    //      _csrf: 420351da-f5bc-4123-9232-823ae7b9d977
    //      username: yaro
    //      password: secret
    // response 302:    http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&code_challenge=OVOuNAVMsF37vcWfqC9jtlfArWwXnDSdlvp32G8unB8&code_challenge_method=S256&continue
    // 1: Set-Cookie: JSESSIONID=9E5FEF012278C4215127780551E56640; Path=/
    // 2: Set-Cookie: XSRF-TOKEN=; Path=/ (обнулили)
    // NEXT  SUB REQUEST TO 302
    //      http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&code_challenge=OVOuNAVMsF37vcWfqC9jtlfArWwXnDSdlvp32G8unB8&code_challenge_method=S256&continue
    //      Request: Cookie JSESSIONID
    //      Response 302: http://127.0.0.1:3000/authorized?code=uDNkbwudISjb-zvluIQzsTiW4fNqa_tZqWIgdc3NQXskrq5LRuQVqkMDZbXBOVZSTqSqjp1BgWUqRBtqS1Nwu-AmjtgAmoSNLeeQ_pYDizd9mNGOEPQA0Q-XPe90yS2N
    //      Result 200 OK: http://127.0.0.1:3000/authorized?code=uDNkbwudISjb-zvluIQzsTiW4fNqa_tZqWIgdc3NQXskrq5LRuQVqkMDZbXBOVZSTqSqjp1BgWUqRBtqS1Nwu-AmjtgAmoSNLeeQ_pYDizd9mNGOEPQA0Q-XPe90yS2N
    // -------------------------------------------------------------------
    @POST("/login")
    @FormUrlEncoded
    Call<Void> login(
            @Header("Cookie") String jsessionIdCookie,
            @Header("Cookie") String xsrfCookie,
            @Field("_csrf") String xsrf,
            @Field("username") String username,
            @Field("password") String password
    );


    // REQUEST 3 (уходит сам с фронта, наверное javascript)
    // http://127.0.0.1:9000/oauth2/token?client_id=client&redirect_uri=http://127.0.0.1:3000/authorized&grant_type=authorization_code&code=uDNkbwudISjb-zvluIQzsTiW4fNqa_tZqWIgdc3NQXskrq5LRuQVqkMDZbXBOVZSTqSqjp1BgWUqRBtqS1Nwu-AmjtgAmoSNLeeQ_pYDizd9mNGOEPQA0Q-XPe90yS2N&code_verifier=viCROgbZPN7fQYcmIRZ_GNmDOVV6R2-MfRSRWkAqQCk
    // + Authorization: Basic Y2xpZW50OnNlY3JldA==
    // result:
    //    {
    //        "access_token": "eyJraWQiOiIwYmUzZjY3ZS0wODdmLTQ2YzUtYTdjYi1mNzhiNDE5OThjZDAiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ5YXJvIiwiYXVkIjoiY2xpZW50IiwibmJmIjoxNjg2MjQwMDgyLCJzY29wZSI6WyJvcGVuaWQiXSwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTo5MDAwIiwiZXhwIjoxNjg2Mjc2MDgyLCJpYXQiOjE2ODYyNDAwODJ9.Os6RIQIV-9tHJ8_QDikvu2wT4S5kMinA7IXh7JOV6k0-hA6lO6tLrysqvBUaObz5-3JT7dnhhl-QiMSHyG3un6sup-5Aqz01bYV32uuQFltKf9q5sL4dhSEvK9aBTauwxgHnkVV3BxfbEkw-DnNg5P3x_i7R4f1Lmd3ZO2R0fwb9ssVQlGI5VJAg9QJdc6rVI7TrxEZ5CNvx8kv87P1SGYQU4K53bp7_b4Nqal-42xCG3ISEaRiIdEeepMDSE_nq2EetgKOf2aJDQGZiDQ0Tn_ZFq8glkUvdO47pvAvrgo9tpfvH53tpFQEe6ZBK9uBUmV-Dpfb2jQUVyb3RcG0wjg",
    //            "refresh_token": "CorGu2QeFjaoXWWlSMktrYOBKee4dud1qytBnGYwJRZQpmF7oaiWHvNRcWDWmDVdTh_Fu7spB7c1tn7EaSXrDilNXasj6Pyveb9UBl9H0px9iv_rOrXgIhR4g1AYp-DK",
    //            "scope": "openid",
    //            "id_token": "eyJraWQiOiIwYmUzZjY3ZS0wODdmLTQ2YzUtYTdjYi1mNzhiNDE5OThjZDAiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjkwMDAiLCJzdWIiOiJ5YXJvIiwiYXVkIjoiY2xpZW50IiwiZXhwIjoxNjg2MjQxODgzLCJpYXQiOjE2ODYyNDAwODMsImF6cCI6ImNsaWVudCJ9.PCyq5nu7Id4gTeN1xNqP_fD6KXnVrnyEHTPMC4fjIbsZRPRl_vjjFv6rJBW1Def4VJTowsBICrMl-202rebXWtDB_WQEi7hL95Hc5JbpQxBs3jyvzqoGQtvHhjXX8J-mfNkOqZmF4btkq9lXVGflH6EwrWJqJfNcBhyyeIisznONpshNkb448WOwmKnLvZHGKMGRVi_Z1pjg5bW7rCwG-yzjiyTWTD2KEBOYfedI8_FcjT4lzOw8zKG4I8LQ-3UWkTHIkIfjRAwk664iA5eADdNvHflE1UWl8i-5Z4G25VNCKM_AjTwq63uDg3NP9Yiq4t0ENa8rRo-nnhooPnqD2g",
    //            "token_type": "Bearer",
    //            "expires_in": 35999
    //    }
    // в браузере (Session Storage)
    //    codeChallenge	OVOuNAVMsF37vcWfqC9jtlfArWwXnDSdlvp32G8unB8
    //    id_token	eyJraWQiOiIwYmUzZjY3ZS0wODdmLTQ2YzUtYTdjYi1mNzhiNDE5OThjZDAiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjkwMDAiLCJzdWIiOiJ5YXJvIiwiYXVkIjoiY2xpZW50IiwiZXhwIjoxNjg2MjQxODgzLCJpYXQiOjE2ODYyNDAwODMsImF6cCI6ImNsaWVudCJ9.PCyq5nu7Id4gTeN1xNqP_fD6KXnVrnyEHTPMC4fjIbsZRPRl_vjjFv6rJBW1Def4VJTowsBICrMl-202rebXWtDB_WQEi7hL95Hc5JbpQxBs3jyvzqoGQtvHhjXX8J-mfNkOqZmF4btkq9lXVGflH6EwrWJqJfNcBhyyeIisznONpshNkb448WOwmKnLvZHGKMGRVi_Z1pjg5bW7rCwG-yzjiyTWTD2KEBOYfedI8_FcjT4lzOw8zKG4I8LQ-3UWkTHIkIfjRAwk664iA5eADdNvHflE1UWl8i-5Z4G25VNCKM_AjTwq63uDg3NP9Yiq4t0ENa8rRo-nnhooPnqD2g
    //    codeVerifier	viCROgbZPN7fQYcmIRZ_GNmDOVV6R2-MfRSRWkAqQCk
    @POST("/oauth2/token")
    Call<JsonNode> token(
            @Header("Authorization") String authorization,
            @Query("client_id") String clientId,
            @Query(value = "redirect_uri", encoded = true) String redirectUri,
            @Query("grant_type") String grantType,
            @Query("code") String code,
            @Query("code_verifier") String codeVerifier
    );

}
