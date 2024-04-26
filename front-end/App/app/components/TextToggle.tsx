import React from "react"
import { StyleProp, TouchableHighlight, ViewStyle } from "react-native"
import { Text } from "./Text"
import { colors } from "app/theme"

export interface TextToggleProps {
  label: string
  pressed: boolean
  onPress: () => void
  divide?: boolean
  location?: "left" | "right"
}

export function TextToggle(props: TextToggleProps) {
  const { label, pressed, onPress, divide, location } = props

  const pressedStyle: StyleProp<ViewStyle> = [
    $container,
    {
      borderColor: colors.black,
    },
    divide && { flex: 1 },
    location && location === "left"
      ? { alignItems: "flex-start", paddingHorizontal: 20 }
      : location === "right"
      ? { alignItems: "flex-end", paddingHorizontal: 20 }
      : {},
  ]

  const defaultStyle: StyleProp<ViewStyle> = [
    $container,
    {
      borderColor: colors.palette.gray100,
    },
    divide && { flex: 1 },
    location && location === "left"
      ? { alignItems: "flex-start", paddingHorizontal: 20 }
      : location === "right"
      ? { alignItems: "flex-end", paddingHorizontal: 20 }
      : {},
  ]

  return (
    <TouchableHighlight
      onPress={onPress}
      underlayColor={colors.transparent}
      style={pressed ? pressedStyle : defaultStyle}
    >
      <Text preset={pressed ? "default" : "blur"}>{label}</Text>
    </TouchableHighlight>
  )
}

const $container: ViewStyle = {
  height: 48,
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  borderRadius: 4,
  borderWidth: 1.5,
}
