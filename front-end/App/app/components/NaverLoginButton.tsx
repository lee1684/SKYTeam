import React from "react"
import { Button } from "./Button"
import { Image, ImageStyle, TextStyle, ViewStyle } from "react-native"
import { colors } from "app/theme"
import { Text } from "./Text"

const naverLogo = require("../../assets/icons/naver.png")

export function NaverLoginButton({
  onSuccessfulLogin,
  onFailedLogin,
}: {
  onSuccessfulLogin: () => void
  onFailedLogin: () => void
}) {
  async function onPress() {
    try {
      onSuccessfulLogin()
    } catch {
      onFailedLogin()
    }
  }

  return (
    <Button
      style={$viewStyle}
      pressedStyle={$pressedViewStyle}
      LeftAccessory={(props) => <Image source={naverLogo} {...props} style={$imageStyle} />}
      onPress={onPress}
    >
      <Text style={$textStyle} size="sm" weight="medium">
        Naver로 시작하기
      </Text>
    </Button>
  )
}

const $viewStyle: ViewStyle = {
  height: 48,
  borderWidth: 0,
  backgroundColor: colors.naver,
}

const $pressedViewStyle: ViewStyle = {
  backgroundColor: colors.naver,
  opacity: 0.8,
}

const $imageStyle: ImageStyle = {
  position: "absolute",
  left: 16,
}

const $textStyle: TextStyle = {
  color: colors.white,
}
