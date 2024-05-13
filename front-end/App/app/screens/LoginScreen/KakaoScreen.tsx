import React, { FC } from "react"
import { ViewStyle } from "react-native"
import { useStores } from "app/models"
import { WebViewScreen } from "app/components/WebViewScreen"
import WebView from "react-native-webview"
import { LoginScreenProps } from "app/navigators/LoginNavigator"
import { useHeader } from "app/utils/useHeader"
import { api } from "app/services/api"
import { saveString } from "app/utils/storage"
import Config from "app/config"

export const KakaoScreen: FC<LoginScreenProps<"Kakao">> = function KakaoScreen(_props) {
  const { navigation } = _props

  const {
    authenticationStore: { setAuthToken },
  } = useStores()

  useHeader({
    safeAreaEdges: ["top"],
    leftIcon: "back",
    onLeftPress: () => navigation.goBack(),
    title: "카카오 로그인 테스트",
  })

  return (
    <WebViewScreen preset="fixed" contentContainerStyle={$screenContentContainer}>
      <WebView
        startInLoadingState
        javaScriptEnabled
        mixedContentMode="compatibility"
        injectedJavaScript={`
          if(document.location.href.includes("/login/oauth2/code/kakao?code=")) {
            window.ReactNativeWebView.postMessage(document.querySelector("pre").innerText);
          }
        `}
        source={{ uri: `${Config.API_URL}oauth2/authorization/kakao` }}
        onMessage={(event) => {
          const { access, refresh } = JSON.parse(event.nativeEvent.data)
          setAuthToken(access)
          api.apisauce.setHeader("Authorization", `Bearer ${access}`)

          console.log("access", access)

          saveString("access", access)
          saveString("refresh", refresh)
        }}
      />
    </WebViewScreen>
  )
}
const $screenContentContainer: ViewStyle = {
  flex: 1,
}
