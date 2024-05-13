import { CompositeScreenProps } from "@react-navigation/native"
import React from "react"
import { ViewStyle } from "react-native"
import { InProgressMeetingScreen, TerminatedMeetingScreen } from "../screens"
import { colors, spacing } from "../theme"
import { AppStackParamList, AppStackScreenProps } from "./AppNavigator"

import {
  MaterialTopTabScreenProps,
  createMaterialTopTabNavigator,
} from "@react-navigation/material-top-tabs"
import { useHeader } from "app/utils/useHeader"

export type TicketTabParamList = {
  InProgressMeeting: undefined
  TerminatedMeeting: undefined
}

export type TicketTabScreenProps<T extends keyof TicketTabParamList> = CompositeScreenProps<
  MaterialTopTabScreenProps<TicketTabParamList, T>,
  AppStackScreenProps<keyof AppStackParamList>
>

const Tab = createMaterialTopTabNavigator<TicketTabParamList>()

export function TicketNavigator() {
  useHeader({
    title: "증표 리스트",
  })

  return (
    <Tab.Navigator
      screenOptions={{
        tabBarShowLabel: true,
        tabBarStyle: [$tabBar],
        tabBarItemStyle: $tabBarItem,
      }}
    >
      <Tab.Screen
        options={{ tabBarLabel: "진행 중인 모임" }}
        name="InProgressMeeting"
        component={InProgressMeetingScreen}
      />
      <Tab.Screen
        options={{ tabBarLabel: "참가했던 모임" }}
        name="TerminatedMeeting"
        component={TerminatedMeetingScreen}
      />
    </Tab.Navigator>
  )
}

const $tabBar: ViewStyle = {
  backgroundColor: colors.white,
  borderTopColor: colors.transparent,
}

const $tabBarItem: ViewStyle = {
  paddingTop: spacing.md,
}
