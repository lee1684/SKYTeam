import { colors } from "app/theme"
import { ExtendedEdge, useSafeAreaInsetsStyle } from "app/utils/useSafeAreaInsetsStyle"
import { StatusBar, StatusBarProps } from "expo-status-bar"
import React from "react"
import {
  KeyboardAvoidingView,
  KeyboardAvoidingViewProps,
  Platform,
  ScrollViewProps,
  StyleProp,
  View,
  ViewStyle,
} from "react-native"

const isIos = Platform.OS === "ios"

interface BaseScreenProps {
  children?: React.ReactNode
  style?: StyleProp<ViewStyle>
  contentContainerStyle?: StyleProp<ViewStyle>
  safeAreaEdges?: ExtendedEdge[]
  backgroundColor?: string
  statusBarStyle?: "light" | "dark"
  keyboardOffset?: number
  StatusBarProps?: StatusBarProps
  KeyboardAvoidingViewProps?: KeyboardAvoidingViewProps
}

interface FixedScreenProps extends BaseScreenProps {
  preset?: "fixed"
}

interface ScrollScreenProps extends BaseScreenProps {
  preset?: "scroll"
  keyboardShouldPersistTaps?: "handled" | "always" | "never"
  ScrollViewProps?: ScrollViewProps
}

interface AutoScreenProps extends Omit<ScrollScreenProps, "preset"> {
  preset?: "auto"
  scrollEnabledToggleThreshold?: { percent?: number; point?: number }
}
export type WebViewScreenProps = ScrollScreenProps | FixedScreenProps | AutoScreenProps

export function WebViewScreen(props: WebViewScreenProps) {
  const {
    children,
    backgroundColor = colors.background,
    KeyboardAvoidingViewProps,
    keyboardOffset = 0,
    safeAreaEdges,
    StatusBarProps,
    statusBarStyle = "dark",
  } = props

  const $containerInsets = useSafeAreaInsetsStyle(safeAreaEdges)

  return (
    <View style={[$containerStyle, { backgroundColor }, $containerInsets]}>
      <StatusBar style={statusBarStyle} {...StatusBarProps} />

      <KeyboardAvoidingView
        behavior={isIos ? "padding" : "height"}
        keyboardVerticalOffset={keyboardOffset}
        {...KeyboardAvoidingViewProps}
        style={[$keyboardAvoidingViewStyle, KeyboardAvoidingViewProps?.style]}
      >
        {children}
      </KeyboardAvoidingView>
    </View>
  )
}

const $containerStyle: ViewStyle = {
  flex: 1,
  height: "100%",
  width: "100%",
}

const $keyboardAvoidingViewStyle: ViewStyle = {
  flex: 1,
}
