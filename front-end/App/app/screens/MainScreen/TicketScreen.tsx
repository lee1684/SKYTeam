import React, { FC } from "react"
import { ViewStyle } from "react-native"
import { Screen, Text } from "../../components"
import { MainTabScreenProps } from "../../navigators/MainNavigator"

export const TicketScreen: FC<MainTabScreenProps<"Ticket">> = function TicketScreen(_props) {
  return (
    <Screen preset="scroll" safeAreaEdges={["top"]} contentContainerStyle={$container}>
      <Text>Ticket Screen</Text>
    </Screen>
  )
}

const $container: ViewStyle = {
  flex: 1,
}
