import { CompositeScreenProps } from "@react-navigation/native"
import React from "react"
import { AppStackParamList, AppStackScreenProps } from "./AppNavigator"
import { NativeStackScreenProps, createNativeStackNavigator } from "@react-navigation/native-stack"
import { CheckFirstVisitScreen } from "app/screens/WelcomeScreen/CheckFirstVisit"
import { SettingProfileScreen } from "app/screens/WelcomeScreen/SettingProfile"
import { SettingLocationScreen } from "app/screens/WelcomeScreen/SettingLocation"
import { SettingHobbyScreen } from "app/screens/WelcomeScreen/SettingHobby"

export type WelcomeParamList = {
  CheckFirstVisit: undefined
  SettingProfile: undefined
  SettingLocation: undefined
  SettingHobby: undefined
}

export type WelcomeScreenProps<T extends keyof WelcomeParamList> = CompositeScreenProps<
  NativeStackScreenProps<WelcomeParamList, T>,
  AppStackScreenProps<keyof AppStackParamList>
>

const Stack = createNativeStackNavigator<WelcomeParamList>()

export function WelcomeNavigator() {
  return (
    <Stack.Navigator
      screenOptions={{
        headerShown: false,
      }}
    >
      <Stack.Screen name="CheckFirstVisit" component={CheckFirstVisitScreen} />
      <Stack.Screen name="SettingProfile" component={SettingProfileScreen} />
      <Stack.Screen name="SettingLocation" component={SettingLocationScreen} />
      <Stack.Screen name="SettingHobby" component={SettingHobbyScreen} />
    </Stack.Navigator>
  )
}
