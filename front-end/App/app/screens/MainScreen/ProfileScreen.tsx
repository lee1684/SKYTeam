import React, { FC } from "react"
import { ViewStyle } from "react-native"
import { Button, Screen, Text } from "../../components"
import { MainTabScreenProps } from "../../navigators/MainNavigator"
import { useStores } from "app/models"

export const ProfileScreen: FC<MainTabScreenProps<"Profile">> = function ProfileScreen(_props) {
  const {
    authenticationStore: { logout },
  } = useStores()

  return (
    <Screen preset="scroll" safeAreaEdges={["top"]} contentContainerStyle={$container}>
      <Text>Profile Screen</Text>
      <Button onPress={logout}>로그아웃</Button>
    </Screen>
  )
}

const $container: ViewStyle = {
  flex: 1,
}
