import React, { FC } from "react"
import { ViewStyle } from "react-native"
import { Screen, Text } from "../../components"
import { MainTabScreenProps } from "../../navigators/MainNavigator"
export const SearchScreen: FC<MainTabScreenProps<"Search">> = function SearchScreen(_props) {
  return (
    <Screen preset="fixed" safeAreaEdges={["top"]} contentContainerStyle={$container}>
      <Text>Search</Text>
    </Screen>
  )
}

const $container: ViewStyle = {
  flex: 1,
}
