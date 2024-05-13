import React, { FC } from "react"
import { ViewStyle } from "react-native"
import { ListItem, Screen, Text } from "../../../components"
import { useStores } from "app/models"
import { useHeader } from "app/utils/useHeader"
import { ProfileScreenProps } from "app/navigators/ProfileNavigator"
import { api } from "app/services/api"

export const SettingScreen: FC<ProfileScreenProps<"Setting">> = function SettingScreen(_props) {
  const { navigation } = _props
  const {
    authenticationStore: { logout },
  } = useStores()

  useHeader({
    leftIcon: "back",
    onLeftPress: () => {
      navigation.goBack()
    },
    title: "설정",
  })

  function Withdrawal() {
    api.deleteWithdrawal().then(() => {
      logout()
    })
  }

  function handleLogout() {
    logout()
    api.deleteLogout()
  }

  return (
    <Screen preset="scroll" contentContainerStyle={$container}>
      <ListItem onPress={handleLogout} style={$ButtonContainer}>
        <Text>로그아웃</Text>
      </ListItem>
      <ListItem onPress={Withdrawal} style={$ButtonContainer}>
        <Text>회원탈퇴</Text>
      </ListItem>
    </Screen>
  )
}

const $container: ViewStyle = {
  paddingVertical: 20,
  paddingHorizontal: 20,
  flex: 1,
}

const $ButtonContainer: ViewStyle = {
  height: 60,
}
