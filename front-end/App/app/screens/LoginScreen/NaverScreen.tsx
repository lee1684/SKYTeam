import React, { FC } from "react"
import { ViewStyle } from "react-native"
import { useStores } from "app/models"
import { WebViewScreen } from "app/components/WebViewScreen"
import WebView from "react-native-webview"
import { LoginScreenProps } from "app/navigators/LoginNavigator"
import { useHeader } from "app/utils/useHeader"
import AsyncStorage from "@react-native-async-storage/async-storage"

export const NaverScreen: FC<LoginScreenProps<"Naver">> = function NaverScreen(_props) {
  const { navigation } = _props

  const {
    authenticationStore: { setAuthToken },
  } = useStores()

  useHeader({
    safeAreaEdges: ["top"],
    leftIcon: "back",
    onLeftPress: () => navigation.goBack(),
    title: "네이버 로그인 테스트",
  })

  return (
    <WebViewScreen preset="fixed" contentContainerStyle={$screenContentContainer}>
      <WebView
        startInLoadingState
        javaScriptEnabled
        mixedContentMode="compatibility"
        injectedJavaScript={`
          if(document.location.href.includes("/login/oauth2/code/naver?code=")) {
            window.ReactNativeWebView.postMessage(document.querySelector("pre").innerText);
          }
        `}
        source={{ uri: "http://3.34.0.190:8080/oauth2/authorization/naver" }}
        onMessage={(event) => {
          const { access, refresh } = JSON.parse(event.nativeEvent.data)
          setAuthToken(access)
          AsyncStorage.setItem("access", access)
          AsyncStorage.setItem("refresh", refresh)
        }}
      />
    </WebViewScreen>
  )
}
const $screenContentContainer: ViewStyle = {
  flex: 1,
}
