import React from "react"
import { Button } from "./Button"
import { Image, ImageStyle, ViewStyle } from "react-native"
import { colors } from "app/theme"
import { Text } from "./Text"

const kakaoLogo = require("../../assets/icons/kakao.png")

export function KakaoLoginButton() {
  return (
    <Button
      style={$viewStyle}
      pressedStyle={$pressedViewStyle}
      LeftAccessory={(props) => <Image source={kakaoLogo} {...props} style={$imageStyle} />}
    >
      <Text size="sm" weight="medium">
        Kakao로 시작하기
      </Text>
    </Button>
  )
}

const $viewStyle: ViewStyle = {
  height: 48,
  borderWidth: 0,
  backgroundColor: colors.kakao,
}

const $pressedViewStyle: ViewStyle = {
  backgroundColor: colors.kakao,
  opacity: 0.8,
}

const $imageStyle: ImageStyle = {
  position: "absolute",
  left: 16,
}
