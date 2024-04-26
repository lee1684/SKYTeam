import React from "react"
import { View, ViewStyle } from "react-native"
import { Button } from "./Button"
import { colors } from "app/theme"
import { Text } from "./Text"

export interface BottomStepBarProps {
  text?: string
  onPress?: () => void
}
export function BottomStepBar(props: BottomStepBarProps) {
  const { text, onPress } = props

  return (
    <View style={$container}>
      <Button
        style={$button}
        pressedStyle={$buttonPressed}
        pressedTextStyle={$pressedTextStyle}
        onPress={onPress}
      >
        {text ? <Text preset="white">{text}</Text> : null}
      </Button>
    </View>
  )
}

const $container: ViewStyle = {
  backgroundColor: colors.transparent,
  zIndex: 100,
  width: "100%",
  display: "flex",
  paddingHorizontal: 16,
  paddingVertical: 20,
  justifyContent: "center",
  alignItems: "center",
}

const $button: ViewStyle = {
  width: "100%",
  height: 48,
  backgroundColor: colors.palette.blue,
  borderWidth: 0,
}

const $buttonPressed: ViewStyle = {
  backgroundColor: colors.palette.blue,
  opacity: 0.5,
}

const $pressedTextStyle: ViewStyle = {
  opacity: 0.5,
}
