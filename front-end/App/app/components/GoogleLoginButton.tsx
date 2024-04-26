import React from "react"
import { Button } from "./Button"
import { Image, ImageStyle, ViewStyle } from "react-native"
import { colors } from "app/theme"
import { Text } from "./Text"

const googleLogo = require("../../assets/icons/google.png")

export function GoogleLoginButton({
  onSuccessfulLogin,
  onFailedLogin,
}: {
  onSuccessfulLogin: () => void
  onFailedLogin: () => void
}) {
  function onPress() {
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
      LeftAccessory={(props) => <Image source={googleLogo} {...props} style={$imageStyle} />}
      onPress={onPress}
    >
      <Text size="sm" weight="medium">
        Google로 시작하기
      </Text>
    </Button>
  )
}

const $viewStyle: ViewStyle = {
  height: 48,
  borderWidth: 1,
  borderColor: "rgba(216, 216, 218, 1)",
  backgroundColor: colors.google,
}

const $pressedViewStyle: ViewStyle = {
  backgroundColor: colors.google,
  opacity: 0.8,
}

const $imageStyle: ImageStyle = {
  position: "absolute",
  left: 16,
}
