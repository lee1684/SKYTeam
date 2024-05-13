import { CompositeScreenProps, useNavigation } from "@react-navigation/native"
import React from "react"
import { ViewStyle } from "react-native"
import { TicketInfoScreen, TicketViewScreen } from "../screens"
import { colors, spacing } from "../theme"
import { AppStackParamList, AppStackScreenProps } from "./AppNavigator"

import {
  MaterialTopTabScreenProps,
  createMaterialTopTabNavigator,
} from "@react-navigation/material-top-tabs"
import { useHeader } from "app/utils/useHeader"
import { TicketAuthScreen } from "app/screens/MainScreen/CommonScreen/TicketAuthScreen"

export type TicketInfoTabParamList = {
  TicketAuth: undefined
  TicketViewLook: undefined
  TicketViewInfo: undefined
}

export type TicketInfoScreenProps<T extends keyof TicketInfoTabParamList> = CompositeScreenProps<
  MaterialTopTabScreenProps<TicketInfoTabParamList, T>,
  AppStackScreenProps<keyof AppStackParamList>
>

export const StateContext = React.createContext({})

const Tab = createMaterialTopTabNavigator<TicketInfoTabParamList>()

export function TicketInfoNavigator() {
  const navigation = useNavigation()
  const [state, setState] = React.useState({})

  useHeader({
    leftIcon: "back",
    onLeftPress: () => {
      navigation.goBack()
    },
    rightIcon: "community",
  })

  return (
    <StateContext.Provider value={{ state, setState }}>
      <Tab.Navigator
        screenOptions={{
          tabBarShowLabel: true,
          tabBarStyle: [$tabBar],
          tabBarItemStyle: $tabBarItem,
        }}
      >
        <Tab.Screen
          options={{
            tabBarLabel: "인증하기",
          }}
          name="TicketAuth"
          component={TicketAuthScreen}
        />
        <Tab.Screen
          options={{
            tabBarLabel: "증표 보기",
          }}
          name="TicketViewLook"
          component={TicketViewScreen}
        />

        <Tab.Screen
          options={{ tabBarLabel: "모임 정보" }}
          name="TicketViewInfo"
          component={TicketInfoScreen}
        />
      </Tab.Navigator>
    </StateContext.Provider>
  )
}

const $tabBar: ViewStyle = {
  backgroundColor: colors.white,
  borderTopColor: colors.transparent,
}

const $tabBarItem: ViewStyle = {
  paddingTop: spacing.md,
}
