import { colors } from "app/theme"
import React from "react"
import { View, ViewStyle } from "react-native"
import { LinearGradient } from "expo-linear-gradient"

export function StepBar({ currentStep }: { currentStep: number }) {
  function $currentStep() {
    return {
      ...$currentBar,
      width: currentStep * 100 + "%",
    }
  }

  return (
    <View style={$container}>
      <View style={$totalStep}>
        <LinearGradient
          style={$currentStep() as ViewStyle}
          colors={["rgba(96,163,255,1)", "rgba(0, 107,255,1)"]}
          start={{ x: 0, y: 0 }}
          end={{ x: 1, y: 1 }}
        ></LinearGradient>
      </View>
    </View>
  )
}

const $container: ViewStyle = {
  minHeight: 32,
  display: "flex",
  justifyContent: "center",
  alignContent: "center",
}

const $totalStep: ViewStyle = {
  height: 8,
  marginHorizontal: 20,
  backgroundColor: colors.palette.gray50,
  borderRadius: 4,
}

const $currentBar: ViewStyle = {
  height: 8,
  borderRadius: 4,
}
