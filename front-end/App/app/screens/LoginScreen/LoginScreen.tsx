import React, { FC } from "react"
import { Image, View, ViewStyle } from "react-native"
import { Screen } from "../../components"
import { KakaoLoginButton } from "app/components/KakaoLoginButton"
import { NaverLoginButton } from "app/components/NaverLoginButton"
import { GoogleLoginButton } from "app/components/GoogleLoginButton"
import { LoginScreenProps } from "app/navigators/LoginNavigator"

export const LoginScreen: FC<LoginScreenProps<"Login">> = function LoginScreen(_props) {
  const { navigation } = _props

  function onSuccessfulLogin(social: string) {
    switch (social) {
      case "google":
        navigation.navigate("Auth", { screen: "Google" })
        break
      case "kakao":
        navigation.navigate("Auth", { screen: "Kakao" })
        break
      case "naver":
        navigation.navigate("Auth", { screen: "Naver" })
        break
    }
  }
  function onFailedLogin() {
    console.log("Failed to login")
  }

  return (
    <Screen
      preset="fixed"
      contentContainerStyle={$screenContentContainer}
      safeAreaEdges={["top", "bottom"]}
    >
      <View style={$logoView}>
        <Image source={require("assets/icons/salon-logo.png")} />
      </View>
      <View style={$loginButtonBox}>
        <GoogleLoginButton
          onSuccessfulLogin={() => {
            onSuccessfulLogin("google")
          }}
          onFailedLogin={onFailedLogin}
        />
        <KakaoLoginButton
          onSuccessfulLogin={() => {
            onSuccessfulLogin("kakao")
          }}
          onFailedLogin={onFailedLogin}
        />
        <NaverLoginButton
          onSuccessfulLogin={() => {
            onSuccessfulLogin("naver")
          }}
          onFailedLogin={onFailedLogin}
        />
      </View>
    </Screen>
  )
}

const $screenContentContainer: ViewStyle = {
  flex: 1,
  paddingTop: 120,
  paddingBottom: 80,
}

const $loginButtonBox: ViewStyle = {
  display: "flex",
  flexDirection: "column",
  flex: 1,
  justifyContent: "flex-end",
  marginHorizontal: 32,
  gap: 16,
}

const $logoView: ViewStyle = {
  display: "flex",
  alignItems: "center",
}
