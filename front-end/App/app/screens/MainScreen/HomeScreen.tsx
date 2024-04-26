import React, { FC } from "react"
import { ViewStyle } from "react-native"
import { Screen, Text } from "../../components"
import { MainTabScreenProps } from "../../navigators/MainNavigator"
import { useHeader } from "app/utils/useHeader"

export const HomeScreen: FC<MainTabScreenProps<"Home">> = function HomeScreen(_props) {
  useHeader({
    title: "홈",
  })

  return (
    <Screen preset="scroll" safeAreaEdges={["top"]} contentContainerStyle={$container}>
      <Text>home</Text>
    </Screen>
  )
}

const $container: ViewStyle = {
  flex: 1,
}
